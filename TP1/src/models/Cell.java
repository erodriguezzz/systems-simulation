package models;

import java.util.HashSet;
import java.util.Set;

public class Cell {

    private int row;
    private int col;
    private Set<Particle> particles;

    public Cell(int row, int col) {
        this.particles = new HashSet<>();
        this.row = row;
        this.col = col;
    }

    public Set<Particle> getParticles() {
        return particles;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
