package models;

public enum Color {
    RED(255, 0, 0),
    GREEN(0, 255, 0),
    BLACK(0, 0, 0),
    BLUE(0, 0, 255),
    PURPLE(128, 0, 128);
    final int r;
    final int g;
    final int b;

    Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static Color[] getAllColors() {
        return new Color[]{RED, GREEN, BLACK, BLUE, PURPLE};
    }

    @Override
    public String toString() {
        return r + " " + g + " " + b;
    }
}
