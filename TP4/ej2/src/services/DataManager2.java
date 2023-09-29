package services;

import models.LinearParticle;
import models.Particle;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class DataManager2 {
    private double max_radius = 0;
    private float L;
    private int N;
    private float time;
    private HashSet<LinearParticle> particles = new HashSet<>();

    public DataManager2(String staticPath, String dynamicPath) {
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
            this.N = dynamicScanner.nextInt();
            this.time = dynamicScanner.nextFloat();

            int counter = 0;
            while (staticScanner.hasNext() && dynamicScanner.hasNext()) {
                int id = dynamicScanner.nextInt();
                float x = dynamicScanner.nextFloat();
                float y = dynamicScanner.nextFloat();
                float speed = dynamicScanner.nextFloat();
                double radius = dynamicScanner.nextFloat();
                double mass = dynamicScanner.nextFloat();
                double force = dynamicScanner.nextFloat();

                counter++;
                System.out.println("ID" + particles.size());
                particles.add(new LinearParticle(id, x, speed, radius, mass)); //TODO: avoid hard coding
            }
            System.out.println("COUNTER" + counter);
            staticScanner.close();
            dynamicScanner.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void writeDynamicFile(Set<LinearParticle> particles, String filePath, double time) {
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
                data.append(10 + "\n");
                data.append("Frame: " + time + '\n');
                for (LinearParticle p : particles) {
                    data.append(p.getId() + " " + p.getX() + " 0 " + p.getVx() + " " + p.getRadius() + " " + p.getMass() + " " + p.getForce() + "\n");
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

    public HashSet<LinearParticle> getParticles() {
        System.out.println("PARTICLES" + particles.size());
        return particles;
    }

    public double getMaxRadius() {
        return max_radius;
    }
}
