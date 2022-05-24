package application;

import backend.boards.BoardDTO;
import backend.facades.BattleshipsGenerator;
import backend.facades.BattleshipsSolver;
import backend.solvers.NoSolutionException;
import backend.utility.Coord;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Battleships {

    // purely for debug
    private static void printSolution(BoardDTO dto, List<Integer> rowLimits, List<Integer> columnLimits) {
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

    private static Map<Integer, Map<Coord, Set<Integer>>> generateConstrains() {
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

    public static void main(String[] args) throws NoSolutionException {
        var constrains = generateConstrains();

        TreeMap<Integer, Integer> shipLengths = new TreeMap<>();
        shipLengths.put(1, 4);
        shipLengths.put(2, 3);
        shipLengths.put(3, 2);
        shipLengths.put(4, 1);

        long startTime = System.nanoTime();

        BattleshipsGenerator generator = new BattleshipsGenerator(constrains, shipLengths, new Coord(9, 9), 6);
        var generated = generator.generate();

        long endTime = System.nanoTime();
        System.out.println("Generation time: " + ((endTime - startTime) / 1000000000) + "s");

        BattleshipsSolver solver = new BattleshipsSolver(constrains);
        BoardDTO dto = solver.solve(generated.initValues(), generated.rowLimits(), generated.columnLimits(), shipLengths);
        printSolution(dto, generated.rowLimits(), generated.columnLimits());
    }

}
