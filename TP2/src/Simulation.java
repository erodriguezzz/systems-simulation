import models.Grid;
import models.NonPeriodicGrid;
import models.Particle;
import models.PeriodicGrid;
import services.DataManager;

import java.util.Set;

public class Simulation {
    private static final int rc = 1;
    private static final double noise = 5;
    private static final boolean isPeriodic = true;
    private Grid grid;
    private DataManager dm;

    Simulation(){
        this.dm = new DataManager(
                "./TP2/data/input/Static100.txt",
                "./TP2/data/input/Dynamic100.txt");
        this.grid = isPeriodic ?
                new PeriodicGrid(dm.getL(), dm.getParticles(), rc):
                new NonPeriodicGrid(dm.getL(), dm.getParticles(), rc);
    }

    public static void main(String[] args) {
        
    }

    public void findParticleNeighbours(){
        return;
    }

    private double findVa(){
        double VX = 0, VY = 0;
        for(Particle particle: dm.getParticles()){
            VX += particle.getVelocity().getVX();
            VY += particle.getVelocity().getVY();
        };
        return Math.sqrt(Math.pow(VX, 2)+Math.pow(VY, 2));
    }

}
