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
    private double leftSideI = 0;
    private double rightSideI = 0;
    private double totalI = 0;
    private final double leftPerimeter;
    private final double rightPerimeter;
    private final double totalPerimeter;

    public Domain(double l) {
        if (l > DOMAIN_LENGTH) {
            throw new IllegalArgumentException("L must be smaller than M");
        }
        this.M = DOMAIN_LENGTH;
        this.L = l;
        this.upperCorner = new Particle(0, new Velocity(0,0), M, (L+M)/2, Double.POSITIVE_INFINITY);
        this.lowerCorner = new Particle(0, new Velocity(0,0), M, (M-L)/2, Double.POSITIVE_INFINITY);
        this.leftPerimeter = 3*M + ((M-L)/2);
        this.rightPerimeter = 2*M + L;
        this.totalPerimeter = leftPerimeter + rightPerimeter;
    }

    public void addPressure(double v, CollisionType type) {
        switch (type) {
            case LEFT_HORIZONTAL_WALL:
            case LEFT_WALL:
            case MID_WALL:
                leftSideI += Math.abs(v) / leftPerimeter;
                break;
            case RIGHT_HORIZONTAL_WALL:
            case RIGHT_WALL:
                rightSideI += Math.abs(v) / rightPerimeter;
                break;
            case UPPER_CORNER:
            case LOWER_CORNER:
                break;
            default:
                throw new RuntimeException("Cannot compute pressure for collision of type " + type);
        }
        totalI += v / totalPerimeter;
    }

    public double getLeftSidePressure(double time) {
        return leftSideI / time;
    }

    public double getRightSidePressure(double time) {
        return rightSideI / time;
    }

    public double getTotalPressure(double time) {
        return totalI / time;
    }

    /*
    * This method returns the minimum time it will take for the particle to collide with a wall.
    * It also returns whether the collision is with the upper corner (2), the lower corner (1)
    * or no corner (0) in the isCornerCollision variable.
     */
    public Collision getNextWallCollision(Particle p, double currentTime) {
        double time = Double.POSITIVE_INFINITY;
        double x = p.getX();
        double y = p.getY();
        double vx = p.getVx();
        double vy = p.getVy();
        double radius = p.getRadius();
        CollisionType type = null;

        double timeToUpperCorner = p.timeToCollision(this.upperCorner);
        double timeToLowerCorner = p.timeToCollision(this.lowerCorner);
        // if (p.getId() == 110) {
        //     System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        //     System.out.println("Time to upper corner = " + timeToUpperCorner);
        //     System.out.println("Time to lower corner = " + timeToLowerCorner);
        //     System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        // }
        if (timeToUpperCorner >= 0) {
            time = Math.min(time, timeToUpperCorner);
            if (time == timeToUpperCorner) {
                type = CollisionType.UPPER_CORNER;
            }
        }
        if (timeToLowerCorner >= 0) {
            time = Math.min(time, timeToLowerCorner);
            if (time == timeToLowerCorner) {
                type = CollisionType.LOWER_CORNER;
            }
        }

        if (vx > 0) {
            //Chequeo si voy a chocar con mid o right
            double timeToMidWall = (M - x - radius) / vx;
            double upperMidWallY = y + vy * timeToMidWall; //TODO: check if we should add/substract radius here
            double lowerMidWallY = y + vy * timeToMidWall; //TODO: check if we should add/substract radius here
            if (x < M && (upperMidWallY > (M + L) / 2 || lowerMidWallY < (M - L) / 2)) {
                //Choco con mid
                if (timeToMidWall > 0) {
                    time = Math.min(time, timeToMidWall);
                    if (time == timeToMidWall)
                        type = CollisionType.MID_WALL;
                }
                
            } else {
                //Choco con right
                time = Math.min(time, (2 * M - x - radius) / vx);
                if (time == (2 * M - x - radius) / vx)
                    type = CollisionType.RIGHT_WALL;
            }
        } else if (vx < 0) {
            time = Math.min(time, (radius - x) / vx);
            if (time == (radius - x) / vx)
                type = CollisionType.LEFT_WALL;
        }
        if (vy > 0) {
            double timeToMidUpper = ((M + L) / 2 - y - radius) / vy;
            double midUpperX = x + radius + vx * timeToMidUpper; //TODO: check if we should add/substract radius here
            if (timeToMidUpper < 0 || midUpperX < M) {
                double timeToCeiling = (M - y - radius) / vy;
                //Choco con left horizontal
                time = Math.min (time, timeToCeiling);
                if (time == timeToCeiling)
                    type = CollisionType.LEFT_HORIZONTAL_WALL;
            } else if (timeToMidUpper > 0 && midUpperX > M) {
                //Choco con right horizontal
                time = Math.min(time, timeToMidUpper);
                if (time == timeToMidUpper)
                    type = CollisionType.RIGHT_HORIZONTAL_WALL;
            }
        } else if (vy < 0) {
            double timeToMidLower = ((M - L) / 2 - y + radius) / vy;
            double midLowerX = x + radius + vx * timeToMidLower; //TODO: check if we should add/substract radius here
            if (timeToMidLower < 0 || midLowerX < M) {
                double timeToFloor = (radius - y) / vy;
                //Choco con left horizontal
                time = Math.min(time, timeToFloor);
                if (time == timeToFloor)
                    type = CollisionType.LEFT_HORIZONTAL_WALL;
            } else if (timeToMidLower > 0 && midLowerX > M) {
                /* Choco con right horizontal */
                time = Math.min(time, timeToMidLower);
                if (time == timeToMidLower)
                    type = CollisionType.RIGHT_HORIZONTAL_WALL;
            }
        }
        Particle p2 = null;
        if (type == CollisionType.UPPER_CORNER)
            p2 = this.upperCorner;
        else if (type == CollisionType.LOWER_CORNER)
            p2 = this.lowerCorner;
        return new Collision(p, p2, time + currentTime, type);
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
