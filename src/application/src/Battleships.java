package application.src;

import backend.src.boards.BoardDTO;
import backend.src.facades.BattleshipsSolver;
import backend.src.solvers.NoSolutionException;
import backend.src.utility.Coord;
import backend.src.utility.InitValue;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Battleships {

    // purely for debug
    private static void printSolution(BattleshipsSolver solver, List<InitValue> initValueList, List<Integer> rowLimits, List<Integer> columnLimits, TreeMap<Integer, Integer> shipLimits) throws NoSolutionException {
        BoardDTO dto = solver.solve(initValueList, rowLimits, columnLimits, shipLimits);
        System.out.print(" ");
        for (Integer value : columnLimits) {
            System.out.print(value);
        }
        System.out.print("\n");
        var rowItr = rowLimits.iterator();
        for (var row : dto) {
            System.out.print(rowItr.next());
            for (Integer value : row) {
                System.out.print(value.equals(1) ? "□" : "■");
            }
            System.out.print("\n");
        }
    }

    public static void main(String[] args) throws NoSolutionException {
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

        List<InitValue> initValueList = List.of(new InitValue(new Coord(2, 2), 4));
        List<Integer> rows = List.of(3, 1, 2, 3, 0, 1);
        List<Integer> cols = List.of(3, 0, 3, 0, 1, 3);

        TreeMap<Integer, Integer> shipLengths = new TreeMap<>();
        shipLengths.put(1, 3);
        shipLengths.put(2, 2);
        shipLengths.put(3, 1);

        BattleshipsSolver solver = new BattleshipsSolver(constrains);
        printSolution(solver, initValueList, rows, cols, shipLengths);
    }

}
