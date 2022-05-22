package backend.constrains;

import backend.boards.Board;
import backend.boards.GeneratorBoard;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Constrains {
    private final List<Integer>                       rowLimits;
    private final List<Integer>                       columnLimits;
    private final Map<Integer, Integer>               shipLimits;

    private final static Set<Integer>                 shipFieldVals = Stream.of(2, 3).collect(Collectors.toCollection(HashSet::new));

    protected static class ContainedPair {
        public int                              ships = 0;
        public int                              empty = 0;
    }

    public Constrains(List<Integer> rowLimits, List<Integer> columnLimits, Map<Integer, Integer> shipLimits) {
        this.rowLimits = rowLimits;
        this.columnLimits = columnLimits;
        this.shipLimits = shipLimits;
    }

    protected ContainedPair contained(GeneratorBoard.Cell[] row) {
        ContainedPair pair = new ContainedPair();
        for (GeneratorBoard.Cell cell : row) {
            int value = shipFieldVals.stream()
                    .filter(cell.value::contains)
                    .collect(Collectors.toSet()).size() > 0 ? 1 : 0;
            if (cell.value.size() == 1) {
                pair.ships += value;
            } else {
                pair.empty += value;
            }
        }
        return pair;
    }

    protected abstract boolean rowConstrain(GeneratorBoard.Cell[] row, int rowLimit);

    public boolean boardConstrain(Board<Set<Integer>> board) {
        GeneratorBoard genBoard = (GeneratorBoard) board;

    }
}
