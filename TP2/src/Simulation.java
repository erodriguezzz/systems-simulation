import models.*;
import services.DataManager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Simulation {
    private static final int rc = 1;
    private static final double noise = 0.1;
    private static final int totalSeconds = 400;
    private static final double timeStepper = 0.3;
    private static final double tolerance=0.001;
    private double lastVa = 0;
    private int stableCount = 0;
    private final int stableMarker = 20;
    private Grid grid;
    private DataManager dm;
    private Set<Particle> particles;
    private Map<Particle, Set<Particle>> related = new TreeMap<>();

    Simulation(){
        this.dm = new DataManager(
                "./data/input/Static300.txt",
                "./data/input/Dynamic300.txt",
                "./data/output/Dynamic10.dump");
        this.particles = dm.getParticles();
        this.grid = new PeriodicGrid(dm.getL(), dm.getParticles(), rc);
    }

    public static void main(String[] args) {
        Simulation simulation= new Simulation();
        // simulation.visualization();
        double va = simulation.run();
        System.out.println("Va: " + va);
    }

    /**
     * This method will determine the new approximation o theta according to the
     * surrounding particles' angles.
     * @param particle - The particle which theta is being recalculated
     */
    public void determineTheta(Particle particle){
        double dx = Math.cos(particle.getVelocity().getTheta());
        double dy = Math.sin(particle.getVelocity().getTheta());
        for (Particle p: related.get(particle)) {
            dx += Math.cos(p.getVelocity().getTheta());
            dy += Math.sin(p.getVelocity().getTheta());
        }
        dx /= related.get(particle).size() + 1;
        dy /= related.get(particle).size() + 1;
        particle.setTheta(Math.atan2(dy, dx) + (Math.random()-0.5) * noise);
    }

    public void moveParticles(double time){
        particles.forEach(p -> {
            p.updatePosition(time, grid.getL());
            determineTheta(p);
        });
        grid.resetParticles(particles);
    }

    /**
     * This method will simulate the transition of the particles between times
     */
    public void simulate(double time){
        related.clear();
        for (Particle p : particles) {
            related.putIfAbsent(p, new HashSet<>());
        }
        findParticleNeighbours();
        moveParticles(timeStepper);
        dm.writeDynamicFile(particles, "./data/output/Dynamic10.dump", time);
    }

    public double run(){
        double time = 0;
        while(time <= totalSeconds &&  endCondition(findVa()) == false){
            simulate(time);
            time += timeStepper;
        }
        System.out.println("Iterations " + Math.round(time/timeStepper));
        return findVa();
    }

    private boolean endCondition(double newVa){
        if ( Math.abs(newVa - lastVa) < tolerance) {
            stableCount++;

            if (stableCount == stableMarker){
                return true;
            }
        }
        else{
            stableCount = 0;
        }

        // update last value
        lastVa = newVa;
        return false;
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
                        if (!p.equals(q) && grid.getDistance(p, q) < rc){
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
        double VX = 0, VY = 0, v0 = 1;
        boolean flag = true;
        for (Particle p: particles) {
            VX += p.getVelocity().getVX();
            VY += p.getVelocity().getVY();
            if (flag) {
                v0 = p.getVelocity().getVelocity();
                flag = false;
            }
        }
        double avg = Math.sqrt(Math.pow(VX, 2) + Math.pow(VY, 2));
        return avg / (dm.getN() * v0); // This assumes all particles have the same velocity

    }



}
