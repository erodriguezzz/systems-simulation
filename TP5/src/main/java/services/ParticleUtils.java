package services;

import models.Particle;
public class ParticleUtils {

    private static double calculateDistance(Particle p1, Particle p2) {
        double x1 = p1.getPosition().getX();
        double y1 = p1.getPosition().getY();
        double x2 = p2.getPosition().getX();
        double y2 = p2.getPosition().getY();

        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public static boolean overlap(Particle p1, Particle p2) {
        if (!p1.equals(p2)) {
            return ParticleUtils.calculateDistance(p1, p2) < p1.getRadius() + p2.getRadius();
        } else return false;
    }
}