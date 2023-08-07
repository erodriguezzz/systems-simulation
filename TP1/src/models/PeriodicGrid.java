package models;

import java.util.Set;

public class PeriodicGrid extends Grid{
    public PeriodicGrid(float rows, int columns) {
        super(rows, columns);
    }

    public void setAllNeighbours(double rc, boolean cim) {
        for (int row = 0; row < getNumberOfRows(); row++) {
            for (int col = 0; col < getNumberOfRows(); col++) {
                Cell cell = getCell(row, col);
                if (cim)
                    CIM(cell, rc);
                else
                    bruteForce(cell, rc);
            }
        }
    }

    private void CIM(Cell cell, double rc) {
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
                if (y + dy >= 0 && !(dx == 0 && dy == 0) && !(dy == -1 && dx == 0)) {
                    int row = x + dx, col = y + dy;
                    boolean exceedX = false, exceedY = false;
                    if (row >= getNumberOfRows()) {
                        row = 0;
                        exceedX = true;
                    }
                    if (col >= getNumberOfRows()) {
                        col = 0;
                        exceedY = true;
                    }
                    // java 8 final variables
                    final int cellRow = row;
                    final int cellCol = col;
                    final boolean invertX = exceedX, invertY = exceedY;
                    // for each particle in cell, add all particles in neighbour cell
                    // check dist(p_a, p_b) - ra - rb <= rc
                    particles.forEach(
                            particle -> {
                                getCell(cellRow, cellCol).getParticles().forEach(
                                        part -> {
                                            if (getDistance(particle, part, invertX, invertY) <= rc ) { //TODO: calcular bien la distancia en los límites
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

    private double getDistance(Particle p1, Particle p2, boolean invertX, boolean invertY) {
        if (invertX || invertY) {
            float newX, newY;
            newX = invertX ? (getSize() - p1.getX()) * -1 : p1.getX();
            newY = invertY ? (getSize() - p1.getY()) * -1 : p1.getY();
            return getDistance(new Particle(p1.getId(), newX, newY, p1.getRadius()), p2);
        }

        return getDistance(p1, p2);
    }

    private void bruteForce(Cell cell, double rc){
        //TODO: implementar algoritmo. Puede que no sea necesario que reciba la Cell
    }


}
