package backend.utility;

import backend.utility.Coord;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CoordTest {

    @Test
    void coordValueTest() {
        Coord coord = new Coord(12, 45);
        assertEquals(coord.getX(), 12);
        assertEquals(coord.getY(), 45);
    }

    @Test
    void coordNegativeValueTest() {
        Coord coord = new Coord(-12, 45);
        assertEquals(coord.getX(), -12);
        assertEquals(coord.getY(), 45);
    }

    @Test
    void coordEqualsTest() {
        Coord coord1 = new Coord(-12, 45);
        Coord coord2 = new Coord(-12, 45);
        assertEquals(coord1, coord2);
    }

    @Test
    void coordNoEqualsTest() {
        Coord coord1 = new Coord(-12, 45);
        Coord coord2 = new Coord(12, 45);
        assertNotEquals(coord1, coord2);
    }

    @Test
    void coordTransposeTest() {
        Coord coord1 = new Coord(12, 45);
        Coord coord2 = new Coord(45, 12);
        assertEquals(coord1.transpose(), coord2);
    }

    @Test
    void coordOtherHashcodeTest() {
        Coord coord1 = new Coord(12, 45);
        Coord coord2 = new Coord(45, 12);
        assertNotEquals(coord1.hashCode(), coord2.hashCode());
    }

    @Test
    void coordSameHashcodeTest() {
        Coord coord1 = new Coord(12, 45);
        Coord coord2 = new Coord(12, 45);
        assertEquals(coord1.hashCode(), coord2.hashCode());
    }

}
