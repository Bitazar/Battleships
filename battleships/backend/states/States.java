package backend.states;

import backend.boards.Board;
import backend.solvers.Solver;
import backend.utility.Coord;

import java.util.Set;

public interface States {

    Set<Integer> getStates(Solver solver, Board<Set<Integer>> board, Coord position, Coord difference);

}
