package services;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

import models.Pair;
import models.Particle;
import models.Color;

public class DataManager {
    private double max_radius = 0;
    private float L;
    private int N;
    private float time;
    private List<Particle> particles = new ArrayList<>();

    public DataManager(String staticPath, String dynamicPath) {
        readParticlesFiles(staticPath, dynamicPath);
        // TODO: should we include the clear of OutputFiles?
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
                double x = dynamicScanner.nextDouble();
                double y = dynamicScanner.nextDouble() + 7;
                float speed = dynamicScanner.nextFloat();
                float speed2 = dynamicScanner.nextFloat();
                float speed3 = dynamicScanner.nextFloat();
                double radius = dynamicScanner.nextDouble();
                double mass = dynamicScanner.nextDouble();

                particles.add(new Particle(id, new Pair(x/100, y/100), (radius/100), (mass/1000), 1.0E-3, Color.RED));
            }
            staticScanner.close();
            dynamicScanner.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void writeDynamicFile(List<Particle> particles, String filePath, double time, List<Particle> limits) {
        try {
            File file = new File(filePath);
            FileWriter writer = new FileWriter(file, true);
            
            StringBuilder data = new StringBuilder();
            int size = particles.size() + limits.size();
            data.append(size + "\n");
            data.append("Frame: " + time + '\n');
            int lastId = 0;
            for (Particle p : particles) {
                data.append(p.getId() + " " + 
                p.getPosition().getX() + " " +
                p.getPosition().getY() + " " +
                p.getVelocity().getX() + " " + 
                p.getVelocity().getY() + " 0 " +
                p.getRadius() + " " + 
                p.getMass() + 
                " 255 0 0" +
                "\n");
                lastId = p.getId();
            }
            lastId++;
            for(Particle l : limits){
                data.append(lastId + " " +
                l.getPosition().getX() + " " +
                l.getPosition().getY() + " " +
                l.getVelocity().getX() + " " + 
                l.getVelocity().getY() + " 0 " +
                l.getRadius() + " " + 
                l.getMass() + 
                " 255 255 255" +"\n");
                lastId++;
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
