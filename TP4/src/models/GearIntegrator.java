package models;

import java.util.Collection;
import java.util.function.BiFunction;

public class GearIntegrator implements Integrator {

    private static final double[] coefficients = {3 / 16.0, 251 / 360.0, 1, 11 / 18.0, 1 / 6.0, 1 / 60.0};
    private static final double[] A = {0, 0, 0, 0, 0, 0};

    @Override
    public void solve(double dt, Collection<Particle> particles, BiFunction<Double, Double, Double> force) {
        for (Particle p : particles) {
            double mass = p.getMass();

            double x = p.getX();
            double v = p.getSpeed();
            double a = A[2] == 0 ? force.apply(x, v) / mass : A[2];
            double x3 = A[3] == 0 ? force.apply(v, a) / mass : A[3];
            double x4 = A[4] == 0 ? force.apply(a, x3) / mass : A[4];
            double x5 = A[5] == 0 ? force.apply(x3, x4) / mass : A[5];

            double xPredict = x + v * dt + a * Math.pow(dt,2) / 2 + x3 * Math.pow(dt,3) / 6 + x4 * Math.pow(dt, 4) / 24 + x5 * Math.pow(dt, 5) / 120;
            double x1Predict = v + a * dt + x3 * Math.pow(dt,2) / 2 + x4 * Math.pow(dt,3) / 6 + x5 * Math.pow(dt, 4) / 24;
            double x2Predict = a + x3 * dt + x4 * Math.pow(dt,2) / 2 + x5 * Math.pow(dt,3) / 6;
            double x3Predict = x3 + x4 * dt + x5 * Math.pow(dt,2) / 2;
            double x4Predict = x4 + x5 * dt;

            double deltaA = force.apply(xPredict, x1Predict) / mass - x2Predict;
            double deltaR2 = deltaA * Math.pow(dt, 2) / 2;

            A[0] = xPredict + deltaR2 * coefficients[0];
            A[1] = x1Predict + deltaR2 * coefficients[1] / dt;
            A[2] = x2Predict + deltaR2 * coefficients[2] / Math.pow(dt, 2) * 2;
            A[3] = x3Predict + deltaR2 * coefficients[3] / Math.pow(dt, 3) * 6;
            A[4] = x4Predict + deltaR2 * coefficients[4] / Math.pow(dt, 4) * 24;
            A[5] = x5 + deltaR2 * coefficients[5] / Math.pow(dt, 5) * 120;

            p.setX(A[0]);
            p.setSpeed(A[1]);
        }

    }

}
