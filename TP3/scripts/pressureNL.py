import os
import glob
import numpy as np
import matplotlib.pyplot as plt

# Define the values of N and L
N_values = [200, 250, 300]
L_values = [0.03, 0.05, 0.07, 0.09]
file_list = []
color_list = ['b', 'g', 'r', 'c', 'm']
# Create a dictionary to store data for each combination of N and L
data = {}



for iteration in range(len(N_values)):
    file_list = []
    for i in range(len(L_values)):
        # format noise[i] to exactly 2 decimals
        file_list.append(f"./data/output/FP_{N_values[iteration]}_L_{L_values[i]}.txt")
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
    x = [1/(0.09*0.09 + 0.09*L) for L in L_values]
    plt.scatter(x, pressure_perL, marker='o', linestyle='-', color=color_list[iteration], label=f'N= {N_values[iteration]} ')
    plt.errorbar(x, pressure_perL, yerr=stds, fmt='o', color=color_list[iteration], ecolor=color_list[iteration])
    pressurre=[]
    stds=[]

plt.xlabel('A⁻¹')
plt.ylabel('P')
plt.legend()
plt.grid(True)
plt.savefig('./data/output/graphs/pressureCHETO.png')

plt.cla()