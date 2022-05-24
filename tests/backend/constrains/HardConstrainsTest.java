package backend.constrains;

import backend.boards.Board;
import backend.boards.GeneratorBoard;
import backend.constrains.HardConstrains;
import backend.utility.Coord;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class HardConstrainsTest {

    private static Board<Set<Integer>> generateInvalidBoard() {
        GeneratorBoard board = new GeneratorBoard(new Coord(6, 6));
        for (int x = 0; x < 6; ++x) {
            for (int y = 0;y < 6; ++y) {
                board.generateCell(new Coord(x, y), new HashSet<>(Set.copyOf(List.of(1))));
            }
        }
        return board;
    }

    private static Board<Set<Integer>> generateValidBoard() {
        GeneratorBoard board = new GeneratorBoard(new Coord(6, 6));
        for (int x = 0; x < 6; ++x) {
            for (int y = 0;y < 6; ++y) {
                board.generateCell(new Coord(x, y), new HashSet<>(Set.copyOf(List.of(1))));
            }
        }
        board.setValue(new Coord(1, 1), new HashSet<>(Set.copyOf(List.of(2))));
        board.setValue(new Coord(1, 2), new HashSet<>(Set.copyOf(List.of(2))));
        return board;
    }

    private static TreeMap<Integer, Integer> generateShipLimits() {
        TreeMap<Integer, Integer> map = new TreeMap<Integer, Integer>();
        map.put(2, 1);
        return map;
    }

    private static final Board<Set<Integer>>                invalidBoard = generateInvalidBoard();
    private static final Board<Set<Integer>>                validBoard = generateValidBoard();
    private static final List<Integer>                      rowLimits = new ArrayList<>(List.of(0, 1, 1, 0, 0, 0));
    private static final List<Integer>                      colLimits = new ArrayList<>(List.of(0, 2, 0, 0, 0, 0));
    private static final TreeMap<Integer, Integer>          shipLimits = generateShipLimits();

    private static final HardConstrains                     constrains = new HardConstrains(rowLimits, colLimits, shipLimits);

    @Test
    void invalidBoardConstrainTest() {
        assertFalse(constrains.boardConstrain(invalidBoard));
    }

    @Test
    void validBoardConstrainTest() {
        assertTrue(constrains.boardConstrain(validBoard));
    }

    @Test
    void invalidShipConstrainTest() {
        assertFalse(constrains.shipLengthConstrain(invalidBoard));
    }

    @Test
    void validShipConstrainTest() {
        assertTrue(constrains.shipLengthConstrain(validBoard));
    }

    @Test
    void validConstrainTest() {
        assertTrue(constrains.check(validBoard));
    }

    @Test
    void invalidConstrainTest() {
        assertFalse(constrains.check(invalidBoard));
    }

}
