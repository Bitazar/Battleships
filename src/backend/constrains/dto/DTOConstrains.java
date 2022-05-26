/**
 * @author Mateusz Jaracz
 */
package backend.constrains.dto;

import backend.boards.Board;
import backend.utility.Coord;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class DTOConstrains extends backend.constrains.BattleshipsConstrains<Integer> {

    private final Map<Integer, Map<Coord, Set<Integer>>>        vectorizedFieldsConstrains;
    private final static Set<Integer>                           neighborValues = Stream.of(-1, 0, 1).collect(Collectors.toCollection(HashSet::new));

    /**
     * Stores information how many ships and how many empty fields are in the given range
     */
    protected static class ContainedPair {
        public int                                              ships = 0;
        public int                                              empty = 0;
    }

    /**
     * Constructs a new DTOConstrains object
     *
     * @param rowLimits the row limits
     * @param columnLimits the column limits
     * @param shipLimits the ship limits
     */

    public DTOConstrains(List<Integer> rowLimits, List<Integer> columnLimits, TreeMap<Integer, Integer> shipLimits, Map<Integer, Map<Coord, Set<Integer>>> vectorizedFieldsContains) {
        super(rowLimits, columnLimits, shipLimits);
        this.vectorizedFieldsConstrains = vectorizedFieldsContains;
    }

    /**
     * Returns how many ships and how many empty fields are in the given range
     *
     * @param range the range object
     * @return how many ships and how many empty fields are in the given range
     * @param <Range> the range type
     */
    protected <Range extends Iterable<Integer>> ContainedPair contained(Range range) {
        ContainedPair pair = new ContainedPair();
        for (Integer cellValue : range) {
            if (cellValue == 0) {
                pair.empty += 1;
            } else {
                pair.ships += cellValue > 1 ? 1 : 0;
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
    protected abstract <Range extends Iterable<Integer>> boolean rangeConstrain(Range range, int rangeLimit);

    /**
     * Performs the one iteration of the getRangeShips method
     *
     * @param states the current states of the method
     * @param cell the cell value
     * @param ships the ships lengths
     */
    @Override
    protected void rangeShipsAutomate(ShipPair states, Integer cell, TreeMap<Integer, Integer> ships) {
        if (cell > 1) {
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
    public abstract boolean shipLengthConstrain(Board<Integer> board);

    /**
     * Checks if the vectorized constrain is being satisfied
     *
     * @param board the board object
     * @param position the position of the cell
     * @param neighbor the position of the cell's neighbor
     * @param diff the difference between cell's position and neighbor's position
     * @return whether the vectorized constrain is being satisfied
     */
    private boolean checkVectorizedConstrain(Board<Integer> board, Coord position, Coord neighbor, Coord diff) {
        return vectorizedFieldsConstrains.get(board.accessCell(position)).get(diff).contains(board.accessCell(neighbor));
    }

    /**
     * Checks if the local vectorized constrains are being satisfied
     *
     * @param board the board object
     * @param position the position of the cell
     * @return whether the local vectorized constrains are being satisfied
     */
    private boolean checkLocalVectorizedConstrains(Board<Integer> board, Coord position) {
        for (Integer u : neighborValues) {
            for (Integer v : neighborValues) {
                if (u != 0 || v != 0) {
                    Coord neighbor = new Coord(position.x() + u, position.y() + v);
                    if (board.onBoard(neighbor) && !checkVectorizedConstrain(board, position, neighbor, new Coord(u, v))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Checks if the vectorized fields constraints are being satisfied
     *
     * @param board the board object
     * @return whether the vectorized fields constraints are being satisfied
     */
    public boolean vectorizedFieldsConstrain(Board<Integer> board) {
        for (int y = 0; y < board.getHeight(); ++y) {
            for (int x = 0; x < board.getWidth(); ++x) {
                Coord position = new Coord(x, y);
                if (board.accessCell(position) > 2 && !checkLocalVectorizedConstrains(board, position)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if all constrains are being satisfied
     *
     * @param board the board object
     * @return whether all constrains are being satisfied
     */
    @Override
    public boolean check(Board<Integer> board) {
        return super.check(board) && vectorizedFieldsConstrain(board);
    }

}
