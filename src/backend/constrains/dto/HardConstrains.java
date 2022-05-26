/**
 * @author Mateusz Jaracz
 */
package backend.constrains.dto;

import backend.boards.Board;
import backend.utility.Coord;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Constrains that can only be checked on the complete board
 */
public class HardConstrains extends DTOConstrains {

    /**
     * Constructs a new HardConstrains object
     *
     * @param rowLimits the row limits
     * @param columnLimits the column limits
     * @param shipLimits the ship limits
     * @param vectorizedFieldsContains the vectorized fields constrains
     */
    public HardConstrains(List<Integer> rowLimits, List<Integer> columnLimits, TreeMap<Integer, Integer> shipLimits, Map<Integer, Map<Coord, Set<Integer>>> vectorizedFieldsContains) {
        super(rowLimits, columnLimits, shipLimits, vectorizedFieldsContains);
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
    protected <Range extends Iterable<Integer>> boolean rangeConstrain(Range range, int rangeLimit) {
        ContainedPair pair = contained(range);
        return pair.ships == rangeLimit && pair.empty == 0;
    }

    /**
     * Checks if the ship length constrain is being satisfied
     *
     * @param board the board object
     * @return whether the ship length constrain is being satisfied
     */
    @Override
    public boolean shipLengthConstrain(Board<Integer> board) {
        return getShipLengths(board).equals(shipLimits);
    }

}
