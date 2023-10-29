import random
import math
import json

with open("../java/config.json", "r") as f:
    config = json.load(f)

N_values = config["Ns"]
R_MAX = config["Rmax"]
R_MIN = config["Rmin"]
MASS = config["Mass"]
DOMAIN_HEIGHT = int(config["M"])
DOMAIN_WIDTH = int(config["L"])


def generate_particles(N, mass, v):
    with open(f"../data/input/Static_N_{N}_v_{v}.dump", "w") as f:
        positions = set()
        for _ in range(int(N)):
            while True:
                x = random.uniform(R_MAX, DOMAIN_WIDTH - R_MAX)
                y = random.uniform(R_MAX, DOMAIN_HEIGHT - R_MAX)
                radius = random.uniform(R_MIN, R_MAX)
                position = (x, y, radius)
                if all(
                    math.sqrt((float(x) - float(px))**2 + (float(y) - float(py))**2) >= radius + pradius
                    for px, py, pradius in positions
                ):
                    break
            positions.add(position)
            f.write(f"{radius}   {mass}\n")

        with open(f"../data/input/Dynamic_N_{N}_v_{v}.dump", "w") as f:
            f.write(str(N) + "\n")
            f.write("0" + "\n")
            id = 0 
            for x, y, radius in positions:
                vx = 0
                # vx = v * math.cos(theta)
                # if N == 25:
                    # f.write(f"{id} {DOMAIN_LENGTH/25*id} {y} {vx} {0} {0} {radius} {mass} \n")
                # else:
                f.write(f"{id} {x} {y} {0} {0} {0} {radius} {mass} \n")
                id = id + 1


for N in N_values:
    for v in range(0, 1):
        generate_particles(int(N), 1, v)
        print("N:" + str(int(N)) + " finished")

print("Files generated.")
