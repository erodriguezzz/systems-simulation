import matplotlib.pyplot as plt
import numpy as np
import random
from static_generator import N, L, UNIQUE

LIMITS_PARTICLES_AMOUNT = 1080
FRAME_STEPPER = LIMITS_PARTICLES_AMOUNT + 2
REGRESION_SPACE = 0.0001
INTERVAL_LENGTH = 100
TOLERANCE_FOR_REGRESION_BREAKPOINT = 0.0001

def process_simulation_output(input_file, considered_particles, interval):
    with open(input_file, 'r') as f:
        lines = f.readlines()

    displacements = {}
    base_values = []

    for i in range(1, len(lines), (FRAME_STEPPER + considered_particles) * interval):
        frame_info = lines[i].split()
        frame_number = float(frame_info[1])
        frame_data = []
        frame_DCM = 0
        frame_std = []

        for j in range(i + 1, i + considered_particles + 1):
            parts = lines[j].split()
            particle_id, x, y, _, _, _, _, _, _ = map(float, parts)
            if 1 <= particle_id <= considered_particles:
                frame_data.append((particle_id, x, y))
            if i == 1:
                base_values.append((particle_id, x, y))

        for id, x, y in frame_data:
            displacement = ((x - base_values[int(id) - 1][1])**2 + (y - base_values[int(id) - 1][2])**2)**0.5
            frame_DCM += displacement**2
            frame_std.append(displacement**2)

        DCM = frame_DCM/considered_particles

        displacements[frame_number] = {"DCM": DCM, "STD": np.std(frame_std)}

        # Check if every particle_id in the frame is between 1 and considered_particles
        for id, _, _ in frame_data:
            if id < 1 or id > considered_particles:
                print(f'Error: particle_id {id} is not between 1 and {considered_particles}')
                raise Exception("Invalid particle_id")

    return displacements

def find_DCM_values_for_linear_regression(DCM_values, tolerance):
    mean = np.mean(DCM_values[len(DCM_values)//2:])
    # Find the first value that is closer to the mean's zone, in order to end linear regresion
    for i in range(len(DCM_values)//2):
        if abs(DCM_values[i] - (mean-REGRESION_SPACE)) < tolerance:
            return DCM_values[0:i+1], i + 1
    return find_DCM_values_for_linear_regression(DCM_values, tolerance * 10)


def calculate_diffusion_coefficient(DCM_values, frames):
    # Crear un arreglo de tiempo desde 0 hasta el número de elementos en DCM_values

    real_values, value = find_DCM_values_for_linear_regression(DCM_values, TOLERANCE_FOR_REGRESION_BREAKPOINT)

    tiempo = np.arange(len(real_values))

    print(f"length of tiempo: {len(tiempo)} - tiempo = {str(tiempo)}")
    print(f"length of real_values: {len(real_values)} - real_values = {str(real_values)}")
    print(f"length of frames: {len(frames[0:len(tiempo)])} - frames = {str(frames[0:len(tiempo)])}")

    # Realizar una regresión lineal para obtener la pendiente y el intercepto
    pendiente, _ = np.polyfit(frames[0:len(tiempo)], real_values, 1)

    return pendiente / 2, value

def plot_DCM(dictionary, N_particles, Lsize):
    frames = list(dictionary.keys())
    DCM_values = [value['DCM'] for value in dictionary.values()]
    STD_errors = [value['STD'] for value in dictionary.values()]

    x = np.arange(len(frames))

    fig, ax = plt.subplots(figsize=(15, 9))
    error_bars = ax.errorbar(x[1:], DCM_values[1:], yerr=STD_errors[1:], fmt='o', color=(random.random(), random.random(), random.random()), label='STD')

    x_labels = []
    for frame in frames:
        x_labels.append(f'{frame:.2f}')

    ax.set_xlabel('Time')
    ax.set_ylabel('DCM')
    ax.set_xticks(x)
    ax.set_xticklabels(x_labels, rotation=45, ha="right")

    D, break_line = calculate_diffusion_coefficient(DCM_values[1:len(DCM_values)], frames[1:len(frames)])

    # Agregar la línea de regresión solo hasta el punto de break_line
    y_regresion = np.array(frames[1:break_line]) * 2 * D + np.mean(DCM_values[1:4]) # y = mx + b
    print("breakline: " + str(break_line))
    print("frames: " + str(frames[1:break_line]))
    print("m: " + str(2 * D))

    ax.plot([i+1 for i in range(len(frames[1:break_line]))], y_regresion, color='red', label=f'Regresion Lineal (D={D:.4f})')

    # Guardar la figura
    plt.savefig(f'./data/output/graphs/DCMvsTime_N_{N_particles}_L_{Lsize}_D_{D:f}.png')
    plt.close()
    return D


if UNIQUE:
    displacements = process_simulation_output(f"./data/output/Dynamic_N_{N[0]}_L_{L[0]}.dump", considered_particles=N[0], interval=INTERVAL_LENGTH)
    plot_DCM(displacements, N[0], L[0])
    print(f'N = {N[0]}, L = {L[0]}: Done')
else:
    for i in range(len(N)):
        for j in range(len(L)):
            displacements = process_simulation_output(f"./data/output/Dynamic_N_{N[i]}_L_{L[j]}.dump", considered_particles=N[i], interval=INTERVAL_LENGTH)
            plot_DCM(displacements, N[i], L[j])
            print(f'N = {N[i]}, L = {L[j]}: Done')