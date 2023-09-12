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

    private static final double DOMAIN_LENGTH = 0.09;

    private final Particle upperCorner;
    private final Particle lowerCorner;

    public Domain(double l) {
        if (l > DOMAIN_LENGTH) {
            throw new IllegalArgumentException("L must be smaller than M");
        }
        this.M = DOMAIN_LENGTH;
        this.L = l;
        this.upperCorner = new Particle(0, new Velocity(0,0), M, (L+M)/2, Double.POSITIVE_INFINITY);
        this.lowerCorner = new Particle(0, new Velocity(0,0), M, (M-L)/2, Double.POSITIVE_INFINITY);
    }

    /*
    * This method returns the minimum time it will take for the particle to collide with a wall.
    * It also returns whether the collision is with the upper corner (2), the lower corner (1)
    * or no corner (0) in the isCornerCollision variable.
     */
    public Collision getNextWallCollision(Particle p, double currentTime) {
        double time;
        double x = p.getX();
        double y = p.getY();
        double vx = p.getVx();
        double vy = p.getVy();
        double radius = p.getRadius();
        time = this.upperCorner.timeToCollision(p);
        if (time != -1) {
            return new Collision(p, this.upperCorner, time);
        }
        time = this.lowerCorner.timeToCollision(p);
        if (time != -1) {
            return new Collision(p, this.lowerCorner, time);
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
        return new Collision(p, time + currentTime);
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
