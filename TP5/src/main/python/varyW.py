import matplotlib.pyplot as plt
import numpy as np

def getTimes(path):
    with open(path) as file:
        timesStr = file.read()

    times = list(map(float, timesStr.split('\n')))

    return np.array(times)

x1 = getTimes('../data/output/timeFMLM/time_d=5.0_w=5.0_v=0.dump')
x2 = getTimes('../data/output/timeFMLM/time_d=5.0_w=10.0_v=0.dump')
x3 = getTimes('../data/output/timeFMLM/time_d=5.0_w=15.0_v=0.dump')
x4 = getTimes('../data/output/timeFMLM/time_d=5.0_w=20.0_v=0.dump')
x5 = getTimes('../data/output/timeFMLM/time_d=5.0_w=30.0_v=0.dump')
x6 = getTimes('../data/output/timeFMLM/time_d=5.0_w=50.0_v=0.dump')


errors = []
Qs = []

plt.xlabel('Frecuencia (rad/s)')
plt.ylabel('Q (p/s)')
fs = [5, 10, 15, 20, 30, 50]

# for x, label in zip([x1,x3,x6], ['5',  '15',  '50']):
for x, label in zip([x1, x2, x3, x4,x5,x6], ['5', '10', '15', '20', '30', '50']):
    Q = (len(x))/(x[-1]-x[0])
    Qs.append(Q)

    # regresion lineal de los datos
    m, b = np.polyfit(x, range(len(x)), 1)
    # errors.append(b)
    # Qs.append(m)
    # bs.append(b)

    mean = np.mean(x)

    f = []
    for i in range(len(x)):
        f.append(Q*x[i])

    S = np.sqrt(np.sum((x-f)**2)/(len(x)-2))

    Sxx = np.sum((x - mean)**2)

    error = S / np.sqrt(Sxx)

    errors.append(error)

    #no me agrega la linea, no se porque
plt.plot(fs, Qs, marker='o', linestyle='-', color='red')
plt.errorbar(fs, Qs, yerr=errors, label="w = " + label, color='blue')

plt.savefig('./varyw.png')