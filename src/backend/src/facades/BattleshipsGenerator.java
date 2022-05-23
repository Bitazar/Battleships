package backend.src.facades;

import backend.src.boards.Board;
import backend.src.boards.GeneratorBoard;
import backend.src.constrains.Constrains;
import backend.src.constrains.HardConstrains;
import backend.src.constrains.SoftConstrains;
import backend.src.solvers.NoSolutionException;
import backend.src.solvers.WaveFunctionCollapse;
import backend.src.states.BattleshipsStates;
import backend.src.states.States;
import backend.src.utility.Coord;
import backend.src.utility.InitValue;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BattleshipsGenerator {

    private final Map<Integer, Map<Coord, Set<Integer>>>        constrains;
    private final TreeMap<Integer, Integer>                     shipLengths;
    private final Coord                                         dimensions;
    private final int                                           resolution;

    private final States                                        states = new BattleshipsStates();

    public record Solution(List<InitValue> initValues, List<Integer> rowLimits, List<Integer> columnLimits) { }

    private static class SoftGeneratorConstrains implements Constrains {
        private final SoftConstrains                        constrains;

        public SoftGeneratorConstrains(TreeMap<Integer, Integer> shipLengths) {
            constrains = new SoftConstrains(null, null, shipLengths);
        }

        public boolean check(Board<Set<Integer>> board) {
            return constrains.shipLengthConstrain(board);
        }
    }

    private static class HardGeneratorConstrains implements Constrains {
        private final HardConstrains                        constrains;

        public HardGeneratorConstrains(TreeMap<Integer, Integer> shipLengths) {
            constrains = new HardConstrains(null, null, shipLengths);
        }

        public boolean check(Board<Set<Integer>> board) {
            return constrains.shipLengthConstrain(board);
        }
    }

    public BattleshipsGenerator(Map<Integer, Map<Coord, Set<Integer>>> constrains, TreeMap<Integer, Integer> shipLengths, Coord dimensions, int resolution) {
        this.constrains = constrains;
        this.shipLengths = shipLengths;
        this.dimensions = dimensions;
        this.resolution = resolution;
    }

    private List<Integer> calculateRowLimits(Board<Integer> board) {
        List<Integer> rowLimits = new ArrayList<>();
        for (var row : board) {
            int limit = 0;
            for (Integer value : row) {
                limit += value == 2 ? 1 : 0;
            }
            rowLimits.add(limit);
        }
        return rowLimits;
    }

    private List<Integer> calculateColumnLimits(Board<Integer> board) {
        List<Integer> columnLimits = new ArrayList<>();
        for (var row : board.transpose()) {
            int limit = 0;
            for (Integer value : row) {
                limit += value == 2 ? 1 : 0;
            }
            columnLimits.add(limit);
        }
        return columnLimits;
    }

    private List<Coord> getNeighborhood(Board<Integer> board, Coord position) {
        int x = position.getX(), y = position.getY();
        List<Coord> neighborhood = new ArrayList<>(Arrays.asList(
                new Coord(x, y - 1),
                new Coord(x, y + 1),
                new Coord(x + 1, y),
                new Coord(x - 1, y)
        ));
        neighborhood.removeIf(c-> !board.onBoard(c));
        neighborhood.removeIf(c-> board.accessCell(c) == 1);
        return neighborhood;
    }

    private int directedVector(Coord neighbor, Coord position) {
        int xDiff = position.getX() - neighbor.getX();
        int yDiff = position.getY() - neighbor.getY();
        if (xDiff != 0) {
            return xDiff > 0 ? 3 : 5;
        }
        return yDiff > 0 ? 4 : 6;
    }

    private int vectorize(Board<Integer> board, Coord position) {
        List<Coord> neighborhood = getNeighborhood(board, position);
        if (neighborhood.isEmpty()) {
            return 7;
        }
        return neighborhood.size() == 1 ? directedVector(neighborhood.get(0), position) : 8;
    }

    private List<Coord> generateIndexes(Board<Integer> board) {
        List<Coord> coords = new ArrayList<>();
        for (int x = 0; x < dimensions.getX(); ++x) {
            for (int y = 0;y < dimensions.getY(); ++y) {
                Coord coord = new Coord(x, y);
                if (board.accessCell(coord) != 1) {
                    coords.add(coord);
                }
            }
        }
        return coords;
    }

    private List<InitValue> strip(Board<Integer> board) {
        List<Coord> indexes = generateIndexes(board);
        Collections.shuffle(indexes);
        List<InitValue> initValues = new ArrayList<>();
        for (int i = 0; i < resolution; ++i) {
            initValues.add(new InitValue(indexes.get(i),
                    board.accessCell(indexes.get(i)) == 1 ? 1 : vectorize(board, indexes.get(i))));
        }
        return initValues;
    }

    public Solution generate() throws NoSolutionException {
        SoftGeneratorConstrains soft = new SoftGeneratorConstrains(shipLengths);
        HardGeneratorConstrains hard = new HardGeneratorConstrains(shipLengths);
        WaveFunctionCollapse solver = new WaveFunctionCollapse(soft, hard, constrains, states);
        var result = solver.solve(new GeneratorBoard(dimensions), new ArrayList<>());
        return new Solution(
                strip(result),
                calculateRowLimits(result),
                calculateColumnLimits(result)
        );
    }

}
