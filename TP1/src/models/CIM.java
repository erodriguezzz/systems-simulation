package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * TODO:
 * creo que deberíamos cambiar esta clase. Cambié la logica para que cada particula
 * tenga definida su propias coordenadas, y a partir de la misma la grilla sabra en qué
 * celda ubicarla.
 */
public class CIM {
    public static void setNeighbours(Cell cell, Grid grid) {
        Set<Particle> particles = cell.getParticles();

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
                if (y + dy >= 0 && !(dx == 0 && dy == 0) && !(dy == -1 && dx == 0) && x + dx < grid.getGridSize() && y + dy < grid.getGridSize()) {
                    neighbours.addAll(grid.getCell(x + dx, y + dy).getParticles());
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
        for (int row = 0; row < grid.getGridSize(); row++) {
            for (int col = 0; col < grid.getGridSize(); col++) {
                Cell cell = grid.getCell(row, col);
                setNeighbours(cell, grid);
            }
        }
    }
}