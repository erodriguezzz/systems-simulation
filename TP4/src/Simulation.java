import models.Algorithm;
import models.Domain;
import models.Oscilator;
import models.Particle;
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
    

    Simulation(int N, double L, int version){
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
        this.domain = new Oscilator(k, gamma, this.particles);
    }

    private static void uniqueSimulation(int N, double L, int version){
        Simulation sim = new Simulation(N, L, version);
        double time = 0, timeToNextCollision;
        while (time < totalSeconds) {
            sim.domain.moveParticles(time, Algorithm.VERLET);
            time += timeStepper;
        }
        return;
    }

    public static void main(String[] args) {
        Simulation analyticSim = new Simulation(10, 5, 1);
        Simulation verletSim = new Simulation(10, 5, 2);
        Simulation beemanSim = new Simulation(10, 5, 3);
        Simulation gearSim = new Simulation(10, 5, 4);
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
            for (Particle p : analyticSim.particles) {
                System.out.println(p.getX());
            }
            System.out.println("Verlet");
            for (Particle p : verletSim.particles) {
                System.out.println(p.getX());
            }

//            System.out.println("Beeman");
//            for (Particle p : beemanSim.particles) {
//                System.out.println(p.getX());
//            }
//            System.out.println("Gear");
//            for (Particle p : gearSim.particles) {
//                System.out.println(p.getX());
//            }



            System.out.println();
            System.out.println();
        }
        return;
    }




}
