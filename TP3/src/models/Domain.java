package models;

// import sun.java2d.xr.MutableInteger;

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
            return new Collision(p, this.upperCorner, time, CollisionType.PARTICLE);
        }
        time = this.lowerCorner.timeToCollision(p);
        if (time != -1) {
            return new Collision(p, this.lowerCorner, time, CollisionType.PARTICLE);
        }
        CollisionType type = null;
        if (vx > 0) {
            double timeToRightWall = (M + L - x - radius) / vx;
            // Check if I'm on the right side of the domain. If not, check if I will collide with the middle wall.
            if (x + radius > M) {
                time = timeToRightWall;
                type = CollisionType.RIGHT_WALL;
            } else {
                time = (M - x - radius) / vx;
                double middleY = y + vy * time;
                type = CollisionType.MID_WALL;
                if (middleY + radius < (L + M) / 2 || middleY - radius > (M - L) / 2) {
                    time = timeToRightWall;
                    type = CollisionType.RIGHT_WALL;
                }
            }
        } else if (vx < 0) {
            time = (radius - x) / vx;
            type = CollisionType.LEFT_WALL;
        }
        if (vy > 0) {
            double timeToMidUpper = ((M + L) / 2 - y - radius) / vy;
            double midUpperX = x + vx * timeToMidUpper;
            double timeToCeiling = (M - y - radius) / vy;
            if (y > (M+L)/ 2 || midUpperX + radius < M) {
                    time = Math.min(time, timeToCeiling);
                    if (time == timeToCeiling) {
                        type = CollisionType.LEFT_UPPER_WALL;
                    }
            } else {
                    time = Math.min(time, timeToMidUpper);
                    if (time == timeToMidUpper) {
                        type = CollisionType.RIGHT_UPPER_WALL;
                    }
            }
        } else if (vy < 0) {
            double timeToMidLower = ((M - L) / 2 - y + radius) / vy;
            double midLowerX = x + vx * timeToMidLower;
            double timeToFloor = (radius - y) / vy;
            if (y < (M-L)/2 || midLowerX + radius < M) {
                time = Math.min(time, timeToFloor);
                if (time == timeToFloor) {
                    type = CollisionType.LEFT_LOWER_WALL;
                }
            } else {
                time = Math.min(time, timeToMidLower);
                if (time == timeToMidLower) {
                    type = CollisionType.RIGHT_LOWER_WALL;
                }
            }
        }
        return new Collision(p, time + currentTime, type);
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
