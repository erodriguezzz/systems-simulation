package models;

import models.Particle;
import models.Color;
import services.ForcesUtils;
import services.JsonConfigurer;
import services.ParticleUtils;

import java.util.ArrayList;
import java.util.List;

import static services.ForcesUtils.*;

public class Grid {
    private static final double ZERO = 0.0;
    private static final double FREE_SPACE = 7;
    private static final double GENERATION_LIMIT = 40;
    private double DIM_Y;
    private static final int cols = 8;
    private static final int rowsInside = 30;
    private static final int rowsTotal = 33;
    private final double CELL_DIMENSION_Y;
    private final double CELL_DIMENSION_X;
    // private final Limit topRightLimit;
    // private final Limit bottomLeftLimit;
    private final double topRightLimitInitialY = 77; // TODO
    private final double bottomLeftLimitInitialY = 7;
    private final double leftLimitHole;
    private final double rightLimitHole ;
    private double bottom = 7, top = 77, left = 0, right = 20;
    private double movement;

    private final Cell[][] cells;
    private final JsonConfigurer config;

    public Grid(double holeSize, JsonConfigurer config){
        this.config = config;
        this.DIM_Y = config.getM() + FREE_SPACE; // se tiene en cuenta el espacio fuera de la "caja"
        this.CELL_DIMENSION_X = config.getL() / (double) cols;
        this.CELL_DIMENSION_Y = DIM_Y / (double) rowsTotal;
        cells = new Cell[rowsTotal][cols];
        for (int row = 0; row < rowsTotal; row++) {
            for (int col = 0; col < cols; col++) {
                cells[row][col] = new Cell();
            }
        }
        leftLimitHole = config.getL() / 2 - holeSize / 2;
        rightLimitHole = config.getL() / 2 + holeSize / 2;
        // this.bottomLeftLimit = bottomLeftLimit;
        // this.topRightLimit = topRightLimit;
        // this.bottomLeftLimitInitialY = bottomLeftLimit.getY();
        // this.topRightLimitInitialY = topRightLimit.getY();
        // leftLimitHole = topRightLimit.getPosition().getX() / 2 - holeSize / 2;
        // rightLimitHole = topRightLimit.getPosition().getX() / 2 + holeSize / 2;
    }

    public void shake(double t, double w) {
        movement = config.getA() * Math.sin(w * t);
        bottom = (bottomLeftLimitInitialY + movement);
    }

    public void add(Particle particle) {
        Cell cell = getCell(particle.getPosition().getX(), particle.getPosition().getY());
        if (cell != null) {
            cell.add(particle);
        } else
            throw new IllegalStateException("Cell does not exists");
    }

    public void addAll(List<Particle> particles) {
        particles.forEach(this::add);
    }

    private static final Pair FloorNormalVersor = new Pair(ZERO, -1.0);
    private static final Pair TopNormalVector = new Pair(ZERO, 1.0);
    private static final Pair LeftNormalVector = new Pair(-1.0, ZERO);
    private static final Pair RightNormalVector = new Pair(1.0, ZERO);

    private boolean outsideHole(Particle particle) {
        return particle.getPosition().getX() < leftLimitHole || particle.getPosition().getX() > rightLimitHole;
    }

