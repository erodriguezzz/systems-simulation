class Grid:

    def __init__(self, rows, columns):
        self.rows = rows
        self.columns = columns
        self.grid = [[Cell(row, col) for col in range(columns)] for row in range(rows)]

    def get_cell(self, row, column):
        return self.grid[row][column]

    def get_number_of_rows(self):
        return self.rows

    def get_number_of_columns(self):
        return self.columns
    
    def add_to_cell(self, row, column, particle):
        self.grid[row][column].add_particle(particle)
    
    def get_particles(self, x, y):
        return [particle for particle in self.grid[x][y].get_particles()]
    
    def __str__(self):
        str_grid = [['' for _ in range(self.columns)] for _ in range(self.rows)]

        for row in range(self.rows):
            for col in range(self.columns):
                cell_values = self.grid[row][col].get_particles()
                if len(cell_values) > 0:
                    str_grid[row][col] = f"[{', '.join(str(cell_value) for cell_value in cell_values)}]"

        col_widths = [max(len(str_grid[row][col]) for row in range(self.rows)) for col in range(self.columns)]

        grid_str = ''
        for row in range(self.rows):
            grid_str += '+' + '+'.join(['-' * (col_widths[col] + 2) for col in range(self.columns)]) + '+\n'
            grid_str += '| ' + ' | '.join(str_grid[row][col].ljust(col_widths[col]) for col in range(self.columns)) + ' |\n'
        grid_str += '+' + '+'.join(['-' * (col_widths[col] + 2) for col in range(self.columns)]) + '+'

        return grid_str

class Cell:
    def __init__(self, row, col):
        self.row = row
        self.col = col
        self.list = []
    
    def add_particle(self, particle):
        self.list.append(particle)
    
    def get_particles(self):
        return self.list
    
    def get_head(self):
        return self.head
    
    def __str__(self):
        return (str(particle) for particle in self.list)

class Particle:

    def __init__(self, name):
        self.name = name
        self.neighbours = [] # This could be a set to make sure the same particle is not added twice, but if the algorithm is implemented correctly it should not be necessary
    
    def add_neighbour(self, particle):
        self.neighbours.append(particle)

    def __str__(self):
        return self.name
    
    def get_neighbours(self):
        return self.neighbours