package models;

import java.util.Collection;
import java.util.function.BiFunction;

public class BeemanIntegrator implements Integrator {

    @Override
    public void solve(double dt, Collection<Particle> particles, BiFunction<Double, Double, Double> force) {
        for (Particle p : particles) {
            double x = p.getX();
            double v = p.getSpeed();
            double a = force.apply(x, v) / p.getMass();
            double previousX = p.getPreviousX() == null ? (x - p.getSpeed() * dt - dt*dt * a /2) : p.getPreviousX();
            double mass = p.getMass();

            double predicted = v + 3 * dt * force.apply(x, v) / (2 * mass) - dt * dt * force.apply(previousX, v-a*dt) / (2 * mass);

            double x_new = x + v * dt + (2.0 / 3.0) * a * dt * dt - (1.0 / 6.0) * force.apply(previousX, v-a*dt) / mass * dt * dt;
            double v_new = v + dt * force.apply(x_new, predicted) / (3 * mass) + 5.0/6 * dt * a - dt * force.apply(previousX, v-a*dt) / (6 * mass);


            p.setX(x_new);
            p.setSpeed(v_new);
        }
    }

}
