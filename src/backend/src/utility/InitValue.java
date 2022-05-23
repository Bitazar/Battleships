package backend.src.utility;

import java.util.Objects;

public class InitValue {
    private final Coord                 coord;
    private final int                   value;

    public InitValue(Coord coord, int value) {
        this.coord = coord;
        this.value = value;
    }

    public Coord getCoord() {
        return coord;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InitValue initValue)) return false;
        return value == initValue.value && coord.equals(initValue.coord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coord, value);
    }

    @Override
    public String toString() {
        return "InitValue{" +
                "coord=" + coord +
                ", value=" + value +
                '}';
    }

}
