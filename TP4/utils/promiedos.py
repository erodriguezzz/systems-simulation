import math
import matplotlib.pyplot as plt
import glob
import numpy as np


def get_stationary(ys):
    prev = ys[0]
    stationary = 0
    for v in ys:
        if abs(v - prev) < 0.0001:
            stationary = stationary + 1
        else:
            stationary = 0
        prev = v
        
    return 1600-stationary


file_list = []
color_list = ['b', 'g', 'r', 'c', 'y', 'm']


K = ['0.001']
# K = ['0.01', '0.001', '1.0E-4']
ks = [-2, -3, -4, -5]
N = [5, 10, 15, 20, 25, 30]

k_thetas = []
FRAMEMAX = 1600

mean_versions = {}
stationary_mean = {}
noise = [i*0.2 for i in range(1, 30)]
for iteration in range(len(N)):
    mean_versions.update({iteration: {}})
    stationary_mean.update({iteration: {}})
    for frame in range(0, FRAMEMAX):
        mean_versions[iteration].update({frame: []})
    for version in range(0, 1):
        stationary_mean[iteration].update({version: []})
        
        # file_list = []
        file_name=(f"./data/output/Dynamic2_N_{N[iteration]}_dt_{K[0]}_v_{version}.dump")
        # file_name2=(f"./data/output/Dynamic2_N_25_dt_{K[iteration+1]}.dump")
        # file_list.append(f"./data/output/Dynamic_N_25_dt_{K[iteration+1]}.txt")
        # Loop through each file and read data
        with open(file_name, "r") as file:
            lines = file.readlines()
            for frame in range(0, FRAMEMAX):
                sum = 0
                for p in range(0, N[iteration]):
                    id1, x1, _, vx1, _, u1, r1, m1 = map(float, lines[(N[iteration]+2) *frame + p + 2].strip().split())
                    sum += vx1
                value = sum/N[iteration]
                stationary_mean[iteration][version].append(value)
                # values es para version tengo una lista con el promedio por frame
                # mean versions para cada N tengo una lista por cada version por cada frame
                mean_versions[iteration][frame].append(value)

    # print((mean_versions[iteration][0]))
    ys = [np.mean(mean_versions[iteration][frame]) for frame in range(0, FRAMEMAX)]
    
    stds = [np.std(mean_versions[iteration][frame]) for frame in range(0, FRAMEMAX)]
    # plt.scatter(range(0, FRAMEMAX), ys, marker='o', linestyle='-', color=color_list[iteration], label=f'N= {N[iteration]}')
    # plt.errorbar(range(0, FRAMEMAX), ys, yerr=stds, fmt='o', color=color_list[iteration], ecolor=color_list[iteration], capthick=2)

di2 = {}
for iteration in range(len(N)):
    di2.update({iteration: []})
    for version in range(0, 20):
        di2[iteration].append(get_stationary(stationary_mean[iteration][version]))

plt.scatter(N, [np.mean(di2[iteration]) for iteration in range(len(N))], marker='o', linestyle='-')
stds = [np.std(di2[iteration]) for iteration in range(len(N))]
plt.errorbar(N, [np.mean(di2[iteration]) for iteration in range(len(N))], yerr=stds, fmt='o', color=color_list[iteration], ecolor=color_list[iteration], capthick=2)

plt.xlabel('Tiempo hasta estacionario')
plt.ylabel('N')
plt.legend()
plt.grid(True)
plt.savefig('./data/output/graphs/grafico_loco.1.png')

plt.cla()
