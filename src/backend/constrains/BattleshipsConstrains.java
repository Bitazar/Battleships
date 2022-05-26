/**
 * @author Mateusz Jaracz
 */
package backend.constrains;

import backend.boards.Board;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Base class for the battleships board constraints
 *
 * @param <Value> the board's element type
 */
public abstract class BattleshipsConstrains <Value> implements Constrains<Value> {

    protected final List<Integer>                       rowLimits;
    protected final List<Integer>                       columnLimits;
    protected final TreeMap<Integer, Integer>           shipLimits;

    /**
     * Mutable pair used to pass data between rangeLoop method and getRangeShips method
     */
    protected static class ShipPair {
        public int                                      length = 0;
        public boolean                                  isShip = false;
    }

    /**
     * Constructs a new BattleshipsConstrains object
     *
     * @param rowLimits the row limits
     * @param columnLimits the column limits
     * @param shipLimits the ship limits
     */
    public BattleshipsConstrains(List<Integer> rowLimits, List<Integer> columnLimits, TreeMap<Integer, Integer> shipLimits) {
        this.rowLimits = rowLimits;
        this.columnLimits = columnLimits;
        this.shipLimits = adjustShipLimits(shipLimits);
    }

    /**
     * Adjusts the ship limits to the checking implementation
     *
     * @param shipLimits the ship limits
     * @return the adjusted ship limits
     */
    private TreeMap<Integer, Integer> adjustShipLimits(TreeMap<Integer, Integer> shipLimits) {
        TreeMap<Integer, Integer> adjustedShipLimits = new TreeMap<>();
        int size = 0;
        for (Integer key : shipLimits.descendingKeySet()) {
            Integer value = shipLimits.get(key);
            size += key * value;
            adjustedShipLimits.put(key, value);
        }
        adjustedShipLimits.put(1, adjustedShipLimits.getOrDefault(1, 0) + size);
        return adjustedShipLimits;
    }

    /**
     * Checks the limit constrain on the given range
     *
     * @param range the range object
     * @param rangeLimit the range limits
     * @return whether the limit constrain is satisfied
     * @param <Range> the range type
     */
    protected abstract <Range extends Iterable<Value>> boolean rangeConstrain(Range range, int rangeLimit);

    /**
     * Checks the status of constrains on the board's rows
     *
     * @param board the board object
     * @return the status of constrains on the board's rows
     */
    private boolean rowConstrainsStatus(Board<Value> board) {
        var rowItr = rowLimits.iterator();
        for (var row : board) {
            if (!rangeConstrain(row, rowItr.next())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks the status of constrains on the board's columns
     *
     * @param board the board object
     * @return the status of constrains on the board's columns
     */
    private boolean columnConstrainsStatus(Board<Value> board) {
        var colItr = columnLimits.iterator();
        for (var column : board.transpose()) {
            if (!rangeConstrain(column, colItr.next())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks the status of the constraints of the board
     *
     * @param board the board object
     * @return the status of the constraints of the board
     */
    public boolean boardConstrain(Board<Value> board) {
        return rowConstrainsStatus(board) && columnConstrainsStatus(board);
    }

    /**
     * Performs the one iteration of the getRangeShips method
     *
     * @param states the current states of the method
     * @param cell the cell value
     * @param ships the ships lengths
     */
    protected abstract void rangeShipsAutomate(ShipPair states, Value cell, TreeMap<Integer, Integer> ships);

    /**
     * Returns the lengths of the ships lying on the given range
     *
     * @param range the range object
     * @param ships the ships lengths
     * @param <Range> the range type
     */
    private <Range extends Iterable<Value>> void getRangeShips(Range range, TreeMap<Integer, Integer> ships) {
        ShipPair pair = new ShipPair();
        for (Value cell : range) {
            rangeShipsAutomate(pair, cell, ships);
        }
        if (pair.isShip) {
            ships.put(pair.length, ships.getOrDefault(pair.length, 0) + 1);
        }
    }

    /**
     * Returns the lengths of the ships on the board
     *
     * @param board the board object
     * @return the lengths of the ships on the board
     */
    protected Map<Integer, Integer> getShipLengths(Board<Value> board) {
        TreeMap<Integer, Integer> ships = new TreeMap<>();
        for (var row : board) {
            getRangeShips(row, ships);
        }
        for (var column : board.transpose()) {
            getRangeShips(column, ships);
        }
        return ships;
    }

    /**
     * Checks if the ship length constrain is being satisfied
     *
     * @param board the board object
     * @return whether the ship length constrain is being satisfied
     */
    public abstract boolean shipLengthConstrain(Board<Value> board);

    /**
     * Checks if all constrains are being satisfied
     *
     * @param board the board object
     * @return whether all constrains are being satisfied
     */
    public boolean check(Board<Value> board) {
        return boardConstrain(board) && shipLengthConstrain(board);
    }

}
