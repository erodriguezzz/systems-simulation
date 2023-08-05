from models import Grid, Cell
from itertools import product

# Sets the neighbours for all the particles in a cell using the Cell Index Method
def set_neighbours(cell: Cell, grid: Grid):
    particles = cell.get_particles()

    # Handle particles of the same cell separately so as to make sure they are not added twice
    [particle.add_neighbour(part) for particle, part in product(particles, particles) if not part == particle]
 
    # Add particles of neighbour cells
    x, y = cell.row, cell.col
    neighbours = [
        par for dy, dx in product([-1, 0, 1], [0,1])
        if y + dy >= 0 and not ( (dx==0 and dy==0) or (dy == -1 and dx == 0) or x + dx >= grid.get_number_of_rows() or y + dy >= grid.get_number_of_columns())
        for par in grid.get_particles(x + dx, y + dy)
    ]

    for (particle, part) in product(particles, neighbours):
        particle.add_neighbour(part)
        part.add_neighbour(particle)
    
    
# Sets the neighbours for all particles in a grid using the Cell Index Method
def set_all_neighbours(grid: Grid):
    [set_neighbours(grid.get_cell(row, col), grid) for row, col in product(range(grid.get_number_of_rows()) ,range(grid.get_number_of_columns()))]