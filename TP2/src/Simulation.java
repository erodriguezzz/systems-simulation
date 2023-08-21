import models.*;
import services.DataManager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Simulation {
    private static final int rc = 1;
    private static final double noise = 5;
    private static final boolean isPeriodic = true;
    private static final int totalSeconds = 16;
    private Grid grid;
    private DataManager dm;
    private Set<Particle> particles;
    private Map<Particle, Set<Particle>> related = new TreeMap<>();

    Simulation(){
        this.dm = new DataManager(
                "./TP2/data/input/Static100.txt",
                "./TP2/data/input/Dynamic100.txt");
        this.particles = dm.getParticles();
        this.grid = isPeriodic ?
                new PeriodicGrid(dm.getL(), dm.getParticles(), rc):
                new NonPeriodicGrid(dm.getL(), dm.getParticles(), rc);
    }

    public static void main(String[] args) {
        Simulation simulation= new Simulation();
        // simulation.visualization();
    }

    /**
     * This method will determine the new approximation o theta according to the
     * surrounding particles' angles.
     * @param particle - The particle which theta is being recalculated
     */
    public void determineTheta(Particle particle){
        double dx = Math.cos(particle.getVelocity().getTheta());
        double dy = Math.sin(particle.getVelocity().getTheta());
        for (Particle p: related.get(particle)){
            dx += Math.cos(p.getVelocity().getTheta());
            dy += Math.sin(p.getVelocity().getTheta());
        }
        dx /= related.get(particle).size() + 1;
        dy /= related.get(particle).size() + 1;
        particle.setTheta(Math.atan2(dy, dx) + (Math.random()-0.5) * noise);
    }

    public void moveParticles(){
        particles.forEach(p -> {
            grid.updatePositions(p);
            determineTheta(p);
        });
    }

    /**
     * This method will simulate the transition of the particles between times
     */
    public void simulate(){
        related.clear();
        findParticleNeighbours();
        moveParticles();
    }

    public double run(){
        int time = 0;
        while(time <= totalSeconds){
            simulate();

            time++;
        }
        return findVa();
    }

    /**
     * This method will set the corresponding map relating all particles to its
     * neighbours according to the rc chosen. It is important to mention that if
     * particles are in the same cell, it's pointless to check distances comparing
     * agains rc, as L/rc=M
     */
    public void findParticleNeighbours(){
        for (int i = 0; i < grid.getM(); i++) {
            for (int j = 0; j < grid.getM(); j++) {
                for (Particle p: grid.getCells()[i][j].getParticles()){
                    for (Particle q: grid.getCells()[i][j].getParticles()){
                        if (!p.equals(q)){
                            addNeighbourRelation(p, q);
                        }
                    }
                    for (Cell cell: grid.getCells()[i][j].getAdjacentNeighbours()){
                        for (Particle q: cell.getParticles()){
                            if (grid.getDistance(p, q) < rc){
                                addNeighbourRelation(p, q);
                                addNeighbourRelation(q, p);
                            }
                        }
                    }
                }
            }
        }
    }

    private void addNeighbourRelation(Particle p, Particle q){
        related.putIfAbsent(p, new HashSet<>());
        related.get(p).add(q);
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
