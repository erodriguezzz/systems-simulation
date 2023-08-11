package models;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
public abstract class Grid {

    private float L;
    private int M;
    private Cell[][] cells;
    private float cellSize;

    public Grid(float L, int M) {
        this.L = L;
        this.M = M;
        this.cellSize = L/M;
        this.cells = new Cell[M][M];
        for (int row = 0; row < M; row++) {
            for (int col = 0; col < M; col++) {
                cells[row][col] = new Cell(row, col);
            }
        }
        // Arrays.stream(cells).forEach(row -> Arrays.fill(row, new Cell()));
    }

    /**
     * TODO:
     * podríamos querer cambiar esta excepción por un continue, para que no se agregue ninguna
     * partícula si alguna está fuera de los límites
     **/
    public void addParticles(Set<Particle> particles) {

        particles.forEach(particle -> {
            if (particle.getX() >= L || particle.getY() >= L) {
                throw new IllegalArgumentException("Particle out of bounds");
            }
            int row = (int) Math.floor(particle.getX()/cellSize);
            int col = (int) Math.floor(particle.getY()/cellSize);
            cells[row][col].getParticles().add(particle);
        });
    }

    protected abstract void CIM(Cell cell, double rc);

    protected abstract void bruteForce(Cell[][] cells, double rc);

    public void setAllNeighbours(double rc, boolean cim) {
        for (int row = 0; row < getNumberOfRows(); row++) {
            for (int col = 0; col < getNumberOfRows(); col++) {
                Cell cell = getCell(row, col);
                if(cim)
                    CIM(cell, rc);
                else
                    bruteForce(this.cells, rc);
            }
        }
    }

    public void clearAllNeighbours() {
        for (int row = 0; row < getNumberOfRows(); row++) {
            for (int col = 0; col < getNumberOfRows(); col++) {
                Cell cell = getCell(row, col);
                cell.getParticles().forEach(particle -> particle.setNeighbours(new ArrayList<>()));
            }
        }
    }

    public Cell getCell(int row, int col) {
        return cells[row][col];
    }

    public double getDistance(Particle p1, Particle p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2)) - p1.getRadius() - p2.getRadius() ;
    }

    public float getNumberOfRows() {
        return M;
    }

    public float getSize() {return M;}

    public Cell[][] getCells() {
        return cells;
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

}
