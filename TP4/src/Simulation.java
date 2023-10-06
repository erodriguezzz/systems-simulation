import models.*;
import models.Gear;
import services.DataManager;

public class Simulation {
    private final DataManager dm;
    private final Integrator integrator;
    private static final double totalSeconds = 5;
    private static final Integrator[] INTEGRATORS = {
            new VerletIntegrator(), new BeemanIntegrator(), new Gear()
    };
    private static final double[] timeSteppers = {1E-1, 1E-2, 1E-3, 1E-4, 1E-5, 1E-6};

    Simulation(double timeStepper, Integrator integrator) {
        this.integrator = integrator;

        this.dm = new DataManager(
                "./data/input/Static.dump",
                "./data/input/Dynamic.dump",
                "./data/output/Dynamic_Int_" + integrator + "_TS_" + timeStepper + ".dump");
    }

    private static void oscilatorSim(double timeStepper, Integrator integrator){
        Simulation sim = new Simulation(timeStepper, integrator);
        Particle p = sim.dm.getParticles().iterator().next();
        final double[] solution = sim.integrator.run(
                p, timeStepper, totalSeconds
        );

        sim.dm.writeOscilatorFile(solution, timeStepper);
    }

    public static void main(String[] args) {
        for (Integrator integrator : INTEGRATORS) {
            for (double timeStepper : timeSteppers) {
                oscilatorSim(timeStepper, integrator);
            }
        }
    }
}
