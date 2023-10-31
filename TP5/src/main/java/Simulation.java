
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import models.Grid;
import models.Pair;
import services.DataManager;
import models.BeemanIntegrator;
import models.Particle;
import services.JsonConfigurer;

public class Simulation {

    // static double L = 135;
    static double finalTime = 1000;
    static double LIMIT_MASS = 0;
    static double LIMIT_RADIUS = 0.3;

    private static void uniqueSimulation(int N, double dt, double w, int v, double D, JsonConfigurer config) throws IOException {

        DataManager dm = new DataManager(
                "../data/input/Static_N_" + N + "_v_" + v + ".dump",
                "../data/input/Dynamic_N_" + N + "_v_" + v + ".dump",
                dt,
                config);
        List<Particle> particles = dm.getParticles();
        double currentTime = dt;
        // int iterationPerFrame = (int) Math.ceil(0.1 / dt.doubleValue());
        int iterationPerFrame = config.getIterationPerFrame().intValue();
        int frame = 0;
        // Grid grid = new Grid(particles);
        BeemanIntegrator beemanIntegrator = new BeemanIntegrator(dt, D, w, particles, config);

        List<Particle> limits = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            // TODO: change id schema to avoid problems
            // TODO: change domain design to match with config file
            limits.add(new Particle(i, new Pair(0.0, i * (config.getM() + 7) / config.getL()), LIMIT_MASS, LIMIT_RADIUS, dt, config));
            limits.add(new Particle(i, new Pair(config.getL(), i * (config.getM() + 7) / config.getL()), LIMIT_MASS, LIMIT_RADIUS, dt, config));

            limits.add(new Particle(i, new Pair(i/1.0, 7.0), LIMIT_MASS, LIMIT_RADIUS, dt, config));
            limits.add(new Particle(i, new Pair(i/1.0, (config.getM() + 7)), LIMIT_MASS, LIMIT_RADIUS, dt, config));
            // limits.add(new Particle(i, BigDecimal.valueOf(i*0.2),
            // BigDecimal.valueOf(-10), BigDecimal.valueOf(0), BigDecimal.valueOf(0.3)));

        }

        while (currentTime - finalTime < 0) {
            frame++;
            beemanIntegrator.run(currentTime);
            if (frame == iterationPerFrame) {
                System.out.format("Frame: %.4f\n", currentTime);
//                dm.writeDynamicFile(beemanIntegrator.getParticles(),
//                        "../data/output/d=" + D +
//                                "_w=" + w +
//                                "_v=" + v +
//                                ".dump",
//                        // "./data/output/Dynamic2_N_" + beemanIntegrator.getParticles().size() + "_dt_"
//                        // + dt + "_v_" + v + ".dump",
//                        currentTime, limits);
                frame = 0;
            }
            currentTime = currentTime + dt;
        }
        dm.writeTimeFile("../data/output/time_d=" + D +
                        "_w=" + w +
                        "_v=" + v +
                        ".dump",
                beemanIntegrator.getTimes());
        System.out.println("N " + N + " dt " + dt + " version " + v + " finished!");
    }

    public static void main(String[] args) throws IOException {

        JsonConfigurer config = new JsonConfigurer("./config.json");

        double[] dtValues = config.getDts().stream().mapToDouble(Double::doubleValue).toArray();
        int[] Ns = config.getNs().stream().mapToInt(Double::intValue).toArray();
        double[] ws= config.getWs().stream().mapToDouble(Double::doubleValue).toArray();
        double[] Ds= config.getDs().stream().mapToDouble(Double::doubleValue).toArray();

        ExecutorService executor = Executors.newFixedThreadPool(Ns.length * dtValues.length * ws.length * Ds.length * 10);
        List<Future<?>> futures = new ArrayList<>();

        for (double dt : dtValues) {
            for (int n : Ns) {
                for (int v = 0; v < 1; v++) {
                    for (double w : ws) {
                        for (double D: Ds){
                            final int version = v;
                            Future<?> future = executor.submit(() -> {
                                try {
                                    uniqueSimulation(n, dt, w, version, D, config);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                            futures.add(future);
                        }
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
