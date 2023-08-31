package services;

import java.io.File;
import java.io.FileWriter;
import java.util.*;
import models.Particle;
import models.Velocity;

public class DataManager {
    private double max_radius = 0;
    private float L;
    private int N;
    private int time;
    private Set<Particle> particles = new HashSet<>();

    public DataManager(String staticPath, String dynamicPath, String[] output) {
        readParticlesFiles(staticPath, dynamicPath);
        for(String s : output) {
            clearOutputFile(s);
        }
    }

    public void writeVa(double time, double va, int N, int L, double noise) {
        try {
            File file = new File("./data/output/VaN_" + N + "_L_" + L + "_noise_" + noise +".txt");
            FileWriter writer = new FileWriter(file, true);
            writer.write(time + " " + va + "\n");
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void clearOutputFile(String output) {
        try {
            File file = new File(output);
            FileWriter writer = new FileWriter(file, false);
            writer.write("");
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void readParticlesFiles(String staticPath, String dynamicPath) {

        try {
            File staticFile = new File(staticPath);
            File dynamicFile = new File(dynamicPath);

            Scanner staticScanner = new Scanner(staticFile);
            Scanner dynamicScanner = new Scanner(dynamicFile);
            this.time = dynamicScanner.nextInt();
            this.L = staticScanner.nextFloat();
            this.N = staticScanner.nextInt();

            while (staticScanner.hasNext() && dynamicScanner.hasNext()) {
                float x = dynamicScanner.nextFloat();
                float y = dynamicScanner.nextFloat();
                float radius = staticScanner.nextFloat();
                if (radius > max_radius) {
                    this.max_radius = radius;
                }
                float theta = dynamicScanner.nextFloat(); // TODO: check where this prop should be used
                float velocity = staticScanner.nextFloat();
                particles.add(new Particle(radius, new Velocity(velocity, theta), x, y));
            }
            staticScanner.close();
            dynamicScanner.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void writeDynamicFile(Set<Particle> particles, String filePath, double time) {
        try {
            File file = new File(filePath);
            FileWriter writer = new FileWriter(file, true);
            if (file.createNewFile()) {
                int id = 1;
                File startingFile = new File("./TP2/data/input/dynamic.txt");
                Scanner scanner = new Scanner(startingFile);
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    writer.write(id + " " + line + "\n");
                    id++;
                }
                scanner.close();
                System.out.println("File created successfully");
            } else {
                StringBuilder data = new StringBuilder();
                data.append(getN() + "\n");
                data.append("Frame: " + time + '\n');
                for (Particle p : particles) {
                    data.append(p.getId() + " " + p.getX() + " " + p.getY() + " " + p.getVelocity().getVX() + " " + p.getVelocity().getVY() + " " + p.getTheta() +"\n");
                }
                writer.write(data.toString());
            }
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public float getL() {
        return L;
    }

    public int getN() {
        return N;
    }

    public Set<Particle> getParticles() {
        return particles;
    }

    public double getMaxRadius() {
        return max_radius;
    }
}
