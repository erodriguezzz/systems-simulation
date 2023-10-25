package models;

import java.util.List;

public class Grid {
    private double frequency;
    private double KN = -250;
    private double KT = (-500);
    private double GAMMA = (-2.5);
    private double MHU = (-0.7);
    private double gravity = (-9.8);
    private double bottom = (0), top = (70), left = (0), right = (20);
    public void sin(){

    }

    public void updateForces(List<Particle> particles){
        for(Particle p : particles){
            p.setFy(p.getFy() + gravity);

            for (Particle otherP: particles){
                if(!p.equals(otherP)){ //TODO adapt collides with?
                    double distance = p.distance(otherP);
                    double superposition = distance - p.getRadius() - otherP.getRadius();
                    if(superposition < 0){
                        // System.out.println("Collision!: "+p.getId()+" "+otherP.getId());
                        // System.out.println("Posititions "+p.getX()+" "+p.getY()+" "+otherP.getX()+" "+otherP.getY());
                        double Fn = KN * superposition + GAMMA * (p.getVx() - otherP.getVx());
                        // BigDecimal Ft1 = KT.multiply(superposition).multiply(p.getVy().subtract(otherP.getVy())); //TODO: check if this is correct
                        double Ft1 = (Double.MAX_VALUE);
                        double Ft2 = MHU * Fn * (int) Math.signum(p.getVy() - otherP.getVy());

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
}
