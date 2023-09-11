package models;

public class Collision implements Comparable<Collision> {
    private final Particle p1;
    private final Particle p2;
    private final double time;
    private final boolean isWallCollision;

    public Collision(Particle p1, Particle p2, double time) {
        this.p1 = p1;
        this.p2 = p2;
        this.time = time;
        this.isWallCollision = false;
    }

    public Collision(Particle p1, double time) {
        this.p1 = p1;
        this.p2 = null;
        this.time = time;
        this.isWallCollision = true;
    }

    public boolean isWallCollision() {
        return isWallCollision;
    }

    public void exec(double M, double L) {
        double x1 = p1.getX();
        double y1 = p1.getY();
        double vx1 = p1.getVx();
        double vy1 = p1.getVy();
        double radius1 = p1.getRadius();
        if (this.isWallCollision) {
            System.out.println("Collision between " + p1.getId() + " and wall");
            System.out.println("x1 = " + p1.getX() + " y1 = " + p1.getY() + " vx1 = " + p1.getVx() + " vy1 = " + p1.getVy() + " radius1 = " + p1.getRadius());
            // Collision with vertical wall
            if (x1 + radius1 >= M+L || x1 - radius1 <= 0 || (x1 + radius1 >= M && ((y1 + radius1 > (L + M)/2 ) || (y1 - radius1 < (L - M)/2) ))) {
                p1.setVelocity(new Velocity(-vx1, vy1)); //TODO: decide whether we should use the Velocity class setters or create a new Velocity object
            }
            // Collision with horizontal wall
            if ( (y1 + radius1 >= M) || y1 - radius1 <= 0 || (x1 + radius1 >= M && (y1 + radius1 >= (L + M)/2 || y1 - radius1 <= (L - M)/2))) {
                p1.setVelocity(new Velocity(vx1, -vy1)); //TODO: decide whether we should use the Velocity class setters or create a new Velocity object
            }
            System.out.println("x1 = " + p1.getX() + " y1 = " + p1.getY() + " vx1 = " + p1.getVx() + " vy1 = " + p1.getVy() + " radius1 = " + p1.getRadius());

        } else {
            // TODO: check copilot implemented algorithm correctly
            if (p2 == null) {
                throw new IllegalArgumentException("p2 cannot be null if isWallCollision is false");
            }
            System.out.println("Collision between " + p1.getId() + " and " + p2.getId());
            
            double x2 = p2.getX();
            double y2 = p2.getY();
            double vx2 = p2.getVx();
            double vy2 = p2.getVy();
            double radius2 = p2.getRadius();
            double dx = x2 - x1;
            double dy = y2 - y1;
            double dvx = vx2 - vx1;
            double dvy = vy2 - vy1;
            double dvdr = dx*dvx + dy*dvy;
            double dist = Math.sqrt(dx*dx + dy*dy);

            double sigma = radius1 + radius2;
            System.out.println("Dist = " + dist + " sigma^2 = " + sigma*sigma);
            System.out.println();
            double J = 2*p1.getMass()*p2.getMass()*dvdr / ((p1.getMass() + p2.getMass())*sigma);
            double Jx = J*dx / dist;
            double Jy = J*dy / dist;
            p1.setVelocity(new Velocity(vx1 + Jx/p1.getMass(), vy1 + Jy/p1.getMass())); //TODO: decide whether we should use the Velocity class setters or create a new Velocity object
            p2.setVelocity(new Velocity(vx2 - Jx/p2.getMass(), vy2 - Jy/p2.getMass())); //TODO: decide whether we should use the Velocity class setters or create a new Velocity object
        }
        System.out.println();

    }

    public Particle getP1() {
        return this.p1;
    }

    public Particle getP2() {
        return this.p2;
    }

    public double getTime() {
        return this.time;
    }

    @Override
    public String toString() {
        return "Collision{" +
                "p1=" + p1 +
                ", p2=" + p2 +
                ", time=" + time +
                '}';
    }

    @Override
    public int compareTo(Collision o) {
        return Double.compare(this.time, o.time);
    }

    // Override equals method so that a collision is equal to another if they're both wall collisions involving the same particle or they're both ParticlCollisions involving the same two particles in any order
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Collision))
            return false;
        Collision collision = (Collision) o;
        if (isWallCollision != collision.isWallCollision)
            return false;
        if (isWallCollision) {
            return (p1.equals(collision.p1));
        } else {
            return (p1.equals(collision.p1) && (p2 != null && p2.equals(collision.p2))) || (p1.equals(collision.p2) && (p2 != null && p2.equals(collision.p1)));
        }
    }
}
