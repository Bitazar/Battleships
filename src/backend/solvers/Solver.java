package backend.solvers;

import backend.boards.Board;
import backend.constrains.Constrains;
import backend.utility.Coord;
import backend.utility.InitValue;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Base class for board solvers
 *
 * @param <BoardValue> the board's field type
 * @param <StateValue> the board's state type
 */
public abstract class Solver <BoardValue extends Collection<?>, StateValue> {
    protected final Constrains<BoardValue>                      softConstrains;
    protected final Constrains<BoardValue>                      hardConstrains;
    protected final Map<StateValue, Map<Coord, BoardValue>>     constrains;

    /**
     * Creates a new Solver object
     *
     * @param softConstrains the soft constraints
     * @param hardConstrains the hard constraints
     * @param constrains the board's states constraints
     */
    public Solver(Constrains<BoardValue> softConstrains, Constrains<BoardValue> hardConstrains, Map<StateValue, Map<Coord, BoardValue>> constrains) {
        this.softConstrains = softConstrains;
        this.hardConstrains = hardConstrains;
        this.constrains = constrains;
    }

    /**
     * Returns the soft constraints
     *
     * @return the soft constraints
     */
    public Constrains<BoardValue> getSoftConstrains() {
        return softConstrains;
    }

    /**
     * Returns the hard constraints
     *
     * @return the hard constraints
     */
    public Constrains<BoardValue> getHardConstrains() {
        return hardConstrains;
    }

    /**
     * Returns the board's states constraints
     *
     * @return the board's states constraints
     */
    public Map<StateValue, Map<Coord, BoardValue>> getConstrains() {
        return constrains;
    }

    /**
     * Solves the given board
     *
     * @param emptyBoard the empty board
     * @param solvedBoard the solved board
     * @param initValueList the initial value list for the problem
     * @return the solved board
     * @throws NoSolutionException if no solution can be found
     */
    public abstract Board<StateValue> solve(Board<BoardValue> emptyBoard, Board<StateValue> solvedBoard, List<InitValue<StateValue>> initValueList) throws NoSolutionException;

}
