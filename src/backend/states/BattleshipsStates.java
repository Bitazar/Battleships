/**
 * @author Mateusz Jaracz
 */
package backend.states;

import backend.boards.Board;
import backend.solvers.Solver;
import backend.utility.Coord;

import java.util.*;

/**
 * Manages the states of the Battleships board and their transforms
 */
public class BattleshipsStates implements States<Set<Integer>, Integer> {

    /**
     * Generates the BoardValue states from the StateValue set
     *
     * @param superstate the board field superstate
     * @return the BoardValue states
     */
    @Override
    public Set<Integer> generateState(Set<Integer> superstate) {
        return new HashSet<>(superstate);
    }

    /**
     * Returns the list of states from the given board state
     *
     * @param boardStates  the board state
     * @return the list of states
     */
    @Override
    public List<Integer> updateStates(Set<Integer> boardStates) {
        return new ArrayList<>(boardStates);
    }

    /**
     * Collapses the given state to the board form
     *
     * @param state the collapsed state
     * @return the collapsed state in the board form
     */
    @Override
    public Set<Integer> collapseState(Integer state) {
        return new HashSet<>(Collections.singleton(state));
    }

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
    @Override
    public Set<Integer> updateStates(Solver<Set<Integer>, Integer> solver, Board<Set<Integer>> board, Coord position, Coord difference) {
        Coord neighbor = new Coord(position.x() + difference.x(), position.y() + difference.y());
        Set<Integer> states = new HashSet<>(board.accessCell(neighbor));
        for (Integer state : board.accessCell(position)) {
            var value = solver.getConstrains().get(state).get(difference);
            states.retainAll(value);
        }
        return states;
    }

}
