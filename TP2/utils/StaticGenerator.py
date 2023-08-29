import math
import random

def generate_file(L, N, v):
    with open("./data/input/Static" + str(N) + ".txt", "w") as f:
        f.write(str(L) + "\n")
        f.write(str(N) + "\n")
        for _ in range(N):
            # random_num = random.uniform(0, 2 * math.pi)  # Adjust the range as needed
            f.write( "0  " + str(v) + "\n")
    
    with open("./data/input/Dynamic" + str(N) + ".txt", "w") as f:
        f.write("0" + "\n")
        for _ in range(N):
            x = format(random.uniform(0, L), ".7e")
            y = format(random.uniform(0, L), ".7e")
            theta = random.uniform(0, 2 * math.pi)  # Adjust the range as needed
            f.write(f"{x}   {y}   {theta}\n")

L = int(input("Enter L: "))
N = int(input("Enter N: "))
# v = float(input("Enter v: "))

generate_file(L, N, 0.3)
print("Files 'output.txt' and 'output_scientific.txt' generated.")
