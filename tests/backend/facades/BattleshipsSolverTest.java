package backend.facades;

import backend.boards.BoardDTO;
import backend.solvers.NoSolutionException;
import backend.utility.Coord;
import backend.utility.InitValue;
import backend.solvers.WaveFunctionCollapseTest;
import backend.states.BattleshipsStatesTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

public class BattleshipsSolverTest {

    private final static Map<Integer, Map<Coord, Set<Integer>>>                 constrains = BattleshipsStatesTest.generateConstrains();

    private final static TreeMap<Integer, Integer>                              shipLengths = WaveFunctionCollapseTest.generateShipLengths();
    private final static Integer[][]                                            validBoard = WaveFunctionCollapseTest.validBoard();
    private final static BattleshipsSolver                                      solver = new BattleshipsSolver(constrains);


    @Test
    void solvingValidPuzzleTest() {
        List<InitValue<Integer>> initValueList = List.of(new InitValue<>(new Coord(2, 2), 4));
        List<Integer> rows = List.of(3, 1, 2, 3, 0, 1);
        List<Integer> cols = List.of(3, 0, 3, 0, 1, 3);
        try {
            BoardDTO result = solver.solve(initValueList, rows, cols, shipLengths);
            for (int y = 0;y < 6; ++y) {
                for (int x = 0; x < 6; ++x) {
                    assertEquals(validBoard[y][x], result.accessCell(new Coord(x, y)));
                }
            }
        } catch (NoSolutionException exception) {
            fail();
        }
    }

    @Test
    void solvingInvalidPuzzleTest() {
        List<InitValue<Integer>> initValueList = List.of(new InitValue<>(new Coord(2, 2), 4));
        List<Integer> rows = List.of(0, 1, 0, 0, 0, 0);
        List<Integer> cols = List.of(0, 0, 1, 0, 0, 0);
        assertThrowsExactly(NoSolutionException.class, () -> solver.solve(initValueList, rows, cols, shipLengths));
    }


}
