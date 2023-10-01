import math
import matplotlib.pyplot as plt
import glob
import numpy as np

file_list = []
color_list = ['b', 'g', 'r', 'c', 'y']


K = ['0.001']
# K = ['0.01', '0.001', '1.0E-4']
ks = [-2, -3, -4, -5]
N = [5, 10, 15, 20, 25, 30]

k_thetas = []
FRAMEMAX = 1600

noise = [i*0.2 for i in range(1, 30)]
for iteration in range(len(K)-1):
    # file_list = []
    file_name=(f"./data/output/Dynamic2_N_{N[iteration]}_dt_{K[[0]]}.dump")
    # file_name2=(f"./data/output/Dynamic2_N_25_dt_{K[iteration+1]}.dump")
    # file_list.append(f"./data/output/Dynamic_N_25_dt_{K[iteration+1]}.txt")
    # Loop through each file and read data
    vas=[]
    stds=[]
    with open(file_name, "r") as file:
        lines = file.readlines()
        values = []
        for frame in range(0, FRAMEMAX):
            sum = 0
            for p in range(0, N[iteration]):
                id1, x1, _, vx1, _, u1, r1, m1 = map(float, lines[27*frame + p + 2].strip().split())
                sum += vx1
            values.append(sum/N[iteration])
    print(iteration)
    plt.scatter(range(0, FRAMEMAX), values, marker='o', linestyle='-', color=color_list[iteration], label=f'K= {K[iteration]}')
    # plt.errorbar(noise, vas, yerr=stds, fmt='o', color=color_list[iteration], ecolor=color_list[iteration], capthick=2)
    vas=[]
    stds=[]

plt.xlabel('Tiempo')
plt.ylabel('Φ(t)')
plt.legend()
plt.grid(True)
plt.savefig('./data/output/graphs/sis2ej2.1.png')

plt.cla()
