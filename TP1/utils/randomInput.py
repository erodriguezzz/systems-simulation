import random

def generate_random_float():
    return random.uniform(0, 100)

def generate_random_file(filename, num_rows):
    with open(filename, 'a') as file:
        file.write(f"0/n")
        for _ in range(num_rows):
            x = generate_random_float()
            y = generate_random_float()
            file.write(f"{x:.7e}   {y:.7e}\n")

if __name__ == "__main__":
    num_rows = 100
    for i in range(1, 5):
        filename = "./TP1/data/input/dynamic" + str(i) + ".txt"
        generate_random_file(filename, num_rows)
        print(f"Random file '{filename}' with {num_rows} rows generated.")

