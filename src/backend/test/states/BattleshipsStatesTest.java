package backend.test.states;

import backend.src.boards.Board;
import backend.src.boards.BoardDTO;
import backend.src.boards.GeneratorBoard;
import backend.src.constrains.Constrains;
import backend.src.constrains.HardConstrains;
import backend.src.constrains.SoftConstrains;
import backend.src.solvers.NoSolutionException;
import backend.src.solvers.Solver;
import backend.src.states.BattleshipsStates;
import backend.src.utility.Coord;
import backend.src.utility.InitValue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BattleshipsStatesTest {

    private static Map<Integer, Map<Coord, Set<Integer>>> generateConstrains() {
        Map<Integer, Map<Coord, Set<Integer>>> constrains = new HashMap<>();
        Map<Coord, Set<Integer>> water = new HashMap<>();
        water.put(new Coord(0, 1), Stream.of(1, 2, 3).collect(Collectors.toCollection(HashSet::new)));
        water.put(new Coord(0, -1), Stream.of(1, 2, 3).collect(Collectors.toCollection(HashSet::new)));
        water.put(new Coord(-1, 0), Stream.of(1, 2, 3).collect(Collectors.toCollection(HashSet::new)));
        water.put(new Coord(1, 0), Stream.of(1, 2, 3).collect(Collectors.toCollection(HashSet::new)));
        water.put(new Coord(-1, 1), Stream.of(1, 2, 3).collect(Collectors.toCollection(HashSet::new)));
        water.put(new Coord(1, 1), Stream.of(1, 2, 3).collect(Collectors.toCollection(HashSet::new)));
        water.put(new Coord(-1, -1), Stream.of(1, 2, 3).collect(Collectors.toCollection(HashSet::new)));
        water.put(new Coord(1, -1), Stream.of(1, 2, 3).collect(Collectors.toCollection(HashSet::new)));
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

    static class DummySolver extends Solver {

        public DummySolver() {
            super(new SoftConstrains(null, null, null),
                    new HardConstrains(null, null, null),
                    generateConstrains());
        }

        public Board<Integer> solve(Board<Set<Integer>> emptyBoard, List<InitValue> initValueList) throws NoSolutionException {
            return new BoardDTO(new Coord(1, 1));
        }

    }

    private static GeneratorBoard generateBoard() {
        GeneratorBoard board = new GeneratorBoard(new Coord(2, 2));
        board.generateCell(new Coord(0, 0), new HashSet<>(List.of(1, 2)));
        board.generateCell(new Coord(0, 1), new HashSet<>(List.of(1, 2)));
        board.generateCell(new Coord(1, 0), new HashSet<>(List.of(1, 2)));
        board.generateCell(new Coord(1, 1), new HashSet<>(List.of(1, 2)));
        return board;
    }

    private static final BattleshipsStates                          states = new BattleshipsStates();

    @Test
    void battleshipStatesShipTest() {
        Solver solver = new DummySolver();
        Board<Set<Integer>> board = generateBoard();
        board.setValue(new Coord(1, 1), new HashSet<>(List.of(2)));
        Set<Integer> neighbourStates = states.getStates(solver, board, new Coord(1, 1), new Coord(-1, -1));
        assertEquals(neighbourStates.size(), 1);
        assertTrue(neighbourStates.contains(1));
        Set<Integer> upStates = states.getStates(solver, board, new Coord(1, 1), new Coord(0, -1));
        assertEquals(upStates.size(), 2);
    }

    @Test
    void battleshipStatesWaterTest() {
        Solver solver = new DummySolver();
        Board<Set<Integer>> board = generateBoard();
        board.setValue(new Coord(1, 1), new HashSet<>(List.of(1)));
        Set<Integer> neighbourStates = states.getStates(solver, board, new Coord(1, 1), new Coord(-1, -1));
        assertEquals(neighbourStates.size(), 2);
        Set<Integer> upStates = states.getStates(solver, board, new Coord(1, 1), new Coord(0, -1));
        assertEquals(upStates.size(), 2);
    }

}