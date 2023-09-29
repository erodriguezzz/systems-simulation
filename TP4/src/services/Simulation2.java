package services;

import models.CircleDomain;
import models.CircleParticle;

public class Simulation2 {
    private static double r = 2.25;
    private double k = 2500;
    static double circleR =21.49;
    static double mass = 0.25;

    Simulation2(int N, double L, int version){
        String[] outputs = {
                    "./data/output/dynamic/Dynamic_N_" + N + "_L_" + L +"_v" +version+".dump",
                    "./data/output/VaN_" + N + "_L_" + L + "_v" +version+".txt",
                    "./data/output/VaN_" + N + "_L_" + L +"_v" +version+".txt",
                    "./data/output/VaN_" + N + "_L_" + L +"_v" +version+".txt",
        };
        this.dm = new DataManager(
                "./data/input/Static_N_" + N + ".dump",
                "./data/input/Dynamic_N_" + N + ".dump",
                outputs);
        this.particles = new TreeSet<>(dm.getParticles());
        //this.domain = new Domain(dm.getL(), this.particles); // TODO: adjust DataManager to new input format
        this.domain = new CircleDomain();
    }

    private static void uniqueSimulation2(int N, double L, int version){
        Simulation2 sim = new Simulation2(N, L, version);
        double time = 0, timeToNextCollision;
        while (time < totalSeconds) {
            sim.domain.moveParticles(time, Algorithm.VERLET);
            time += timeStepper;
        }
        return;
    }

    public static void main(String[] args) {
        Simulation2 analyticSim = new Simulation2(10, 5, 1);
        Simulation2 verletSim = new Simulation2(10, 5, 2);
        Simulation2 beemanSim = new Simulation2(10, 5, 3);
        Simulation2 gearSim = new Simulation2(10, 5, 4);
        double time = 0, previousTime = 0;
        while (time < totalSeconds) {
            previousTime = time;
            time += timeStepper;
            System.out.println("Time: " + time);
            analyticSim.domain.moveParticles(time);
            verletSim.domain.moveParticles(time - previousTime, Algorithm.VERLET);
            beemanSim.domain.moveParticles(time - previousTime, Algorithm.BEEMAN);
            gearSim.domain.moveParticles(time - previousTime, Algorithm.GEAR);
            System.out.println("Analytic");
            for (CircleParticle p : analyticSim.particles) {
                System.out.println(p.getX());
            }
            System.out.println("Verlet");
            for (CircleParticle p : verletSim.particles) {
                System.out.println(p.getX());
            }



            System.out.println();
            System.out.println();
        }
        return;
    }
}
