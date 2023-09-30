package models;

import java.util.TreeSet;
import java.util.function.BiFunction;

/**
 * This class represents the domain of the simulation as specified in the assignment.
 */
public class Domain {
    private final double L;

    private final TreeSet<Particle> particles;

    private final Integrator integrator;

    public Domain(double L, TreeSet<Particle> particles, Integrator integrator) {
        this.L = L;
        this.particles = particles;
        this.integrator = integrator;
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

    public void moveParticles (double deltaTime, BiFunction<Double, Double, Double> force) {
        integrator.solve(deltaTime, particles, force);
    }

    public TreeSet<Particle> getParticles() {
        return particles;
    }

    private void verlet(double deltaTime) {
        for (Particle p : particles) {
            double x = p.getX();
            double v = p.getSpeed();
            double f = getForce(p);

            double previous = p.getPreviousX() == null ? (x - v * deltaTime) : p.getPreviousX();
            // double previous = p.getPreviousX() == null ? 1 : p.getPreviousX();
            double x_new = 2*x - previous + f * deltaTime * deltaTime / p.getMass();
            double v_new = v + f * deltaTime / p.getMass();
            p.setX(x_new);
            p.setSpeed(v_new);
        }
    }

    public Integrator getIntegrator() {
        return integrator;
    }

    protected double getForce(Particle p) {
        // return p.getAcceleration();
        return 0;
    }

    /*
    private double euler(double deltaTime, Particle p) {
        double x = p.getX();
        double v = p.getSpeed();
        double f = getForce(p);
        double v_new = v + f * deltaTime / p.getMass();
        double x_new = x + v_new * deltaTime + f / p.getMass() * deltaTime * deltaTime / 2 ;
        System.out.println("Euler x_new: " + x_new + "---------------------------------------------------");
        return x_new;
    }
     */

    private void beeman(double deltaTime) {
        // Implement Beeman predictor-corrector algorithm
        for (Particle p : particles) {

        }
    }

    private void gear(double deltaTime) {

    }

}
