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
                leftSideI += v / leftPerimeter;
                break;
            case RIGHT_HORIZONTAL_WALL:
            case RIGHT_WALL:
                rightSideI += v / rightPerimeter;
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
        double time;
        double x = p.getX();
        double y = p.getY();
        double vx = p.getVx();
        double vy = p.getVy();
        double radius = p.getRadius();
        time = this.upperCorner.timeToCollision(p);
        /* //TODO: particles are leaving the grid through the corner. Handle times.
        if (time >= 0) {
            return new Collision(p, time, CollisionType.UPPER_CORNER);
        }
        time = this.lowerCorner.timeToCollision(p);
        if (time >= 0) {
            return new Collision(p, time, CollisionType.LOWER_CORNER);
        }
         */
        CollisionType type = null;
        /*
        if (vx > 0) {
            double timeToRightWall = (2 * M  - x - radius) / vx;
            // Check if I'm on the right side of the domain. If not, check if I will collide with the middle wall.
            if (x + radius > M) {
                time = timeToRightWall;
                type = CollisionType.RIGHT_WALL;
            } else {
                time = (M - x - radius) / vx;
                double middleY = y + vy * time;
                type = CollisionType.MID_WALL;
                if (middleY + radius < (L + M) / 2 && middleY - radius > (M - L) / 2) {
                    time = timeToRightWall;
                    type = CollisionType.RIGHT_WALL;
                }
            }
        } else if (vx < 0) {
            time = (radius - x) / vx;
            System.out.println("Time to left wall = " + time);
            type = CollisionType.LEFT_WALL;
        }
        if (vy > 0) {
            double timeToMidUpper = ((M + L) / 2 - y - radius) / vy;
            double midUpperX = x + vx * timeToMidUpper;
            double timeToCeiling = (M - y - radius) / vy;
            if (y > (M+L)/ 2 || midUpperX + radius < M) {
                if (time > 0 && timeToCeiling > 0)
                    time = Math.min(time, timeToCeiling);
                if (time == timeToCeiling) {
                    type = CollisionType.LEFT_HORIZONTAL_WALL;
                    System.out.println("LEFT HORIZONTAL WALL");
                }
                System.out.println("RIGHT HORIZONTAL WALL 1");
            } else {
                    System.out.println("Time: "+ time + " Time to midupper: " + timeToMidUpper);
                    if (time > 0 && timeToMidUpper > 0)
                        time = Math.min(time, timeToMidUpper);
                    if (time == timeToMidUpper) {
                        type = CollisionType.RIGHT_HORIZONTAL_WALL;
                        System.out.println("RIGHT HORIZONTAL WALL 2");
                    }
            }
            System.out.println("Time after vy>0 = " + time);
        } else if (vy < 0) {
            double timeToMidLower = ((M - L) / 2 - y + radius) / vy;
            double midLowerX = x + vx * timeToMidLower;
            double timeToFloor = (radius - y) / vy;
            if (y < (M-L)/2 || midLowerX + radius < M) {
                if (time > 0 && timeToFloor > 0)
                    time = Math.min(time, timeToFloor);
                if (time == timeToFloor) {
                    type = CollisionType.LEFT_HORIZONTAL_WALL;
                }
            } else {
                if (time >0 && timeToMidLower > 0)
                    time = Math.min(time, timeToMidLower);
                if (time == timeToMidLower) {
                    type = CollisionType.RIGHT_HORIZONTAL_WALL;
                }
            }
            System.out.println("Time after vy<0 = " + time);
        }
        return new Collision(p, time + currentTime, type);

         */

        if (vx > 0) {
            //Chequeo si voy a chocar con mid o right
            double timeToMidWall = (M - x - radius) / vx;
            double upperMidWallY = y + radius + vy * timeToMidWall;
            double lowerMidWallY = y - radius + vy * timeToMidWall;
            if (x < M && (upperMidWallY > (M + L) / 2 || lowerMidWallY < (M - L) / 2)) {
                //Choco con mid
                time = timeToMidWall;
                type = CollisionType.MID_WALL;
            } else {
                //Choco con right
                time = (2 * M - x - radius) / vx;
                type = CollisionType.RIGHT_WALL;
            }
        } else if (vx < 0) {
            time = (radius - x) / vx;
            type = CollisionType.LEFT_WALL;
        }
        if (vy > 0) {
            double timeToMidUpper = ((M + L) / 2 - y - radius) / vy;
            double midUpperX = x + vx * timeToMidUpper;
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
            double midLowerX = x + radius + vx * timeToMidLower;
            if (timeToMidLower < 0 || midLowerX < M) {
                double timeToFloor = (radius - y) / vy;
                if (p.getId() == 6){
                    System.out.println("##############################################");
                    System.out.println("Time = " + time + " Time to floor = " + timeToFloor);
                    System.out.println("##############################################");
                }
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
