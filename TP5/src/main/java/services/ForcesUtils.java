package services;

import models.Pair;
import models.Particle;

public class ForcesUtils {
    public double Knormal;
    public double Gravity;
    public double Gamma;
    public double Mu;
    public double Ktan;
    public double dt;

    private static final Pair FloorNormalVersor = new Pair(0.0, -1.0);
    private static final Pair TopNormalVector = new Pair(0.0, 1.0);
    private static final Pair LeftNormalVector = new Pair(-1.0, 0.0);
    private static final Pair RightNormalVector = new Pair(1.0, 0.0);
    public ForcesUtils(double dt, JsonConfigurer config) {
        this.dt = dt;
        this.Knormal = config.getKn();
        this.Gravity = config.getG();
        this.Gamma = config.getGamma();
        this.Mu = config.getMu();
        this.Ktan = 2 * Knormal;
    }
    public double getNormalForce(double superpositionA, double superpositionB) {
        return -Knormal * (superpositionA) - Gamma * (superpositionB); // (N.1)
    }

    public Pair getNormalForce(double superpositionA, Pair versor, double superpositionB) {

        double force = getNormalForce(superpositionA, superpositionB);

        return versor.scale(force);
    }

    //  (T.1)
    public double getTangentialForce(double superpositionA, double relativeTangentialVelocity, double superpositionB) {
        double res1 = - Mu * Math.abs(getNormalForce(superpositionA, superpositionB)) * Math.signum(relativeTangentialVelocity);
        double res2 = -Ktan  * (relativeTangentialVelocity * dt);
        return Math.min(res1, res2);
    }

    public Pair getTangentialForce(double superpositionA, Pair relativeTangentialVelocity, Pair normalVersor, double superpositionB) {

        Pair tan = new Pair(-normalVersor.getY(), normalVersor.getX());

        double force = getTangentialForce(superpositionA, relativeTangentialVelocity.dot(tan), superpositionB);

        return tan.scale(force);
    }

    public Pair getWallForce(double superpositionA, Pair relativeTangentialVelocity, Pair normalVersor, double superpositionB) {

        Pair tan = new Pair(-normalVersor.getY(), normalVersor.getX());

        double forceT = getTangentialForce(superpositionA, relativeTangentialVelocity.dot(tan), superpositionB);
        double forceN = getNormalForce(superpositionA, superpositionB);
        return normalVersor.scale(forceN).sum(tan.scale(forceT));
    }



//    -------------------------------------------


//    public double getNormalForce(double superposition, Particle A, Particle B) {
//        Pair relativeVelocity;
//        if(B == null) {
//            relativeVelocity = A.getVelocity();
//        } else {
//            relativeVelocity = A.getVelocity().subtract(B.getVelocity());
//        }
//
//        return -Knormal * (superposition) - Gamma * (relativeVelocity.getX() + relativeVelocity.getY());
//
//    }
//
//
//    public Pair getNormalForce(double superposition, Pair versor, Particle A, Particle B) {
//        double force = getNormalForce(superposition, A, B);
//
//        return versor.scale(force);
//    }
//
//
//    public double getTangencialForceT3(double superposition, double relativeTangencialVelocity) {
//        return -Ktan * (relativeTangencialVelocity) * dt;
//    }
//
//    public double getTangencialForceT1(double superposition, double relativeTangencialVelocity, Particle A, Particle B) {
////        if(relativeTangencialVelocity == 0){
////            System.out.println("Llegué");
////        }
//        return -Mu * Math.abs(getNormalForce(superposition, A, B)) * Math.signum(relativeTangencialVelocity);
//    }
//
//    public Pair getTangencialForce(double superposition, Pair relativeTangencialVelocity, Pair normalVersor, Particle A, Particle B) {
//        Pair tan = new Pair(-normalVersor.getY(), normalVersor.getX());
//        A.addAcumVel(B, relativeTangencialVelocity.dot(tan));
//        double forceT3 = getTangencialForceT3(superposition, A.getAccumVel(B));
//        // double forceT3 = getTangencialForceT3(superposition, relativeTangencialVelocity.dot(tan));
//        double forceT1 = getTangencialForceT1(superposition, relativeTangencialVelocity.dot(tan), A, B);
//        double force = Math.min(forceT1, forceT3);
//        return tan.scale(force);
//    }
//
//    public Pair getWallForce(double superposition, Pair relativeTangencialVelocity, Pair normalVersor, Particle A, Particle B) {
//        Pair tan = new Pair(-normalVersor.getY(), normalVersor.getX());
//        int index;
//        if(normalVersor.equals(FloorNormalVersor)){
//            index=0;
//        }
//        else if (normalVersor.equals(TopNormalVector)){
//            index = 1;
//        }
//        else if (normalVersor.equals(LeftNormalVector)){
//            index = 2 ;
//        }
//        else {
//            index = 3;
//        }
//        // la septima se viene 7777777777777
//        A.addAcumVelWall(index, relativeTangencialVelocity.dot(tan));
//        double forceT3 = getTangencialForceT3(superposition, A.getAccumVelWall(index));
//        double forceT1 = getTangencialForceT1(superposition, relativeTangencialVelocity.dot(tan), A, B);
//        double forceT = Math.min(forceT1, forceT3);
//        double forceN = getNormalForce(superposition, A, B);
//        return normalVersor.scale(forceN).sum(tan.scale(forceT));
//    }

}
