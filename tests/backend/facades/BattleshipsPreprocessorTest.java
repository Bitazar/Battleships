package backend.facades;

import backend.facades.BattleshipsPreprocessor;
import backend.utility.Coord;
import backend.utility.InitValue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BattleshipsPreprocessorTest {

    private static final BattleshipsPreprocessor            preprocessor = new BattleshipsPreprocessor(new Coord(6, 6));

    @Test
    void preprocessLeftVectorizedTest() {
        List<InitValue<Integer>> results = preprocessor.preprocess(List.of(new InitValue<>(new Coord(2, 2), 3)));
        assertTrue(results.contains(new InitValue<>(new Coord(2, 2), 2)));
        assertTrue(results.contains(new InitValue<>(new Coord(1, 2), 2)));
        assertTrue(results.contains(new InitValue<>(new Coord(3, 2), 1)));
        assertTrue(results.contains(new InitValue<>(new Coord(2, 1), 1)));
        assertTrue(results.contains(new InitValue<>(new Coord(2, 3), 1)));
    }

    @Test
    void preprocessUpVectorizedTest() {
        List<InitValue<Integer>> results = preprocessor.preprocess(List.of(new InitValue<>(new Coord(2, 2), 4)));
        assertTrue(results.contains(new InitValue<>(new Coord(2, 2), 2)));
        assertTrue(results.contains(new InitValue<>(new Coord(1, 2), 1)));
        assertTrue(results.contains(new InitValue<>(new Coord(3, 2), 1)));
        assertTrue(results.contains(new InitValue<>(new Coord(2, 1), 2)));
        assertTrue(results.contains(new InitValue<>(new Coord(2, 3), 1)));
    }

    @Test
    void preprocessRightVectorizedTest() {
        List<InitValue<Integer>> results = preprocessor.preprocess(List.of(new InitValue<>(new Coord(2, 2), 5)));
        assertTrue(results.contains(new InitValue<>(new Coord(2, 2), 2)));
        assertTrue(results.contains(new InitValue<>(new Coord(1, 2), 1)));
        assertTrue(results.contains(new InitValue<>(new Coord(3, 2), 2)));
        assertTrue(results.contains(new InitValue<>(new Coord(2, 1), 1)));
        assertTrue(results.contains(new InitValue<>(new Coord(2, 3), 1)));
    }

    @Test
    void preprocessDownVectorizedTest() {
        List<InitValue<Integer>> results = preprocessor.preprocess(List.of(new InitValue<>(new Coord(2, 2), 6)));
        assertTrue(results.contains(new InitValue<>(new Coord(2, 2), 2)));
        assertTrue(results.contains(new InitValue<>(new Coord(1, 2), 1)));
        assertTrue(results.contains(new InitValue<>(new Coord(3, 2), 1)));
        assertTrue(results.contains(new InitValue<>(new Coord(2, 1), 1)));
        assertTrue(results.contains(new InitValue<>(new Coord(2, 3), 2)));
    }

    @Test
    void preprocessSingleVectorizedTest() {
        List<InitValue<Integer>> results = preprocessor.preprocess(List.of(new InitValue<>(new Coord(2, 2), 7)));
        assertTrue(results.contains(new InitValue<>(new Coord(2, 2), 2)));
        assertTrue(results.contains(new InitValue<>(new Coord(1, 2), 1)));
        assertTrue(results.contains(new InitValue<>(new Coord(3, 2), 1)));
        assertTrue(results.contains(new InitValue<>(new Coord(2, 1), 1)));
        assertTrue(results.contains(new InitValue<>(new Coord(2, 3), 1)));
    }

}
