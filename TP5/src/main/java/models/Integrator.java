package models;

public abstract class Integrator {

    public abstract double[] run(Particle p, double dt, double finalT);

    protected double calculateA(double x, double v) {
        return -10000 * x - 100 * v;
    }

}