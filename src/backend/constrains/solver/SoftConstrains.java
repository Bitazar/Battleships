/**
 * @author Mateusz Jaracz
 */
package backend.constrains.solver;

import backend.boards.Board;

import java.util.List;
import java.util.TreeMap;
import java.util.Set;

/**
 * Constrains that can be checked on the incomplete board
 */
public class SoftConstrains extends SolverConstrains {

    /**
     * Constructs a new SoftConstrains object
     *
     * @param rowLimits the row limits
     * @param columnLimits the column limits
     * @param shipLimits the ship limits
     */
    public SoftConstrains(List<Integer> rowLimits, List<Integer> columnLimits, TreeMap<Integer, Integer> shipLimits) {
        super(rowLimits, columnLimits, shipLimits);
    }

    /**
     * Checks the limit constrain on the given range
     *
     * @param range the range object
     * @param rangeLimit the range limits
     * @return whether the limit constrain is satisfied
     * @param <Range> the range type
     */
    @Override
    protected <Range extends Iterable<Set<Integer>>> boolean rangeConstrain(Range range, int rangeLimit) {
        SolverConstrains.ContainedPair pair = contained(range);
        return pair.ships <= rangeLimit && pair.ships + pair.empty >= rangeLimit;
    }

    /**
     * Checks if the ship length constrain is being satisfied
     *
     * @param board the board object
     * @return whether the ship length constrain is being satisfied
     */
    @Override
    public boolean shipLengthConstrain(Board<Set<Integer>> board) {
        var shipLengths = getShipLengths(board);
        for (Integer key : shipLimits.descendingKeySet()) {
            if (!shipLengths.containsKey(key)) {
                return true;
            } else if (!shipLengths.get(key).equals(shipLimits.get(key))) {
                return shipLengths.get(key) < shipLimits.get(key);
            }
        }
        return true;
    }

}
