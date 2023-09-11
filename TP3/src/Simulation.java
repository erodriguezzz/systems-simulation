import models.Domain;
import models.Collision;
import models.Particle;
import services.DataManager;
import sun.java2d.xr.MutableInteger;

import java.util.Set;
import java.util.TreeSet;

public class Simulation {
    private static final int totalSeconds = 400;
    private static final double timeStepper = 0.3;
    private final Domain domain;
    private final DataManager dm;
    private final Set<Particle> particles;
    private final TreeSet<Collision> collisions;
    

    Simulation(int N, double L, int version){
        String[] outputs = {
                    "./data/output/dynamic/Dynamic_N_" + N + "_L_" + L +"_v" +version+".dump",
                    "./data/output/VaN_" + N + "_L_" + L + "_v" +version+".txt",
                    "./data/output/VaN_" + N + "_L_" + L +"_v" +version+".txt",
                    "./data/output/VaN_" + N + "_L_" + L +"_v" +version+".txt",
        };
        this.dm = new DataManager(
                "./data/input/Static_N_" + N + ".dump",
                "./data/input/Dynamic_N_" + N + ".dump",
                outputs);
        this.particles = dm.getParticles();
        this.collisions = new TreeSet<>();
        this.domain = new Domain(dm.getL());
    }

    private static void uniqueSimulation(int N, double L, int version){
        Simulation sim = new Simulation(N, L, version);
        double time = 0, timeToNextCollision;
        timeToNextCollision = sim.calculateCollisions();
        if (timeToNextCollision == Double.POSITIVE_INFINITY) {
            throw new RuntimeException("No collisions found");
        }
        while (time < totalSeconds) {
            sim.moveParticles(timeToNextCollision - time);
            sim.dm.writeDynamicFile(sim.particles, "./data/output/Dynamic_N_" + N + "_L_" + L + ".txt", time);
            Collision next = sim.collisions.first();
            next.exec(sim.domain.getM(), sim.domain.getL());
            sim.collisions.removeIf(c -> c.getP1().equals(next.getP1()) || (c.getP2() != null && c.getP2().equals(next.getP1())) || c.getP1().equals(next.getP2()) || (c.getP2() != null && c.getP2().equals(next.getP2())));
            // sim.collisions.remove(next);
            sim.calculateCollisions(next.getP1());
            if (next.getP2() != null)
               sim.calculateCollisions(next.getP2());
            // sim.calculateCollisions();
            time = timeToNextCollision;
            timeToNextCollision = sim.collisions.first().getTime();
        }
        return;
    }

    public static void main(String[] args) {
        int[] Ns = {200, 500};
        double[] Ls = {0.03, 0.05, 0.07, 0.09};
        boolean unique = false;
        if(!unique){
            for (int N : Ns) {
                for(double L : Ls ){
                    uniqueSimulation(N, L, 1); //TODO: avoid hard coding
                }
            }
        } else {
            uniqueSimulation(Ns[0], Ls[0], 1); //TODO: avoid hard coding
        }
    }

    public void moveParticles(double time){
        particles.forEach(p -> {
            p.updatePosition(time);
        });
    }

    public double calculateCollisions(Particle p) {
        // Create wall collisions
        Collision wallCollision = domain.getNextWallCollision(p);
        double timeToFirstCollision = wallCollision.getTime();
        collisions.add(wallCollision);

        // Create particle collisions
        for (Particle q : particles) {
            if (p != q) {
                double timeToCollision = p.timeToCollision(q);
                if (timeToCollision >= 0) {
                    collisions.add(new Collision(p, q, timeToCollision));
                    timeToFirstCollision = Math.min(timeToFirstCollision, timeToCollision);
                }
            }
        }
        return timeToFirstCollision;
    }

    public double calculateCollisions() {
        double timeToFirstCollision = Double.POSITIVE_INFINITY;
        for (Particle p : particles) {
            timeToFirstCollision = Math.min(timeToFirstCollision, calculateCollisions(p));
        }
        return timeToFirstCollision;
    }

}
