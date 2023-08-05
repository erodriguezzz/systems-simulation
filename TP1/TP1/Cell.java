package TP1;

import java.util.ArrayList;
import java.util.List;

public class Cell {

    private int row;
    private int col;
    private List<Particle> list;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.list = new ArrayList<>();
    }

    public void addParticle(Particle particle) {
        list.add(particle);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public List<Particle> getParticles() {
        return list;
    }

    @Override
    public String toString() {
        return list.toString();
    }
}