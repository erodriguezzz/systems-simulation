package models;

import java.util.List;

public class Gear extends Integrator {

    private final static double[] gearCoefficients = { 3.0 / 16.0, 251.0 / 360.0, 1.0, 11.0 / 18.0, 1.0 / 6.0,
            1.0 / 60.0 };
    private List<Particle> particles;
    private double dt;

    public Gear() {
    }

    public Gear(List<Particle> particles, double dt) {
        this.particles = particles;
        this.dt = dt;
    }

    @Override
    public double[] run(Particle p, double dt, double tf) {
        double mass = p.getMass();
        int steps = (int) Math.floor(tf / dt);
        final double[] x = new double[steps];

        double[] predictedValues = new double[6];
        x[0] = p.getX();

        for (int t = 1; t < steps; t++) {
            // Predict
            predictedValues = gearPredictor(p.getX(), p.getSpeed(), p.getAx(), p.getX3(), p.getX4(), p.getX5());

            // Evaluate
            final double da = (calculateA(predictedValues[0], predictedValues[1]) / mass) - predictedValues[2];
            final double R2 = da * Math.pow(dt, 2) * 0.5;

            // Correct
            p.setX(predictedValues[0] + gearCoefficients[0] * R2);
            p.setSpeed(predictedValues[1] + gearCoefficients[1] * R2 / dt);
            p.setAx(predictedValues[2] + gearCoefficients[2] * R2 * 2 / Math.pow(dt, 2));
            p.setX3(predictedValues[3] + gearCoefficients[3] * R2 * 6 / Math.pow(dt, 3));
            p.setX4(predictedValues[4] + gearCoefficients[4] * R2 * 24 / Math.pow(dt, 4));
            p.setX5(predictedValues[5] + gearCoefficients[5] * R2 * 120 / Math.pow(dt, 5));

            x[t] = p.getX();
        }

        return x;
    }

    private double[] gearPredictor(double r, double r1, double r2, double r3, double r4, double r5) {
        double rp = r + r1 * dt + r2 * Math.pow(dt, 2) / 2 + r3 * Math.pow(dt, 3) / 6 + r4 * Math.pow(dt, 4) / 24
                + r5 * Math.pow(dt, 5) / 120;
        double r1p = r1 + r2 * dt + r3 * Math.pow(dt, 2) / 2 + r4 * Math.pow(dt, 3) / 6 + r5 * Math.pow(dt, 4) / 24;
        double r2p = r2 + r3 * dt + r4 * Math.pow(dt, 2) / 2 + r5 * Math.pow(dt, 3) / 6;
        double r3p = r3 + r4 * dt + r5 * Math.pow(dt, 2) / 2;
        double r4p = r4 + r5 * dt;
        return new double[] { rp, r1p, r2p, r3p, r4p, r5 };

    }

    @Override
    public String toString() {
        return "gear";
    }

}