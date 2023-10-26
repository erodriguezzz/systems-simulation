package services;

import models.Pair;

public class ForcesUtils {

    public static final double K_NORMAL = 250;
    public static final double GAMMA = 2.5;
    public static final double GRAVITY = -0.05;

    public static final double K_TAN = 2 * K_NORMAL;

    public static double getNormalForce(double superposition, double relativeNormalVelocity) {
        return -K_NORMAL * (superposition) - GAMMA * (relativeNormalVelocity);
    }


    public static Pair getNormalForce(double superposition, double relativeNormalVelocity, Pair versor) {

        double force = getNormalForce(superposition, relativeNormalVelocity);

        return versor.scale(force);
    }

    public static double getTangencialForce(double superposition, double relativeTangencialVelocity) {
        return -K_TAN * (superposition) * (relativeTangencialVelocity);
    }

    public static Pair getTangencialForce(double superposition, Pair relativeTangencialVelocity, Pair normalVersor) {

        Pair tan = new Pair(-normalVersor.getY(), normalVersor.getX());

        double force = getTangencialForce(superposition, relativeTangencialVelocity.dot(tan));

        return tan.scale(force);
    }

    public static Pair getWallForce(double superposition, Pair relativeTangencialVelocity, Pair normalVersor) {

        Pair tan = new Pair(-normalVersor.getY(), normalVersor.getX());

        double forceT = getTangencialForce(superposition, relativeTangencialVelocity.dot(tan));
        double forceN = getNormalForce(superposition, relativeTangencialVelocity.getX() + relativeTangencialVelocity.getY());
        return normalVersor.scale(forceN).sum(tan.scale(forceT));
    }

}
