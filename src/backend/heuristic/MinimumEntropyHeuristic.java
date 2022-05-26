/**
 * @author Mateusz Jaracz
 */
package backend.heuristic;

import backend.boards.Board;
import backend.utility.Coord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Finds the fields with minimum entropy and returns the coordinates of one of the
 * minimum entropy field's
 *
 * @param <BoardValue> the boar's field type
 */
public class MinimumEntropyHeuristic<BoardValue extends Collection<?>> implements Heuristic<BoardValue> {

    /**
     * Returns number of cell's states. When state has only one state [it is collapsed]
     * returns the maximum value for the integer
     *
     * @param cell the cell's value
     * @return the number of cell's states
     */
    private int getLength(BoardValue cell) {
        return cell.size() > 1 ? cell.size() : Integer.MAX_VALUE;
    }

    /**
     * Returns the field coordinates with the minimum entropy
     *
     * @param lengths the numbers of states of the cells
     * @param dimensions the board dimension
     * @param minimum the minimum number of the states
     * @return the field coordinates with the minimum entropy
     */
    private List<Coord> lengthsArgmin(Integer[][] lengths, Coord dimensions, Integer minimum) {
        List<Coord> argmin = new ArrayList<>();
        for (int y = 0; y < dimensions.y(); ++y) {
            for (int x = 0; x < dimensions.x(); ++x) {
                if (lengths[y][x].equals(minimum)) {
                    argmin.add(new Coord(x, y));
                }
            }
        }
        return argmin;
    }

    /**
     * Returns the list of the coordinates of the cell's with the minimum entropy
     *
     * @param board the board object
     * @return the list of the coordinates of the cell's with the minimum entropy
     */
    private List<Coord> minimumEntropy(Board<BoardValue> board) {
        Integer[][] lengths = new Integer[board.getHeight()][board.getWidth()];
        Integer minimum = Integer.MAX_VALUE;
        for (int y = 0; y < board.getHeight(); ++y) {
            for (int x = 0; x < board.getWidth(); ++x) {
                lengths[y][x] = getLength(board.accessCell(new Coord(x, y)));
                minimum = minimum > lengths[y][x] ? lengths[y][x] : minimum;
            }
        }
        return lengthsArgmin(lengths, board.getDimensions(), minimum);
    }

    /**
     * Chooses the position of the field with the minimum entropy from the board
     *
     * @param board the board object
     * @return the chose filed
     */
    @Override
    public Coord choose(Board<BoardValue> board) {
        List<Coord> entropy = minimumEntropy(board);
        return entropy.get(87832744 % entropy.size());
    }

}
