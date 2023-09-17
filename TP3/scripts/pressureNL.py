import os
import glob
import numpy as np
import matplotlib.pyplot as plt

# Define the values of N and L
N_values = [200, 230, 240, 250]
L_values = [0.03, 0.05, 0.07, 0.09]
file_list = []
color_list = ['b', 'g', 'r', 'c', 'm']
# Create a dictionary to store data for each combination of N and L
data = {}



for iteration in range(len(L_values)):
    file_list = []
    for i in range(len(N_values)):
        # format noise[i] to exactly 2 decimals
        file_list.append(f"./data/output/FP_{N_values[i]}_L_{L_values[iteration]}.txt")
    # Loop through each file and read data
    pressure_perL=[]
    pressure=[]
    stds=[]
    for idx, file_name in enumerate(file_list):
        pressure=[]
        with open(file_name, "r") as file:
            # vas.append(0)
            # _per_error=[]
            lines = file.readlines()
            for line in lines:
                pressure.append( float(line.strip().split()[0]))
                # va_per_error = (pressure)
            pressure_perL.append(np.mean(pressure))
            stds.append(np.std(pressure))
    # print(N[iteration], L[iteration], len(noise), (vas))
    plt.scatter(N_values, pressure_perL, marker='o', linestyle='-', color=color_list[iteration], label=f'L= {L_values[iteration]} ')
    plt.errorbar(N_values, pressure_perL, yerr=stds, fmt='o', color=color_list[iteration], ecolor=color_list[iteration], capthick=2)
    pressurre=[]
    stds=[]

plt.xlabel('Cantidad de Particulas')
plt.ylabel('Presion')
plt.legend()
plt.grid(True)
plt.savefig('./data/output/graphs/pressureCHETO.png')

plt.cla()

# Function to read and process a data file
# def read_data_file(file_path):
#     time_values = []
#     pressure_left_values = []
#     pressure_right_values = []

#     with open(file_path, 'r') as file:
#         for line in file:
#             parts = line.strip().split()
#             if len(parts) == 3:
#                 time_values.append(float(parts[0]))
#                 pressure_left_values.append(float(parts[1]))
#                 pressure_right_values.append(float(parts[2]))

#     return time_values, pressure_left_values, pressure_right_values

# # Loop through N and L values
# for N in N_values:
#     for L in L_values:
#         # Create a key for the data dictionary
#         key = f"N_{N}_L_{L}"

#         # Find all files that match the pattern
#         file_pattern = f"./data/output/Pressure_{N}_L_{L}_v*"
#         data[key] = []

#         for file_path in glob.glob(file_pattern):
#             time, pressure_left, pressure_right = read_data_file(file_path)
#             # print(time, pressure_left, pressure_right)
#             data[key].append((time, pressure_left, pressure_right))

# # Create box plots for each combination of N and L
# for key, values in data.items():
#     plt.figure(figsize=(8, 6))
#     plt.boxplot([np.std(pressure_left) for (_, pressure_left, _) in values], positions=[1], labels=['Pressure Left'], showfliers=False)
#     plt.boxplot([np.std(pressure_right) for (_, _, pressure_right) in values], positions=[2], labels=['Pressure Right'], showfliers=False)
#     plt.xlabel('Version')
#     plt.ylabel('Standard Deviation')
#     plt.title(f'Standard Deviation of Pressure for {key}')
#     plt.xticks([1, 2], ['Pressure Left', 'Pressure Right'])
#     plt.grid(True)

# # Show the plots
# plt.savefig(f'./data/output/graphs/PressurevsNL.png')
