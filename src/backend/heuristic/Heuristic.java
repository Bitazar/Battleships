/**
 * @author Mateusz Jaracz
 */
package backend.heuristic;

import backend.boards.Board;
import backend.utility.Coord;

import java.util.Collection;

/**
 * Represents the heuristic using to choose field from the board
 *
 * @param <BoardValue> the board's field type
 */
public interface Heuristic <BoardValue extends Collection<?>> {

    /**
     * Chooses the value from the board
     *
     * @param board the board object
     * @return the chose filed
     */
    Coord choose(Board<BoardValue> board);

}
