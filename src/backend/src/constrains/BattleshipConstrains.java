package backend.src.constrains;

import backend.src.boards.Board;
import backend.src.boards.GeneratorBoard;
import backend.src.utility.Coord;

import java.util.*;

public abstract class BattleshipConstrains implements Constrains {

    protected final List<Integer> rowLimits;
    protected final List<Integer>                     columnLimits;
    protected final TreeMap<Integer, Integer> shipLimits;

    protected static class ContainedPair {
        public int                              ships = 0;
        public int                              empty = 0;
    }

    public BattleshipConstrains(List<Integer> rowLimits, List<Integer> columnLimits, TreeMap<Integer, Integer> shipLimits) {
        this.rowLimits = rowLimits;
        this.columnLimits = columnLimits;
        this.shipLimits = shipLimits;
    }

    protected <Range extends Iterable<Set<Integer>>> BattleshipConstrains.ContainedPair contained(Range row) {
        BattleshipConstrains.ContainedPair pair = new BattleshipConstrains.ContainedPair();
        for (Set<Integer> cellValue : row) {
            int value = cellValue.contains(2) || cellValue.contains(3) ? 1 : 0;
            if (cellValue.size() == 1) {
                pair.ships += value;
            } else {
                pair.empty += value;
            }
        }
        return pair;
    }

    protected abstract <Range extends Iterable<Set<Integer>>> boolean rangeConstrain(Range range, int rangeLimit);

    private boolean rowConstrainsStatus(Board<Set<Integer>> board) {
        var rowIter = rowLimits.iterator();
        for (Board.Row<Set<Integer>> row : board) {
            if (!rangeConstrain(row, rowIter.next())) {
                return false;
            }
        }
        return true;
    }

    private boolean columnConstrainsStatus(Board<Set<Integer>> board) {
        var colIter = columnLimits.iterator();
        for (Board.Column<Set<Integer>> column : board.transpose()) {
            if (!rangeConstrain(column, colIter.next())) {
                return false;
            }
        }
        return true;
    }

    public boolean boardConstrain(Board<Set<Integer>> board) {
        return rowConstrainsStatus(board) && columnConstrainsStatus(board);
    }

    private Set<List<Coord>> getBoardShips(Board<Set<Integer>> board) {
        Set<List<Coord>> ships = new HashSet<>();
        for (Board.Row<Set<Integer>> row : board) {
            for (int i = 0; i < row.getSize(); ++i) {
                GeneratorBoard.Row genRow = (GeneratorBoard.Row) row;
                List<Coord> ship = genRow.getShip(i);
                if (ship != null) {
                    ships.add(ship);
                }
            }
        }
        return ships;
    }

    protected Map<Integer, Integer> getShipLengths(Board<Set<Integer>> board) {
        Set<List<Coord>> ships = getBoardShips(board);
        Map<Integer, Integer> shipLengths = new HashMap<>();
        for (List<Coord> ship : ships) {
            if (!shipLengths.containsKey(ship.size())) {
                shipLengths.put(ship.size(), 1);
            } else {
                shipLengths.replace(ship.size(), shipLengths.get(ship.size()) + 1);
            }
        }
        return shipLengths;
    }

    public abstract boolean shipLengthConstrain(Board<Set<Integer>> board);

    public boolean check(Board<Set<Integer>> board) {
        return boardConstrain(board) && shipLengthConstrain(board);
    }

}
