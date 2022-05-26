package backend.constrains.dto;

import backend.boards.Board;
import backend.boards.BoardDTO;
import backend.io.MAPReader;
import backend.utility.Coord;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HardConstrainsTest {

    private static Board<Integer> generateBlankBoard() {
        BoardDTO board = new BoardDTO(new Coord(6, 6));
        for (int x = 0; x < 6; ++x) {
            for (int y = 0;y < 6; ++y) {
                board.generateCell(new Coord(x, y), 1);
            }
        }
        return board;
    }

    private static Board<Integer> generateInvalidBoardType3() {
        Board<Integer> board = generateBlankBoard();
        board.setValue(new Coord(1, 1), 3);
        return board;
    }

    private static Board<Integer> generateValidBoardType3() {
        Board<Integer> board = generateBlankBoard();
        board.setValue(new Coord(1, 1), 3);
        board.setValue(new Coord(0, 1), 2);
        return board;
    }

    private static Board<Integer> generateInvalidBoardType4() {
        Board<Integer> board = generateBlankBoard();
        board.setValue(new Coord(1, 1), 4);
        return board;
    }

    private static Board<Integer> generateValidBoardType4() {
        Board<Integer> board = generateBlankBoard();
        board.setValue(new Coord(1, 1), 4);
        board.setValue(new Coord(1, 0), 2);
        return board;
    }

    private static Board<Integer> generateInvalidBoardType5() {
        Board<Integer> board = generateBlankBoard();
        board.setValue(new Coord(1, 1), 5);
        return board;
    }

    private static Board<Integer> generateValidBoardType5() {
        Board<Integer> board = generateBlankBoard();
        board.setValue(new Coord(1, 1), 5);
        board.setValue(new Coord(2, 1), 2);
        return board;
    }

    private static Board<Integer> generateInvalidBoardType6() {
        Board<Integer> board = generateBlankBoard();
        board.setValue(new Coord(1, 1), 6);
        return board;
    }

    private static Board<Integer> generateValidBoardType6() {
        Board<Integer> board = generateBlankBoard();
        board.setValue(new Coord(1, 1), 6);
        board.setValue(new Coord(1, 2), 2);
        return board;
    }

    private static Board<Integer> generateInvalidBoardType7() {
        Board<Integer> board = generateBlankBoard();
        board.setValue(new Coord(1, 1), 7);
        board.setValue(new Coord(1, 2), 2);
        return board;
    }

    private static Board<Integer> generateValidBoardType7() {
        Board<Integer> board = generateBlankBoard();
        board.setValue(new Coord(1, 1), 7);
        return board;
    }

    private static Board<Integer> generateValidBoard() {
        Board<Integer> board = generateBlankBoard();
        board.setValue(new Coord(1, 1), 2);
        board.setValue(new Coord(1, 2), 2);
        return board;
    }

    private static TreeMap<Integer, Integer> generateShipLimits() {
        TreeMap<Integer, Integer> map = new TreeMap<>();
        map.put(2, 1);
        return map;
    }

    private static Map<Integer, Map<Coord, Set<Integer>>> generateConstrains() throws IOException {
        MAPReader reader = new MAPReader();
        return reader.read(Files.newBufferedReader(Path.of("assets/DTOStates.map")));
    }

    private static final Board<Integer>                     invalidBoard = generateBlankBoard();
    private static final Board<Integer>                     validBoard = generateValidBoard();

    private static final List<Integer> rowLimits = new ArrayList<>(List.of(0, 1, 1, 0, 0, 0));
    private static final List<Integer>                      colLimits = new ArrayList<>(List.of(0, 2, 0, 0, 0, 0));
    private static final TreeMap<Integer, Integer>          shipLimits = generateShipLimits();
    private static final HardConstrains                     constrains;

    static {
        try {
            constrains = new HardConstrains(rowLimits, colLimits, shipLimits, generateConstrains());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void invalidBoardType3VectorizedTest() {
        assertFalse(constrains.vectorizedFieldsConstrain(generateInvalidBoardType3()));
    }

    @Test
    void validBoardType3VectorizedTest() {
        assertTrue(constrains.vectorizedFieldsConstrain(generateValidBoardType3()));
    }

    @Test
    void invalidBoardType4VectorizedTest() {
        assertFalse(constrains.vectorizedFieldsConstrain(generateInvalidBoardType4()));
    }

    @Test
    void validBoardType4VectorizedTest() {
        assertTrue(constrains.vectorizedFieldsConstrain(generateValidBoardType4()));
    }

    @Test
    void invalidBoardType5VectorizedTest() {
        assertFalse(constrains.vectorizedFieldsConstrain(generateInvalidBoardType5()));
    }

    @Test
    void validBoardType5VectorizedTest() {
        assertTrue(constrains.vectorizedFieldsConstrain(generateValidBoardType5()));
    }

    @Test
    void invalidBoardType6VectorizedTest() {
        assertFalse(constrains.vectorizedFieldsConstrain(generateInvalidBoardType6()));
    }

    @Test
    void validBoardType6VectorizedTest() {
        assertTrue(constrains.vectorizedFieldsConstrain(generateValidBoardType6()));
    }

    @Test
    void invalidBoardType7VectorizedTest() {
        assertFalse(constrains.vectorizedFieldsConstrain(generateInvalidBoardType7()));
    }

    @Test
    void validBoardType7VectorizedTest() {
        assertTrue(constrains.vectorizedFieldsConstrain(generateValidBoardType7()));
    }

    @Test
    void invalidBoardConstrainTest() {
        assertFalse(constrains.boardConstrain(invalidBoard));
    }

    @Test
    void validBoardConstrainTest() {
        assertTrue(constrains.boardConstrain(validBoard));
    }

    @Test
    void validConstrainTest() {
        assertTrue(constrains.check(validBoard));
    }

    @Test
    void invalidConstrainTest() {
        assertFalse(constrains.check(invalidBoard));
    }

    @Test
    void invalidShipConstrainTest() {
        assertFalse(constrains.shipLengthConstrain(invalidBoard));
    }

    @Test
    void validShipConstrainTest() {
        assertTrue(constrains.shipLengthConstrain(validBoard));
    }

}
