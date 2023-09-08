import math
import random

def generate_file(L, N, radius, v, mass):
    with open("./data/input/Static_N_" + str(N) + "_L_" + str(L) + ".dump", "w") as f:
        positions = set()
        for _ in range(N):
            while True:
                x = format(random.uniform(radius, L - radius), ".7e")
                y = format(random.uniform(radius, L - radius), ".7e")
                position = (x, y)
                if all(math.sqrt((float(x) - float(px))**2 + (float(y) - float(py))**2) >= 2 * radius for px, py in positions):
                    break
            positions.add(position)
            f.write(f"{radius}   {mass}\n")
    
    with open("./data/input/Dynamic_N_" + str(N) + "_L_" + str(L) + ".dump", "w") as f:
        f.write("0" + "\n")
        for x, y in positions:
            theta = random.uniform(0, 2 * math.pi)
            vx = v * math.cos(theta)
            vy = v * math.sin(theta)
            f.write(f"{x}   {y}   {vx}  {vy}\n")

L = [0.03, 0.05, 0.07, 0.09]
N = [200, 500]

for i in range(len(N)):
    generate_file(L[i], N[i], 0.0015, 0.01, 1)
print("Files generated.")
