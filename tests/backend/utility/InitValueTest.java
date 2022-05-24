package backend.utility;

import backend.utility.Coord;
import backend.utility.InitValue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InitValueTest {

    @Test
    void valueHandlingTest() {
        InitValue value = new InitValue(new Coord(1, 2), 5);
        assertEquals(value.getCoord(), new Coord(1, 2));
        assertEquals(value.getValue(), 5);
    }

    @Test
    void equalsValidTest() {
        InitValue value1 = new InitValue(new Coord(1, 2), 5);
        InitValue value2 = new InitValue(new Coord(1, 2), 5);
        assertEquals(value1, value2);
    }

    @Test
    void equalsInvalidTest() {
        InitValue value1 = new InitValue(new Coord(1, 2), 5);
        InitValue value2 = new InitValue(new Coord(1, 7), 5);
        assertNotEquals(value1, value2);
    }

}
