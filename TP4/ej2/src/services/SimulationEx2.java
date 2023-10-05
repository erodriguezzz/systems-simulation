
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import models2.Particle;

public class SimulationEx2 {

    static double L = 135;
    static double finalTime = 180.01;

    private static void uniqueSimulation(int N, double dt, int v) throws IOException {

        DataManager2 dm = new DataManager2(
                "./data/input/Static2_N_" + N + "_v_" + v + ".dump",
                "./data/input/Dynamic2_N_" + N + "_v_" + v + ".dump");
        // Collision collision = new Collision(dm.getParticles(), dt);
        List<Particle> particles = dm.getParticles();
        double currentTime = dt;
        int iterationPerFrame = (int) Math.ceil(0.1 / dt);
        int frame = 0;
        Gear gear = new Gear(particles, dt);

        while (currentTime <= finalTime) {
            frame++;
            gear.run();
            if (frame == iterationPerFrame) {
                dm.writeDynamicFile(gear.getParticles(),
                        "./data/output/Dynamic2_N_" + gear.getParticles().size() + "_dt_" + dt + "_v_" + v + ".dump",
                        currentTime);
                frame = 0;
            }
            currentTime += dt;
        }
        System.out.println("N " + N + " dt " + dt + " version " + v + " finished!");
    }

    public static void main(String[] args) throws IOException {

        double[] dtValues = { 1.0E-1, 1.0E-2, 1.0E-3, 1.0E-4, 1.0E-5 };
        int Ns[] = { 5, 10, 15, 25, 20, 30 };

        ExecutorService executor = Executors.newFixedThreadPool(Ns.length * dtValues.length * 10);
        List<Future<?>> futures = new ArrayList<>();

        for (double dt : dtValues) {
            for (int n : Ns) {
                for (int v = 0; v < 1; v++) {
                    final int version = v;
                    Future<?> future = executor.submit(() -> {
                        try {
                            uniqueSimulation(n, dt, version);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    futures.add(future);
                }
            }

        }

        // Wait for all threads to complete
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        System.out.println("Simulation finished!");

    }

}
