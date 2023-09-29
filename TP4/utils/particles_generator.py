import random
import math

DOMAIN_RADIUS = 21.49;

def generate_particles(N, radius, mass):
    with open(f"./data/input/Static_N_{N}.dump", "w") as f:
        positions = set()
        for _ in range(N):
            while True:
                theta = random.uniform(0, 2*math.pi)
                # y = format(random.uniform(radius, DOMAIN_LENGTH - radius), ".7e")
                x = math.cos(theta) * DOMAIN_RADIUS
                y = math.sin(theta) * DOMAIN_RADIUS
                position = (x, y)
                if all(
                    math.sqrt((float(x) - float(px))**2 + (float(y) - float(py))**2) >= 2 * radius
                    for px, py in positions
                ):
                    break
            positions.add(position)
            f.write(f"{radius}   {mass}\n")

    with open(f"./data/input/Dynamic_N_{N}.dump", "w") as f:
        f.write(str(N) + "\n")
        f.write("0" + "\n")
        for x, y in positions:
            vx = random.uniform(9, 12)
            # vx = v * math.cos(theta)
            f.write(f"{x}   {y}   {radius}  {vx} \n")

N_values = [20]

for N in N_values:
    generate_particles(N, 2.25, 0.0025)

print("Files generated.")
