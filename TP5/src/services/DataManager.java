package services;

import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.*;

import models.Particle;

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
                float x = dynamicScanner.nextFloat();
                float y = dynamicScanner.nextFloat();
                float speed = dynamicScanner.nextFloat();
                float speed2 = dynamicScanner.nextFloat();
                float speed3 = dynamicScanner.nextFloat();
                double radius = dynamicScanner.nextFloat();
                double mass = dynamicScanner.nextFloat();

                particles.add(new Particle(id, BigDecimal.valueOf(x), BigDecimal.valueOf(y), BigDecimal.valueOf(mass), BigDecimal.valueOf(radius)));
            }
            staticScanner.close();
            dynamicScanner.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void writeDynamicFile(List<Particle> particles, String filePath, BigDecimal time, List<Particle> limits) {
        try {
            File file = new File(filePath);
            FileWriter writer = new FileWriter(file, true);
            
            StringBuilder data = new StringBuilder();
            int size = particles.size() + limits.size();
            data.append(size + "\n");
            data.append("Frame: " + time.stripTrailingZeros().toString() + '\n');
            for (Particle p : particles) {
                data.append(p.getId() + " " + 
                p.getX() + " " +
                p.getY() + " " +
                p.getVx() + " " + 
                p.getVy() + " 0 " +
                p.getRadius() + " " + 
                p.getMass() + 
                " 255 0 0" +
                "\n");
            }
            for(Particle l : limits){
                data.append(l.getId() + " " + 
                l.getX() + " " +
                l.getY() + " " +
                l.getVx() + " " + 
                l.getVy() + " 0 " +
                l.getRadius() + " " + 
                l.getMass() + 
                " 255 255 255" +"\n");
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
