
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import services.DataManager;
import models.BeemanIntegrator;
import models.Particle;

public class Simulation {

    // static double L = 135;
    static BigDecimal finalTime = BigDecimal.valueOf(10);

    private static void uniqueSimulation(int N, BigDecimal dt, int v) throws IOException {

        DataManager dm = new DataManager(
                "./data/input/Static_N_" + N + "_v_" + v + ".dump",
                "./data/input/Dynamic_N_" + N + "_v_" + v + ".dump");
        List<Particle> particles = dm.getParticles();
        BigDecimal currentTime = dt;
        // int iterationPerFrame = (int) Math.ceil(0.1 / dt.doubleValue());
        int iterationPerFrame = 10;
        int frame = 0;
        BeemanIntegrator beemanIntegrator = new BeemanIntegrator(particles, dt);
        List<Particle> limits = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            limits.add(new Particle(i, BigDecimal.valueOf(0), BigDecimal.valueOf(i*0.7), BigDecimal.valueOf(0), BigDecimal.valueOf(0.3)));
            limits.add(new Particle(i, BigDecimal.valueOf(20), BigDecimal.valueOf(i*0.7), BigDecimal.valueOf(0), BigDecimal.valueOf(0.3)));

            limits.add(new Particle(i, BigDecimal.valueOf(i*0.2), BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0.3)));
            limits.add(new Particle(i, BigDecimal.valueOf(i*0.2), BigDecimal.valueOf(70), BigDecimal.valueOf(0), BigDecimal.valueOf(0.3)));
            limits.add(new Particle(i, BigDecimal.valueOf(i*0.2), BigDecimal.valueOf(-10), BigDecimal.valueOf(0), BigDecimal.valueOf(0.3)));

        }

        while (currentTime.compareTo(finalTime) < 0) {
            frame++;
            beemanIntegrator.run();
            if (frame == iterationPerFrame) {
                dm.writeDynamicFile(beemanIntegrator.getParticles(),
                        "./data/output/Dynamic2_N_" + beemanIntegrator.getParticles().size() + "_dt_" + dt + "_v_" + v + ".dump",
                        currentTime, limits);
                frame = 0;
            }
            currentTime = currentTime.add(dt);
        }
        System.out.println("N " + N + " dt " + dt + " version " + v + " finished!");
    }

    public static void main(String[] args) throws IOException {

        double[] dtValues = {  1.0E-3 };
        int Ns[] = { 200 };

        ExecutorService executor = Executors.newFixedThreadPool(Ns.length * dtValues.length * 10);
        List<Future<?>> futures = new ArrayList<>();

        for (double dt : dtValues) {
            for (int n : Ns) {
                for (int v = 0; v < 1; v++) {
                    final int version = v;
                    Future<?> future = executor.submit(() -> {
                        try {
                            uniqueSimulation(n, BigDecimal.valueOf(dt), version);
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
