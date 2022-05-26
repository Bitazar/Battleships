/**
 * @author Mateusz Jaracz
 */
package application;

import backend.boards.BoardDTO;
import backend.facades.BattleshipsGenerator;
import backend.facades.BattleshipsSolver;
import backend.io.MAPReader;
import backend.solvers.NoSolutionException;
import backend.utility.Coord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Battleships {

    // purely for debug
    public static void printSolution(BoardDTO dto, List<Integer> rowLimits, List<Integer> columnLimits) {
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

    /**
     * Generates constrains used by the backend.
     * Should be replaced with loading from a file
     */
    public static void main(String[] args) throws NoSolutionException, IOException {
        var reader = new MAPReader();
        var constrains = reader.read(Files.newBufferedReader(Path.of("assets/BaseStates.map")));

        TreeMap<Integer, Integer> shipLengths = new TreeMap<>();
        shipLengths.put(1, 3);
        shipLengths.put(2, 2);
        shipLengths.put(3, 1);

        long startTime = System.nanoTime();

        BattleshipsGenerator generator = new BattleshipsGenerator(constrains, shipLengths, new Coord(6, 6), 4);
        var generated = generator.generate();

        long endTime = System.nanoTime();
        System.out.println("Generation time: " + ((endTime - startTime) / 1000000000) + "s");

        BattleshipsSolver solver = new BattleshipsSolver(constrains);
        BoardDTO dto = solver.solve(generated.initValues(), generated.rowLimits(), generated.columnLimits(), shipLengths);
        printSolution(dto, generated.rowLimits(), generated.columnLimits());
    }

}
