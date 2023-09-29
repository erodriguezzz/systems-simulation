package models;

import java.util.TreeSet;

public class Oscilator extends Domain {
    private final double k;
    private final double gamma;

    public Oscilator(double k, double gamma, TreeSet<Particle> particles) {
        super(Double.POSITIVE_INFINITY, particles);
        this.k = k;
        this.gamma = gamma;
    }

    @Override
    public void moveParticles(double deltaTime) {
        for (Particle p : super.getParticles()) {
            double x = p.getX();
            double v = p.getSpeed();
            double m = p.getMass();
            double a = (-k * x - gamma * v)/m ;
            double x_new = Math.exp(-gamma * deltaTime / (2 * m)) * (Math.cos(Math.sqrt(k / m - gamma * gamma / (4*m*m)) * deltaTime));
            double v_new = v + a * deltaTime;
            p.setX(x_new);
            p.setSpeed(v_new);
        }
    }

    public double getK() {
        return k;
    }

    public double getGamma() {
        return gamma;
    }

    @Override
    protected double getForce(Particle p) {
        p.setAcceleration(-k * p.getX() / p.getMass() - gamma * p.getSpeed() / p.getMass());
        return p.getAcceleration() * p.getMass();
    }
}
