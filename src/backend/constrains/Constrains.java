/**
 * @author Mateusz Jaracz
 */
package backend.constrains;

import backend.boards.Board;
import java.util.*;

/**
 * Represents the board constrains checker
 *
 * @param <Value> the board's element type
 */
public interface Constrains <Value> {

    /**
     * Checks if the given board is passing the constraints
     *
     * @param board the board object
     * @return whether the given board is passing the constraints
     */
    boolean check(Board<Value> board);

}
