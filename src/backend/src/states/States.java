package backend.src.states;

import backend.src.boards.Board;
import backend.src.solvers.Solver;
import backend.src.utility.Coord;

import java.util.Set;

public interface States {

    Set<Integer> getStates(Solver solver, Board<Set<Integer>> board, Coord position, Coord difference);

}
