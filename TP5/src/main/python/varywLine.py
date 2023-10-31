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

x1 = getTimes('../data/output/timeFMLM/time_d=5.0_w=5.0_v=0.dump')
x2 = getTimes('../data/output/timeFMLM/time_d=5.0_w=10.0_v=0.dump')
x3 = getTimes('../data/output/timeFMLM/time_d=5.0_w=15.0_v=0.dump')
x4 = getTimes('../data/output/timeFMLM/time_d=5.0_w=20.0_v=0.dump')
x5 = getTimes('../data/output/timeFMLM/time_d=5.0_w=30.0_v=0.dump')
x6 = getTimes('../data/output/timeFMLM/time_d=5.0_w=50.0_v=0.dump')


for x, label, color in zip([x1, x2, x3, x4, x5,x6], ['5', '10', '15', '20', '30', '50'], colors):
# for x, label, color in zip([x1,x3,x6], ['5',  '15',  '50'], colors):
    count, limits = np.histogram(x, bins=1000)

    accumCount = np.cumsum(count)

    plt.step(limits[:-1], accumCount, where='post', label=label + " rad/s", color=color)

plt.xlabel('Tiempo (s)')
plt.ylabel('Descarga (particulas)')
plt.legend()


plt.savefig('./varywline.png')
