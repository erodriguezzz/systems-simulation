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
    

    Simulation(int N, double M, double L, double v){
        String[] outputs = {
                    "./data/output/dynamic/Dynamic_N_" + N + "_M_" + M + "_L_" + L +"_v" +v+".dump",
                    "./data/output/VaN_" + N + "_M_" + M + "_L_" + L + "_v" +v+".txt",
                    "./data/output/VaN_" + N + "_M_" + M + "_L_" + L +"_v" +v+".txt",
                    "./data/output/VaN_" + N + "_M_" + M + "_L_" + L +"_v" +v+".txt",
};
        this.dm = new DataManager(
                "./data/input/Static_N_"  + N + "_M_"+ M + "_L_" + L + "_v_1.txt",
                "./data/input/Dynamic_N_" + N + "_M_" + M + "_L_" + L + "_v_1.txt",
                outputs);
        this.particles = dm.getParticles();
        this.domain = new Domain(dm.getL(), dm.getL()); // TODO: adjust DataManager to new input format
    }

    public static void main(String[] args) {
        Simulation sim = new Simulation(100, 0.09, 0.03, 0.01); //TODO: avoid hard coding
        double time = 0, timeToNextCollision = sim.calculateCollisions();
        if (timeToNextCollision < 0) {
            throw new RuntimeException("No collisions found");
        }
        while (time < totalSeconds) {
            while (time < timeToNextCollision) {
                sim.moveParticles(time);
                time += timeStepper;
            }
            sim.dm.writeDynamicFile(sim.particles, "./output/temp.txt", time); //TODO: check if this is what the theory means by "storing only events"
            sim.collisions.first().exec(sim.domain.getM(), sim.domain.getL());
            sim.collisions.remove(sim.collisions.first());
            timeToNextCollision = sim.calculateCollisions();
        }
        
    }

    public void moveParticles(double time){
        particles.forEach(p -> {
            p.updatePosition(time);
        });
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