    public void updateForces() {
        for (int row = 0; row < rowsTotal; row++) {
            for (int col = 0; col < cols; col++) {
                List<Particle> neighbours = getNeighbours(row, col);
                List<Particle> current = cells[row][col].getParticles();
                final int  newRow = row, newCol = col;

                current.forEach(
                        p -> {
                            // System.out.println("ROW: " + newRow + " COL: " + newCol + " PARTICLE: " + p.getId() + " POSITION: " + p.getPosition() + " VELOCITY: " + p.getVelocity() + " ACCELERATION: " + p.getAcceleration());
                            // Add gravity
                            p.addToForce(ZERO, p.getMass() * ForcesUtils.GRAVITY);

                            current.forEach(n -> {
                                double diff = p.getPosition().module(n.getPosition());
                                double sumRad = p.getRadius() + n.getRadius();

                                if (diff < sumRad && !n.equals(p)) {

                                    Pair normalVersor = n.getPosition().subtract(p.getPosition()).scale(1.0 / diff);
                                    p.addToForce(getNormalForce(sumRad - diff, normalVersor, p, n)); //p.getVelocity().module(n.getVelocity())

                                    Pair relativeVelocity = p.getVelocity().subtract(n.getVelocity());
                                    p.addToForce(getTangencialForce(sumRad - diff, relativeVelocity, normalVersor, p, n));
                                }
                                else {
                                    n.resetAcumVel(p);
                                }
                            });

                            // Add particle forces
                            neighbours.forEach(
                                    n -> {

                                        double diff = p.getPosition().module(n.getPosition());
                                        double superposition = p.getRadius() + n.getRadius() - diff;

                                        if (superposition > ZERO && !n.equals(p)) {

                                            Pair normalVersor = n.getPosition().subtract(p.getPosition())
                                                    .scale(1.0 / diff);

                                            Pair normalForce = getNormalForce(superposition, normalVersor, p, n);

                                            p.addToForce(normalForce);
                                            n.addToForce(normalForce.scale(-1.0));

                                            Pair relativeVelocity = p.getVelocity().subtract(n.getVelocity());
                                            Pair tangencialForce = getTangencialForce(superposition, relativeVelocity,
                                                    normalVersor, p, n);

                                            p.addToForce(tangencialForce);
                                            n.addToForce(tangencialForce.scale(-1.0));
                                        }
                                        else {
                                            p.resetAcumVel(n);
                                        }
                                    });
                        });

                if (row <= (rowsTotal - rowsInside)) {// pared inferior con el agujero
                    updateForceFloor(current);
                }

                if (row == rowsTotal - 1)
                    updateForceTop(current);

                if (col == 0)
                    updateForceLeftWall(current);

                if (col == cols - 1)
                    updateForceRightWall(current);

            }
        }
    }

    private void updateForceFloor(List<Particle> particles) {
        particles.forEach(p -> {
            if (outsideHole(p) && !p.isGone())
                floorForce(p);
        });
    }

    private void floorForce(Particle particle) {
    //    System.out.println("Particle " + particle.getId() + " position: " + particle.getPosition() + " velocity: " + particle.getVelocity() + " acceleration: " + particle.getAcceleration());
        double superposition = particle.getRadius() - (particle.getPosition().getY() - bottom);
        if (superposition > ZERO)
            particle.addToForce(
                    getWallForce(superposition,  particle.getVelocity(), FloorNormalVersor, particle, null));
        else {
            particle.resetAcummWall(0);
        }
    }

    private void updateForceTop(List<Particle> particles) {
        particles.forEach(this::topForce);
    }

    private void topForce(Particle p) {
        double superposition = p.getRadius() - (top - p.getPosition().getY());
        if (superposition > ZERO)
            p.addToForce(
                    getWallForce(superposition, p.getVelocity(), TopNormalVector, p, null));
        else {
            p.resetAcummWall(1);
        }
    }

    private void updateForceLeftWall(List<Particle> particles) {
        particles.forEach(this::leftForce);
    }

    private void leftForce(Particle p) {
        double superposition = p.getRadius() - (p.getPosition().getX() - left);
        if (superposition > ZERO)
            p.addToForce(
                    getWallForce(superposition, p.getVelocity(), LeftNormalVector, p, null));
        else {
            p.resetAcummWall(2);
        }
    }

    private void updateForceRightWall(List<Particle> particles) {
        particles.forEach(this::rightForce);
    }

    private void rightForce(Particle p) {
        double superposition = p.getRadius() - (right - p.getPosition().getX());
        if (superposition > ZERO)
            p.addToForce(
                    getWallForce(superposition, p.getVelocity(), RightNormalVector, p ,null));
        else{
            p.resetAcummWall(3);
        }
    }

