package models;

import java.math.BigDecimal;
import java.util.List;

public class BeemanIntegrator extends Integrator {

    private List<Particle> particles;
    private BigDecimal dt;
    private final BigDecimal frecuency = BigDecimal.valueOf(0.1);
    private final BigDecimal d = BigDecimal.valueOf(0.1);
    private Grid grid;

    public BeemanIntegrator(List<Particle> particles, BigDecimal dt) {
        this.particles = particles;
        this.dt = dt;
    }

    public void run() {
        grid.sin();

        particles.forEach(p -> predict(p));
        particles.forEach(p -> p.resetForce());

        grid.updateForces();

        particles.forEach(p -> correct(p));
        particles.forEach(p -> p.resetForce());

        grid.updateForces();

        // for (Particle p : particles) {
        // double[] x = run(p, dt.doubleValue(), dt.doubleValue());
        // p.setX(x[0]);
        // }
    }

    @Override
    public double[] run(Particle p, double dt, double finalT) {
        // int steps = (int) Math.floor(finalT / dt);
        // double[] x = new double[steps], v = new double[] { p.getVx(), p.getVx() };
        // x[0] = p.getX();

        // for (int t = 1; t < steps; t++) {
        // calculatePositionAndVelocity(p, x, v, t, dt, p.getMass());
        // }

        // return x;
        double[] a = { 0 };
        return a;
    }

    // private void calculatePositionAndVelocity(Particle p, double[] x, double[] v,
    // int t, double dt, double mass) {
    // double xPrev = x[t - 1] - dt * v[0];
    // x[t] = x[t - 1] + dt * v[0] + 2 / (3 * mass) * Math.pow(dt, 2) *
    // calculateA(x[t - 1], v[0]) -
    // Math.pow(dt, 2) / (6 * mass) * calculateA(xPrev, v[1]);
    // p.setX(x[t]);

    // double auxV = v[0] + 3 / (2 * mass) * dt * calculateA(x[t - 1], v[0]) -
    // dt * calculateA(xPrev, v[0]) / (2 * mass);
    // double newV = v[0] + dt * calculateA(x[t], auxV) / (3 * mass) +
    // 5 / (6 * mass) * dt * calculateA(x[t - 1], v[0]) - dt / (6 * mass) *
    // calculateA(xPrev, v[0]);
    // v[1] = v[0];
    // v[0] = newV;
    // p.setVx(newV);

    // }

    private void predict(Particle p) {
        // TODO: evaluar con los multiplicadores de la aceleracion
        BigDecimal ax = p.getFx().divide(p.getMass());
        BigDecimal ay = p.getFy().divide(p.getMass());

        BigDecimal x = p.getX().add(p.getVx().multiply(dt)).add(ax.multiply(dt.pow(2).divide(BigDecimal.valueOf(2))));
        BigDecimal y = p.getY().add(p.getVy().multiply(dt)).add(ay.multiply(dt.pow(2).divide(BigDecimal.valueOf(2))));

        p.setX(x);
        p.setY(y);

        p.setVx(p.getVx().add(ax.multiply(dt.multiply(BigDecimal.valueOf(1.5)))));
        // double actualAcceleration = p.getX3();
        // p.setX(p.getX().sum(
        // p.getVx().scale(dt).sum(
        // actualAcceleration.scale(B).sum(
        // prevAcceleration.scale(C)).scale(sqrDt))));

        // p.actualVelocity = velocity;

        // p.velocity = p.actualVelocity.sum(
        // p.actualAcceleration.scale(1.5 * dt).sum(
        // prevAcceleration.scale(-0.5 * dt)));

        // resetForce at the end
    }

    private void correct(Particle p) {
    }

    @Override
    public String toString() {
        return "beeman";
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public BigDecimal getDt() {
        return dt;
    }
}