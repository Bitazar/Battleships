package backend.boards;

import backend.boards.Board;
import backend.boards.BoardDTO;
import backend.utility.Coord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardDTOTest {

    @Test
    void boardCreationTest() {
        BoardDTO board = new BoardDTO(new Coord(8, 8));
    }

    @Test
    void boardDimensionsTest() {
        BoardDTO board = new BoardDTO(new Coord(7, 8));
        assertEquals(board.getHeight(), 8);
        assertEquals(board.getWidth(), 7);
        assertEquals(board.getDimensions(), new Coord(7, 8));
    }

    @Test
    void boardAccessCellTest() {
        BoardDTO board = new BoardDTO(new Coord(7, 8));
        assertEquals(board.accessCell(new Coord(6, 7)), null);
    }

    @Test
    void boardAccessCellOutOfRange() {
        BoardDTO board = new BoardDTO(new Coord(7, 8));
        assertThrowsExactly(ArrayIndexOutOfBoundsException.class, () -> board.accessCell(new Coord(7, 8)));
    }

    @Test
    void boardGenerateCellTest() {
        BoardDTO board = new BoardDTO(new Coord(7, 8));
        board.generateCell(new Coord(6, 7), 5);
        assertNotEquals(board.accessCell(new Coord(6, 7)), null);
        assertEquals(board.accessCell(new Coord(6, 7)),  5);
    }

    @Test
    void accessRowCellTest() {
        BoardDTO board = new BoardDTO(new Coord(7, 8));
        board.generateCell(new Coord(6, 0), 5);
        var row = board.iterator().next();
        assertEquals(row.get(6), 5);
    }

    @Test
    void accessColumnCellTest() {
        BoardDTO board = new BoardDTO(new Coord(7, 8));
        board.generateCell(new Coord(0, 7), 5);
        var row = board.columnIterator().next();
        assertEquals(row.get(7), 5);
    }

    @Test
    void onBoardValidTest() {
        BoardDTO board = new BoardDTO(new Coord(7, 8));
        assertTrue(board.onBoard(new Coord(1 ,0)));
    }

    @Test
    void onBoardNegativeValueTest() {
        BoardDTO board = new BoardDTO(new Coord(7, 8));
        assertFalse(board.onBoard(new Coord(-1 ,0)));
    }

    @Test
    void onBoardUpLimitValueTest() {
        BoardDTO board = new BoardDTO(new Coord(7, 8));
        assertFalse(board.onBoard(new Coord(7 ,0)));
    }

    @Test
    void accessCellEntireBoardTest() {
        BoardDTO board = new BoardDTO(new Coord(7, 8));
        for (int y = 0; y < 8; ++y) {
            for (int x = 0; x < 7 ;++x) {
                board.generateCell(new Coord(x, y), x + y);
            }
        }
        for (int y = 0; y < 8; ++y) {
            for (int x = 0; x < 7 ;++x) {
                assertEquals(board.accessCell(new Coord(x, y)), x + y);
            }
        }
    }

    @Test
    void accessCellViaRowIteratorTest() {
        BoardDTO board = new BoardDTO(new Coord(7, 8));
        for (int y = 0; y < 8; ++y) {
            for (int x = 0; x < 7 ;++x) {
                board.generateCell(new Coord(x, y), x + y);
            }
        }
        int y = 0;
        for (var row : board) {
            int x = 0;
            for (Integer cell : row) {
                assertEquals(cell, x++ + y);
            }
            ++y;
        }
    }

    @Test
    void accessCellViaColumnIteratorTest() {
        BoardDTO board = new BoardDTO(new Coord(7, 8));
        for (int y = 0; y < 8; ++y) {
            for (int x = 0; x < 7 ;++x) {
                board.generateCell(new Coord(x, y), x + y);
            }
        }
        int x = 0;
        for (var column : board.transpose()) {
            int y = 0;
            for (Integer cell : column) {
                assertEquals(cell, x + y++);
            }
            ++x;
        }
    }

    @Test
    void cloneBoardTest() {
        BoardDTO board = new BoardDTO(new Coord(7, 8));
        for (int y = 0; y < 8; ++y) {
            for (int x = 0; x < 7 ;++x) {
                board.generateCell(new Coord(x, y), x + y);
            }
        }
        Board<Integer> cloned = board.clone();
        board.setValue(new Coord(1, 1), 15);
        for (int y = 0; y < 8; ++y) {
            for (int x = 0; x < 7 ;++x) {
                assertEquals(cloned.accessCell(new Coord(x, y)), x + y);
            }
        }
    }

}
