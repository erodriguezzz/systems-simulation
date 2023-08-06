package models;
import java.util.Arrays;
import java.util.Set;
public class Grid {

    private int L;
    private int M;
    private Cell[][] cells;
    private float cellSize;

    public Grid(int L, int M) {
        this.L = L;
        this.M = M;
        this.cellSize = L/ (float) M;
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

    public Cell getCell(int row, int col) {
        return cells[row][col];
    }

    public int getGridSize() {
        return M;
    }

}
