package backend.solvers;

import backend.boards.BoardDTO;
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

    public abstract BoardDTO solve(Coord dimensions, List<InitValue> initValueList);

}
