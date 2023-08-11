import models.*;
import services.DataManager;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Simulation {
    // private static final int M = 15;
    private static final double rc = 5;

    public static Grid populateDefaultGrid() {
        Grid grid = new NonPeriodicGrid(9, 9);
        Particle p1 = new Particle(1, 2.4f, 2.4f, 0.1f);
        Particle p2 = new Particle(2, 1.2f, 2, 0.1f);
        Particle p3 = new Particle(3, 0.55f, 0.55f, 0.1f);
        Particle p4 = new Particle(4, 6, 4, 0.1f);
        grid.addParticles(new HashSet<>(Arrays.asList(p1, p2, p3, p4)));
        return grid;
    }

    public static Grid populateRandomGrid(float size, int cells, int particleQty, boolean isPeriodic) {
        // TODO: fix random generator
        Set<Particle> particles = new HashSet<>();
        Random random = new Random();
        for (int i = 1; i <= particleQty; i++) {
            float particleX = random.nextFloat() * size;
            float particleY = random.nextFloat() * size;
            float particleRadius = random.nextFloat() * 10 + 1.5f;
            particles.add(new Particle(i, particleX, particleY, particleRadius));
        }
        // Get the max radius for the particles in particles
        float maxRadius = particles.stream().map(Particle::getRadius).max(Float::compareTo).get();
        return populateGrid(size, (int) Math.floor(size/(rc + maxRadius)), particles, isPeriodic);
    }

    public static Grid populateGrid(float size, int cells, Set<Particle> particles, boolean isPeriodic) {
        Grid grid;
        if (isPeriodic)
            grid = new PeriodicGrid(size, cells);
        else
            grid = new NonPeriodicGrid(size, cells);
        grid.addParticles(particles);
        return grid;
    }

    public static void gridToXYZ(Grid grid, int N, int particleId) {
        try (FileWriter writer = new FileWriter("./data/output/particles.xyz")) {
            writer.append(N + "\n\n"); // Write the total number of particles as the first line

            for (int row = 0; row < grid.getNumberOfRows(); row++) {
                for (int col = 0; col < grid.getNumberOfRows(); col++) {
                    grid.getCell(row, col).getParticles().forEach(particle -> {
                        try {
                            String line = null;
                            if (particle.getId() == particleId) {
                                line = String.format("%.4f %.4f 0 255 255 %.4f\n", particle.getX(), particle.getY(), particle.getRadius());
                            }
                            // Write the particle element symbol, and the x, y, and z coordinates separated
                            // by space
                            line = String.format("%.4f %.4f 255 255 0 0.2\n", particle.getX(), particle.getY());
                            writer.append(line);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        // Grid grid = populateDefaultGrid();
        DataManager dm = new DataManager("./data/input/Static100.txt", "./data/input/Dynamic100.txt");

        int M = (int) Math.floor(dm.getL() / (rc + 2* dm.getMaxRadius()));
        System.out.println("M: " + M);
        Grid grid = populateGrid(dm.getL(), M, dm.getParticles(), true);
        try {
            FileWriter writer = new FileWriter("./data/output/vecinos.xyz");

            System.out.println(grid);
            long start = System.currentTimeMillis();
            // grid.setAllNeighbours(rc, false);
            long end = System.currentTimeMillis();
            System.out.println("Time elapsed (brute force): " + (end - start) + " ms");
            grid.clearAllNeighbours();
            start = System.currentTimeMillis();
            grid.setAllNeighbours(rc, true);
            end = System.currentTimeMillis();
            System.out.println("Time elapsed (cell index): " + (end - start) + " ms");
            // Print each particle's neighbors
            for (int row = 0; row < M; row++) {
                for (int column = 0; column < M; column++) {
                    grid.getCell(row, column).getParticles().forEach(particle -> {
                        System.out.println("Particle " + particle + " neighbors: " + particle.getNeighbours());
                        try {
                            // writer.append("Particle " + particle.getId() + " neighbors: " + particle.getNeighbours().stream().collect(n -> n.getId()) + "\n");
                            writer.append(particle.getId() + ";" + particle.getRadius() + ";" + particle.getX() + ";" + particle.getY() + ";" +particle.getNeighbours().stream().map(neighbor -> String.valueOf(neighbor.getId())).collect(Collectors.joining(", ")) + "\n");

                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    });
                }
            }
            writer.close();
            gridToXYZ(grid, dm.getN(), 4);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
