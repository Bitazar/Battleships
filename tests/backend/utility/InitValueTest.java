package backend.utility;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InitValueTest {

    @Test
    void valueHandlingTest() {
        InitValue<Integer> value = new InitValue<>(new Coord(1, 2), 5);
        assertEquals(value.coord(), new Coord(1, 2));
        assertEquals(value.value(), 5);
    }

    @Test
    void equalsValidTest() {
        InitValue<Integer> value1 = new InitValue<>(new Coord(1, 2), 5);
        InitValue<Integer> value2 = new InitValue<>(new Coord(1, 2), 5);
        assertEquals(value1, value2);
    }

    @Test
    void equalsInvalidTest() {
        InitValue<Integer> value1 = new InitValue<>(new Coord(1, 2), 5);
        InitValue<Integer> value2 = new InitValue<>(new Coord(1, 7), 5);
        assertNotEquals(value1, value2);
    }

}
