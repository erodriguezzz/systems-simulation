package models;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.function.BiFunction;

public class GearIntegrator implements Integrator {

    private final static double[] FACTORIAL = {1.0, 1.0, 2.0, 6.0, 24.0, 120.0};
    private final static double[] ALPHA = {3.0 / 16.0, 251.0 / 360.0, 1.0, 11.0 / 18.0, 1.0 / 6.0, 1.0 / 60.0};

    @Override
    public double[] solve(Particle p, double dt, double tf, BiFunction<Double, Double, Double> force) {
        double v0 = p.getSpeed(), r0 = p.getX(), mass = p.getMass();
        int size = (int) Math.floor(tf / dt);
        final double[] position = new double[size];

        final double[] r = new double[6], rpred = new double[6]; // Order 5
        position[0] = r[0] = r0;
        r[1] = v0;
        r[2] = force.apply(r0, v0) / mass;

        for (int i = 1; i < size; i++) {
            // Predict
            for (int j = 0; j < rpred.length; j++) {
                rpred[j] = taylorExpansion(r, dt, j);
            }

            // Evaluate
            final double da = (force.apply(rpred[0], rpred[1]) / mass) - rpred[2];
            final double dR2 = da * dt * dt * 0.5;

            // Correct
            for (int j = 0; j < r.length; j++) {
                r[j] = correct(rpred[j], dR2, dt, j);
            }

            position[i] = r[0];
        }

        return position;
    }

    private double taylorExpansion(double[] values, double dt, int startFrom) {
        double sum = values[startFrom];
        for (int i = startFrom + 1; i < values.length; i++) {
            sum += values[i] * Math.pow(dt, i - startFrom) / FACTORIAL[i - startFrom];
        }
        return sum;
    }

    private double correct(double predicted, double dR2, double dt, int index) {
        return predicted + ALPHA[index] * dR2 * FACTORIAL[index] / Math.pow(dt, index);
    }

    @Override
    public String toString() {
        return "gear";
    }

}