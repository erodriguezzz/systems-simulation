package models;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PeriodicGrid extends Grid{
    public PeriodicGrid(float rows, int columns) {
        super(rows, columns);
    }

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
        for (int dy = 0; dy <= 1; dy++){
            for(int dx = -1; dx <=1; dx++) {
                if (!(dx == 0 && dy == 0) && !(dx == -1 && dy == 0)) {

                    int row = x + dx, col = y + dy;
                    boolean exceedX = false, exceedY = false, borderX = false;
                    if (row >= getNumberOfRows()) {
                        row = 0;
                        exceedX = true;
                    }
                    if (col >= getNumberOfRows()) {
                        col = 0;
                        exceedY = true;
                    }
                    if (row < 0) {
                        row = (int) getNumberOfRows() - 1;
                        borderX = true;
                    }
                    if(cell.getCol() == getNumberOfRows() -1 && cell.getRow() == 0) {
                        System.out.println(getCell(row, col).getParticles());
                        System.out.println("row:"+ row + " col:"+ col);
                        if (row == 13 && col == 0) {
                            System.out.println("borderX: " + borderX);
                            System.out.println("exceedX: " + exceedX + " exceedY: " + exceedY);
                        }
                    }
                    // java 8 final variables
                    final int cellRow = row;
                    final int cellCol = col;
                    final boolean invertX = exceedX, invertY = exceedY, finalBorderX = borderX;
                    // for each particle in cell, add all particles in neighbour cell
                    // check dist(p_a, p_b) - ra - rb <= rc
                    particles.forEach(
                            particle -> {
                                getCell(cellRow, cellCol).getParticles().forEach(
                                        part -> {
                                            if (getDistance(particle, part, invertX, invertY, finalBorderX) <= rc ) { //TODO: calcular bien la distancia en los límites
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

    private double getDistance(Particle p1, Particle p2, boolean invertX, boolean invertY, boolean borderX) {
        float newX, newY;
        newX = borderX ? getSize() + p1.getX() : invertX ? (getSize() - p1.getX()) * -1 : p1.getX();
        newY = invertY ? (getSize() - p1.getY()) * -1 : p1.getY();
        return getDistance(new Particle(p1.getId(), newX, newY, p1.getRadius()), p2);
    }

    private double getBruteDistance(Particle p1, Particle p2) {
        // return Math.min(Math.min(Math.min(getDistance(p1, p2, false, false), getDistance(p1, p2, false, true)), getDistance(p1, p2, true, false)), getDistance(p1, p2, true, true));
        return 0;
    }

    protected void bruteForce(Cell[][] cells, double rc){
        Set<Particle> particles = new HashSet<>();
        Arrays.stream(cells).forEach(row -> Arrays.stream(row).forEach(cell -> particles.addAll(cell.getParticles())));
        particles.forEach(particle -> particles.forEach(part -> {
            if (!part.equals(particle) && getBruteDistance(particle, part) <= rc) {
                particle.addNeighbour(part);
                part.addNeighbour(particle);
            }
        }));
    }


}
