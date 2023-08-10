package services;

import java.io.File;
import java.util.*;
import models.Particle;

public class DataManager {

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

            int id = 1;
            while (staticScanner.hasNext() && dynamicScanner.hasNext()) {
                float x = dynamicScanner.nextFloat();
                float y = dynamicScanner.nextFloat();
                float radius = staticScanner.nextFloat();
                float prop = staticScanner.nextFloat(); // TODO: check where this prop should be used
                particles.add(new Particle(id, x, y, radius));
                id++;
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

}
