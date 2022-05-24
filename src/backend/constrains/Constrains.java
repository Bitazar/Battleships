package backend.constrains;

import backend.boards.Board;
import java.util.*;

public interface Constrains {
    public boolean check(Board<Set<Integer>> board);

}
