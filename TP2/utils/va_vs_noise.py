import matplotlib.pyplot as plt
import glob
import numpy as np


# List all files with the specified format
# file_list =[ ["./data/output/VaN_50_L_20_noise_0.5.txt", "./data/output/VaN_200_L_20_noise_0.5.txt", "./data/output/VaN_800_L_20_noise_0.5.txt"]
file_list = []
# List of colors to use for each dataset
color_list = ['b', 'g', 'r', 'c']
# color_list = ['b', 'g', 'r', 'c', 'm']


L = [5, 7, 14]
N = [300, 588, 2352]
noise = [i*0.2 for i in range(1, 30)]
for iteration in range(len(L)):
    file_list = []
    for i in range(len(noise)):
        # format noise[i] to exactly 2 decimals
        noise2 = format(noise[i], ".2f")
        file_list.append(f"./data/output/noise_VaN_{N[iteration]}_L_{L[iteration]}_noise_{noise2}.txt")
    # Loop through each file and read data
    vas=[]
    stds=[]
    for idx, file_name in enumerate(file_list):
        with open(file_name, "r") as file:
            # vas.append(0)
            va_per_error=[]
            lines = file.readlines()
            for line in lines:
                x, y = map(float, line.strip().split())
                va_per_error.append(y)
            vas.append(np.mean(va_per_error))
            stds.append(np.std(va_per_error))
    # print(N[iteration], L[iteration], len(noise), (vas))
    plt.scatter(noise, vas, marker='o', linestyle='-', color=color_list[iteration], label=f'L= {L[iteration]} N= {N[iteration]}')
    plt.errorbar(noise, vas, yerr=stds, fmt='o', color=color_list[iteration], ecolor=color_list[iteration], capthick=2)
    vas=[]
    stds=[]

plt.xlabel('Ruido')
plt.ylabel('Va')
plt.legend()
plt.grid(True)
plt.savefig('./data/output/graphs/va_ruido.png')

plt.cla()