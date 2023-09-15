import models.CollisionType;
import models.Domain;
import models.Limit;
import models.Collision;
import models.Particle;
import services.DataManager;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Simulation {
    private static final int totalSeconds = 400;
    private static final double timeStepper = 0.3;
    private final Domain domain;
    private final DataManager dm;
    private final Set<Particle> particles;
    private TreeSet<Collision> collisions;
    private final double deltaT = 0.25;
    private Set<Limit> limits = new HashSet<>();
    

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
        for (double i = 0; i < 0.09; i += 0.0005) {

            this.limits.add(new Limit(i, 0));
            this.limits.add(new Limit(i, 0.09));
            this.limits.add(new Limit(0, i));
            this.limits.add(new Limit(0.09+i, (0.09-L)/2));
            this.limits.add(new Limit(0.09+i, (0.09+L)/2));
            if(i > (0.09+L)/2 || i < (0.09-L)/2){
                this.limits.add(new Limit(0.09, i));
            }
            else{
                this.limits.add(new Limit(0.18, i));
            }
        }
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
        timeOfNextCollision = this.calculateCollisions(time);
        this.showFirstThreeCollisions();
        if (timeOfNextCollision == Double.POSITIVE_INFINITY) {
            throw new RuntimeException("No collisions found");
        }
        int counter6 = 0;
        while (time < totalSeconds) {
            Collision next = this.collisions.first();
            this.moveParticles(timeOfNextCollision - time); //TODO: en post-procesamiento (python) hay que calcular el desplazamiento cuadrático medio
            // time = timeOfNextCollision;
            if (next.getType() != CollisionType.PARTICLE) {
                double collisionV;
                if (next.getType() == CollisionType.LEFT_HORIZONTAL_WALL || next.getType() == CollisionType.RIGHT_HORIZONTAL_WALL)
                    collisionV = next.getP1().getVy();
                else
                    collisionV = next.getP1().getVx();
                domain.addPressure(collisionV, next.getType()); // TODO: add deltaT
            }
            next.collide(this.domain.getM(), this.domain.getL());
            this.dm.writeDynamicFile(this.particles, this.limits, "./data/output/Dynamic_N_" + N + "_L_" + L + ".dump", time);

            /* TODO: Verificar si tenemos que agregar un chequeo para elimiar las colisiones en las que forma parte alguna de las esquinas */
            this.collisions.removeIf(c -> c.getP1().equals(next.getP1()) ||
                                        (c.getP2() != null && c.getP2().equals(next.getP1())) ||
                                        c.getP1().equals(next.getP2()) ||
                                        (c.getP2() != null && c.getP2().equals(next.getP2()))
                                             &&
                                             (
                                                 !c.getP1().equals(this.domain.getUpperCorner()) &&
                                                 !c.getP2().equals(this.domain.getUpperCorner()) &&
                                                 !c.getP1().equals(this.domain.getLowerCorner()) &&
                                                 !c.getP2().equals(this.domain.getLowerCorner())
                                             ));
            /*
            if (next.getP1().getId() == 121) {
                collisions.forEach(c -> {
                    if (c.getP1().getId() == 121 || (c.getP2() != null && c.getP2().getId() == 121)) {
                        System.out.println("-------------------------------------------------------------------------");
                        System.out.println(c);
                    }
                });
            }
             */

            time = timeOfNextCollision;
            if (next.getP1().getId() == 6 || (next.getP2() != null && next.getP2().getId() == 6)) {
                counter6++;
                System.out.println();
                System.out.println("Counter6 = " + counter6);
                System.out.print("p1 = " + next.getP1().getId() + " p2 = " + (next.getP2() == null ? "wall" : next.getP2().getId()));
                System.out.println("\n");
            }
            if (next.getP1() != domain.getUpperCorner() && next.getP1() != domain.getLowerCorner())
                this.calculateCollisions(next.getP1(), time);
            if (next.getP2() != null && next.getP2() != domain.getUpperCorner() && next.getP2() != domain.getLowerCorner()) {
                this.calculateCollisions(next.getP2(), time);
            }
            /*
            if (next.getP1().getId() == 121) {
                System.out.println("Particle 121 collisions:");
                collisions.forEach(c -> {
                    if (c.getP1().getId() == 121 || (c.getP2() != null && c.getP2().getId() == 121)) {
                        System.out.println(c);
                    }
                });
                System.out.println();
            }
             */

            // System.out.println();
            this.showFirstThreeCollisions();

            /*
            double aux = this.collisions.first().getTime();
            while (aux < time) {
                System.out.println("Error en el set");
                this.collisions.remove(this.collisions.first());
                aux = this.collisions.first().getTime();
            }
            timeOfNextCollision = aux;
             */
            timeOfNextCollision = this.collisions.first().getTime();
            /*
            collisions = new TreeSet<>();
             time = timeOfNextCollision;
             timeOfNextCollision = calculateCollisions(time);
             */
            System.out.println();
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
        // System.out.println("Calculating collisions for time "+ currentTime + " for particle " + p.getId());
        // Create wall collisions
        Collision wallCollision = domain.getNextWallCollision(p, currentTime);
        double timeOfFirstCollision = wallCollision.getTime();
        if (timeOfFirstCollision < 0) {
            System.out.println("Negative wall collision time " + timeOfFirstCollision + " for particle " + p.getId());
        }
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

    public double calculateCollisions(double time) {
        double timeOfFirstCollision = Double.POSITIVE_INFINITY;
        for (Particle p : particles) {
            timeOfFirstCollision = Math.min(timeOfFirstCollision, calculateCollisions(p, time));
        }
        return timeOfFirstCollision;
    }

}
