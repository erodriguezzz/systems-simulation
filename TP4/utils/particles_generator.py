import random
import math

DOMAIN_LENGTH = 135;

def generate_particles(N, radius, mass):
    with open(f"./data/input/Static2_N_{N}.dump", "w") as f:
        positions = set()
        for _ in range(N):
            while True:
                theta = random.uniform(0, 2*math.pi)
                # y = format(random.uniform(radius, DOMAIN_LENGTH - radius), ".7e")
                if (N == 30):
                    x = DOMAIN_LENGTH/(30)*(len(positions))
                else:
                    x = random.uniform(0, DOMAIN_LENGTH )
                y = 0
                position = (x, y)
                # if all(
                #     math.sqrt((float(x) - float(px))**2 + (float(y) - float(py))**2) >= 2 * radius
                #     for px, py in positions
                # ):
                break
            positions.add(position)
            f.write(f"{radius}   {mass}\n")

        with open(f"./data/input/Dynamic2_N_{N}.dump", "w") as f:
            f.write(str(N) + "\n")
            f.write("0" + "\n")
            id = 0 
            for x, y in positions:
                vx = random.uniform(9, 12)
                # vx = v * math.cos(theta)
                # if N == 25:
                    # f.write(f"{id} {DOMAIN_LENGTH/25*id} {y} {vx} {0} {0} {radius} {mass} \n")
                # else:
                f.write(f"{id} {x} {y} {vx} {0} {0} {radius} {mass} \n")
                id  = id + 1

N_values = [30]

for N in N_values:
    generate_particles(N, 2.25, 25)
    print("N:" + str(N)+ "finished")

print("Files generated.")
