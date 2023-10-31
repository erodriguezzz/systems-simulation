import matplotlib.pyplot as plt
import numpy as np

def getTimes(path):
    with open(path) as file:
        timesStr = file.read()

    times = list(map(float, timesStr.split('\n')))

    return np.array(times)

x1 = getTimes('../data/output/time_d=3.0_w=20.0_v=0.dump')
x2 = getTimes('../data/output/time_d=4.0_w=20.0_v=0.dump')
x3 = getTimes('../data/output/time_d=5.0_w=20.0_v=0.dump')
x4 = getTimes('../data/output/time_d=6.0_w=20.0_v=0.dump')


errors = []
Qs = []

plt.xlabel('Rendija (cm)')
plt.ylabel('Q (p/s)')
ds = [3, 4, 5, 6]

# for x, label in zip([x1,x3,x6], ['5',  '15',  '50']):
for x, label in zip([x1, x2, x3, x4], ['3', '4', '5', '6']):
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
plt.plot(ds, Qs, marker='o', linestyle='-', color='red')
plt.errorbar(ds, Qs, yerr=errors, label="w = " + label, color='blue')

plt.savefig('./varyd.png')