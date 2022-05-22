package backend.utility;

import java.util.Objects;

public class Coord {
    public int                                  x;
    public int                                  y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Coord coord)) return false;
        if (!super.equals(object)) return false;
        return x == coord.x && y == coord.y;
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), x, y);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Coord{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
};