    private List<Particle> getNeighbours(int row, int col) {
        List<Particle> particles = new ArrayList<>();

        if (row < rowsTotal - 1)
            particles.addAll(cells[row + 1][col].getParticles());

        if (row < rowsTotal - 1 && col < cols - 1)
            particles.addAll(cells[row + 1][col + 1].getParticles());

        if (col < cols - 1)
            particles.addAll(cells[row][col + 1].getParticles());

        if (row > 0 && col < cols - 1)
            particles.addAll(cells[row - 1][col + 1].getParticles());

        return particles;
    }

    private List<Particle> getAllNeighbours(int row, int col) {
        List<Particle> particles = new ArrayList<>();

        int[][] diff = {
                { 0, 0 }, { 0, 1 }, { 0, -1 }, { 1, 0 }, { 1, 1 }, { 1, -1 }, { -1, 0 }, { -1, 1 }, { -1, -1 }
        };

        for (int[] a : diff) {
            try {
                particles.addAll(
                        cells[row + a[0]][col + a[1]].getParticles());
            } catch (IndexOutOfBoundsException ignored) {
            }
        }

        return particles;
    }

    public int update() {
        int goneParticles = 0;
        for (int i = 0; i < rowsTotal; i++) {
            for (int j = 0; j < cols; j++) {

                for (int k = 0; k < cells[i][j].getParticles().size(); k++) {
                    if (!updateParticleCell(cells[i][j].getParticles().get(k), i, j))
                        goneParticles += 1;

                }

            }

        }
        return goneParticles;
    }

    private Cell getCell(double x, double y) {
        if (x >= config.getL() || x < 0 || y < 0 || y >= DIM_Y)
            throw new IllegalStateException();
        int row = getIndexY(y);
        int col = getIndexX(x);
        return cells[row][col];
    }

    private int getIndexX(double value) {
        return (int) (value / CELL_DIMENSION_X);
    }

    private int getIndexY(double value) {
        return (int) (value / CELL_DIMENSION_Y);
    }

    private boolean moveFromCell(Particle particle, int row, int col, int newRow, int newCol) {
        try {
            if (newRow < 0) {
                particle.reInject();
                cells[row][col].remove(particle);

                boolean overlap;
                int c, r;
                do {
                    overlap = false;
                    // set random x between 0 and 20, considering p radius
                    particle.getPosition()
                            .setX(particle.getRadius() + Math.random() * (config.getL() - 2.0 * particle.getRadius()));
                    particle.getPosition().setY(GENERATION_LIMIT + FREE_SPACE + Math.random() * ((config.getM() - GENERATION_LIMIT) - particle.getRadius()));
                    c = getIndexX(particle.getPosition().getX());
                    r = getIndexY(particle.getPosition().getY());

                    for (Particle existingParticle : getAllNeighbours(r, c)) {
                        if (ParticleUtils.overlap(particle, existingParticle))
                            overlap = true;
                    }
                } while (overlap);

                cells[r][c].add(particle);
                particle.setGone(false);

                return false;
            } else {
                cells[newRow][newCol].add(particle);
                cells[row][col].remove(particle);
                // if (particle.isGone() && particle.getColor().equals(Color.RED))
                //     particle.setColor(Color.BLACK);
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
    }

    private boolean updateParticleCell(Particle particle, int row, int col) {

        Pair inferiorLimit = new Pair(((double) col) * CELL_DIMENSION_X, ((double) row) * CELL_DIMENSION_Y + movement);
        Pair superiorLimit = new Pair(((double) (col + 1)) * CELL_DIMENSION_X,
                ((double) (row + 1)) * CELL_DIMENSION_Y + movement);

        if (!particle.isGone() && !outsideHole(particle) && particle.getPosition().getY() < bottom)
            particle.setGone(true);

        Pair inferiorDiff = particle.getPosition().subtract(inferiorLimit);
        Pair superiorDiff = particle.getPosition().subtract(superiorLimit);

        return moveFromCell(particle, row, col,
                inferiorDiff.getY() < 0 ? row - 1 : superiorDiff.getY() >= 0 ? row + 1 : row,
                inferiorDiff.getX() < 0 ? col - 1 : superiorDiff.getX() >= 0 ? col + 1 : col);

    }
}
