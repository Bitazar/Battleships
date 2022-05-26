/**
 * @author Mateusz Jaracz
 */
package backend.utility;

/**
 * Represents the position on the two-dimensional discrete space
 */
public record Coord(int x, int y) {
    /**
     * Checks if two coordinates are same
     *
     * @param object the reference object with which to compare
     * @return whether two coordinates are same
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Coord point = (Coord) object;
        return x() == point.x() && y() == point.y();
    }

    /**
     * Returns the coordinate hash code
     *
     * @return the coordinate hash code
     */
    public int hashCode() {
        return 31 * x() + y();
    }

    /**
     * Represents the coordinate as a string
     *
     * @return the string representation of the coordinate
     */
    @Override
    public String toString() {
        return "Coord{" +
                "x=" + x() +
                ", y=" + y() +
                '}';
    }

    /**
     * Transposes the coordinate
     *
     * @return the transposed coordinate
     */
    public Coord transpose() {
        return new Coord(y(), x());
    }

    /**
     * Returns the x-axis value
     *
     * @return the x-axis value
     */
    @Override
    public int x() {
        return x;
    }

    /**
     * Returns the y-axis value
     *
     * @return the y-axis value
     */
    @Override
    public int y() {
        return y;
    }
}
