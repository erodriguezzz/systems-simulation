package models;

import java.util.Collection;
import java.util.function.BiFunction;

public interface Integrator {

    public void solve(double dt, Collection<Particle> particles, BiFunction<Double, Double, Double> force);

}
