import math
import random

def generate_file(L, N, radius, v, mass):
    with open("./data/input/Static_N_" + str(N) + "_L_" + str(L) + ".txt", "w") as f:
        f.write(str(L) + "\n")
        f.write(str(N) + "\n")
        positions = set()
        id = 0
        for _ in range(N):
            while True:
                x = format(random.uniform(radius, L - radius), ".7e")
                y = format(random.uniform(radius, L - radius), ".7e")
                position = (x, y)
                if all(math.sqrt((float(x) - float(px))**2 + (float(y) - float(py))**2) >= 2 * radius for px, py in positions):
                    break
            positions.add(position)
            f.write(f"{id}  0  {x}  {y}  {radius}  {mass}\n")
            id = id+1
    
    with open("./data/input/Dynamic_N_" + str(N) + "_L_" + str(L) + ".txt", "w") as f:
        f.write("0" + "\n")
        for x, y in positions:
            theta = random.uniform(0, 2 * math.pi)
            vx = v * math.cos(theta)
            vy = v * math.sin(theta)
            f.write(f"0 {x}   {y}   {vx}  {vy}\n")

L = [5, 7, 14]
N = [200, 500, 1000]

for i in range(len(N)):
    generate_file(L[i], N[i], 0.0015, 0.01, 1)
print("Files generated.")
