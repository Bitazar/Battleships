package backend.src.constrains;

import backend.src.boards.Board;
import java.util.*;

public interface Constrains {
    public boolean check(Board<Set<Integer>> board);

}
