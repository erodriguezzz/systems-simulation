package TP1;

import java.util.ArrayList;
import java.util.List;

public class CIM {
    public static void setNeighbours(Cell cell, Grid grid) {
        List<Particle> particles = cell.getParticles();

        // Handle particles of the same cell separately so as to make sure they are not added twice
        for (Particle particle : particles) {
            for (Particle part : particles) {
                if (!part.equals(particle)) {
                    particle.addNeighbour(part);
                }
            }
        }

        // Add particles of neighbor cells
        int x = cell.getRow();
        int y = cell.getCol();
        List<Particle> neighbours = new ArrayList<>();
        for (int dx = 0; dx <= 1; dx++){
            for(int dy = -1; dy <=1; dy++) {
                if (y + dy >= 0 && !(dx == 0 && dy == 0) && !(dy == -1 && dx == 0) && x + dx < grid.getNumberOfRows() && y + dy < grid.getNumberOfColumns()) {
                    neighbours.addAll(grid.getParticles(x + dx, y + dy));
                }
            }
        }
        for (Particle particle : particles) {
            for (Particle part : neighbours) {
                particle.addNeighbour(part);
                part.addNeighbour(particle);
            }
        }
    }

    // Sets the neighbors for all particles in a grid using the Cell Index Method
    public static void setAllNeighbours(Grid grid) {
        for (int row = 0; row < grid.getNumberOfRows(); row++) {
            for (int col = 0; col < grid.getNumberOfColumns(); col++) {
                Cell cell = grid.getCell(row, col);
                setNeighbours(cell, grid);
            }
        }
    }
}