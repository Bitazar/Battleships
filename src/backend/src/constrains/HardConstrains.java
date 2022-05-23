package backend.src.constrains;

import backend.src.boards.Board;

import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class HardConstrains extends BattleshipsConstrains {

    public HardConstrains(List<Integer> rowLimits, List<Integer> columnLimits, TreeMap<Integer, Integer> shipLimits) {
        super(rowLimits, columnLimits, shipLimits);
    }

    @Override
    protected <Range extends Iterable<Set<Integer>>> boolean rangeConstrain(Range range, int rangeLimit) {
        BattleshipsConstrains.ContainedPair pair = contained(range);
        return pair.ships == rangeLimit && pair.empty == 0;
    }

    @Override
    public boolean shipLengthConstrain(Board<Set<Integer>> board) {
        return getShipLengths(board).equals(shipLimits);
    }

}
