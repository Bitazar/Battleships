package backend.src.utility;

public class Coord {
    private int                                  x;
    private int                                  y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coord point = (Coord) o;
        return getX() == point.getX() && getY() == point.getY();
    }

    public int hashCode() {
        return 31 * getX() + getY();
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Coord{" +
                "x=" + getX() +
                ", y=" + getY() +
                '}';
    }

    public Coord transpose() {
        return new Coord(getY(), getX());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
};