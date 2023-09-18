import numpy as np
import matplotlib.pyplot as plt
import math

N_values = [200, 250, 300]
L_values = [0.03, 0.05, 0.07, 0.09]
file_list = []
color_list = ['b', 'g', 'r', 'c', 'm']


def calculate_diffusion_coefficient(x, y):
    # Crear un arreglo de tiempo desde 0 hasta el número de elementos en DCM_values
    tiempo = np.arange(len(x))

    # Realizar una regresión lineal para obtener la pendiente y el intercepto
    pendiente, _ = np.polyfit(x[0:len(tiempo)], y, 1)

    return pendiente, _


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
            pressure_perL.append(2*np.mean(pressure))
            stds.append(np.std(pressure))
    # print(N[iteration], L[iteration], len(noise), (vas))
    x=[]
    x.append(0)
    x = [1/(0.09*0.09 + 0.09*L) for L in L_values]
    plt.scatter(x, pressure_perL, marker='o', linestyle='-', color=color_list[iteration], label=f'N= {N_values[iteration]} ')
    plt.errorbar(x, pressure_perL, yerr=stds, fmt='o', color=color_list[iteration], ecolor=color_list[iteration])
    
    # pendiente, b = calculate_diffusion_coefficient(x, pressure_perL)
    x = [abs(1/(0.09*0.09 + 0.09*L) - 1/(0.09*0.09 + 0.09*0.03)) for L in L_values]
    c= [x* 0.0001 for x in range(-500, 2000)]
    pres1 = pressure_perL[0]
    pressure_perL = [abs(yi - pres1) for yi in pressure_perL]
    ec = []
    for ci in c:
        ec.append(sum([math.pow(yi-pressure_perL[0] - ci*xi,2) for xi, yi in zip(x, pressure_perL)]))
    # plt.scatter(x, pressure_perL, marker='o', linestyle='-', color=color_list[iteration], label=f'N= {N_values[iteration]} ')
    plt.plot(c, ec, marker='o', markersize=0.1 ,linestyle='-', color=color_list[iteration], label=f'N= {N_values[iteration]} ')
    for(eci, ci) in zip(ec, c):
        if eci == min(ec):
           pendiente = ci

    y_regresion = np.array(x) *  pendiente  # y = mx 
    print(pendiente)
    plt.plot(x, y_regresion, color=color_list[iteration], label=f'Ajuste del modelo Lineal (m={pendiente:.4f})')
    pressurre=[]
    stds=[]


plt.xlabel('Inversa del área ($\\frac{1}{m^2}$)')
plt.ylabel('Presion ($\\frac{kg}{m \\cdot s^2}$)')
plt.legend()
plt.grid(True)
plt.savefig('./data/output/graphs/regresionPvsA-1.png')

plt.cla()