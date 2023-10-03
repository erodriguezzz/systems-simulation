import models.*;
import services.DataManager;

public class Simulation {
    private final DataManager dm;
    private final Integrator integrator;
    private static final double K = 10000, GAMMA = 100, totalSeconds = 5;
    private static final Integrator[] INTEGRATORS = {
            new VerletIntegrator(), new BeemanIntegrator(), new GearIntegrator()
    };
    private static final double[] timeSteppers = {1E-1, 1E-2, 1E-3, 1E-4, 1E-5, 1E-6};

    Simulation(double timeStepper, Integrator integrator) {
        this.integrator = integrator;

        this.dm = new DataManager(
                "./data/input/Static.dump",
                "./data/input/Dynamic.dump",
                "./data/output/Dynamic_Int_" + integrator + "_TS_" + timeStepper + ".dump");
    }

    private static void uniqueSimulation(double timeStepper, Integrator integrator){
        Simulation sim = new Simulation(timeStepper, integrator);
        Particle p = sim.dm.getParticles().iterator().next();
        final double[] solution = sim.integrator.solve(
                p, timeStepper, totalSeconds, (r, v) -> -K * r - GAMMA * v
        );

        sim.dm.writeOscilatorFile(solution, timeStepper);
    }

    public static void main(String[] args) {
        for (Integrator integrator : INTEGRATORS) {
            for (double timeStepper : timeSteppers) {
                uniqueSimulation(timeStepper, integrator);
            }
        }
    }
}
