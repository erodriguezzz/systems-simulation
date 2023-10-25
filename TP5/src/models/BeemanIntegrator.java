package models;

import java.util.List;

public class BeemanIntegrator extends Integrator {

    private List<Particle> particles;
    private double dt;
    private final double frecuency = 0.1;
    private final double d = 0.1;
    private Grid grid;
    private double[] beemanRConstants = { 2 / 3, -1 / 6 };
    private double[] beemanVConstants = { 3 / 2, -1 / 2 };
    private double[] beemanVConstantsCorrected = { 1 / 3, 5 / 6, -1 / 6 };

    public BeemanIntegrator(List<Particle> particles, double dt) {
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
        double ax = p.getAx();
        double prevAx = p.getPrevAx();
        double newX = p.getX() + p.getVx() * dt + ax * dt * dt * beemanRConstants[0] + prevAx * dt * dt * beemanRConstants[1];
        p.setX(newX);

        double ay = p.getAy();
        double prevAy = p.getPrevAy();
        double newY = p.getY() + p.getVy() * dt + ay * dt * dt * beemanRConstants[0] + prevAy * dt * dt * beemanRConstants[1];
        p.setY(newY);
        

        // BEGIN SETTING VELOCITY

        double vx = p.getVx();
        double newVx = vx + ax * dt * beemanVConstants[0] + prevAx * dt * beemanVConstants[1];
        p.setVx(newVx);

        double vy = p.getVy();
        double newVy = vy + ay * dt * beemanVConstants[0] + prevAy * dt * beemanVConstants[1];
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
        double vx = p.getVx();
        double ax = p.getAx();
        double axP = p.evaluateAx();
        double prevAx = p.getPrevAx();
        p.setVx(vx + axP * beemanVConstantsCorrected[0] + ax * beemanVConstantsCorrected[1] + prevAx * beemanVConstantsCorrected[2]);
        p.setPrevAx(ax);

        double vy = p.getVy();
        double ay = p.getAy();
        double ayP = p.evaluateAy();
        double prevAy = p.getPrevAy();
        p.setVy(vy + ayP * beemanVConstantsCorrected[0] + ay * beemanVConstantsCorrected[1] + prevAy * beemanVConstantsCorrected[2]);
        p.setPrevAy(ay);
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public double getDt() {
        return dt;
    }
}