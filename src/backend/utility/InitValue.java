/**
 * @author Mateusz Jaracz
 */
package backend.utility;

/**
 * Represents the initial values on the board
 *
 * @param coord the position the cell
 * @param value the value of the cell
 *
 * @param <Value> the value's field type
 */
public record InitValue <Value> (Coord coord, Value value) {

    /**
     * Checks if two init values are same
     *
     * @param object the reference object with which to compare
     * @return whether two init values are same
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof InitValue initValue)) return false;
        return value == initValue.value && coord.equals(initValue.coord);
    }

    /**
     * Returns the init value hash code
     *
     * @return the init value hash code
     */
    @Override
    public int hashCode() {
        return coord.hashCode();
    }

    /**
     * Represents the initial value as a string
     *
     * @return the string representation of the initial value
     */
    @Override
    public String toString() {
        return "InitValue{" +
                "coord=" + coord +
                ", value=" + value +
                '}';
    }

}
