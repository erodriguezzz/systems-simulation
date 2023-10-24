package models;

import java.math.BigDecimal;
import java.util.List;

public class BeemanIntegrator extends Integrator {

    private List<Particle> particles;
    private BigDecimal dt;
    private final BigDecimal frecuency = BigDecimal.valueOf(0.1);
    private final BigDecimal d = BigDecimal.valueOf(0.1);
    private Grid grid;
    private BigDecimal[] beemanRConstants = { BigDecimal.valueOf(2 / 3), BigDecimal.valueOf(-1 / 6) };
    private BigDecimal[] beemanVConstants = { BigDecimal.valueOf(3 / 2), BigDecimal.valueOf(-1 / 2) };
    private BigDecimal[] beemanVConstantsCorrected = { BigDecimal.valueOf(1 / 3), BigDecimal.valueOf(5 / 6),
            BigDecimal.valueOf(-1 / 6) };

    public BeemanIntegrator(List<Particle> particles, BigDecimal dt) {
        this.particles = particles;
        this.dt = dt;
        this.grid = new Grid();
    }

    public void run() {
        // grid.sin();

        particles.forEach(p -> predict(p));
        particles.forEach(p -> p.resetForce());

        grid.updateForces(particles);

        particles.forEach(p -> correct(p));
        particles.forEach(p -> p.resetForce());

        grid.updateForces(particles);

    }

    @Override
    public double[] run(Particle p, double dt, double finalT) {
        // int steps = (int) Math.floor(finalT / dt);
        // double[] x = new double[steps], v = new double[] { p.getVx(), p.getVx() };
        // x[0] = p.getX();

        // for (int t = 1; t < steps; t++) {
        // calculatePositionAndVelocity(p, x, v, t, dt, p.getMass());
        // }

        // return x;
        double[] a = { 0 };
        return a;
    }

    // private void calculatePositionAndVelocity(Particle p, double[] x, double[] v,
    // int t, double dt, double mass) {
    // double xPrev = x[t - 1] - dt * v[0];
    // x[t] = x[t - 1] + dt * v[0] + 2 / (3 * mass) * Math.pow(dt, 2) *
    // calculateA(x[t - 1], v[0]) -
    // Math.pow(dt, 2) / (6 * mass) * calculateA(xPrev, v[1]);
    // p.setX(x[t]);

    // double auxV = v[0] + 3 / (2 * mass) * dt * calculateA(x[t - 1], v[0]) -
    // dt * calculateA(xPrev, v[0]) / (2 * mass);
    // double newV = v[0] + dt * calculateA(x[t], auxV) / (3 * mass) +
    // 5 / (6 * mass) * dt * calculateA(x[t - 1], v[0]) - dt / (6 * mass) *
    // calculateA(xPrev, v[0]);
    // v[1] = v[0];
    // v[0] = newV;
    // p.setVx(newV);

    // }

    private void predict(Particle p) {

        // INITAL CONDITIONS
        p.setAx(p.evaluateAx());
        p.setAy(p.evaluateAy());
            
        // BEGIN SETTING POSITIONS
        BigDecimal ax = p.getAx();
        BigDecimal prevAx = p.getPrevAx();
        BigDecimal newX = p.getX().add(
                p.getVx().multiply(dt)).add(
                        ax.multiply(dt.pow(2).multiply(beemanRConstants[0])))
                .add(
                        prevAx.multiply(dt.pow(2).multiply(beemanRConstants[1])));
        p.setX(newX);

        BigDecimal ay = p.getAy();
        BigDecimal prevAy = p.getPrevAy();
        BigDecimal newY = p.getY().add(
                p.getVy().multiply(dt)).add(
                        ay.multiply(dt.pow(2).multiply(beemanRConstants[0])))
                .add(
                        prevAy.multiply(dt.pow(2).multiply(beemanRConstants[1])));
        p.setY(newY);
        

        // BEGIN SETTING VELOCITY

        BigDecimal vx = p.getVx();
        BigDecimal newVx = vx.add(
                ax.multiply(dt.multiply(beemanVConstants[0]))).add(
                        prevAx.multiply(dt.multiply(beemanVConstants[1])));
        p.setVx(newVx);

        BigDecimal vy = p.getVy();
        BigDecimal newVy = vy.add(
                ay.multiply(dt.multiply(beemanVConstants[0]))).add(
                        prevAy.multiply(dt.multiply(beemanVConstants[1])));
        p.setVy(newVy);
        // if(p.getId() == 1){
        //         System.out.println(
        //         " dt: " + dt +
        //         // " ay: " + ay +
        //         // " prevAy: " + prevAy +
        //         " newy: " + p.getY() +
        //         " newY: " + p.getY().add(p.getVx().multiply(dt)) +
        //         " oldVELy * dt: " + p.getVx().multiply(dt) +
        //         " newVELy: " + newVy +
        //         "\n"
        //         );
        // }
    }

    private void correct(Particle p) {
        BigDecimal vx = p.getVx();
        BigDecimal ax = p.getAx();
        BigDecimal axP = p.evaluateAx();
        BigDecimal prevAx = p.getPrevAx();
        p.setVx(vx.add(axP.multiply(beemanVConstantsCorrected[0])).add(ax.multiply(beemanVConstantsCorrected[1]))
                .add(prevAx.multiply(beemanVConstantsCorrected[2])));
        p.setPrevAx(ax);

        BigDecimal vy = p.getVy();
        BigDecimal ay = p.getAy();
        BigDecimal ayP = p.evaluateAy();
        BigDecimal prevAy = p.getPrevAy();
        p.setVy(vy.add(ayP.multiply(beemanVConstantsCorrected[0])).add(ay.multiply(beemanVConstantsCorrected[1]))
                .add(prevAy.multiply(beemanVConstantsCorrected[2])));
        p.setPrevAy(ay);
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public BigDecimal getDt() {
        return dt;
    }
}