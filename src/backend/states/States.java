/**
 * @author Mateusz Jaracz
 */
package backend.states;

import backend.boards.Board;
import backend.solvers.Solver;
import backend.utility.Coord;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Manages the states of the board and theirs transformations
 *
 * @param <BoardValue> the board's field type
 * @param <StateValue> the board's state type
 */
public interface States <BoardValue extends Collection<?>, StateValue> {

    /**
     * Generates the board states from the states set
     *
     * @param superstate the board field superstate
     * @return the board states
     */
    BoardValue generateState(Set<StateValue> superstate);

    /**
     * Returns the list of states from the given board state
     *
     * @param boardStates  the board state
     * @return the list of states
     */
    List<StateValue> updateStates(BoardValue boardStates);

    /**
     * Collapses the given state to the board form
     *
     * @param state the collapsed state
     * @return the collapsed state in the board form
     */
    BoardValue collapseState(StateValue state);

    /**
     * Updates the value of the states in the given cell
     *
     * @param solver the solver object
     * @param board the board object
     * @param position the position of the cell
     * @param difference the difference between the cell's neighbor's position
     * and the cell position
     * @return the updated value of the states
     */
    BoardValue updateStates(Solver<BoardValue, StateValue> solver, Board<BoardValue> board, Coord position, Coord difference);

}
