package models;

import java.util.function.BiFunction;

public interface Integrator {
    public double[] solve(Particle p, double dt, double tf, BiFunction<Double, Double, Double> force);
}