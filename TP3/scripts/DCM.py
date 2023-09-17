import matplotlib.pyplot as plt
import numpy as np
import random
from static_generator import N, L, UNIQUE

LIMITS_PARTICLES_AMOUNT = 1080
FRAME_STEPPER = LIMITS_PARTICLES_AMOUNT + 2
REGRESION_SPACE = 0.0001
INTERVAL_LENGTH = 100
TOLERANCE_FOR_REGRESION_BREAKPOINT = 0.0001

def process_simulation_output(considered_particles, interval):

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
            displacement = ((x - base_values[int(id) - 1][1])**2 + (y - base_values[int(id) - 1][2])**2)
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


def encontrar_frame_con_igualdad(archivo):
    with open(archivo, 'r') as file:
        return float(file.readline().split()[1])

def calculate_diffusion_coefficient(DCM_values, frames, n, l, v):
    # Crear un arreglo de tiempo desde 0 hasta el número de elementos en DCM_values

    t0 = encontrar_frame_con_igualdad(f'./data/output/FP_{n}_L_{l}_v_{v}.dump')

    stationary_frames = [value for value in frames if value > t0]

    real_values, value = find_DCM_values_for_linear_regression(DCM_values[len(stationary_frames):], TOLERANCE_FOR_REGRESION_BREAKPOINT)

    tiempo = np.arange(len(real_values))

    # Realizar una regresión lineal para obtener la pendiente y el intercepto
    pendiente, b = np.polyfit(frames[:len(tiempo)], real_values, 1)

    return pendiente / 4, value, b

def plot_DCM(dictionary, N_particles, Lsize, version):
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

    D, break_line, b = calculate_diffusion_coefficient(DCM_values[1:len(DCM_values)], frames[1:len(frames)], N_particles, Lsize, version)

    # Agregar la línea de regresión solo hasta el punto de break_line
    y_regresion = np.array(frames[1:break_line]) * 4 * D + b # y = mx + b

    ax.plot([i+1 for i in range(len(frames[1:break_line]))], y_regresion, color='red', label=f'Regresion Lineal (D={D:.4f})')

    # Guardar la figura
    plt.savefig(f'./data/output/graphs/DCMvsTime_N_{N_particles}_L_{Lsize}_D_{D:f}.png')
    plt.close()
    return D


if UNIQUE:
    displacements = process_simulation_output(f"./data/output/Dynamic_N_{N[0]}_L_{L[0]}_v_1.dump", considered_particles=N[0], interval=INTERVAL_LENGTH)
    plot_DCM(displacements, N[0], L[0], 1)
    print(f'N = {N[0]}, L = {L[0]}: Done')
else:
    for i in range(len(N)):
        for j in range(len(L)):
            displacements = process_simulation_output(f"./data/output/Dynamic_N_{N[i]}_L_{L[j]}_v_1.dump", considered_particles=N[i], interval=INTERVAL_LENGTH)
            plot_DCM(displacements, N[i], L[j], 1) # TODO: change
            print(f'N = {N[i]}, L = {L[j]}: Done')