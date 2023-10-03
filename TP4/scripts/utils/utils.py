from typing import List
import numpy as np
import math


def _analytic(A, gamma, mass, k, t):
    return A * math.exp(-(gamma * t) / (2 * mass)) * math.cos(
        math.sqrt((k / mass) - (gamma ** 2) / (4 * mass ** 2)) * t)


def analytic_solution(dt, tf):
    size = math.floor(tf / dt)
    return [_analytic(1, 100, 70, 10000, i * dt) for i in range(size)]


def read_oscillator_data(filename) -> (List[float]):
    positions = []
    with open(filename, 'r') as file:
        i = 1
        for line in file:
            if i % 2 == 0:
                # print(line.strip())
                positions.append(float(line.strip()))
            i += 1
    return positions


def mse(expected, actual):
    error = []
    for e, a in zip(expected, actual):
        error.append((e - a) ** 2)
    return np.mean(error)