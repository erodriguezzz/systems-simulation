package services;

import java.util.HashSet;
import java.util.TreeSet;

import models.Algorithm;
import models.LinearDomain;
import models.LinearParticle;
import models.Particle;

public class Simulation2 {
    private static double r = 2.25;
    private double k = 2500;
    static double circleR =21.49;
    static double mass = 0.25;

    public static double totalSeconds = 1000;
    public static double timeStepper = 0.1;

    private DataManager2 dm;
    private HashSet<LinearParticle> particles;
    private LinearDomain domain;

    Simulation2(int N, double L, int version){
        String[] outputs = {
                    "./data/output/dynamic/Dynamic2_N_" + N + ".dump"
        };
        this.dm = new DataManager2(
                "./data/input/Static2_N_" + N + ".dump",
                "./data/input/Dynamic2_N_" + N + ".dump");
        this.particles = dm.getParticles();
        System.out.println("Particles: " + particles.size());
        //this.domain = new Domain(dm.getL(), this.particles); // TODO: adjust DataManager to new input format
        this.domain = new LinearDomain(L, particles);
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
        Simulation2 verletSim = new Simulation2(10, 5, 2);
        double time = 0, previousTime = 0;
        while (time < totalSeconds) {
            previousTime = time;
            time += timeStepper;
            // System.out.println("Time: " + time);
            verletSim.domain.moveParticles(time - previousTime, Algorithm.VERLET);

            // System.out.println("Verlet");
            for (LinearParticle p : verletSim.particles) {
                // System.out.println(p.getX());
            }
            DataManager2.writeDynamicFile(verletSim.particles, "./data/input/Dynamic2_N_" + 10 + ".dump", time);

            // System.out.println();
            // System.out.println();
        }
        return;
    }
}
