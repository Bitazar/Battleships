/**
 * @author Mateusz Jaracz
 */
package backend.facades;

import backend.boards.Board;
import backend.boards.BattleshipsBoard;
import backend.boards.BoardDTO;
import backend.constrains.Constrains;
import backend.constrains.solver.HardConstrains;
import backend.constrains.solver.SoftConstrains;
import backend.heuristic.Heuristic;
import backend.heuristic.MinimumEntropyHeuristic;
import backend.solvers.NoSolutionException;
import backend.solvers.WaveFunctionCollapse;
import backend.states.BattleshipsStates;
import backend.states.States;
import backend.utility.Coord;
import backend.utility.InitValue;

import java.util.*;

/**
 * Generates a new Battleships Board
 */
public class BattleshipsGenerator {

    private final Map<Integer, Map<Coord, Set<Integer>>>        constrains;
    private final TreeMap<Integer, Integer>                     shipLengths;
    private final Coord                                         dimensions;
    private final int                                           resolution;
    private final Heuristic<Set<Integer>>                       heuristic = new MinimumEntropyHeuristic<>();
    private final States<Set<Integer>, Integer>                 states = new BattleshipsStates();

    /**
     * Constructs a new Solution object
     *
     * @param initValues the initial values of the board
     * @param rowLimits the row limits
     * @param columnLimits the column limits
     */
    public record Solution(List<InitValue<Integer>> initValues, List<Integer> rowLimits, List<Integer> columnLimits) { }

    /**
     * The relaxed version of the soft constraints. Does not check the row and column limits
     */
    private static class SoftGeneratorConstrains implements Constrains<Set<Integer>> {
        private final SoftConstrains constrains;

        /**
         * Constructs a new SoftGeneratorConstrains object
         *
         * @param shipLengths the ship lengths
         */
        public SoftGeneratorConstrains(TreeMap<Integer, Integer> shipLengths) {
            constrains = new SoftConstrains(null, null, shipLengths);
        }

        /**
         * Checks if the constraints are satisfied
         *
         * @param board the board object
         * @return whether the constraints are satisfied
         */
        public boolean check(Board<Set<Integer>> board) {
            return constrains.shipLengthConstrain(board);
        }
    }

    /**
     * The relaxed version of the hard constraints. Does not check the row and column limits
     */
    private static class HardGeneratorConstrains implements Constrains<Set<Integer>> {
        private final HardConstrains constrains;

        /**
         * Constructs a new HardGeneratorConstrains object
         *
         * @param shipLengths the ship lengths
         */
        public HardGeneratorConstrains(TreeMap<Integer, Integer> shipLengths) {
            constrains = new HardConstrains(null, null, shipLengths);
        }

        /**
         * Checks if the constraints are satisfied
         *
         * @param board the board object
         * @return whether the constraints are satisfied
         */
        public boolean check(Board<Set<Integer>> board) {
            return constrains.shipLengthConstrain(board);
        }
    }

    /**
     * Constructs a new BattleshipsGenerator object
     *
     * @param constrains the board's states constraints
     * @param shipLengths the ship lengths
     * @param dimensions the board dimensions
     * @param resolution the number of returned initial values
     */
    public BattleshipsGenerator(Map<Integer, Map<Coord, Set<Integer>>> constrains, TreeMap<Integer, Integer> shipLengths, Coord dimensions, int resolution) {
        this.constrains = constrains;
        this.shipLengths = shipLengths;
        this.dimensions = dimensions;
        this.resolution = resolution;
    }

    /**
     * Calculates the row limits of the given board
     *
     * @param board the board object
     * @return the row limits of the given board
     */
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

    /**
     * Calculates the column limits of the given board
     *
     * @param board the board object
     * @return the column limits of the given board
     */
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

    /**
     * Returns the neighborhood of the given cell
     *
     * @param board the board object
     * @param position the cell's position
     * @return the neighborhood of the given cell
     */
    private List<Coord> getNeighborhood(Board<Integer> board, Coord position) {
        int x = position.x(), y = position.y();
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

    /**
     * Transforms the given cell into its vectorized state
     *
     * @param neighbor the neighbor's position
     * @param position the cell's position
     * @return the transformed cell's value
     */
    private int directedVector(Coord neighbor, Coord position) {
        int xDiff = position.x() - neighbor.x();
        int yDiff = position.y() - neighbor.y();
        if (xDiff != 0) {
            return xDiff > 0 ? 3 : 5;
        }
        return yDiff > 0 ? 4 : 6;
    }

    /**
     * Vectorizes the given cell
     *
     * @param board the board object
     * @param position the cell's position
     * @return the transformed cell's value
     */
    private int vectorize(Board<Integer> board, Coord position) {
        List<Coord> neighborhood = getNeighborhood(board, position);
        if (neighborhood.isEmpty()) {
            return 7;
        }
        return neighborhood.size() == 1 ? directedVector(neighborhood.get(0), position) : 8;
    }

    /**
     * Generates the indexes of the cells with the ships on the board
     *
     * @param board the board object
     * @return the list with the indexes of the cells with the ships on the board
     */
    private List<Coord> generateIndexes(Board<Integer> board) {
        List<Coord> coords = new ArrayList<>();
        for (int x = 0; x < dimensions.x(); ++x) {
            for (int y = 0; y < dimensions.y(); ++y) {
                Coord coord = new Coord(x, y);
                if (board.accessCell(coord) != 1) {
                    coords.add(coord);
                }
            }
        }
        return coords;
    }

    /**
     * Strips the board into the list of initial values
     *
     * @param board the board object
     * @return the list of initial values
     */
    private List<InitValue<Integer>> strip(Board<Integer> board) {
        List<Coord> indexes = generateIndexes(board);
        Collections.shuffle(indexes);
        List<InitValue<Integer>> initValues = new ArrayList<>();
        for (int i = 0; i < resolution; ++i) {
            initValues.add(new InitValue<>(indexes.get(i),
                    board.accessCell(indexes.get(i)) == 1 ? 1 : vectorize(board, indexes.get(i))));
        }
        return initValues;
    }

    /**
     * Generates a new board
     *
     * @return the generated solution
     * @throws NoSolutionException when the board cannot be generated
     */
    public Solution generate() throws NoSolutionException {
        SoftGeneratorConstrains soft = new SoftGeneratorConstrains(shipLengths);
        HardGeneratorConstrains hard = new HardGeneratorConstrains(shipLengths);
        WaveFunctionCollapse<Set<Integer>, Integer> solver = new WaveFunctionCollapse<>(soft, hard, constrains, states, heuristic);
        var result = solver.solve(new BattleshipsBoard(dimensions), new BoardDTO(dimensions), new ArrayList<>());
        return new Solution(
                strip(result),
                calculateRowLimits(result),
                calculateColumnLimits(result)
        );
    }

}
