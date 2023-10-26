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

    public BeemanIntegrator(Grid grid, double dt) {
        this.particles = grid.getParticles();
        this.dt = dt;
        this.grid = grid;
    }

    public void run(double time) {
        // grid.sin();
        // if(time > 0.224){
        //     // System.out.println("hola");
        // }
        particles.forEach(p -> p.prediction(dt));
        particles.forEach(p -> p.resetForce());

        grid.updateForces(particles, time);

        particles.forEach(p -> p.correction(dt));
        particles.forEach(p -> p.resetForce());

        grid.updateForces(particles, time);

        // particles.forEach(p -> {
        //     if(p.getId() == 1){ 
        //         System.out.println(p);
        //     }
        // });

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
        double newX = p.getX() + p.getVx() * dt + p.getAx() * dt * dt * beemanRConstants[0] + p.getPrevAx() * dt * dt * beemanRConstants[1];
        p.setX(newX);

        double newY = p.getY() + p.getVy() * dt + p.getAy() * dt * dt * beemanRConstants[0] + p.getPrevAy() * dt * dt * beemanRConstants[1];
        p.setY(newY);
        

        // BEGIN SETTING VELOCITY

        double vx = p.getVx();
        double newVx = vx + p.getAx() * dt * beemanVConstants[0] + p.getPrevAx() * dt * beemanVConstants[1];
        p.setVx(newVx);

        double vy = p.getVy();
        double newVy = vy + p.getAy() * dt * beemanVConstants[0] + p.getPrevAy() * dt * beemanVConstants[1];
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