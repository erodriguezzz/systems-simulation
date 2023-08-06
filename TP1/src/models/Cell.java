package models;

import java.util.HashSet;
import java.util.Set;

public class Cell {

    private int row;
    private int col;

    // TODO: podría ser útil tener una referencia a las celdas adjacentes para visitar los vecinos
    private Set<Particle> particles;

    public Cell(int x, int y) {
        this.particles = new HashSet<>();
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
