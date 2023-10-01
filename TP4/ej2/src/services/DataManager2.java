package services;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

import models2.Particle;

public class DataManager2 {
    private double max_radius = 0;
    private float L;
    private int N;
    private float time;
    private List<Particle> particles = new ArrayList<>();

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

            while (staticScanner.hasNext() && dynamicScanner.hasNext()) {
                int id = dynamicScanner.nextInt();
                float x = dynamicScanner.nextFloat();
                float y = dynamicScanner.nextFloat();
                float speed = dynamicScanner.nextFloat();
                float speedy = dynamicScanner.nextFloat();
                float auxU = dynamicScanner.nextFloat();
                double radius = dynamicScanner.nextFloat();
                double mass = dynamicScanner.nextFloat();

                Random random = new Random();
                double u = random.nextDouble() * 3 + 9;

                particles.add(new Particle(id, x, speed, u, radius, mass)); //TODO: avoid hard coding
            }
            staticScanner.close();
            dynamicScanner.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void writeDynamicFile(List<Particle> particles, String filePath, double time) {
        try {
            File file = new File(filePath);
            FileWriter writer = new FileWriter(file, true);
            
            StringBuilder data = new StringBuilder();
            data.append(particles.size() + "\n");
            data.append("Frame: " + time + '\n');
            for (Particle p : particles) {
                data.append(p.getId() + " " + 
                p.getX() + " 0 " + 
                p.getVx() + " 0 " + 
                p.getU() + " " +
                p.getRadius() + " " + 
                p.getMass() + "\n");
            }
            writer.write(data.toString());
            
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

    public List<Particle> getParticles() {
        return particles;
    }

    public double getMaxRadius() {
        return max_radius;
    }
}
