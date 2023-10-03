package services;

import models.Particle;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class DataManager {
    private double max_radius = 0;
    private float L;
    private int N;
    private float time;
    private Set<Particle> particles = new HashSet<>();
    private String output;

    public DataManager(String staticPath, String dynamicPath, String output) {
        readParticlesFiles(staticPath, dynamicPath);
        this.output = output;
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
            this.N = dynamicScanner.nextInt();
            // this.time = dynamicScanner.nextFloat();

            while (staticScanner.hasNext() && dynamicScanner.hasNext()) {
                float x = dynamicScanner.nextFloat();
                float speed = dynamicScanner.nextFloat();
                double radius = staticScanner.nextFloat();
                double mass = staticScanner.nextFloat();

                if (radius > max_radius) {
                    this.max_radius = radius;
                }

                particles.add(new Particle(radius, x, speed, mass)); //TODO: avoid hard coding
            }
            staticScanner.close();
            dynamicScanner.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void writeOscilatorFile(double[] solution, double timeStepper) {
        try {
            File file = new File(output);
            FileWriter writer = new FileWriter(file, true);
            if (file.createNewFile()) {
                double time = 0;
                for (double r : solution) {
                    writer.write("Frame: " + time + '\n');
                    writer.write(String.format("%.10g\n", r));
                    time += timeStepper;
                }
            } else {
                clearOutputFile(output);
                double time = 0;
                for (double r : solution) {
                    writer.write("Frame: " + time + '\n');
                    writer.write(String.format("%.10g\n", r));
                    time += timeStepper;
                }
            }
            writer.close();
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
                    data.append(p.getId() + " " + p.getX() + "\n");
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
