package backend.src.states;

import backend.src.boards.Board;
import backend.src.solvers.Solver;
import backend.src.utility.Coord;

import java.util.HashSet;
import java.util.Set;

public class BattleshipsStates implements States {

    @Override
    public Set<Integer> getStates(Solver solver, Board<Set<Integer>> board, Coord position, Coord difference) {
        Coord neighbor = new Coord(position.getX() + difference.getX(), position.getY() + difference.getY());
        Set<Integer> states = new HashSet<>(board.accessCell(neighbor));
        for (Integer state : board.accessCell(position)) {
            var value = solver.getConstrains().get(state).get(difference);
            states.retainAll(value);
        }
        return states;
    }

}
