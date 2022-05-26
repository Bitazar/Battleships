package backend.boards;

import backend.utility.Coord;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BattleshipsBoardTest {

    @Test
    void boardCreationTest() {
        new BattleshipsBoard(new Coord(8, 8));
    }

    @Test
    void boardDimensionsTest() {
        BattleshipsBoard board = new BattleshipsBoard(new Coord(7, 8));
        assertEquals(board.getHeight(), 8);
        assertEquals(board.getWidth(), 7);
        assertEquals(board.getDimensions(), new Coord(7, 8));
    }

    @Test
    void boardAccessCellTest() {
        BattleshipsBoard board = new BattleshipsBoard(new Coord(7, 8));
        assertNull(board.accessCell(new Coord(6, 7)));
    }

    @Test
    void boardAccessCellOutOfRange() {
        BattleshipsBoard board = new BattleshipsBoard(new Coord(7, 8));
        assertThrowsExactly(ArrayIndexOutOfBoundsException.class, () -> board.accessCell(new Coord(7, 8)));
    }

    @Test
    void boardGenerateCellTest() {
        BattleshipsBoard board = new BattleshipsBoard(new Coord(7, 8));
        board.generateCell(new Coord(6, 7), new HashSet<>());
        assertNotEquals(board.accessCell(new Coord(6, 7)), null);
        assertEquals(0, board.accessCell(new Coord(6, 7)).size());
        board.accessCell(new Coord(6, 7)).add(7);
        assertEquals(1, board.accessCell(new Coord(6, 7)).size());
        assertEquals(board.accessCell(new Coord(6, 7)).iterator().next(), 7);
    }

    @Test
    void accessRowCellTest() {
        BattleshipsBoard board = new BattleshipsBoard(new Coord(7, 8));
        board.generateCell(new Coord(6, 0), new HashSet<>());
        board.accessCell(new Coord(6, 0)).add(7);
        var row = board.iterator().next();
        assertEquals(row.get(6).iterator().next(), 7);
    }

    @Test
    void accessColumnCellTest() {
        BattleshipsBoard board = new BattleshipsBoard(new Coord(7, 8));
        board.generateCell(new Coord(0, 7), new HashSet<>());
        board.accessCell(new Coord(0, 7)).add(7);
        var column = board.columnIterator().next();
        assertEquals(column.get(7).iterator().next(), 7);
    }

    @Test
    void onBoardValidTest() {
        BattleshipsBoard board = new BattleshipsBoard(new Coord(7, 8));
        assertTrue(board.onBoard(new Coord(1 ,0)));
    }

    @Test
    void onBoardNegativeValueTest() {
        BattleshipsBoard board = new BattleshipsBoard(new Coord(7, 8));
        assertFalse(board.onBoard(new Coord(-1 ,0)));
    }

    @Test
    void onBoardUpLimitValueTest() {
        BattleshipsBoard board = new BattleshipsBoard(new Coord(7, 8));
        assertFalse(board.onBoard(new Coord(7 ,0)));
    }

    @Test
    void accessCellEntireBoardTest() {
        BattleshipsBoard board = new BattleshipsBoard(new Coord(7, 8));
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
        BattleshipsBoard board = new BattleshipsBoard(new Coord(7, 8));
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
        BattleshipsBoard board = new BattleshipsBoard(new Coord(7, 8));
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
        BattleshipsBoard board = new BattleshipsBoard(new Coord(7, 8));
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

}