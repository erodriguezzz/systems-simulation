package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NonPeriodicGrid extends Grid{
    public NonPeriodicGrid(float rows, int columns) {
        super(rows, columns);
    }

    @Override
    protected void CIM(Cell cell, double rc) {
        Set<Particle> particles = cell.getParticles();

        // Handle particles of the same cell separately to make sure they are not added twice
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
        for (int dx = 0; dx <= 1; dx++){
            for(int dy = -1; dy <=1; dy++) {
                if (y + dy >= 0 && !(dx == 0 && dy == 0) && !(dy == -1 && dx == 0) && x + dx < getNumberOfRows() && y + dy < getNumberOfRows()) {
                    // java 8 final variables
                    final int deltaX = dx;
                    final int deltaY = dy;
                    // for each particle in cell, add all particles in neighbour cell
                    // check dist(p_a, p_b) - ra - rb <= rc
                    particles.forEach(
                            particle -> {
                                getCell(x + deltaX, y + deltaY).getParticles().forEach(
                                        part -> {
                                            if (getDistance(particle, part) <= rc ) {
                                                particle.addNeighbour(part);
                                                part.addNeighbour(particle);
                                            }
                                        }
                                );
                            }
                    );
                }
            }
        }
    }

    protected void bruteForce(Cell[][] cells, double rc) {
        Set<Particle> particles = new HashSet<>();
        for (Cell[] row : cells)
            for (Cell cell :row)
                particles.addAll(cell.getParticles());
        for (Particle particle : particles)
            for (Particle part : particles)
                if (!part.equals(particle) && getDistance(particle, part) <= rc) {
                    particle.addNeighbour(part);
                    part.addNeighbour(particle);
                }
    }



}
