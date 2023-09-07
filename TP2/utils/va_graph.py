import matplotlib.pyplot as plt
import glob
import numpy as np

# Create lists to store x and y values
x_values = []
y_values = []

# List all files with the specified format
# file_list =[ ["./data/output/VaN_50_L_20_noise_0.5.txt", "./data/output/VaN_200_L_20_noise_0.5.txt", "./data/output/VaN_800_L_20_noise_0.5.txt"]
file_list = []
# List of colors to use for each dataset
color_list = ['b', 'g', 'r', 'c']
# color_list = ['b', 'g', 'r', 'c', 'm']

L=[5, 5, 5]
N = [300, 300, 300]
noise = [0.3, 1.7, 5.0]
for i in range(len(N)):
    file_list.append(f"./data/output/VaN_{N[i]}_L_{L[i]}_noise_{noise[i]}.txt")
# Loop through each file and read data
for idx, file_name in enumerate(file_list):
    with open(file_name, "r") as file:
        lines = file.readlines()
        i=0
        for line in lines:
            if i>1000:
                break
            i = i+1
            x, y = map(float, line.strip().split())
            x_values.append(x)
            y_values.append(y)
    
    # Create the plot for each dataset with a different color
    density = float(N[idx] / (L[idx] * L[idx]))
    plt.plot(x_values, y_values, marker='o', linestyle='-', color=color_list[idx], label=f'Noise {noise[idx]:.1f}', markersize=0.3, linewidth=0.5)
    
    # Reset lists for the next dataset
    x_values = []
    y_values = []

plt.xlabel('Iteration')
plt.ylabel('Va')
plt.legend()
plt.grid(True)
plt.savefig('./data/output/graphs/va_iteraciones_ruido.png')

plt.cla()
# ------------------------------------------------------------------------------------------------------------------
import numpy as np
import matplotlib.pyplot as plt

x_values_list = []
y_values_list = []
y_std = []

file_list = []
color_list = ['b', 'g', 'r', 'c']

L = [20, 20, 20]
N = [200, 800]
noise = [0.4, 0.4, 0.4]


# 200v1 t;va
# 200v2
# 200v9

# 800v1
# 800v2
# 800v9

# 200 t;avg(va)
# 800 t;avg(va)
for i in range(len(N)):
    file_list.append([])
    for v in range( 10):
        file_list[i].append(f"./data/output/VaN_{N[i]}_L_{L[i]}_noise_{noise[i]}_v{v}.txt")
print(len(file_list[0]))
for idx, file_array in enumerate(file_list):
    # y_values_list = []
    
    ys = [[0 for _ in range( 10)] for _ in range(1502)] # 1500x10
    
    for position, file_name in enumerate(file_array):
        with open(file_name, "r") as file:
            lines = file.readlines()
            i=0
            for line in lines:
                i=i+1
                x, y = map(float, line.strip().split())
                # print('i', i, 'position', position, 'x', x, 'y', y)
                ys[i][position] = y
    
    y_values_list = [np.mean(fila) for fila in ys]

    y_std.append(np.std(ys[idx]))
    density = float(N[idx] / (L[idx] * L[idx]))

    x_values_list = [otro_iterador for otro_iterador in range(1502)]
    plt.plot(
        x_values_list, y_values_list
        # , yerr=y_std[idx],
        ,marker='o', linestyle='-', 
        color=color_list[idx],
        label=f'Density {density:.1f}'
        , markersize=0.5, linewidth=0.5
    )
y_values2 = []
x_values2 = []
with open(f"./data/output/VaN_{3000}_L_{20}_noise_{noise[0]}_v{2}.txt", "r") as file:
    lines = file.readlines()
    i=0
    for line in lines:
        x, y = map(float, line.strip().split())
        x_values2.append(x)
        y_values2.append(y)

# Create the plot for each dataset with a different color
density = 7.5
plt.plot(
        x_values2, y_values2
        # , yerr=y_std[idx],
        ,marker='o', linestyle='-', 
        color='r',
        label=f'Density {density:.1f}'
        , markersize=0.5, linewidth=0.5
    )
plt.xlabel('Iteration')
plt.ylabel('Va')
plt.legend()
plt.grid(True)
plt.savefig('./data/output/graphs/va_iteraciones_densidad.png')

plt.clf()

