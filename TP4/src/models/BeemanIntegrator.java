package models;

import java.util.function.BiFunction;

public class BeemanIntegrator implements Integrator{
    @Override
    public double[] solve(Particle p, double dt, double tf, BiFunction<Double, Double, Double> force) {
        double v0 = p.getSpeed(), r0 = p.getX(), mass = p.getMass();
        int size = (int) Math.floor(tf / dt);
        double[] r = new double[size];
        double[] v = new double[] {v0, v0}; // v[0] -> v in time i-1, v[1] -> v in time i-2
        r[0] = r0;
        double rprev = r[0] - dt * v[0];
        r[1] = r[0] + dt * v[0] + 2 * dt*dt * force.apply(r[0], v[0]) / (3 * mass) -
                dt*dt * force.apply(rprev, v[1]) / (6 * mass);
        p.setX(r[1]);

        // TODO: Could this be done within the loop? Definitely. Do I want to think how to do it? No
        double predicted = v0 + 3 * dt * force.apply(r[0], v0) / (2 * mass) -
                dt * force.apply(rprev, v0) / (2 * mass);
        double corrected = v0 + dt * force.apply(r[1], predicted) / (3 * mass) +
                5 * dt * force.apply(r0, v0) / (6 * mass) - dt * force.apply(rprev, v0) / (6 * mass);
        v[0] = corrected;
        p.setSpeed(corrected);

        for(int i = 2; i < size; i++) {
            r[i] = r[i-1] + dt * v[0] + 2 * dt*dt * force.apply(r[i-1], v[0]) / (3 * mass)-
                    dt*dt * force.apply(r[i-2], v[1]) / (6 * mass);
            p.setX(r[i]);
            predicted = v[0] + 3 * dt * force.apply(r[i-1], v[0]) / (2 * mass) -
                    dt * force.apply(r[i-2], v[1]) / (2 * mass);
            corrected = v[0] + dt * force.apply(r[i], predicted) / (3 * mass) +
                    5 * dt * force.apply(r[i-1], v[0]) / (6 * mass) - dt * force.apply(r[i-2], v[1]) / (6 * mass);
            p.setSpeed(corrected);
            v[1] = v[0];
            v[0] = corrected;
        }

        return r;
    }

    @Override
    public String toString() {
        return "beeman";
    }
}