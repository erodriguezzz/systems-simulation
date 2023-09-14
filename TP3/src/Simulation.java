import models.CollisionType;
import models.Domain;
import models.Collision;
import models.Particle;
import services.DataManager;
// import sun.java2d.xr.MutableInteger;

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
        this.domain = new Domain(L);
    }

    private void showFirstThreeCollisions(){
        int i = 0;
        System.out.println("First three collisions:---------------------");
        for (Collision c : this.collisions) {
            System.out.println(c);
            i++;
            if (i == 3) {
                break;
            }
        }
        System.out.println("End of first three collisions-------------\n\n");
    }

    private void uniqueSimulation(int N, double L, int version){
        double time = 0, timeOfNextCollision;
        timeOfNextCollision = this.calculateCollisions();
        // this.showFirstThreeCollisions();
        if (timeOfNextCollision == Double.POSITIVE_INFINITY) {
            throw new RuntimeException("No collisions found");
        }

        while (time < totalSeconds) {
            Collision next = this.collisions.first();
            this.moveParticles(timeOfNextCollision - time);
            time = timeOfNextCollision;
            this.dm.writeDynamicFile(this.particles, "./data/output/Dynamic_N_" + N + "_L_" + L + ".dump", time);
            next.collide(this.domain.getM(), this.domain.getL());
            this.collisions.removeIf(c -> c.getP1().equals(next.getP1()) ||
                                        (c.getP2() != null && c.getP2().equals(next.getP1())) ||
                                        c.getP1().equals(next.getP2()) ||
                                        (c.getP2() != null && c.getP2().equals(next.getP2())));

            System.out.print("p1 = " + next.getP1().getId() + " p2 = " + (next.getP2() == null ? "wall" : next.getP2().getId()));
            if (next.getP2() == null)
                System.out.print("-------------------------------------------------------");
            System.out.println("\n");
            if (next.getP1() != domain.getUpperCorner() && next.getP1() != domain.getLowerCorner())
                this.calculateCollisions(next.getP1(), time);
            if (next.getP2() != null && next.getP2() != domain.getUpperCorner() && next.getP2() != domain.getLowerCorner()) {
                this.calculateCollisions(next.getP2(), time);
            }
            // System.out.println();
            // this.showFirstThreeCollisions();
            timeOfNextCollision = this.collisions.first().getTime();
        }
        return;
    }

    public static void main(String[] args) {
        int[] Ns = {200, 500};
        double[] Ls = {0.03, 0.05, 0.07, 0.09};
        boolean unique = true;
        if(!unique){
            for (int N : Ns) {
                for(double L : Ls ){
                    Simulation sim = new Simulation(N, L, 1);
                    sim.uniqueSimulation(N, L, 1); //TODO: avoid hard coding
                }
            }
        } else {
            int N = Ns[0];
            double L = Ls[0];
            Simulation sim = new Simulation(N, L, 1);
            sim.uniqueSimulation(N, L, 1); //TODO: avoid hard coding
        }
    }

    public void moveParticles(double time){
        particles.forEach(p -> {
            p.updatePosition(time);
        });
    }

    public double calculateCollisions(Particle p, double currentTime) {
        // Create wall collisions
        Collision wallCollision = domain.getNextWallCollision(p, currentTime);
        double timeOfFirstCollision = wallCollision.getTime();
        collisions.add(wallCollision);

        // Create particle collisions
        for (Particle q : particles) {
            if (p != q) {
                double timeToCollision = p.timeToCollision(q);
                if (timeToCollision >= 0) {
                    Collision particleCollision = new Collision(p, q, timeToCollision + currentTime, CollisionType.PARTICLE);
                    collisions.add(particleCollision);
                    timeOfFirstCollision = Math.min(timeOfFirstCollision, particleCollision.getTime());
                }
            }
        }
        return timeOfFirstCollision;
    }

    public double calculateCollisions() {
        double timeOfFirstCollision = Double.POSITIVE_INFINITY;
        for (Particle p : particles) {
            timeOfFirstCollision = Math.min(timeOfFirstCollision, calculateCollisions(p, 0));
        }
        return timeOfFirstCollision;
    }

}
