import matplotlib.pyplot as plt
import numpy as np
import random
import math

LIMITS_PARTICLES_AMOUNT = 1080
FRAME_STEPPER = LIMITS_PARTICLES_AMOUNT + 2
REGRESION_SPACE = 0.0001
INTERVAL_LENGTH = 10
TOLERANCE_FOR_REGRESION_BREAKPOINT = 0.0001
DOMAIN_LENGTH = 0.09
N=[200, 210, 220, 230, 240, 250, 260, 270, 280, 290, 300]
L=[0.09, 0.05, 0.07, 0.09]
UNIQUE = True

def process_simulation_output(input_file, considered_particles, interval, t0):

    with open(input_file, 'r') as file:
        lines = file.readlines()

    displacements = {}
    base_values = []
    found = False

    for i in range(1, len(lines), (FRAME_STEPPER + considered_particles) * interval):
        frame_info = lines[i].split()
        frame_number = float(frame_info[1])
        if t0 > frame_number:
            continue
        frame_data = []
        frame_DCM = 0
        frame_std = []

        for j in range(i + 1, i + considered_particles + 1):
            parts = lines[j].split()
            particle_id, x, y, _, _, _, _, _, _ = map(float, parts)
            if 1 <= particle_id <= considered_particles:
                frame_data.append((particle_id, x, y))
            if not found:
                base_values.append((particle_id, x, y))
        found = True

        for id, x, y in frame_data:
            displacement = ((x - base_values[int(id) - 1][1])**2 + (y - base_values[int(id) - 1][2])**2)**0.5
            frame_DCM += displacement**2
            frame_std.append(displacement**2)

        DCM = frame_DCM/considered_particles

        displacements[frame_number] = {"DCM": DCM, "STD": np.std(frame_std)}

    return displacements

def find_DCM_values_for_linear_regression(DCM_values, tolerance):
    mean = np.mean(DCM_values[len(DCM_values)//2:])
    # Find the first value that is closer to the mean's zone, in order to end linear regresion
    for i in range(3*len(DCM_values)//4):
        if abs(DCM_values[i] - mean) < tolerance:
            return DCM_values[0:i+1], i + 1
    return find_DCM_values_for_linear_regression(DCM_values, tolerance * 10)


def encontrar_frame_con_igualdad(archivo):
    with open(archivo, 'r') as file:
        return float(file.readline().split()[1])

def calculate_diffusion_coefficient(DCM_values, frames, n, l, v):

    real_values, value = find_DCM_values_for_linear_regression(DCM_values, TOLERANCE_FOR_REGRESION_BREAKPOINT)

    tiempo = np.arange(len(real_values))
    
    m = 0.0001
    f1 = frames[:len(tiempo)][0]
    x = [item for item in frames[:len(tiempo)]]
    c = [x * 0.000001 for x in range(-100, 100)]
    ec = []
    y1 = real_values[0]
    for ci in c:
        ec.append(sum([math.pow(yi - ci*xi, 2) for xi, yi in zip(x, real_values)]))
    for(eci, ci) in zip(ec, c):
        if eci == min(ec):
            pendiente = ci
            # print(ci)
    x_labels = []
    for frame in c:
        x_labels.append(f'{frame - frames[0]:.2f}')

    
    b = real_values[0] - pendiente*frames[:len(tiempo)][0]

    f1 = frames[:len(tiempo)][0]
    x = [item - f1 for item in frames[:len(tiempo)]]
    c = [x * 0.00001 for x in range(-500, 500)]
    ec = []
    y1 = real_values[0]
    for ci in c:
        ec.append(sum([math.pow(yi - ci*xi, 2) for xi, yi in zip(x, real_values)]))
    for(eci, ci) in zip(ec, c):
        if eci == min(ec):
            pendiente = ci
    print('pendiente gaspi '+str(float(pendiente)))
    # b=pressure_perL[3]-pendiente*x[3]
    b = real_values[0] - pendiente*frames[:len(tiempo)][0]
    b=0
    print('b gaspi' + str(float(b)))
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
        x_labels.append(f'{frame - frames[0]:.2f}')

    ax.set_xlabel('Tiempo (s)')
    ax.set_ylabel("$<z^2> (m^2)$")
    ax.set_xticks(x)
    ax.set_xticklabels(x_labels, rotation=45, ha="right")

    D, break_line, b = calculate_diffusion_coefficient(DCM_values, frames, N_particles, Lsize, version)

    # # Agregar la línea de regresión solo hasta el punto de break_line
    # y_regresion = np.array(frames[1:break_line]) * 4 * D + b # y = mx + b

    ax.plot([i for i in range(len(frames[1:break_line]))], y_regresion, color='red', label=f'Regresion Lineal (D={D:.4f})')

    # # Guardar la figura
    plt.savefig(f'./data/output/graphs/DCMvsTime_N_{N_particles}_L_{Lsize}_D_{D:f}.png')
    plt.close()
    return D


if __name__:
    if UNIQUE:
        displacements = process_simulation_output(f"./data/output/Dynamic_N_{N[0]}_L_{L[0]}_v_1.dump", considered_particles=N[0], interval=INTERVAL_LENGTH, t0=encontrar_frame_con_igualdad(f'./data/output/FP_{N[0]}_L_{L[0]}.txt'))
        plot_DCM(displacements, N[0], L[0], 1)
        print(f'N = {N[0]}, L = {L[0]}: Done')
    else:
        for i in range(len(N)):
            for j in range(len(L)):
                displacements = process_simulation_output(f"./data/output/Dynamic_N_{N[i]}_L_{L[j]}_v_1.dump", considered_particles=N[i], interval=INTERVAL_LENGTH, t0=encontrar_frame_con_igualdad(f'./data/output/FP_{N[i]}_L_{L[j]}.txt'))
                plot_DCM(displacements, N[i], L[j], 1) # TODO: change
                print(f'N = {N[i]}, L = {L[j]}: Done')