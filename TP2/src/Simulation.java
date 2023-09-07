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
    

    Simulation(int N, int L, double noise, int v){
        String[] outputs = {
                    "./data/output/dynamic/Dynamic_N_" + N + "_L_" + L + "_noise_" + noise +"_v" +v+".dump",
                    "./data/output/VaN_" + N + "_L_" + L + "_noise_" + noise +"_v" +v+".txt",
                    "./data/output/VaN_" + N + "_L_" + L + "_noise_" + noise +"_v" +v+".txt",
                    "./data/output/VaN_" + N + "_L_" + L + "_noise_" + noise +"_v" +v+".txt",
};
        this.dm = new DataManager(
                "./data/input/Static_N_" + N + "_L_" + L + "_v_1.txt",
                "./data/input/Dynamic_N_" + N + "_L_" + L + "_v_1.txt",
                outputs);
        this.particles = dm.getParticles();
        this.grid = new PeriodicGrid(dm.getL(), dm.getParticles(), rc);
    }

    public static void main(String[] args) {
        int[] muchosN = {300, 588, 2352};
        int[] muchosL = {5, 7, 14};
        for(int iteration = 0; iteration < muchosN.length; iteration++){
            for (int v= 0; v < 5; v++) {
            int[] nValues= new int[30];
            for(int i=0; i<30; i++){
                nValues[i]=muchosN[iteration];
            }

            int[] LValues= new int[30];
            for(int i=0; i<30; i++){
                LValues[i]=muchosL[iteration];
            }
            
            double[] noiseValues= new double[30];
            for(int i=0; i<30; i++){
                noiseValues[i]=i*0.2;
            }

            for(int i = 0; i < nValues.length; i++){
                Simulation simulation= new Simulation(nValues[i], LValues[i], noiseValues[i], v);
                // simulation.visualization();
                double va = simulation.run(nValues[i], LValues[i], noiseValues[i], v);
                System.out.println("Va_n," + nValues[i] + ":" + va);
                
            }
        }
        }
        
    }

    /**
     * This method will determine the new approximation o theta according to the
     * surrounding particles' angles.
     * @param particle - The particle which theta is being recalculated
     */
    public void determineTheta(Particle particle, double noise){
        double dx = Math.cos(particle.getVelocity().getTheta());
        double dy = Math.sin(particle.getVelocity().getTheta());
        double senSum = dy + related.get(particle).stream().mapToDouble(p -> Math.sin(p.getVelocity().getTheta())).sum();
        double cosSum = dx + related.get(particle).stream().mapToDouble(p -> Math.cos(p.getVelocity().getTheta())).sum();

        // for (Particle p: related.get(particle)) {
        //     dx += Math.cos(p.getVelocity().getTheta());
        //     dy += Math.sin(p.getVelocity().getTheta());
        // }
        dx /= (related.get(particle).size() + 1);
        dy /= (related.get(particle).size() + 1);
        particle.setTheta(Math.atan2(senSum, cosSum) + Math.random() * noise - (noise/2));
    }

    public void moveParticles(double time, double noise){
        particles.forEach(p -> {
            p.updatePosition(time, grid.getL());
            determineTheta(p, noise);
        });
        grid.resetParticles(particles);
    }

    /**
     * This method will simulate the transition of the particles between times
     */
    public void simulate(double time, int N, int L, double noise){
        related.clear();
        for (Particle p : particles) {
            related.putIfAbsent(p, new HashSet<>());
        }
        findParticleNeighbours();
        moveParticles(timeStepper, noise);
        dm.writeDynamicFile(particles, "./data/output/dynamic/Dynamic_N_" + N + "_L_" + L + "_noise_" + noise +"_v1.dump", time);
    }

    public double run(int N, int L, double noise, int v){
        double time = 0;
        while(time <= totalSeconds ){
            // &&  endCondition(findVa()) == false
            simulate(time, N, L, noise);
            time += timeStepper;
            // writeVa(time, findVa(), N, L, noise, v);
        }
        System.out.println("Iterations " + Math.round(time/timeStepper));
        double va = findVa();
        writeVa(N, L, noise, v, va);
        return va;
    }

    private void writeVa(double time, double va, int N, int L, double noise, int v){
        dm.writeVa(time, va, N, L, noise, v);
    }

    private void writeVa(int N, int L, double noise, int v, double va){
        dm.writeVa(N, L, noise, v, va);
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
        double sumVx = particles.stream().mapToDouble(Particle::getVx).sum();
        double sumVy = particles.stream().mapToDouble(Particle::getVy).sum();
        double moduleV = Math.sqrt(Math.pow(sumVx,2) + Math.pow(sumVy,2));

        return moduleV/(dm.getN()*0.05);
        // double VX = 0, VY = 0, v0 = 0.3;
        // boolean flag = true;
        // for (Particle p: particles) {
        //     VX += p.getVelocity().getVX();
        //     VY += p.getVelocity().getVY();
        //     // if (flag) {
        //     //     v0 = p.getVelocity().getVelocity();
        //     //     flag = false;
        //     // }
        // }
        // double avg = Math.sqrt(Math.pow(VX, 2) + Math.pow(VY, 2));
        // return avg / (dm.getN() * v0); // This assumes all particles have the same velocity

    }



}
