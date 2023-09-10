package services;

import models.Particle;
import models.Velocity;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class DataManager {
    private double max_radius = 0;
    private float L;
    private int N;
    private float time;
    private Set<Particle> particles = new HashSet<>();

    public DataManager(String staticPath, String dynamicPath, String[] output) {
        readParticlesFiles(staticPath, dynamicPath);
        // TODO: should we include the clear of OutputFiles?
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
            this.time = dynamicScanner.nextFloat();

            while (staticScanner.hasNext() && dynamicScanner.hasNext()) {
                float x = dynamicScanner.nextFloat();
                float y = dynamicScanner.nextFloat();
                double radius = staticScanner.nextFloat();
                double mass = staticScanner.nextFloat();

                if (radius > max_radius) {
                    this.max_radius = radius;
                }
                double vx = dynamicScanner.nextDouble();
                double vy = dynamicScanner.nextDouble();
                particles.add(new Particle(radius, new Velocity(vx, vy), x, y, mass)); //TODO: avoid hard coding
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
                File startingFile = new File("./data/input/dynamic.txt");
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
                    data.append(p.getId() + " " + p.getX() + " " + p.getY() + " " + p.getVelocity().getVx() + " " + p.getVelocity().getVy() + /* " " + p.getTheta() +*/ "\n");
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
