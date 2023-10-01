import math
import matplotlib.pyplot as plt
import glob
import numpy as np

file_list = []
color_list = ['b', 'g', 'r', 'c', 'y']


# K = ['0.1', '0.01', '0.001', '1.0E-4', '1.0E-5']
K = [  '0.001', '1.0E-4', '1.0E-5']
ks = [1, 2, 3, 4]
N = [25]

k_thetas = []
FRAMEMAX = 1600
# DALE BOOOOOOOOOOOOOOOOOOOOOO

# noise = [i*0.2 for i in range(1, 30)]
for iteration in range(len(K)-1):
    # file_list = []
    file_name=(f"./data/output/Dynamic2_N_25_dt_{K[iteration]}.dump")
    file_name2=(f"./data/output/Dynamic2_N_25_dt_{K[iteration+1]}.dump")
    # Loop through each file and read data
    vas=[]
    stds=[]
    with open(file_name, "r") as file:
        with open(file_name2, "r") as file2:
            lines = file.readlines()
            lines2 = file2.readlines()
            values = []
            for frame in range(0, FRAMEMAX):
                sum = 0
                for p in range(0, 25):
                    id1, x1, _, vx1, _, u1, r1, m1 = map(float, lines[27*frame + p + 2].strip().split())
                    id2, x2, _, vx2, _, u2, r2, m2 = map(float, lines2[27*frame + p + 2].strip().split())
                    sum += min(abs(x1 - x2), 135 - abs(x1 - x2))
                values.append(sum)
    print(iteration)
    plt.scatter(range(0, FRAMEMAX), values, marker='o', linestyle='-', color=color_list[iteration], label=f'K= {ks[iteration]}')
    vas=[]
    stds=[]

plt.xlabel('Tiempo')
plt.ylabel('Φ(t)')
plt.legend()
plt.grid(True)
plt.savefig('./data/output/graphs/sys2ej1.png')

plt.cla()
