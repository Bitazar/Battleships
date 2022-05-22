package backend.constrains;

import backend.boards.Board;

import java.util.List;
import java.util.TreeMap;
import java.util.Set;

public class SoftConstrains extends Constrains {

    public SoftConstrains(List<Integer> rowLimits, List<Integer> columnLimits, TreeMap<Integer, Integer> shipLimits) {
        super(rowLimits, columnLimits, shipLimits);
    }

    @Override
    protected <Range extends Iterable<Set<Integer>>> boolean rangeConstrain(Range range, int rangeLimit) {
        Constrains.ContainedPair pair = contained(range);
        return pair.ships <= rangeLimit && pair.ships + pair.empty >= rangeLimit;
    }

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
