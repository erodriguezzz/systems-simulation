package models2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import services.DataManager2;

public class GearPredictor {
    
    static double L = 135;
    public static double totalTime = 180.01;
    private DataManager2 dm;


    private List<Particle> particles;
    private double dt;
    private int v;
    
    private List<List<Double>> particeDerivatives = new ArrayList<>();
    private static final double[] coefficients = {3.0/16, 251.0/360, 1, 11.0/18, 1.0/6, 1.0/60};

    public GearPredictor(List<Particle> particles, double dt, DataManager2 dm, int v) {
        this.particles = particles;
        this.dt = dt;
        this.dm = dm;
        this.v = v;
    }

    public void run() throws IOException {
        initializeDerivatives(particles, dt);
        gear(dt);
    }

    private double getNewAx(Particle p1, List<Particle> particleList, double dt){
        double sumForces = 0;
        for (Particle p2 : particleList) {
            if (!p1.equals(p2) && p1.isColliding(p2, dt)) {
                sumForces += p1.getCollisionForce(p2);
            }
        }
        return (p1.getForce() + sumForces)/p1.getMass();
    }

    public void gear(double dT) throws IOException {

        double currentTime = dT;
        List<List<Double>> iteration = particeDerivatives;
        int frames=0;
        int iterationsPerFrame = (int) (0.1/dT);
        while(currentTime <= totalTime) {
            frames++;
            for (Particle particle: particles) {
                particle.setX(iteration.get(particle.getId()).get(0));
                particle.setVx(iteration.get(particle.getId()).get(1));
                particle.setAx(iteration.get(particle.getId()).get(2));
            }
            if(frames == iterationsPerFrame) {
                dm.writeDynamicFile(particles, "./data/output/Dynamic2_N_" + particles.size() + "_dt_" + dT + "_v_" + v +".dump", currentTime);
                frames = 0;
            }
            List<List<Double>> newDerivatives = gearPredict(iteration, dT, particles);

            List<Double> deltasR2 = evaluate(newDerivatives, dT, particles);

            iteration = gearCorrect(newDerivatives, dT, deltasR2);

            currentTime += dT;
        }
    }

    public void initializeDerivatives(List<Particle> particles, double dt){
        for (Particle particle: particles) {
            List<Double> auxR = new ArrayList<>();
            //r0
            auxR.add(particle.getX());
            //r1
            auxR.add(particle.getVx());
            //r2
            Double aux = getNewAx(particle, particles, dt);
            auxR.add(aux);
            //r3
            auxR.add(0.0);
            //r4
            auxR.add(0.0);
            //r5
            auxR.add(0.0);

            particeDerivatives.add(auxR);
        }
    }

    public List<List<Double>> gearPredict(List<List<Double>> der, double dT, List<Particle> particles){
        List<List<Double>> newDerivatives = new ArrayList<>();
        int count = 0;
        double r0, r1, r2, r3, r4, r5;
        for(List<Double> rs : der) {
            List<Double> auxNewDerivatives = new ArrayList<>();

            r0 = rs.get(0) + rs.get(1) * dT + rs.get(2) * Math.pow(dT, 2) / 2 + rs.get(3) * Math.pow(dT, 3) / 6 + rs.get(4) * Math.pow(dT, 4) / 24 + rs.get(5) * Math.pow(dT, 5) / 120;
            particles.get(count).setX(r0);
            auxNewDerivatives.add(r0 % L);

            r1 = rs.get(1) + rs.get(2) * dT + rs.get(3) * Math.pow(dT, 2) / 2 + rs.get(4) * Math.pow(dT, 3) / 6 + rs.get(5) * Math.pow(dT, 4) / 24;
            particles.get(count).setVx(r1);
            auxNewDerivatives.add(r1);

            r2 = rs.get(2) + rs.get(3) * dT + rs.get(4) * Math.pow(dT, 2) / 2 + rs.get(5) * Math.pow(dT, 3) / 6;
            particles.get(count).setAx(r2);
            auxNewDerivatives.add(r2);

            r3 = rs.get(3) + rs.get(4) * dT + rs.get(5) * Math.pow(dT, 2) / 2;
            auxNewDerivatives.add(r3);

            r4 = rs.get(4) + rs.get(5) * dT;
            auxNewDerivatives.add(r4);

            r5 = rs.get(5);
            auxNewDerivatives.add(r5);

            newDerivatives.add(auxNewDerivatives);
            count++;
        }

        return newDerivatives;
    }

    // public double getTaylorAprox(List<Double> rs, int terms, double dT, int start){
    //     double taylor = 0, counter = 0;

    //     while(counter < terms){
    //         taylor += rs.get(start)
    //     }

    //     return taylor;
    // }

    private List<Double> evaluate(List<List<Double>> newDerivatives, double dT, List<Particle> particles){
        List<Double> deltasR2 = new ArrayList<>();
        for(Particle particle : particles){

            Double F = getNewAx(particle, particles,dT);
            Double r2 = newDerivatives.get(particle.getId()).get(2);

            double dR2X = (F - r2) * dT*dT / 2;

            deltasR2.add(dR2X);
        }
        return deltasR2;
    }

    public List<List<Double>> gearCorrect(List<List<Double>> der, double dT, List<Double>  dR2){
        List<List<Double>> newDerivatives = new ArrayList<>();
        int count = 0;
        for(List<Double> rs : der) {

            List<Double> auxNewDerivatives = new ArrayList<>();

            double r0x = rs.get(0) + (coefficients[0] * dR2.get(count));
            auxNewDerivatives.add(r0x % L);

            double r1x = rs.get(1) + (coefficients[1] * dR2.get(count) * 1 ) / (dT);
            auxNewDerivatives.add(r1x);


            double r2x = rs.get(2) + (coefficients[2] * dR2.get(count) * 2) / (dT * dT);
            auxNewDerivatives.add(r2x);


            double r3x = rs.get(3) + (coefficients[3] * dR2.get(count) * 6) / (Math.pow(dT, 3));
            auxNewDerivatives.add(r3x);


            double r4x = rs.get(4) + (coefficients[4] * dR2.get(count) * 24) / (Math.pow(dT, 4));
            auxNewDerivatives.add(r4x);

            double r5x = rs.get(5) + (coefficients[5] * dR2.get(count) * 120) / (Math.pow(dT, 5));
            auxNewDerivatives.add(r5x);

            count++;
            newDerivatives.add(auxNewDerivatives);
        }
        return newDerivatives;
    }
}
