package backend.states;

import backend.boards.Board;
import backend.boards.BattleshipsBoard;
import backend.constrains.solver.HardConstrains;
import backend.constrains.solver.SoftConstrains;
import backend.solvers.Solver;
import backend.utility.Coord;
import backend.utility.InitValue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BattleshipsStatesTest {

    public static Map<Integer, Map<Coord, Set<Integer>>> generateConstrains() {
        Map<Integer, Map<Coord, Set<Integer>>> constrains = new HashMap<>();
        Map<Coord, Set<Integer>> water = new HashMap<>();
        water.put(new Coord(0, 1), Stream.of(1, 2).collect(Collectors.toCollection(HashSet::new)));
        water.put(new Coord(0, -1), Stream.of(1, 2).collect(Collectors.toCollection(HashSet::new)));
        water.put(new Coord(-1, 0), Stream.of(1, 2).collect(Collectors.toCollection(HashSet::new)));
        water.put(new Coord(1, 0), Stream.of(1, 2).collect(Collectors.toCollection(HashSet::new)));
        water.put(new Coord(-1, 1), Stream.of(1, 2).collect(Collectors.toCollection(HashSet::new)));
        water.put(new Coord(1, 1), Stream.of(1, 2).collect(Collectors.toCollection(HashSet::new)));
        water.put(new Coord(-1, -1), Stream.of(1, 2).collect(Collectors.toCollection(HashSet::new)));
        water.put(new Coord(1, -1), Stream.of(1, 2).collect(Collectors.toCollection(HashSet::new)));
        Map<Coord, Set<Integer>> ship = new HashMap<>();
        ship.put(new Coord(0, 1), Stream.of(1, 2).collect(Collectors.toCollection(HashSet::new)));
        ship.put(new Coord(0, -1), Stream.of(1, 2).collect(Collectors.toCollection(HashSet::new)));
        ship.put(new Coord(-1, 0), Stream.of(1, 2).collect(Collectors.toCollection(HashSet::new)));
        ship.put(new Coord(1, 0), Stream.of(1, 2).collect(Collectors.toCollection(HashSet::new)));
        ship.put(new Coord(-1, 1), Stream.of(1).collect(Collectors.toCollection(HashSet::new)));
        ship.put(new Coord(1, 1), Stream.of(1).collect(Collectors.toCollection(HashSet::new)));
        ship.put(new Coord(-1, -1), Stream.of(1).collect(Collectors.toCollection(HashSet::new)));
        ship.put(new Coord(1, -1), Stream.of(1).collect(Collectors.toCollection(HashSet::new)));
        constrains.put(1, water);
        constrains.put(2, ship);
        return constrains;
    }

    static class DummySolver extends Solver<Set<Integer>, Integer> {

        public DummySolver() {
            super(new SoftConstrains(null, null, new TreeMap<>()),
                    new HardConstrains(null, null, new TreeMap<>()),
                    generateConstrains());
        }

        public Board<Integer> solve(Board<Set<Integer>> emptyBoard, Board<Integer> resultBoard, List<InitValue<Integer>> initValueList) {
            return resultBoard;
        }

    }

    private static BattleshipsBoard generateBoard() {
        BattleshipsBoard board = new BattleshipsBoard(new Coord(2, 2));
        board.generateCell(new Coord(0, 0), new HashSet<>(List.of(1, 2)));
        board.generateCell(new Coord(0, 1), new HashSet<>(List.of(1, 2)));
        board.generateCell(new Coord(1, 0), new HashSet<>(List.of(1, 2)));
        board.generateCell(new Coord(1, 1), new HashSet<>(List.of(1, 2)));
        return board;
    }

    private static final BattleshipsStates                          states = new BattleshipsStates();

    @Test
    void battleshipStatesShipTest() {
        Solver<Set<Integer>, Integer> solver = new DummySolver();
        Board<Set<Integer>> board = generateBoard();
        board.setValue(new Coord(1, 1), new HashSet<>(List.of(2)));
        Set<Integer> neighbourStates = states.updateStates(solver, board, new Coord(1, 1), new Coord(-1, -1));
        assertEquals(neighbourStates.size(), 1);
        assertEquals(board.accessCell(new Coord(0, 0)).size(), 2);
        assertTrue(neighbourStates.contains(1));
        Set<Integer> upStates = states.updateStates(solver, board, new Coord(1, 1), new Coord(0, -1));
        assertEquals(upStates.size(), 2);
    }

    @Test
    void battleshipStatesWaterTest() {
        Solver<Set<Integer>, Integer> solver = new DummySolver();
        Board<Set<Integer>> board = generateBoard();
        board.setValue(new Coord(1, 1), new HashSet<>(List.of(1)));
        Set<Integer> neighbourStates = states.updateStates(solver, board, new Coord(1, 1), new Coord(-1, -1));
        assertEquals(neighbourStates.size(), 2);
        Set<Integer> upStates = states.updateStates(solver, board, new Coord(1, 1), new Coord(0, -1));
        assertEquals(upStates.size(), 2);
    }

    @Test
    void battleshipsStatesGenerateStateTest() {
        Board<Set<Integer>> board = generateBoard();
        assertEquals(states.generateState(board.accessCell(new Coord(1, 1))), board.accessCell(new Coord(1, 1)));
    }

    @Test
    void battleshipsStatesGetStates() {
        Board<Set<Integer>> board = generateBoard();
        assertEquals(states.updateStates(board.accessCell(new Coord(1, 1))), new ArrayList<>(List.of(1, 2)));
    }

    @Test
    void battleshipsStatesCollapseStateTest() {
        assertEquals(states.collapseState(5), new HashSet<>(Set.of(5)));
    }

}
