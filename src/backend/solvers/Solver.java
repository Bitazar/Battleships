package backend.solvers;

import backend.boards.Board;;
import backend.constrains.Constrains;
import backend.utility.Coord;
import backend.utility.InitValue;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Solver {
    protected final Constrains                                softConstrains;
    protected final Constrains                                hardConstrains;
    protected final Map<Integer, Map<Coord, Set<Integer>>>    constrains;

    public Solver(Constrains softConstrains, Constrains hardConstrains, Map<Integer, Map<Coord, Set<Integer>>> constrains) {
        this.softConstrains = softConstrains;
        this.hardConstrains = hardConstrains;
        this.constrains = constrains;
    }

    public Constrains getSoftConstrains() {
        return softConstrains;
    }

    public Constrains getHardConstrains() {
        return hardConstrains;
    }

    public Map<Integer, Map<Coord, Set<Integer>>> getConstrains() {
        return constrains;
    }

    public abstract Board<Integer> solve(Board<Set<Integer>> emptyBoard, List<InitValue> initValueList) throws NoSolutionException;

}
