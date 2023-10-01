package models;

import java.util.Collection;
import java.util.function.BiFunction;

public class VerletIntegrator implements Integrator {

    @Override
    public void solve(double dt, Collection<Particle> particles, BiFunction<Double, Double, Double> force) {
        for (Particle p : particles) {
            double x = p.getX();
            double v = p.getSpeed();
            double f = force.apply(x, v);

            double previous = p.getPreviousX() == null ? (x - v * dt) : p.getPreviousX();
            // double previous = p.getPreviousX() == null ? 1 : p.getPreviousX();
            double x_new = 2*x - previous + f * dt * dt / p.getMass();
            double v_new = v + f * dt / p.getMass();
            p.setX(x_new);
            p.setSpeed(v_new);
        }
    }
}
