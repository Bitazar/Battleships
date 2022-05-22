package backend.constrains;

import backend.boards.Board;

import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class HardConstrains extends Constrains {

    public HardConstrains(List<Integer> rowLimits, List<Integer> columnLimits, TreeMap<Integer, Integer> shipLimits) {
        super(rowLimits, columnLimits, shipLimits);
    }

    @Override
    protected <Range extends Iterable<Set<Integer>>> boolean rangeConstrain(Range range, int rangeLimit) {
        Constrains.ContainedPair pair = contained(range);
        return pair.ships == rangeLimit && pair.empty == rangeLimit;
    }

    @Override
    public boolean shipLengthConstrain(Board<Set<Integer>> board) {
        return getShipLengths(board).equals(shipLimits);
    }

}
