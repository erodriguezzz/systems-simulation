package services;

import models.Limit;
import models.Particle;
import models.Velocity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DataManager {
    private double max_radius = 0;
    private float L;
    private int N;
    private float time;
    private Set<Particle> particles = new HashSet<>();

    public DataManager(String staticPath, String dynamicPath, String[] output) {
        readParticlesFiles(staticPath, dynamicPath);
        for (String s : output) {
            clearOutputFile(s);
        }
        // TODO: should we include the clear of OutputFiles?
    }

    private void clearOutputFile(String output) {
        try {
            File file = new File(output);
            FileWriter writer = new FileWriter(file, false);
            writer.write("");
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Output file does not exist");
            // throw new RuntimeException(e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void readParticlesFiles(String staticPath, String dynamicPath) {
        try {
            File staticFile = new File(staticPath);
            File dynamicFile = new File(dynamicPath);

            Scanner staticScanner = new Scanner(staticFile);
            Scanner dynamicScanner = new Scanner(dynamicFile);
            this.N = dynamicScanner.nextInt();
            dynamicScanner.nextInt();

            while (staticScanner.hasNext() && dynamicScanner.hasNext()) {
                int id = dynamicScanner.nextInt();
                float x = dynamicScanner.nextFloat();
                float y = dynamicScanner.nextFloat();
                double radius = staticScanner.nextFloat();
                double mass = staticScanner.nextFloat();

                if (radius > max_radius) {
                    this.max_radius = radius;
                }
                double vx = dynamicScanner.nextDouble();
                double vy = dynamicScanner.nextDouble();
                Particle p = new Particle(radius, new Velocity(vx, vy), x, y, mass);
                particles.add(p);
//                System.out.println(p);
            }
            staticScanner.close();
            dynamicScanner.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void writeDynamicFile(Set<Particle> particles, Set<Limit> limits, String filePath, double time) {
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
                int n = getN() + limits.size();
                data.append(n + "\n");
                data.append("Frame: " + time + '\n');
                for (Particle p : particles) {
                    int green = p.getId()==152?255:0;
                    int blue = p.getId()==37?0:255;
                    data.append(p.getId() + " " + p.getX() + " " + p.getY() + " " + p.getVelocity().getVx() + " " + p.getVelocity().getVy()
                    + " " + green
                    + " " + blue
                    + " " + 255
                    + " " + p.getRadius() 
                    /* " " + p.getTheta() +*/+ "\n");
                }
                for (Limit p : limits) {
                    data.append(p.getId() + " " + p.getX() + " " + p.getY() + " " + p.getVelocity().getVx() + " " + p.getVelocity().getVy()
                    + " " + 255
                    + " " + 255
                    + " " + 255
                    + " " + p.getRadius() 
                    /* " " + p.getTheta() +*/+ "\n");
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
