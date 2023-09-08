import models.Domain;
import models.Collision;
import models.Particle;
import services.DataManager;

import java.util.Set;
import java.util.TreeSet;

public class Simulation {
    private static final int totalSeconds = 400;
    private static final double timeStepper = 0.3;
    private final Domain domain;
    private final DataManager dm;
    private final Set<Particle> particles;
    private TreeSet<Collision> collisions;
    

    Simulation(int N, double L, int version){
        String[] outputs = {
                    "./data/output/dynamic/Dynamic_N_" + N + "_L_" + L +"_v" +version+".dump",
                    "./data/output/VaN_" + N + "_L_" + L + "_v" +version+".txt",
                    "./data/output/VaN_" + N + "_L_" + L +"_v" +version+".txt",
                    "./data/output/VaN_" + N + "_L_" + L +"_v" +version+".txt",
        };
        this.dm = new DataManager(
                "./data/input/Static_N_" + N + "_L_" + L + ".txt",
                "./data/input/Dynamic_N_" + N + "_L_" + L + ".txt",
                outputs);
        this.particles = dm.getParticles();
        this.domain = new Domain(dm.getL(), dm.getL()); // TODO: adjust DataManager to new input format
    }

    private static void uniqueSimulation(int N, double L, int version){
        Simulation sim = new Simulation(N, L, 1); //TODO: avoid hard coding
        double time = 0, timeToNextCollision;
        timeToNextCollision = sim.calculateCollisions();
        if (timeToNextCollision < 0) {
            throw new RuntimeException("No collisions found");
        }
        while (time < totalSeconds) {
            sim.moveParticles(timeToNextCollision - time);
            sim.dm.writeDynamicFile(sim.particles, "./output/Dynamic.txt", time);
            Collision next = sim.collisions.first();
            next.exec(sim.domain.getM(), sim.domain.getL());
            for (Collision c : sim.collisions) {
                if (c.getP1().equals(next.getP1()) || ( c.getP2() != null && c.getP2().equals(next.getP1()) ) || c.getP1().equals(next.getP2()) || (c.getP2() != null && c.getP2().equals(next.getP2()))) {
                    sim.collisions.remove(c);
                }
            }
            sim.collisions.remove(next);
            sim.calculateCollisions(next.getP1());
            sim.calculateCollisions(next.getP2());
            time = timeToNextCollision;
            timeToNextCollision = sim.collisions.first().getTime();
        }
        return;
    }

    public static void main(String[] args) {
        int[] Ns = {200, 500, 1000};
        double[] Ls = {0.03, 0.05, 0.07, 0.09};
        boolean unique = false;
        if(!unique){
            for (int N : Ns) {
                for(double L : Ls ){
                    uniqueSimulation(N, L, 1);
                }
            }
        } else {
            uniqueSimulation(Ns[0], Ls[0], 1);
        }
    }

    public void moveParticles(double time){
        particles.forEach(p -> {
            p.updatePosition(time);
        });
    }

    public void calculateCollisions(Particle p) {
        // Create wall collisions
        double timeToCollision = domain.getWallCollisionTime(p); //TODO: check if this collision is with a corner
        collisions.add(new Collision(p, timeToCollision));

        // Create particle collisions
        for (Particle q : particles) {
            if (p != q) {
                timeToCollision = p.timeToCollision(q);
                if (timeToCollision >= 0) {
                    collisions.add(new Collision(p, q, timeToCollision));
                }
            }
        }
        return;
    }

    public double calculateCollisions() {
        double timeToFirstCollision = -1;
        for (Particle p : particles) {
            // Create wall collisions
            timeToFirstCollision = domain.getWallCollisionTime(p);
            collisions.add(new Collision(p, timeToFirstCollision));

            // Create particle collisions
            for (Particle q : particles) { //TODO: check efficency. It might be better to use a custom data structure for both particles and collisions
                if (p != q) {
                    double timeToCollision = p.timeToCollision(q);
                    if (timeToCollision >= 0) {
                        collisions.add(new Collision(p, q, timeToCollision));
                        timeToFirstCollision = Math.min(timeToFirstCollision, timeToCollision);
                    }
                }
            }
        }
        return timeToFirstCollision;
    }

}
