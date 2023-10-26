import random
import math

DOMAIN_HEIGHT = 70
DOMAIN_WIDTH = 20

def generate_particles(N, mass, v):
    with open(f"./data/input/Static_N_{N}_v_{v}.dump", "w") as f:
        positions = set()
        for _ in range(N):
            while True:
                x = random.uniform(1.15, DOMAIN_WIDTH - 1.15)
                y = random.uniform(1.15, DOMAIN_HEIGHT - 1.15)
                radius = random.uniform(0.85, 1.15)
                position = (x, y, radius)
                if all(
                    math.sqrt((float(x) - float(px))**2 + (float(y) - float(py))**2) >= radius + pradius
                    for px, py, pradius in positions
                ):
                    break
            positions.add(position)
            f.write(f"{radius}   {mass}\n")

        with open(f"./data/input/Dynamic_N_{N}_v_{v}.dump", "w") as f:
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
                id  = id + 1

N_values = [2]

for N in N_values:
    for v in range(0, 1):
        generate_particles(N, 1, v)
        print("N:" + str(N)+ " finished")

print("Files generated.")
