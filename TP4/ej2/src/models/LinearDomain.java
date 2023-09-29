package models;

import java.util.HashSet;
import java.util.TreeSet;

/**
 * This class represents the domain of the simulation as specified in the assignment.
 */
public class LinearDomain {
    private final double L;

    private final HashSet<LinearParticle> particles;

    public LinearDomain(double L, HashSet<LinearParticle> particles) {
        this.L = L;
        this.particles = particles;
    }

    public double getL() {
        return L;
    }

    public void moveParticles(double deltaTime, Algorithm algorithm) {
        verlet(deltaTime);
    }

    public void moveParticles (double deltaTime) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public HashSet<LinearParticle> getParticles() {
        return particles;
    }

    private void verlet(double deltaTime) {
        for (LinearParticle p : particles) {
            double x = p.getX();
            double v = p.getVx();
            double f = p.getForce();

            double previous = p.getPreviousX() == null ? euler(-1E-5, p) : p.getPreviousX();
            // double previous = p.getPreviousX() == null ? 1 : p.getPreviousX();
            double x_new = 2*x - previous + f * deltaTime * deltaTime / p.getMass();
            double v_new = v + f * deltaTime / p.getMass();
            p.setX(x_new);
            p.setVx(v_new);
        }
    }

    private double euler(double deltaTime, LinearParticle p) {
        double x = p.getX();
        double v = p.getVx();
        double f = p.getForce();
        // System.out.println("Euler f: " + f);
        double v_new = v + f * deltaTime / p.getMass();
        double x_new = x + v_new * deltaTime + f / p.getMass() * deltaTime * deltaTime / 2 ;
        // System.out.println("Euler x_new: " + x_new + "---------------------------------------------------");
        return x_new;
    }

    protected double getForce(LinearParticle p) {
        return p.getForce();
    }

}
