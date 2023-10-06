package models;

public class BeemanIntegrator extends Integrator{

    @Override
    public double[] run(Particle p, double dt, double finalT) {
        int steps = (int) Math.floor(finalT / dt);
        double[] x = new double[steps], v = new double[] { p.getSpeed(), p.getSpeed() };
        x[0] = p.getX();
    
        for (int t = 1; t < steps; t++) {
            calculatePositionAndVelocity(p, x, v, t, dt, p.getMass());
        }
    
        return x;
    }
    
    private void calculatePositionAndVelocity(Particle p, double[] x, double[] v, int t, double dt, double mass) {
        double xPrev = x[t - 1] - dt * v[0];
        x[t] = x[t - 1] + dt * v[0] + 2 / (3 * mass) * Math.pow(dt, 2) * calculateA(x[t - 1], v[0]) -
                Math.pow(dt, 2) / (6 * mass) * calculateA(xPrev, v[1]);
        p.setX(x[t]);
    
        double auxV = v[0] + 3  / (2 * mass) * dt * calculateA(x[t - 1], v[0]) -
                dt * calculateA(xPrev, v[0]) / (2 * mass);
        double newV = v[0] + dt * calculateA(x[t], auxV) / (3 * mass) +
                5  / (6 * mass) * dt * calculateA(x[t - 1], v[0]) - dt  / (6 * mass) * calculateA(xPrev, v[0]);
        v[1] = v[0];
        v[0] = newV;
        p.setSpeed(newV);

    }


    @Override
    public String toString() {
        return "beeman";
    }
}