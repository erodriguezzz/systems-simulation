package services;

import models.Pair;
import models.Particle;

public class ForcesUtils {
//    TODO: Parametros en m
    public static final double K_NORMAL = 250;
//    public static final double K_NORMAL = 25000;
    public static final double GRAVITY = -4.80;
    public static final double GAMMA = 2.5;
    public static final double MU =  0.5;
    public static final double K_TAN = 2 * K_NORMAL;
    public static final double dt = 10E-4;



    private static final Pair FloorNormalVersor = new Pair(0.0, -1.0);
    private static final Pair TopNormalVector = new Pair(0.0, 1.0);
    private static final Pair LeftNormalVector = new Pair(-1.0, 0.0);
    private static final Pair RightNormalVector = new Pair(1.0, 0.0);
//     public static final double K_NORMAL = 2000;
// //    public static final double K_NORMAL = 25000;
//     public static final double GRAVITY = -5.80;
//     public static final double GAMMA = 2.5;
//     public static final double MU = 0.3;
//     public static final double K_TAN = 2 * K_NORMAL;

//    TODO: Parametros en cm
//    public static final double K_NORMAL = 250;
//    public static final double GRAVITY = -5.0;
//    public static final double GAMMA = 2.5;
//    public static final double MU = 0.7;
//    public static final double K_TAN = 2 * K_NORMAL;

    public ForcesUtils() {

    }

    public static double getNormalForce(double superposition, Particle A, Particle B) {
        Pair relativeVelocity;
        if(B == null) {
            relativeVelocity = A.getVelocity();
        } else {
            relativeVelocity = A.getVelocity().subtract(B.getVelocity());
        }

        return -K_NORMAL * (superposition) - GAMMA * (relativeVelocity.getX() + relativeVelocity.getY());

    }


    public static Pair getNormalForce(double superposition, Pair versor, Particle A, Particle B) {
        double force = getNormalForce(superposition, A, B);

        return versor.scale(force);
    }


    public static double getTangencialForceT3(double superposition, double relativeTangencialVelocity) {
        return -K_TAN * (relativeTangencialVelocity) * dt;
    }

    public static double getTangencialForceT1(double superposition, double relativeTangencialVelocity, Particle A, Particle B) {
        return -MU * Math.abs(getNormalForce(superposition, A, B)) * Math.signum(relativeTangencialVelocity);
    }

    public static Pair getTangencialForce(double superposition, Pair relativeTangencialVelocity, Pair normalVersor, Particle A, Particle B) {
        Pair tan = new Pair(-normalVersor.getY(), normalVersor.getX());
        A.addAcumVel(B, relativeTangencialVelocity.dot(tan));
        double forceT3 = getTangencialForceT3(superposition, A.getAccumVel(B));
        // double forceT3 = getTangencialForceT3(superposition, relativeTangencialVelocity.dot(tan));
        double forceT1 = getTangencialForceT1(superposition, relativeTangencialVelocity.dot(tan), A, B);
        double force = Math.min(forceT1, forceT3);
        return tan.scale(force);
    }

    public static Pair getWallForce(double superposition, Pair relativeTangencialVelocity, Pair normalVersor, Particle A, Particle B) {
        Pair tan = new Pair(-normalVersor.getY(), normalVersor.getX());
        int index;
        if(normalVersor.equals(FloorNormalVersor)){
            index=0;
        }
        else if (normalVersor.equals(TopNormalVector)){
            index = 1;
        }
        else if (normalVersor.equals(LeftNormalVector)){
            index = 2 ;
        }
        else {
            index = 3;
        }
        // la septima se viene 7777777777777
        A.addAcumVelWall(index, relativeTangencialVelocity.dot(tan));
        double forceT3 = getTangencialForceT3(superposition, A.getAccumVelWall(index));
        double forceT1 = getTangencialForceT1(superposition, relativeTangencialVelocity.dot(tan), A, B);
        double forceT = Math.min(forceT1, forceT3);
        double forceN = getNormalForce(superposition, A, B);
        return normalVersor.scale(forceN).sum(tan.scale(forceT));
    }

}
