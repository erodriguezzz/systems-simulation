package models;

import java.util.List;

public class Grid {
    private double frequency;
    private final static double KN = 0.25;
    private final static double KT = (0.5);
    private final static double GAMMA = (2.5);
    private final static double MHU = (0.7);
    private final static double gravity = (-9.8);
    private final static double bottom = (0), top = (77), left = (0), right = (20);
    private Cell[][] cells;
    private List<Particle> particles;

    // Grid size constants
    private static final double W = 0.2;
    private static final double L = 0.77;
    private static final int rows = 33;
    private static final int cols = 8;
    private static final double cell_size_y = L / rows;
    private static final double cell_size_x = W / cols;

    public void sin(){

    }

    //TODO: pending CIM implementation
    public Grid(List<Particle> particles) {
        this.cells = new Cell[rows][cols];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                cells[i][j] = new Cell();
            }
        }
        this.particles = particles;
    }

    public void updateForces(List<Particle> particles){
        for(Particle p : particles){
            p.setFy(p.getFy() + gravity);

            for (Particle otherP: particles){
                if(!p.equals(otherP)){ //TODO adapt collides with?
                    double distance = p.distance(otherP);
                    // TODO: why distance is in the superposition calculus?
                    double superposition = distance - p.getRadius() - otherP.getRadius();
                    if(superposition < 0){
                        // System.out.println("Collision!: "+p.getId()+" "+otherP.getId());
                        // System.out.println("Posititions "+p.getX()+" "+p.getY()+" "+otherP.getX()+" "+otherP.getY());
                        double Fn = - KN * superposition -  GAMMA * (p.getVx() - otherP.getVx());
                        // BigDecimal Ft1 = KT.multiply(superposition).multiply(p.getVy().subtract(otherP.getVy())); //TODO: check if this is correct
                        double Ft1 = - KT * superposition * (p.getVy() - otherP.getVy()); // TODO: should this be superposition?
                        double Ft2 = - MHU * Math.abs(Fn) * (int) Math.signum(p.getVy() - otherP.getVy());

                        double FtFinal = Math.min(Ft1, Ft2);
                        p.addForces(Fn, FtFinal, otherP);


                        // BigDecimal force = BigDecimal.valueOf(2500).multiply(superposition).multiply(BigDecimal.valueOf(-1));
                        // BigDecimal forceX = force.multiply(p.getX().subtract(otherP.getX())).divide(distance);
                        // BigDecimal forceY = force.multiply(p.getY().subtract(otherP.getY())).divide(distance);
                        // p.setFx(p.getFx().add(forceX));
                        // p.setFy(p.getFy().add(forceY));
                    }   
                }
            }
            borderForces(p);
        }
    }

    public void borderForces(Particle p){
        double superpositionBottom = p.getY() - p.getRadius() - bottom;
        double superpositionTop = top - p.getY() - p.getRadius();
        double superpositionLeft = p.getX() - p.getRadius() - left;
        double superpositionRight = right - p.getX() - p.getRadius();

        if(superpositionBottom < 0){
            double Fn = KN * superpositionBottom + GAMMA * p.getVy();
            p.addWallForce(Fn, 0, 1);
        }
        if(superpositionTop < 0){
            double Fn = KN * superpositionTop + GAMMA * p.getVy();
            p.addWallForce(Fn, 0, -1);
        }
        if(superpositionLeft < 0){
            double Fn = KN * superpositionLeft + GAMMA * p.getVx();
            p.addWallForce(Fn, 1, 0);
        }
        if(superpositionRight < 0){
            double Fn = KN * superpositionRight * GAMMA * p.getVx();
            p.addWallForce(Fn, -1, 0);
        }
    }

    public List<Particle> getParticles() {
        return particles;
    }
}
