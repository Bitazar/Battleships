package backend.states;

import backend.boards.Board;
import backend.solvers.Solver;
import backend.utility.Coord;

import java.util.Set;

public class BattleshipStates implements States {

    @Override
    public Set<Integer> getStates(Solver solver, Board<Set<Integer>> board, Coord position, Coord difference) {
        Coord neighbor = new Coord(position.x + difference.x, position.y + difference.y);
        Set<Integer> states = board.accessCell(neighbor);
        for (Integer state : board.accessCell(position)) {
            states.retainAll(solver.getConstrains().get(state).get(difference));
        }
        return states;
    }

}
