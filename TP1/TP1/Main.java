package TP1;

import java.util.Random;

import static TP1.CIM.setAllNeighbours;

public class Main {

    private static final int L = 15;
    private static final int N = 50;
    private static final double rc= 0.25;


    // Creates a default 9x9 grid with 4 particles
    public static Grid populateDefaultGrid() {
        Grid grid = new Grid(9, 9);
        Particle p1 = new Particle("p1", 1);
        Particle p2 = new Particle("p2", 1);
        Particle p3 = new Particle("p3", 1);
        Particle p4 = new Particle("p4", 1);
        grid.addToCell(1, 1, p1);
        grid.addToCell(1, 1, p2);
        grid.addToCell(1, 2, p3);
        grid.addToCell(5, 5, p4);
        return grid;
    }

    // Creates a rows x cols grid with particleQty particles, randomly placed
    public static Grid populateRandomGrid(int rows, int cols, int particleQty) {
        Grid grid = new Grid(rows, cols);
        Random random = new Random();
        for (int i = 1; i <= particleQty; i++) {
            String particleName = "p" + i;
            int particleX = random.nextInt(rows);
            int particleY = random.nextInt(cols);
            Particle particle = new Particle(particleName, 1);
            grid.addToCell(particleX, particleY, particle);
        }
        return grid;
    }

    public static void main(String[] args) {
        // Grid grid = populateDefaultGrid();
        Grid grid = populateRandomGrid(L, L, N);

        System.out.println(grid);

        // Print each particle's neighbors (should be empty, neighbors have not been set yet)
        /*
        for (int row = 0; row < grid.getNumberOfRows(); row++) {
            for (int column = 0; column < grid.getNumberOfColumns(); column++) {
                for (Particle particle : grid.getCell(row, column).getParticles()) {
                    System.out.println("Neighbours of particle " + particle + ": ");
                    System.out.println();
                }
            }
        }
        System.out.println("-------------------------------------------------------------------------------");
        */
        setAllNeighbours(grid);

        // Print each particle's neighbors
        for (int row = 0; row < grid.getNumberOfRows(); row++) {
            for (int column = 0; column < grid.getNumberOfColumns(); column++) {
                for (Particle particle : grid.getCell(row, column).getParticles()) {
                    System.out.println("Neighbours of particle " + particle + ": " + particle.getNeighbours());
                }
            }
        }
    }
}