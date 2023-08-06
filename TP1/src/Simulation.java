import models.*;
import java.util.*;

public class Simulation {
    private static final int L = 15;
    private static final int M = 15;
    private static final int N = 50;
    private static final double rc= 0.25;


    public static Grid populateDefaultGrid() {
        Grid grid = new Grid(9, 9);
        Particle p1 = new Particle(1, 2.4f, 2.4f, 0.1f);
        Particle p2 = new Particle(2, 1.2f, 2, 0.1f);
        Particle p3 = new Particle(3, 0.55f, 0.55f, 0.1f);
        Particle p4 = new Particle(4, 6, 4, 0.1f);
        grid.addParticles(new HashSet<>(Arrays.asList(p1, p2, p3, p4)));
        return grid;
    }

    public static Grid populateRandomGrid(int rows, int cols, int particleQty) {
        Grid grid = new Grid(rows, cols);
        Set<Particle> particles = new HashSet<>();
        Random random = new Random();
        for (int i = 1; i <= particleQty; i++) {
            float particleX = random.nextFloat() * cols;
            float particleY = random.nextFloat() * cols;
            particles.add(new Particle(i, particleX, particleY, 0.1f));
        }
        grid.addParticles(particles);
        return grid;
    }

    public static void main(String[] args) {
        // Grid grid = populateDefaultGrid();
        Grid grid = populateRandomGrid(M, M, N);

        System.out.println(grid);
        CIM.setAllNeighbours(grid);
        // Print each particle's neighbors
        for (int row = 0; row < M; row++) {
            for (int column = 0; column < M; column++) {
                grid.getCell(row, column).getParticles().forEach(particle -> {
                    System.out.println("Particle " + particle + " neighbors: " + particle.getNeighbours());
                    // TODO: imprimir vecinos de la celda
                });
            }
        }
    }
}
