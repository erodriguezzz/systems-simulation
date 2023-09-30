import models.*;
import services.DataManager;
import sun.java2d.xr.MutableInteger;

import java.util.Set;
import java.util.TreeSet;

public class Simulation {
    private static final int totalSeconds = 5;
    private static final double timeStepper = 1E-5;
    private final Domain domain;
    private final DataManager dm;
    private final TreeSet<Particle> particles;
    private final double L = 5;
    private final double m = 70;
    private final double k = 1E4;
    private final double gamma = 100;
    

    Simulation(int N, double L, int version, Integrator integrator){
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
        this.domain = new Oscilator(k, gamma, this.particles, integrator);
    }

    private static void uniqueSimulation(int N, double L, int version){
        // Simulation sim = new Simulation(N, L, version);
        double time = 0, timeToNextCollision;
        while (time < totalSeconds) {
            // sim.domain.moveParticles(time, Algorithm.VERLET);
            time += timeStepper;
        }
        return;
    }

    public static void main(String[] args) {
        Simulation analyticSim = new Simulation(10, 5, 1, null);
        Simulation verletSim = new Simulation(10, 5, 2, new VerletIntegrator());
        Simulation beemanSim = new Simulation(10, 5, 3, new BeemanIntegrator());
        Simulation gearSim = new Simulation(10, 5, 4, new GearIntegrator());

        double time = 0;
        int i = 0;
        double verletMSE = 0, beemanMSE = 0, gearMSE = 0;

        while (time < totalSeconds) {
            System.out.println("Time: " + time);
            analyticSim.domain.moveParticles(time, (x, v) -> -verletSim.k*x - verletSim.gamma*v);
            verletSim.domain.moveParticles(timeStepper, (x, v) -> -verletSim.k*x - verletSim.gamma*v);
            beemanSim.domain.moveParticles(timeStepper, (x, v) -> -verletSim.k*x - verletSim.gamma*v);
            gearSim.domain.moveParticles(timeStepper, (x, v) -> -verletSim.k*x - verletSim.gamma*v);
            System.out.println("Analytic");
            for (Particle p : analyticSim.particles) {
                System.out.println(p.getX());
            }
            System.out.println();
            System.out.println("Verlet");
            for (Particle p : verletSim.particles) {
                System.out.println(p.getX());
            }
            System.out.println("MSE: " + Math.pow((analyticSim.particles.first().getX() - verletSim.particles.first().getX()), 2));

            System.out.println("Beeman");
            for (Particle p : beemanSim.particles) {
                System.out.println(p.getX());
            }
            System.out.println("MSE: " + Math.pow((analyticSim.particles.first().getX() - beemanSim.particles.first().getX()), 2));

            System.out.println("Gear");
            for (Particle p : gearSim.particles) {
                System.out.println(p.getX());
            }
            System.out.println("MSE: " + Math.pow((analyticSim.particles.first().getX() - gearSim.particles.first().getX()), 2));


            System.out.println();
            System.out.println("---------------------------------------------------");
            System.out.println();

            verletMSE += Math.pow((analyticSim.particles.first().getX() - verletSim.particles.first().getX()), 2);
            beemanMSE += Math.pow((analyticSim.particles.first().getX() - beemanSim.particles.first().getX()), 2);
            gearMSE += Math.pow((analyticSim.particles.first().getX() - gearSim.particles.first().getX()), 2);

            time += timeStepper;
            i++;
        }

        System.out.println("Verlet MSE: " + verletMSE/ i);
        System.out.println("Beeman MSE: " + beemanMSE/ i);
        System.out.println("Gear MSE: " + gearMSE/ i);

        return;
    }




}
