package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import models.Domain;
import models2.GearPredictor;
import models2.Particle;

public class SimulationEx2 {

    private DataManager2 dm;
    private List<Particle> particles;
    
    static double L = 135;

    SimulationEx2(int N, double dt){
        this.dm = new DataManager2(
                "./data/input/Static2_N_" + N + ".dump",
                "./data/input/Dynamic2_N_" + N + ".dump");
        this.particles = dm.getParticles();
    }

    private static void uniqueSimulation(int N, double dt) throws IOException{
        SimulationEx2 sim = new SimulationEx2(N, dt);
        GearPredictor gearPredictor = new GearPredictor(sim.particles, dt, sim.dm);
        gearPredictor.run();
        // sim.initialRs(sim.particles, dt);
        // sim.gear(dt);
        System.out.println("N " + N + " dt " + dt +" finished!");
    }

    public static void main(String[] args) throws IOException {
        int Ns[] = {30};
        // int Ns[] = {5, 10, 15, 20, 25};
        // double dts[] = {Math.pow(10, -3), Math.pow(10, -4)};
        double dts[] = {Math.pow(10, -1), Math.pow(10, -2), Math.pow(10, -3), Math.pow(10, -4), Math.pow(10, -5)};

        ExecutorService executor = Executors.newFixedThreadPool(Ns.length * dts.length);
        List<Future<?>> futures = new ArrayList<>();

        for(int N : Ns){
            for(double dt : dts){
                Future<?> future = executor.submit(() -> {
                    try {
                        uniqueSimulation(N, dt);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                futures.add(future);
            }
        }

        // Wait for all threads to complete
        for(Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // Shutdown the executor
        executor.shutdown();
        System.out.println("Simulation finished!");
    }

}
