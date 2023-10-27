
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import models.Grid;
import services.ForcesUtils;
import services.DataManager;
import models.BeemanIntegrator;
import models.Particle;

public class Simulation {

    // static double L = 135;
    static double finalTime = 50;

    private static void uniqueSimulation(int N, double dt, double w, int v) throws IOException {

        DataManager dm = new DataManager(
                "./data/input/Static_N_" + N + "_v_" + v + ".dump",
                "./data/input/Dynamic_N_" + N + "_v_" + v + ".dump");
        List<Particle> particles = dm.getParticles();
        double currentTime = dt;
        // int iterationPerFrame = (int) Math.ceil(0.1 / dt.doubleValue());
        int iterationPerFrame = 1000;
        int frame = 0;
        // Grid grid = new Grid(particles);
        BeemanIntegrator beemanIntegrator = new BeemanIntegrator(dt, 3, w, particles);

        List<Particle> limits = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            // TODO: change id schema to avoid problems
            limits.add(new Particle(i, 0, i * 77 / 20, 0, 0.3));
            limits.add(new Particle(i, 20, i * 77 / 20, 0, 0.3));

            limits.add(new Particle(i, i * 1, 7, 0, 0.3));
            limits.add(new Particle(i, i * 1, 77, 0, 0.3));
            // limits.add(new Particle(i, BigDecimal.valueOf(i*0.2),
            // BigDecimal.valueOf(-10), BigDecimal.valueOf(0), BigDecimal.valueOf(0.3)));

        }

        while (currentTime - finalTime < 0) {
            frame++;
            beemanIntegrator.run(currentTime);
            if (frame == iterationPerFrame) {
                System.out.format("Frame: %.4f\n", currentTime);
                dm.writeDynamicFile(beemanIntegrator.getParticles(),
                        "./data/output/g=" + ForcesUtils.GRAVITY + "_Kn="+ ForcesUtils.K_NORMAL +"_w=" + w+"_N=" + N + ".dump",
                        // "./data/output/Dynamic2_N_" + beemanIntegrator.getParticles().size() + "_dt_"
                        // + dt + "_v_" + v + ".dump",
                        currentTime, limits);
                frame = 0;
            }
            currentTime = currentTime + dt;
        }
        System.out.println("N " + N + " dt " + dt + " version " + v + " finished!");
    }

    public static void main(String[] args) throws IOException {

        double[] dtValues = { 1.0E-4 };
        int Ns[] = { 200 };
        double ws[] = { 5, 10, 15, 20, 30, 50 };
        // double ws[] = { 5, 10, 15};

        ExecutorService executor = Executors.newFixedThreadPool(Ns.length * dtValues.length * 10);
        List<Future<?>> futures = new ArrayList<>();

        for (double dt : dtValues) {
            for (int n : Ns) {
                for (int v = 0; v < 1; v++) {
                    for (double w : ws) {
                        final int version = v;
                        Future<?> future = executor.submit(() -> {
                            try {
                                uniqueSimulation(n, (dt), w, version);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        futures.add(future);
                    }
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
