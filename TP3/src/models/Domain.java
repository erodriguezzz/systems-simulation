package models;

import sun.java2d.xr.MutableInteger;

/**
 * This class represents the domain of the simulation as specified in the assignment.
 */
public class Domain {
    /*
    This represents the length and height of the square part of the domain (left), as well as the length of the right side of it
     */
    private final double M;
    /*
    This represents the height of the right part of the domain
     */
    private final double L;

    private final Particle upperCorner;
    private final Particle lowerCorner;

    public Domain(double m, double l) {
        if (l > m) {
            throw new IllegalArgumentException("L must be smaller than M");
        }
        this.M = m;
        this.L = l;
        this.upperCorner = new Particle(0, new Velocity(0,0), M, (L+M)/2, Double.POSITIVE_INFINITY);
        this.lowerCorner = new Particle(0, new Velocity(0,0), M, (L-M)/2, Double.POSITIVE_INFINITY);
    }

    /*
    * This method returns the minimum time it will take for the particle to collide with a wall.
    * It also returns whether the collision is with the upper corner (2), the lower corner (1)
    * or no corner (0) in the isCornerCollision variable.
     */
    public double getWallCollisionTime(Particle p, MutableInteger isCornerCollision) {
        double time = -1;
        double x = p.getX();
        double y = p.getY();
        double vx = p.getVx();
        double vy = p.getVy();
        double radius = p.getRadius();
        time = upperCorner.timeToCollision(p);
        if (time != -1) {
            isCornerCollision.setValue(2);
            return time;
        }
        time = lowerCorner.timeToCollision(p);
        if (time != -1) {
            isCornerCollision.setValue(1);
            return time;
        }
        if (vx > 0) {
            double timeToRightWall = (M + L - x - radius) / vx;
            // Check if I'm on the right side of the domain. If not, check if I will collide with the middle wall.
            if (x + radius > M) {
                time = timeToRightWall;
            } else {
                time = (M - x - radius) / vx;
                double middleY = y + vy * time;
                if (middleY + radius < (L + M) / 2 || middleY - radius > (L - M) / 2) {
                    time = timeToRightWall;
                }
            }
        } else if (vx < 0) {
            time = (radius - x) / vx;
        }
        if (vy > 0) {
            if (x < M) {
                time = Math.min(time, (M - y - radius) / vy);
            } else {
                time = Math.min(time, ((M+L)/2 - y - radius) / vy);
            }
        } else if (vy < 0) {
            if (x < M) {
                time = Math.min(time, (radius - y) / vy);
            } else {
                time = Math.min(time, ( (M-L)/2 + radius - y) / vy);
            }
        }
        return time;
    }

    public double getM() {
        return M;
    }

    public double getL() {
        return L;
    }

    public Particle getUpperCorner() {
        return upperCorner;
    }

    public Particle getLowerCorner() {
        return lowerCorner;
    }
}
