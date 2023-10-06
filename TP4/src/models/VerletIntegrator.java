package models;

public class VerletIntegrator extends Integrator {

    @Override
    public double[] run(Particle particle, double timeStep, double finalT) {
        double initialPosition = particle.getX();
        double mass = particle.getMass();
        double currentVelocity = particle.getSpeed();

        int steps = (int) Math.floor(finalT / timeStep);
        double[] x = new double[steps];

        initializePositions(x, initialPosition, currentVelocity, timeStep, calculateA(initialPosition, currentVelocity), mass);

        for (int t = 2; t < steps; t++) {
            currentVelocity = updateVelocity(currentVelocity, timeStep, calculateA(x[t-1], currentVelocity), mass);
            x[t] = updatePosition(x[t - 1], x[t - 2], timeStep, calculateA(x[t-1], currentVelocity), mass);
        }

        return x;
    }

    private void initializePositions(double[] x, double initialPosition, double currentVelocity, double timeStep,
                                     double a, double mass) {
        x[0] = initialPosition;
        x[1] = 2 * x[0] - (x[0] - currentVelocity * timeStep) + (timeStep * timeStep * a) / mass;
    }

    private double updateVelocity(double currentVelocity, double timeStep, double a, double mass) {
        return currentVelocity + (timeStep / mass) * a;
    }

    private double updatePosition(double currentPosition, double previousPosition, double timeStep, double a, double mass) {
        return 2 * currentPosition - previousPosition + (timeStep * timeStep * a) / mass;
    }

    @Override
    public String toString() {
        return "verlet";
    }
}