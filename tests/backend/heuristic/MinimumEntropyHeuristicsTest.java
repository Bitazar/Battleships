package backend.heuristic;

import backend.boards.BattleshipsBoard;
import backend.utility.Coord;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MinimumEntropyHeuristicsTest {

    private static BattleshipsBoard generateBoard() {
        BattleshipsBoard board = new BattleshipsBoard(new Coord(4, 4));
        for (int x = 0; x < 4; ++x) {
            for (int y = 0; y < 4; ++y) {
                board.generateCell(new Coord(x, y), new HashSet<>(Set.of(1, 2, 3)));
            }
        }
        return board;
    }

    private final MinimumEntropyHeuristic<Set<Integer>>         heuristic = new MinimumEntropyHeuristic<>();

    @Test
    void minimumEntropyOneFieldTest() {
        BattleshipsBoard board = generateBoard();
        board.setValue(new Coord(1, 1), new HashSet<>(Set.of(1, 2)));
        assertEquals(heuristic.choose(board), new Coord(1, 1));
    }

    @Test
    void minimumEntropyTwoFieldsTest() {
        BattleshipsBoard board = generateBoard();
        board.setValue(new Coord(1, 1), new HashSet<>(Set.of(1, 2)));
        board.setValue(new Coord(1, 3), new HashSet<>(Set.of(1, 2)));
        assertEquals(heuristic.choose(board), new Coord(1, 1));
    }

    @Test
    void minimumEntropyTwoFieldsAndOneCollapsedTest() {
        BattleshipsBoard board = generateBoard();
        board.setValue(new Coord(1, 1), new HashSet<>(Set.of(1, 2)));
        board.setValue(new Coord(1, 3), new HashSet<>(Set.of(1, 2)));
        board.setValue(new Coord(2, 1), new HashSet<>(Set.of(1)));
        assertEquals(heuristic.choose(board), new Coord(1, 1));
    }

    @Test
    void minimumEntropyTwoFieldsNearbyAllCollapsed() {
        BattleshipsBoard board = generateBoard();
        for (int x = 0; x < 4; ++x) {
            for (int y = 0; y < 4; ++y) {
                if (x != 2 || y != 2) {
                    board.setValue(new Coord(x, y), new HashSet<>(Set.of(1)));
                }
            }
        }
        assertEquals(heuristic.choose(board), new Coord(2, 2));
    }

    @Test
    void minimumEntropyTwoFieldsAllCollapsed() {
        BattleshipsBoard board = generateBoard();
        for (int x = 0; x < 4; ++x) {
            for (int y = 0; y < 4; ++y) {
                board.setValue(new Coord(x, y), new HashSet<>(Set.of(1)));
            }
        }
        assertEquals(heuristic.choose(board), new Coord(0, 2));
    }

}
