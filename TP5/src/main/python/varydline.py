import matplotlib.pyplot as plt
import numpy as np

colors = ['r', 'g', 'b', 'y', 'm', 'c']
def getTimes(path):
    with open(path) as file:
        timesStr = file.readlines()

    times = []
    for line in timesStr:
        times.append(float(line))

    data = np.array(times)

    return data

x1 = getTimes('../data/output/time_d=3.0_w=20.0_v=0.dump')
x2 = getTimes('../data/output/time_d=4.0_w=20.0_v=0.dump')
x3 = getTimes('../data/output/time_d=5.0_w=20.0_v=0.dump')
x4 = getTimes('../data/output/time_d=6.0_w=20.0_v=0.dump')


for x, label, color in zip([x1, x2, x3, x4], ['3', '4', '5', '6'], colors):
# for x, label, color in zip([x1,x3,x6], ['5',  '15',  '50'], colors):
    count, limits = np.histogram(x, bins=1000)

    accumCount = np.cumsum(count)

    plt.step(limits[:-1], accumCount, where='post', label=label + " cm", color=color)

plt.xlabel('Tiempo (s)')
plt.ylabel('Descarga (particulas)')
plt.legend()


plt.savefig('./varydline.png')
