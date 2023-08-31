import matplotlib.pyplot as plt
import glob

# Create lists to store x and y values
x_values = []
y_values = []

# List all files with the specified format
# file_list =[ ["./data/output/VaN_50_L_20_noise_0.5.txt", "./data/output/VaN_200_L_20_noise_0.5.txt", "./data/output/VaN_800_L_20_noise_0.5.txt"]
file_list = []
# List of colors to use for each dataset
color_list = ['b', 'g', 'r']
# color_list = ['b', 'g', 'r', 'c', 'm']

L=[20, 20, 20]
N = [400, 1200, 2800]
noise = [2.5, 2.5, 2.5]
for i in range(len(N)):
    file_list.append(f"./data/output/VaN_{N[i]}_L_{L[i]}_noise_{noise[i]}.txt")
# Loop through each file and read data
for idx, file_name in enumerate(file_list):
    with open(file_name, "r") as file:
        lines = file.readlines()
        for line in lines:
            x, y = map(float, line.strip().split())
            x_values.append(x)
            y_values.append(y)
    
    # Create the plot for each dataset with a different color
    density = N[idx] / (L[idx] * L[idx])
    plt.plot(x_values, y_values, marker='o', linestyle='-', color=color_list[idx], label=f'Density {density:.3f}', markersize=1)
    
    # Reset lists for the next dataset
    x_values = []
    y_values = []

plt.xlabel('Iteration')
plt.ylabel('Va')
plt.legend()
plt.grid(True)
plt.savefig('./data/output/graphs/va.png')
