package services;

import java.io.File;
import java.util.*;
import models.Particle;
import models.Velocity;

public class DataManager {
    private double max_radius = 0;
    private float L;
    private int N;
    private int time;
    private Set<Particle> particles = new HashSet<>();

    public DataManager(String staticPath, String dynamicPath) {
        readParticlesFiles(staticPath, dynamicPath);
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
                float theta = staticScanner.nextFloat(); // TODO: check where this prop should be used
                float velocity = staticScanner.nextFloat();
                particles.add(new Particle(radius, new Velocity(velocity, theta), x, y));
            }
            staticScanner.close();
            dynamicScanner.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public int getTime() {
        return time;
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
