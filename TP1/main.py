from models import Particle, Grid
from CIM import set_neighbours, set_all_neighbours
import random

# Creates a default 9x9 grid with 4 particles
def populate_default_grid():
    grid = Grid(9,9)
    p1 = Particle("p1")
    p2 = Particle("p2")
    p3 = Particle("p3")
    p4 = Particle("p4")
    grid.add_to_cell(1,1,p1)
    grid.add_to_cell(1,1,p2)
    grid.add_to_cell(1,2,p3)
    grid.add_to_cell(5,5,p4)
    return grid

# Creates a rows x cols grid with particle_qty particles, randomly placed
def populate_random_grid(rows, cols, particle_qty):
    grid = Grid(rows, cols)
    for i in range(1, particle_qty):
        particle_name = f"p{i}"
        particle_x = random.randint(0, rows -1)
        particle_y = random.randint(0, cols -1)
        particle = Particle(particle_name)
        grid.add_to_cell(particle_x, particle_y, particle)
    return grid



def main():
    # grid = populate_default_grid()
    grid = populate_random_grid(10, 10, 30)
    

    print(grid)

    # Print each particle's neighbours (should be empty, neighbours have not been set yet)
    # print('\n'.join(f"Neighbours of particle {particle}: {', '.join(map(str, particle.get_neighbours()))}" for row in range(grid.get_number_of_rows()) for column in range(grid.get_number_of_columns()) for particle in grid.get_cell(row, column).get_particles()))
    # print("-------------------------------------------------------------------------------")

    set_all_neighbours(grid)
        
    #Print each particle's neighbours
    print('\n'.join(f"Neighbours of particle {particle}: {', '.join(map(str, particle.get_neighbours()))}" for row in range(grid.get_number_of_rows()) for column in range(grid.get_number_of_columns()) for particle in grid.get_cell(row, column).get_particles()))


if __name__ == "__main__":
    main()