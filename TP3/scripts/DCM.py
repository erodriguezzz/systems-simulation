import matplotlib.pyplot as plt
import numpy as np
import random

LIMITS_PARTICLES_AMOUNT = 1080
FRAME_STEPPER = LIMITS_PARTICLES_AMOUNT + 2

def process_simulation_output(input_file, considered_particles, interval, L):
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

def graficar_DCM(diccionario, N, L):
    frames = list(diccionario.keys())
    valores_DCM = [valor['DCM'] for valor in diccionario.values()]
    errores_STD = [valor['STD'] for valor in diccionario.values()]

    x = np.arange(len(frames))  # Posiciones en el eje x

    fig, ax = plt.subplots(figsize=(15, 9))
    barras_STD = ax.errorbar(x, valores_DCM, yerr=errores_STD, fmt='o', color=(random.random(),random.random(), random.random()), label='STD')

    x_labels = []
    for frame in frames:
        x_labels.append(f'{frame:.2f}')

    ax.set_xlabel('Time')
    ax.set_ylabel('DCM')
    ax.set_xticks(x)
    ax.set_xticklabels(x_labels, rotation=45, ha="right")

    # Guardar la figura
    plt.savefig(f'./data/output/graphs/DCMvsTime_N_{N}_L_{L}.png')
    plt.close()

# Usage of the function
N=[200]
L=[0.03]

for i in range(len(N)):
    for j in range(len(L)):
        displacements = process_simulation_output(f"./data/output/Dynamic_N_{N[i]}_L_{L[j]}.dump", considered_particles=N[i], interval=100, L=L[j])
        graficar_DCM(displacements, N[i], L[j])
        print(f'N = {N[i]}, L = {L[j]}: Done')