package TP1;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.floor;

public class Grid {

    private int rows;
    private int columns;
    private Cell[][] grid;

    public Grid(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.grid = new Cell[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                grid[row][col] = new Cell(row, col);
            }
        }
    }

    public Cell getCell(int row, int column) {
        return grid[row][column];
    }

    public int getNumberOfRows() {
        return rows;
    }

    public int getNumberOfColumns() {
        return columns;
    }

    public void addToCell(int row, int column, Particle particle) {
        grid[row][column].addParticle(particle);
    }

    public List<Particle> getParticles(int x, int y) {
        return new ArrayList<>(grid[x][y].getParticles());
    }

    @Override
    public String toString() {
        String[][] strGrid = new String[rows][columns];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                List<Particle> cellValues = grid[row][col].getParticles();
                if (!cellValues.isEmpty()) {
                    strGrid[row][col] = "[" + String.join(", ", cellValues.toString()) + "]";
                } else {
                    strGrid[row][col] = "";
                }
            }
        }

        int[] colWidths = new int[columns];
        for (int col = 0; col < columns; col++) {
            for (int row = 0; row < rows; row++) {
                colWidths[col] = Math.max(colWidths[col], strGrid[row][col].length());
            }
        }

        StringBuilder gridStr = new StringBuilder();
        for (int row = 0; row < rows; row++) {
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
}