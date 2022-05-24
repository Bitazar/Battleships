package backend.solvers;

import backend.boards.BoardDTO;
import backend.boards.GeneratorBoard;
import backend.constrains.HardConstrains;
import backend.constrains.SoftConstrains;
import backend.states.BattleshipsStates;
import backend.utility.Coord;
import backend.utility.InitValue;
import backend.states.BattleshipsStatesTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class WaveFunctionCollapseTest {

    public static TreeMap<Integer, Integer> generateShipLengths() {
        TreeMap<Integer, Integer> shipLengths = new TreeMap<>();
        shipLengths.put(1, 3);
        shipLengths.put(2, 2);
        shipLengths.put(3, 1);
        return shipLengths;
    }

    public static Integer[][] validBoard() {
        Integer[] firstRow = {2, 1, 2, 1, 1, 2};
        Integer[] secondRow = {1, 1, 2, 1, 1, 1};
        Integer[] thirdRow = {2, 1, 2, 1, 1, 1};
        Integer[] fourthRow = {2, 1, 1, 1, 2, 2};
        Integer[] fifthRow = {1, 1, 1, 1, 1, 1};
        Integer[] sixthRow = {1, 1, 1, 1, 1, 2};
        Integer[][] result = {firstRow, secondRow, thirdRow, fourthRow, fifthRow, sixthRow};
        return result;
    }

    private final static Map<Integer, Map<Coord, Set<Integer>>>                 constrains = BattleshipsStatesTest.generateConstrains();
    private final static List<Integer>                                          rows = List.of(3, 1, 2, 3, 0, 1);
    private final static List<Integer>                                          cols = List.of(3, 0, 3, 0, 1, 3);
    private final static TreeMap<Integer, Integer>                              shipLengths = generateShipLengths();
    private final static HardConstrains                                         hard = new HardConstrains(rows, cols, shipLengths);
    private final static SoftConstrains                                         soft = new SoftConstrains(rows, cols, shipLengths);
    private final static BattleshipsStates                                      states = new BattleshipsStates();

    @Test
    void solveBoardTest() throws NoSolutionException {
        WaveFunctionCollapse solver = new WaveFunctionCollapse(soft, hard, constrains, states);
        List<InitValue> initValues = List.of(new InitValue(new Coord(2, 2), 2), new InitValue(new Coord(2, 3), 1));
        BoardDTO result = (BoardDTO) solver.solve(new GeneratorBoard(new Coord(6, 6)), initValues);
        Integer[][] validBoard = this.validBoard();
        for (int y = 0;y < 6; ++y) {
            for (int x = 0; x < 6; ++x) {
                assertEquals(validBoard[y][x], result.accessCell(new Coord(x, y)));
            }
        }
    }

}
