import matplotlib.pyplot as plt
from utils.utils import mse, analytic_solution
from typing import Dict


def plot_oscillator(data: Dict, index, filename):
    plt.cla()

    linestyles = {
        'verlet': 'dashed',
        'beeman': 'dotted',
        'gear': 'dashdot'
    }

    analytic_dt, tf = 0.00001, 5
    analytic = analytic_solution(analytic_dt, tf)
    analytic_time = [i * analytic_dt for i in range(len(analytic))]

    ax = plt.gca()

    ax.set_xlabel(r'Time ($s$)')
    ax.set_ylabel(r'Position ($m$)')

    plt.xlim(0, 4)
    plt.ylim((-1, 1))

    ax.plot(analytic_time, analytic, color='orange', label='Exact', alpha=0.5)
    for key in data.keys():
        dt, tf, position = data[key][index]
        time = [dt * i for i in range(len(position))]
        ax.plot(time, position, linestyle=linestyles[key], label=key)

    ax.legend()

    plt.savefig(f'data/output/{filename}')


def plot_error(data: Dict, filename: str):
    dts, tf = [0.01, 0.001, 0.0001, 0.00001, 0.000001], 5
    errors = {}
    for key in data.keys():
        errors[key] = []

    for i, dt in enumerate(dts):
        analytic = analytic_solution(dt, tf)
        for key in data.keys():
            errors[key].append(mse(analytic, data[key][i + 1][2]))

    ax = plt.gca()
    plt.xscale('log')
    plt.yscale('log')
    ax.set_xlabel(r'$\Delta t$ ($s$)')
    ax.set_ylabel(r'MSE')

    for key in errors:
        ax.plot(dts, errors[key], label=key, marker='o')

    ax.legend()

    plt.savefig(f'data/output/{filename}')