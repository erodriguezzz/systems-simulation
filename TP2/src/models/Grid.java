package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class Grid {

    private Cell[][] cells;
    private final int M;
    private final double L;
    private final double rc; // This ends up being the cell size

    public Grid(double L, Set<Particle> particles, double rc){
        this.L = L;
        this.rc = rc;
        this.M = getM(L, rc);
        this.cells = new Cell[M][M];
    }

    /**
     * This method will set the initial state of the grid
     * @param particles - Set of particles parsed from input files
     */
    public void addParticles(Set<Particle> particles){
        particles.forEach(particle -> {
            int row = (int) Math.floor((particle.getX()%L)/rc);
            int col = (int) Math.floor((particle.getY()%L)/rc);
            cells[row][col].getParticles().add(particle);
        });
    }

    /**
     * This method will be implemented in the different concrete grid classes. It will efficiently calculate
     * each particles' neighbours using Cell Index Method - CIM.
     */
    public abstract void CIM();

    public void resetParticles(Set<Particle> particles){
        cells = new Cell[getM()][getM()];
        particles.forEach(particle -> {
            int row = (int) Math.floor((particle.getX()%L)/rc);
            int col = (int) Math.floor((particle.getY()%L)/rc);
            cells[row][col].getParticles().add(particle);
        });
    }

    /**
     * This method relocates a particle according to its properties
     * @param p - Particle being relocated
     */
    public abstract void updatePositions(Particle p);

    /**
     * This formula corresponds to puntual particles only
     * @param L - Grid size
     * @param rc - Interaction radius of particles
     * @return M - Amount of columns/rows
     */
    public int getM(double L, double rc){
        return (int) (L/rc);
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }

    public int getM() {
        return M;
    }

    public double getL() {
        return L;
    }

    public double getRc() {
        return rc;
    }

    public double getDistance(Particle p1, Particle p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2)) - p1.getRadius() - p2.getRadius() ;
    }

    @Override
    public String toString() {
        String[][] strGrid = new String[this.M][this.M];

        for (int row = 0; row < this.M; row++) {
            for (int col = 0; col < this.M; col++) {
                Set<Particle> cellValues = cells[row][col].getParticles();
                if (!cellValues.isEmpty()) {
                    strGrid[row][col] = "[" + String.join(", ", cellValues.toString()) + "]";
                } else {
                    strGrid[row][col] = "";
                }
            }
        }

        int[] colWidths = new int[this.M];
        for (int col = 0; col < this.M; col++) {
            for (int row = 0; row < this.M; row++) {
                colWidths[col] = Math.max(colWidths[col], strGrid[row][col].length());
            }
        }

        StringBuilder gridStr = new StringBuilder();
        for (int row = 0; row < this.M; row++) {
            gridStr.append("+").append(String.join("+", generateLine('-', colWidths))).append("+\n");
            gridStr.append("| ").append(String.join(" | ", generatePaddedValues(strGrid[row], colWidths))).append(" |\n");
        }
        gridStr.append("+").append(String.join("+", generateLine('-', colWidths))).append("+");

        return gridStr.toString();
    }

    private List<String> generateLine(char ch, int[] colWidths) {
        List<String> line = new ArrayList<>();
        for (int colWidth : colWidths) {
            line.add(new String(new char[colWidth + 2]).replace('\0', ch));
        }
        return line;
    }

    private List<String> generatePaddedValues(String[] values, int[] colWidths) {
        List<String> paddedValues = new ArrayList<>();
        for (int col = 0; col < colWidths.length; col++) {
            int paddingLength = colWidths[col] - values[col].length();
            String paddedValue = values[col] + new String(new char[paddingLength]).replace('\0', ' ');
            paddedValues.add(paddedValue);
        }
        return paddedValues;
    }
    public abstract void setAdjacentNeighbours();

}


