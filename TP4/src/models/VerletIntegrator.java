package models;

import java.util.function.BiFunction;

public class VerletIntegrator implements Integrator {

    @Override
    public double[] solve(Particle p, double dt, double tf, BiFunction<Double, Double, Double> force) {
        double v0 = p.getSpeed(), r0 = p.getX(), mass = p.getMass();
        int size = (int) Math.floor(tf / dt);
        double[] r = new double[size];
        double v = v0;
        r[0] = r0;
        r[1] = 2 * r[0] - (r[0] - v0 * dt) + (dt * dt * force.apply(r[0], v)) / mass;

        for(int i = 2; i < size; i++) {
            v = v + (dt /mass) * force.apply(r[i - 1], v);
            r[i] = 2 * r[i - 1] - r[i - 2] + (dt * dt * force.apply(r[i - 1], v)) / mass;
        }

        return r;
    }

    @Override
    public String toString() {
        return "verlet";
    }
}