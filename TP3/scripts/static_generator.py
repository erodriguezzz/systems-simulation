import math
import random

DOMAIN_LENGTH = 0.09
N=[200, 230, 240, 250]
L=[0.03, 0.05, 0.07, 0.09]
UNIQUE = True

def generate_file(Nparticles, radius, v, mass, version):
    with open("./data/input/Static_N_" + str(Nparticles) +"_v_" + str(version) + ".dump", "w") as f:
        positions = set()
        for _ in range(Nparticles):
            counter = 0
            id = 0
            while True:
                x = random.uniform(radius, DOMAIN_LENGTH - radius)
                y = random.uniform(radius, DOMAIN_LENGTH - radius)
                position = (x, y)
                if all(math.sqrt(math.pow((x - px),2) + math.pow((y - py),2)) >= 2 * radius + 0.001 for px, py in positions):
                    print(f"({id} {counter})")
                    break
                counter = counter + 1
                id = id + 1
            positions.add(position)
            f.write(f"{radius}   {mass}\n")

    with open("./data/input/Dynamic_N_" + str(Nparticles) +"_v_" + str(version) + ".dump", "w") as f:
        f.write(str(Nparticles) + "\n")
        f.write(str(Nparticles + 4*900) + "\n")
        id = 1
        for x, y in positions:
            theta = random.uniform(0, 2 * math.pi)
            vx = v * math.cos(theta)
            vy = v * math.sin(theta)
            f.write(f"{id} {x}   {y}   {vx}  {vy}\n")
            id = id + 1
        # x = 0.0
        # y = 0.0
        # while (x < DOMAIN_LENGTH):
        #     f.write(f"{x}   0   0   0   0.0001\n")
        #     f.write(f"0   {x}   0   0   0.0001\n")
        #     f.write(f"{DOMAIN_LENGTH}  {x}   0   0   0.0001\n")
        #     f.write(f"{x}   {DOMAIN_LENGTH}   0   0   0.0001\n")
        #     x += 0.0001

if UNIQUE:
    generate_file(N[0], 0.0015, 0.01, 1)
else:
    for i in range(len(N)):
        generate_file(N[i], 0.0015, 0.01, 1)
print("Files generated.")
