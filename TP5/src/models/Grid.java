package models;

import java.math.BigDecimal;
import java.util.List;

public class Grid {
    private BigDecimal frequency;
    private BigDecimal KN = BigDecimal.valueOf(-250);
    private BigDecimal KT = BigDecimal.valueOf(-500);
    private BigDecimal GAMMA = BigDecimal.valueOf(-2.5);
    private BigDecimal MHU = BigDecimal.valueOf(-0.7);
    private BigDecimal gravity = BigDecimal.valueOf(-9.8);
    private BigDecimal bottom = BigDecimal.valueOf(0), top = BigDecimal.valueOf(70), left = BigDecimal.valueOf(0), right = BigDecimal.valueOf(20);
    
    public void sin(){

    }

    public void updateForces(List<Particle> particles){
        for(Particle p : particles){
            p.setFy(p.getFy().add(gravity));

            for (Particle otherP: particles){
                if(!p.equals(otherP)){ //TODO adapt collides with?
                    BigDecimal distance = p.distance(otherP);
                    BigDecimal superposition = distance.subtract(p.getRadius()).subtract(otherP.getRadius());
                    if(superposition.compareTo(BigDecimal.ZERO) < 0){
                        // System.out.println("Collision!: "+p.getId()+" "+otherP.getId());
                        // System.out.println("Posititions "+p.getX()+" "+p.getY()+" "+otherP.getX()+" "+otherP.getY());
                        BigDecimal Fn = KN.multiply(superposition).add(GAMMA.multiply(p.getVx().subtract(otherP.getVx())));
                        // BigDecimal Ft1 = KT.multiply(superposition).multiply(p.getVy().subtract(otherP.getVy())); //TODO: check if this is correct
                        BigDecimal Ft1 = BigDecimal.valueOf(Double.MAX_VALUE);
                        BigDecimal Ft2 = MHU.multiply(Fn).multiply(BigDecimal.valueOf(p.getVy().subtract(otherP.getVy()).signum()));

                        BigDecimal FtFinal = Ft1.min(Ft2);
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
        BigDecimal superpositionBottom = p.getY().subtract(p.getRadius()).subtract(bottom);
        BigDecimal superpositionTop = top.subtract(p.getY()).subtract(p.getRadius());
        BigDecimal superpositionLeft = p.getX().subtract(p.getRadius()).subtract(left);
        BigDecimal superpositionRight = right.subtract(p.getX()).subtract(p.getRadius());

        if(superpositionBottom.compareTo(BigDecimal.ZERO) < 0){
            BigDecimal Fn = KN.multiply(superpositionBottom).add(GAMMA.multiply(p.getVy()));
            p.addWallForce(Fn, BigDecimal.valueOf(0), BigDecimal.valueOf(1));
        }
        if(superpositionTop.compareTo(BigDecimal.ZERO) < 0){
            BigDecimal Fn = KN.multiply(superpositionTop).add(GAMMA.multiply(p.getVy()));
            p.addWallForce(Fn, BigDecimal.valueOf(0), BigDecimal.valueOf(-1));
        }
        if(superpositionLeft.compareTo(BigDecimal.ZERO) < 0){
            BigDecimal Fn = KN.multiply(superpositionLeft).add(GAMMA.multiply(p.getVx()));
            p.addWallForce(Fn, BigDecimal.valueOf(1), BigDecimal.valueOf(0));
        }
        if(superpositionRight.compareTo(BigDecimal.ZERO) < 0){
            BigDecimal Fn = KN.multiply(superpositionRight).add(GAMMA.multiply(p.getVx()));
            p.addWallForce(Fn, BigDecimal.valueOf(-1), BigDecimal.valueOf(0));
        }
    }
}
