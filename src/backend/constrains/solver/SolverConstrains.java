/**
 * @author Mateusz Jaracz
 */
package backend.constrains.solver;

import backend.boards.Board;

import java.util.*;

/**
 * Base class for the solver related battleships board constrains
 */
public abstract class SolverConstrains extends backend.constrains.BattleshipsConstrains<Set<Integer>> {

    /**
     * Stores information how many ships and how many empty fields are in the given range
     */
    protected static class ContainedPair {
        public int                                      ships = 0;
        public int                                      empty = 0;
    }

    /**
     * Constructs a new SolverConstrains object
     *
     * @param rowLimits the row limits
     * @param columnLimits the column limits
     * @param shipLimits the ship limits
     */
    public SolverConstrains(List<Integer> rowLimits, List<Integer> columnLimits, TreeMap<Integer, Integer> shipLimits) {
        super(rowLimits, columnLimits, shipLimits);
    }

    /**
     * Returns how many ships and how many empty fields are in the given range
     *
     * @param range the range object
     * @return how many ships and how many empty fields are in the given range
     * @param <Range> the range type
     */
    protected <Range extends Iterable<Set<Integer>>> ContainedPair contained(Range range) {
        ContainedPair pair = new ContainedPair();
        for (Set<Integer> cellValue : range) {
            int value = cellValue.contains(2) ? 1 : 0;
            if (cellValue.size() == 1) {
                pair.ships += value;
            } else {
                pair.empty += value;
            }
        }
        return pair;
    }

    /**
     * Checks the limit constrain on the given range
     *
     * @param range the range object
     * @param rangeLimit the range limits
     * @return whether the limit constrain is satisfied
     * @param <Range> the range type
     */
    protected abstract <Range extends Iterable<Set<Integer>>> boolean rangeConstrain(Range range, int rangeLimit);

    /**
     * Performs the one iteration of the getRangeShips method
     *
     * @param states the current states of the method
     * @param cell the cell value
     * @param ships the ships lengths
     */
    @Override
    protected void rangeShipsAutomate(ShipPair states, Set<Integer> cell, TreeMap<Integer, Integer> ships) {
        if (cell.size() == 1 && cell.contains(2)) {
            if (states.isShip) {
                states.length += 1;
            } else {
                states.isShip = true;
                states.length = 1;
            }
        } else if (states.isShip) {
            states.isShip = false;
            ships.put(states.length, ships.getOrDefault(states.length, 0) + 1);
        }
    }

    /**
     * Checks if the ship length constrain is being satisfied
     *
     * @param board the board object
     * @return whether the ship length constrain is being satisfied
     */
    public abstract boolean shipLengthConstrain(Board<Set<Integer>> board);

}
