import math
import random

def generate_file(L, N, v, iteration):
    with open("./data/input/Static_N_" + str(N) + "_L_" + str(L) + "_v_" + str(iteration) + ".txt", "w") as f:
        f.write(str(L) + "\n")
        f.write(str(N) + "\n")
        for _ in range(N):
            # random_num = random.uniform(0, 2 * math.pi)  # Adjust the range as needed
            f.write( "0  " + str(v) + "\n")
    
    with open("./data/input/Dynamic_N_" + str(N) + "_L_" + str(L) + "_v_" + str(iteration) + ".txt", "w") as f:
        f.write("0" + "\n")
        for _ in range(N):
            x = format(random.uniform(0, L), ".7e")
            y = format(random.uniform(0, L), ".7e")
            theta = random.uniform(0, 2 * math.pi)  # Adjust the range as needed
            f.write(f"{x}   {y}   {theta}\n")


# v = float(input("Enter v: "))
L=[5, 7, 10, 14, 20]
N = [50, 100, 200, 400, 800]
# for iteration in range(1, 6):
for i in range(len(N)):
    generate_file(L[i], N[i], 0.03, 1)

l = 20
N = [50, 100, 200, 400, 800]
for i in range(len(N)):
    generate_file(l, N[i], 0.03, 1)
print("Files generated.")
