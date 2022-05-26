/**
 * @author Mateusz Jaracz
 */
package backend.facades;

import backend.boards.BoardDTO;
import backend.boards.BattleshipsBoard;
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
 * Solves the given Battleships board
 */
public class BattleshipsSolver {
    private final Map<Integer, Map<Coord, Set<Integer>>>        constrains;
    private final States<Set<Integer>, Integer>                 states = new BattleshipsStates();
    private final Heuristic<Set<Integer>>                       heuristic = new MinimumEntropyHeuristic<>();

    /**
     * Constructs a new BattleshipsSolver object
     *
     * @param constrains the board's states constraints
     */
    public BattleshipsSolver(Map<Integer, Map<Coord, Set<Integer>>> constrains) {
        this.constrains = constrains;
    }

    /**
     * Adds the rows that not contains elements to the initial values
     *
     * @param preprocessed the preprocessed set of the initial values
     * @param rowLimits the row limits
     * @param columnLimits the column limits
     */
    private void addZeroRows(Set<InitValue<Integer>> preprocessed, List<Integer> rowLimits, List<Integer> columnLimits) {
        for (int y = 0; y < rowLimits.size(); ++y) {
            if (rowLimits.get(y) == 0) {
                for (int x = 0; x < columnLimits.size(); ++x) {
                    preprocessed.add(new InitValue<>(new Coord(x, y), 1));
                }
            }
        }
    }

    /**
     * Adds the columns that not contains elements to the initial values
     *
     * @param preprocessed the preprocessed set of the initial values
     * @param rowLimits the row limits
     * @param columnLimits the column limits
     */
    private void addZeroColumns(Set<InitValue<Integer>> preprocessed, List<Integer> rowLimits, List<Integer> columnLimits) {
        for (int x = 0; x < columnLimits.size(); ++x) {
            if (columnLimits.get(x) == 0) {
                for (int y = 0; y < rowLimits.size(); ++y) {
                    preprocessed.add(new InitValue<>(new Coord(x, y), 1));
                }
            }
        }
    }

    /**
     * Extends the initial value list with the obvious cases
     *
     * @param rowLimits the row limits
     * @param columnLimits the column limits
     * @param initValueList the list of the initial values
     * @return the extended list of the initial values
     */
    private List<InitValue<Integer>> extendInitialValues(List<Integer> rowLimits, List<Integer> columnLimits, List<InitValue<Integer>> initValueList) {
        BattleshipsPreprocessor preprocessor = new BattleshipsPreprocessor(new Coord(columnLimits.size(), rowLimits.size()));
        Set<InitValue<Integer>> preprocessed = new HashSet<>(preprocessor.preprocess(initValueList));
        addZeroRows(preprocessed, rowLimits, columnLimits);
        addZeroColumns(preprocessed, rowLimits, columnLimits);
        return new ArrayList<>(preprocessed);
    }

    /**
     * Solves the given Battleships board
     *
     * @param initValueList the list of the initial values
     * @param rowLimits the row limits
     * @param columnLimits the column limits
     * @param shipLimits the ship lengths
     * @return the solved board
     * @throws NoSolutionException when board cannot be solved
     */
    public BoardDTO solve(List<InitValue<Integer>> initValueList, List<Integer> rowLimits, List<Integer> columnLimits, TreeMap<Integer, Integer> shipLimits) throws NoSolutionException {
        SoftConstrains soft = new SoftConstrains(rowLimits, columnLimits, shipLimits);
        HardConstrains hard = new HardConstrains(rowLimits, columnLimits, shipLimits);
        Coord dimensions = new Coord(columnLimits.size(), rowLimits.size());
        WaveFunctionCollapse<Set<Integer>, Integer> solver = new WaveFunctionCollapse<>(soft, hard, constrains, states, heuristic);
        return (BoardDTO) solver.solve(new BattleshipsBoard(dimensions), new BoardDTO(dimensions),
            extendInitialValues(rowLimits, columnLimits, initValueList));
    }

}
