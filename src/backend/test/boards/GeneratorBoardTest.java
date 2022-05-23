package backend.test.boards;

import backend.src.boards.Board;
import backend.src.boards.GeneratorBoard;
import backend.src.utility.Coord;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GeneratorBoardTest {

    @Test
    void boardCreationTest() {
        GeneratorBoard board = new GeneratorBoard(new Coord(8, 8));
    }

    @Test
    void boardDimensionsTest() {
        GeneratorBoard board = new GeneratorBoard(new Coord(7, 8));
        assertEquals(board.getHeight(), 8);
        assertEquals(board.getWidth(), 7);
        assertEquals(board.getDimensions(), new Coord(7, 8));
    }

    @Test
    void boardAccessCellTest() {
        GeneratorBoard board = new GeneratorBoard(new Coord(7, 8));
        assertEquals(board.accessCell(new Coord(6, 7)), null);
    }

    @Test
    void boardAccessCellOutOfRange() {
        GeneratorBoard board = new GeneratorBoard(new Coord(7, 8));
        assertThrowsExactly(ArrayIndexOutOfBoundsException.class, () -> board.accessCell(new Coord(7, 8)));
    }

    @Test
    void boardGenerateCellTest() {
        GeneratorBoard board = new GeneratorBoard(new Coord(7, 8));
        board.generateCell(new Coord(6, 7), new HashSet<Integer>());
        assertNotEquals(board.accessCell(new Coord(6, 7)), null);
        assertTrue(board.accessCell(new Coord(6, 7)).size() == 0);
        board.accessCell(new Coord(6, 7)).add(7);
        assertTrue(board.accessCell(new Coord(6, 7)).size() == 1);
        assertEquals(board.accessCell(new Coord(6, 7)).iterator().next(), 7);
    }

    @Test
    void accessRowCellTest() {
        GeneratorBoard board = new GeneratorBoard(new Coord(7, 8));
        board.generateCell(new Coord(6, 0), new HashSet<Integer>());
        board.accessCell(new Coord(6, 0)).add(7);
        var row = board.iterator().next();
        assertEquals(row.get(6).iterator().next(), 7);
    }

    @Test
    void accessColumnCellTest() {
        GeneratorBoard board = new GeneratorBoard(new Coord(7, 8));
        board.generateCell(new Coord(0, 7), new HashSet<Integer>());
        board.accessCell(new Coord(0, 7)).add(7);
        var column = board.columnIterator().next();
        assertEquals(column.get(7).iterator().next(), 7);
    }

    @Test
    void onBoardValidTest() {
        GeneratorBoard board = new GeneratorBoard(new Coord(7, 8));
        assertTrue(board.onBoard(new Coord(1 ,0)));
    }

    @Test
    void onBoardNegativeValueTest() {
        GeneratorBoard board = new GeneratorBoard(new Coord(7, 8));
        assertFalse(board.onBoard(new Coord(-1 ,0)));
    }

    @Test
    void onBoardUpLimitValueTest() {
        GeneratorBoard board = new GeneratorBoard(new Coord(7, 8));
        assertFalse(board.onBoard(new Coord(7 ,0)));
    }

    @Test
    void accessCellEntireBoardTest() {
        GeneratorBoard board = new GeneratorBoard(new Coord(7, 8));
        for (int y = 0; y < 8; ++y) {
            for (int x = 0; x < 7 ;++x) {
                Set<Integer> set = new HashSet<>();
                set.add(x + y);
                board.generateCell(new Coord(x, y), set);
            }
        }
        for (int y = 0; y < 8; ++y) {
            for (int x = 0; x < 7 ;++x) {
                assertEquals(board.accessCell(new Coord(x, y)).iterator().next(), x + y);
            }
        }
    }

    @Test
    void accessCellViaRowIteratorTest() {
        GeneratorBoard board = new GeneratorBoard(new Coord(7, 8));
        for (int y = 0; y < 8; ++y) {
            for (int x = 0; x < 7 ;++x) {
                Set<Integer> set = new HashSet<>();
                set.add(x + y);
                board.generateCell(new Coord(x, y), set);
            }
        }
        int y = 0;
        for (var row : board) {
            int x = 0;
            for (Set<Integer> cell : row) {
                assertEquals(cell.iterator().next(), x++ + y);
            }
            ++y;
        }
    }

    @Test
    void accessCellViaColumnIteratorTest() {
        GeneratorBoard board = new GeneratorBoard(new Coord(7, 8));
        for (int y = 0; y < 8; ++y) {
            for (int x = 0; x < 7 ;++x) {
                Set<Integer> set = new HashSet<>();
                set.add(x + y);
                board.generateCell(new Coord(x, y), set);
            }
        }
        int x = 0;
        for (var column : board.transpose()) {
            int y = 0;
            for (Set<Integer> cell : column) {
                assertEquals(cell.iterator().next(), x + y++);
            }
            ++x;
        }
    }

    @Test
    void cloneBoardTest() {
        GeneratorBoard board = new GeneratorBoard(new Coord(7, 8));
        for (int y = 0; y < 8; ++y) {
            for (int x = 0; x < 7 ;++x) {
                Set<Integer> set = new HashSet<>();
                set.add(x + y);
                board.generateCell(new Coord(x, y), set);
            }
        }
        Board<Set<Integer>> cloned = board.clone();
        board.accessCell(new Coord(1, 1)).add(15);
        for (int y = 0; y < 8; ++y) {
            for (int x = 0; x < 7 ;++x) {
                assertEquals(cloned.accessCell(new Coord(x, y)).iterator().next(), x + y);
            }
        }
    }

    @Test
    void setWaterTest() {
        GeneratorBoard board = new GeneratorBoard(new Coord(7, 8));
        board.generateCell(new Coord(1, 1), Set.copyOf(List.of(1)));
        for (int y = 0; y < 2; ++y) {
            for (int x = 0; x < 2 ;++x) {
                if (x != 1 || y != 1) {
                    assertEquals(board.accessShip(new Coord(x, y)), null);
                }
            }
        }
        assertEquals(board.accessCell(new Coord(1, 1)).iterator().next(), 1);
    }

    @Test
    void setOneShipTest() {
        GeneratorBoard board = new GeneratorBoard(new Coord(7, 8));
        board.setValue(new Coord(1, 1), Set.copyOf(List.of(2)));
        for (int y = 0; y < 2; ++y) {
            for (int x = 0; x < 2 ;++x) {
                if (x != 1 || y != 1) {
                    assertEquals(board.accessShip(new Coord(x, y)), null);
                }
            }
        }
        assertNotEquals(board.accessShip(new Coord(1, 1)), null);
        assertEquals(board.accessShip(new Coord(1, 1)).iterator().next(), new Coord(1, 1));
    }

    @Test
    void lengthenShipTest() {
        GeneratorBoard board = new GeneratorBoard(new Coord(7, 8));
        Set<Integer> set = new HashSet<>();
        set.add(2);
        board.setValue(new Coord(1, 1), set);
        Set<Integer> set2 = new HashSet<>();
        set2.add(2);
        board.setValue(new Coord(1, 2), set2);
        assertNotEquals(board.accessShip(new Coord(1, 1)), null);
        assertNotEquals(board.accessShip(new Coord(1, 2)), null);
        assertEquals(board.accessShip(new Coord(1, 1)).size(), 2);
        assertEquals(board.accessShip(new Coord(1, 2)).size(), 2);
        assertEquals(board.accessShip(new Coord(1, 1)), board.accessShip(new Coord(1, 2)));
        assertTrue(board.accessShip(new Coord(1, 1)).contains(new Coord(1, 1)));
        assertTrue(board.accessShip(new Coord(1, 1)).contains(new Coord(1, 2)));
    }

    @Test
    void concatenateShipTest() {
        GeneratorBoard board = new GeneratorBoard(new Coord(7, 8));
        board.setValue(new Coord(1, 1), Set.copyOf(List.of(2)));
        board.setValue(new Coord(1, 3), Set.copyOf(List.of(2)));
        board.setValue(new Coord(1, 2), Set.copyOf(List.of(2)));
        assertNotEquals(board.accessShip(new Coord(1, 1)), null);
        assertNotEquals(board.accessShip(new Coord(1, 2)), null);
        assertNotEquals(board.accessShip(new Coord(1, 3)), null);
        assertEquals(board.accessShip(new Coord(1, 1)).size(), 3);
        assertEquals(board.accessShip(new Coord(1, 2)).size(), 3);
        assertEquals(board.accessShip(new Coord(1, 3)).size(), 3);
        assertEquals(board.accessShip(new Coord(1, 1)), board.accessShip(new Coord(1, 2)));
        assertEquals(board.accessShip(new Coord(1, 1)), board.accessShip(new Coord(1, 3)));
        assertTrue(board.accessShip(new Coord(1, 1)).contains(new Coord(1, 1)));
        assertTrue(board.accessShip(new Coord(1, 1)).contains(new Coord(1, 2)));
        assertTrue(board.accessShip(new Coord(1, 1)).contains(new Coord(1, 3)));
    }

    @Test
    void shipMutabilityAfterCloneTest() {
        GeneratorBoard board = new GeneratorBoard(new Coord(7, 8));
        board.setValue(new Coord(1, 1), Set.copyOf(List.of(2)));
        board.setValue(new Coord(1, 2), Set.copyOf(List.of(2)));
        assertEquals(board.accessShip(new Coord(1, 2)), board.accessShip(new Coord(1, 1)));
        assertEquals(board.accessShip(new Coord(1, 2)).hashCode(), board.accessShip(new Coord(1, 1)).hashCode());
        GeneratorBoard clone = (GeneratorBoard) board.clone();
        assertEquals(clone.accessShip(new Coord(1, 2)), clone.accessShip(new Coord(1, 1)));
        assertEquals(clone.accessShip(new Coord(1, 2)).hashCode(), clone.accessShip(new Coord(1, 1)).hashCode());
    }

}