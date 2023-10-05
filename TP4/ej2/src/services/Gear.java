import java.util.ArrayList;
import java.util.List;

import models2.Particle;

public class Gear {
    
    private static final double L = 135;
    private static final double[] gearCoefficients = { 3 / 16.0, 251 / 360.0, 1, 11 / 18.0, 1 / 6.0, 1 / 60.0 };

    private List<Particle> particles;
    private double dt;

    public Gear(List<Particle> particles, double dt) {
        this.particles = particles;
        this.dt = dt;
    }

    private double gearEvaluator(Particle p, double[] predictedValues) {
        p.setX(predictedValues[0] % L);
        p.setVx(predictedValues[1]);
        p.setXnoPeriodic(predictedValues[6]);

        double deltaA = evaluateAceleration(p, particles) - predictedValues[2];
        return deltaA * Math.pow(dt, 2) / 2;
    }

    public void run() {
        List<Particle> newParticles = new ArrayList<>();
        for (Particle p1 : particles) {
            Particle particle = new Particle(p1.getId(), p1.getX(), p1.getVx(), p1.getU(), p1.getRadius(), p1.getMass(),
                    p1.getX2(), p1.getX3(), p1.getX4(), p1.getX5(), p1.getXnoPeriodic());

            double[] predictedValues = gearPredictor(particle.getX() % L, p1.getVx(), p1.getX2(), p1.getX3(),
                    p1.getX4(), p1.getX5(), particle.getXnoPeriodic());

            // predictor
            // evaluator

            double R2 = gearEvaluator(particle, predictedValues);

            // evaluator

            // corrector

            particle.setX((predictedValues[0] + gearCoefficients[0] * R2) % L);
            particle.setXnoPeriodic((predictedValues[6] + gearCoefficients[0] * R2));
            particle.setVx(predictedValues[1] + gearCoefficients[1] * R2 / dt);
            particle.setX2(predictedValues[2] + gearCoefficients[2] * R2 * 2 / Math.pow(dt, 2));
            particle.setX3(predictedValues[3] + gearCoefficients[3] * R2 * 6 / Math.pow(dt, 3));
            particle.setX4(predictedValues[4] + gearCoefficients[4] * R2 * 24 / Math.pow(dt, 4));
            particle.setX5(predictedValues[5] + gearCoefficients[5] * R2 * 120 / Math.pow(dt, 5));

            // corrector
            newParticles.add(particle);
        }
        this.particles = newParticles;
    }

    private double uForce(Particle p) {
        return (p.getU() - p.getVx());
    }

    private double evaluateAceleration(Particle p, List<Particle> particles) {
        double colForces = 0.0;
        for (Particle particle : particles) {
            if (!particle.equals(p) && p.collidesWith(particle, dt)) {
                colForces += p.collision(particle);
            }
        }
        return (uForce(p) + colForces) / p.getMass();
    }

    private double[] gearPredictor(double r, double r1, double r2, double r3, double r4, double r5,
            double rNoPeriodic) {
        double rp = r + r1 * dt + r2 * Math.pow(dt, 2) / 2 + r3 * Math.pow(dt, 3) / 6 + r4 * Math.pow(dt, 4) / 24
                + r5 * Math.pow(dt, 5) / 120;
        double rpNoPeriodic = rNoPeriodic + r1 * dt + r2 * Math.pow(dt, 2) / 2 + r3 * Math.pow(dt, 3) / 6
                + r4 * Math.pow(dt, 4) / 24 + r5 * Math.pow(dt, 5) / 120;
        double r1p = r1 + r2 * dt + r3 * Math.pow(dt, 2) / 2 + r4 * Math.pow(dt, 3) / 6 + r5 * Math.pow(dt, 4) / 24;
        double r2p = r2 + r3 * dt + r4 * Math.pow(dt, 2) / 2 + r5 * Math.pow(dt, 3) / 6;
        double r3p = r3 + r4 * dt + r5 * Math.pow(dt, 2) / 2;
        double r4p = r4 + r5 * dt;
        return new double[] { rp % L, r1p, r2p, r3p, r4p, r5, rpNoPeriodic };

    }

    public List<Particle> getParticles() {
        return particles;
    }

}
