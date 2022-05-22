package backend.facades;

import backend.boards.BoardDTO;
import backend.boards.GeneratorBoard;
import backend.constrains.HardConstrains;
import backend.constrains.SoftConstrains;
import backend.solvers.NoSolutionException;
import backend.solvers.WaveFunctionCollapse;
import backend.states.BattleshipStates;
import backend.states.States;
import backend.utility.Coord;
import backend.utility.InitValue;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BattleshipsSolver {
    private final Map<Integer, Map<Coord, Set<Integer>>>        constrains;
    private final States                                        states = new BattleshipStates();

    public BattleshipsSolver(Map<Integer, Map<Coord, Set<Integer>>> constrains) {
        this.constrains = constrains;
    }

    private void addZeroRows(Set<InitValue> preprocessed, List<Integer> rowLimits, List<Integer> columnLimits) {
        for (int y = 0; y < rowLimits.size(); ++y) {
            if (rowLimits.get(y) == 0) {
                for (int x = 0; x < columnLimits.size(); ++x) {
                    preprocessed.add(new InitValue(new Coord(x, y), 1));
                }
            }
        }
    }

    private void addZeroColumns(Set<InitValue> preprocessed, List<Integer> rowLimits, List<Integer> columnLimits) {
        for (int x = 0; x < columnLimits.size(); ++x) {
            if (columnLimits.get(x) == 0) {
                for (int y = 0; y < rowLimits.size(); ++y) {
                    preprocessed.add(new InitValue(new Coord(x, y), 1));
                }
            }
        }
    }

    private List<InitValue> extendInitialValues(List<Integer> rowLimits, List<Integer> columnLimits, List<InitValue> initValueList) {
        BattleshipsPreprocessor preprocessor = new BattleshipsPreprocessor(new Coord(columnLimits.size(), rowLimits.size()));
        Set<InitValue> preprocessed = new HashSet<>(preprocessor.preprocess(initValueList));
        addZeroRows(preprocessed, rowLimits, columnLimits);
        addZeroColumns(preprocessed, rowLimits, columnLimits);
        return new ArrayList<>(preprocessed);
    }

    public BoardDTO solve(List<InitValue> initValueList, List<Integer> rowLimits, List<Integer> columnLimits, TreeMap<Integer, Integer> shipLimits) throws NoSolutionException {
        SoftConstrains soft = new SoftConstrains(rowLimits, columnLimits, shipLimits);
        HardConstrains hard = new HardConstrains(rowLimits, columnLimits, shipLimits);
        WaveFunctionCollapse solver = new WaveFunctionCollapse(soft, hard, constrains, states);
        return (BoardDTO) solver.solve(new GeneratorBoard(new Coord(columnLimits.size(), rowLimits.size())), extendInitialValues(rowLimits, columnLimits, initValueList));
    }

    // purely for debug
    public void printSolution(List<InitValue> initValueList, List<Integer> rowLimits, List<Integer> columnLimits, TreeMap<Integer, Integer> shipLimits) throws NoSolutionException {
        BoardDTO dto = solve(initValueList, rowLimits, columnLimits, shipLimits);
        System.out.print(" ");
        for (Integer value : columnLimits) {
            System.out.print(value);
        }
        System.out.print("\n");
        var rowIter = rowLimits.iterator();
        for (var row : dto) {
            System.out.print(rowIter.next());
            for (Integer value : row) {
                System.out.print(value.equals(1) ? "□" : "■");
            }
            System.out.print("\n");
        }
    }

    public static void main(String[] args) throws NoSolutionException {
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

        List<InitValue> inits = List.of(new InitValue(new Coord(2, 2), 4));
        List<Integer> rows = List.of(3, 1, 2, 3, 0, 1);
        List<Integer> cols = List.of(3, 0, 3, 0, 1, 3);

        TreeMap<Integer, Integer> shipLengths = new TreeMap<>();
        shipLengths.put(1, 3);
        shipLengths.put(2, 2);
        shipLengths.put(3, 1);

        BattleshipsSolver solver = new BattleshipsSolver(constrains);
        solver.printSolution(inits, rows, cols, shipLengths);

    }

}
