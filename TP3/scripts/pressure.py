import matplotlib.pyplot as plt

def plot_pressure(N_particles, L_size, v):
    for N in N_particles:
        for L in L_size:
            x_values = []
            y1_values = []
            y2_values = []

            with open('./data/output/Pressure_' + str(N) + '_L_' + str(L) + '_v' + str(v) +'.txt', 'r') as file:
                for line in file:
                    parts = line.strip().split()
                    if len(parts) == 3:
                        x_values.append(float(parts[0]))
                        y1_values.append(float(parts[1]))
                        y2_values.append(float(parts[2]))
                        if float(parts[0]) > 200:
                            break

            # Create the plot
            plt.figure(figsize=(8, 6))
            plt.plot(x_values, y1_values, label='left' + str(L))
            plt.plot(x_values, y2_values, label='right'+ str(L))
            plt.xlabel('Time')
            plt.ylabel('Pressure')
            plt.legend()
            plt.title('Pressure')
            plt.grid(True)
            plt.savefig(f'./data/output/graphs/PressurevsTime_N_{N}_L_{L}.png')
            plt.close()  # Close the current figure

# N=[200, 230, 240, 250]
N=[200]
L=[0.03, 0.05, 0.07, 0.09]

# for v in range(1, 5):
plot_pressure(N, L, 1)
