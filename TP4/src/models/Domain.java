package models;

import java.util.TreeSet;

/**
 * This class represents the domain of the simulation as specified in the assignment.
 */
public class Domain {
    private final double L;

    private final TreeSet<Particle> particles;

    public Domain(double L, TreeSet<Particle> particles) {
        this.L = L;
        this.particles = particles;
    }

    public double getL() {
        return L;
    }

    public void moveParticles(double deltaTime, Algorithm algorithm) {
        switch (algorithm) {
            case VERLET:
                verlet(deltaTime);
                break;
            case BEEMAN:
                beeman(deltaTime);
                break;
            case GEAR:
                gear(deltaTime);
                break;
        }
    }

    public void moveParticles (double deltaTime) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public TreeSet<Particle> getParticles() {
        return particles;
    }

    private void verlet(double deltaTime) {
        for (Particle p : particles) {
            double x = p.getX();
            double v = p.getSpeed();
            double f = getForce(p);

            double previous = p.getPreviousX() == null ? euler(-1E-5, p) : p.getPreviousX();
            // double previous = p.getPreviousX() == null ? 1 : p.getPreviousX();
            double x_new = 2*x - previous + f * deltaTime * deltaTime / p.getMass();
            double v_new = v + f * deltaTime / p.getMass();
            p.setX(x_new);
            p.setSpeed(v_new);
        }
    }

    protected double getForce(Particle p) {
        return p.getAcceleration();
    }

    private double euler(double deltaTime, Particle p) {
        double x = p.getX();
        double v = p.getSpeed();
        double f = getForce(p);
        System.out.println("Euler f: " + f);
        double v_new = v + f * deltaTime / p.getMass();
        double x_new = x + v_new * deltaTime + f / p.getMass() * deltaTime * deltaTime / 2 ;
        System.out.println("Euler x_new: " + x_new + "---------------------------------------------------");
        return x_new;
    }

    private void beeman(double deltaTime) {


    }

    private void gear(double deltaTime) {

    }

}
