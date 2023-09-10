import math
import random

DOMAIN_LENGTH = 0.09

def generate_file(N, radius, v, mass):
    with open("./data/input/Static_N_" + str(N) +".dump", "w") as f:
        positions = set()
        for _ in range(N):
            while True:
                x = format(random.uniform(radius, DOMAIN_LENGTH - radius), ".7e")
                y = format(random.uniform(radius, DOMAIN_LENGTH - radius), ".7e")
                position = (x, y)
                if all(math.sqrt((float(x) - float(px))**2 + (float(y) - float(py))**2) >= 2 * radius for px, py in positions):
                    break
            positions.add(position)
            f.write(f"{radius}   {mass}\n")
    
    with open("./data/input/Dynamic_N_" + str(N) + ".dump", "w") as f:
        f.write("0" + "\n")
        for x, y in positions:
            theta = random.uniform(0, 2 * math.pi)
            vx = v * math.cos(theta)
            vy = v * math.sin(theta)
            f.write(f"{x}   {y}   {vx}  {vy}\n")

N = [200, 500]

for i in range(len(N)):
    generate_file(N[i], 0.0015, 0.01, 1)
print("Files generated.")
