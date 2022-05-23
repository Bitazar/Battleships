package backend.src.solvers;

import backend.src.boards.Board;
import backend.src.boards.BoardDTO;
import backend.src.constrains.Constrains;
import backend.src.states.States;
import backend.src.utility.Coord;
import backend.src.utility.InitValue;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WaveFunctionCollapse extends Solver {
    private final States                    states;
    private final Random                    rand = new Random();

    private final static Set<Integer>       neighborValues = Stream.of(-1, 0, 1).collect(Collectors.toCollection(HashSet::new));

    public WaveFunctionCollapse(Constrains softConstrains, Constrains hardConstrains, Map<Integer, Map<Coord, Set<Integer>>> constrains, States states) {
        super(softConstrains, hardConstrains, constrains);
        this.states = states;
    }

    public States getStates() {
        return states;
    }

    private Board<Set<Integer>> generateBoard(Board<Set<Integer>> board) {
        Set<Integer> initialSuperstate = constrains.keySet();
        for (int y = 0; y < board.getHeight(); ++y) {
            for (int x = 0; x < board.getWidth(); ++x) {
                board.generateCell(new Coord(x, y), new HashSet<>(initialSuperstate));
            }
        }
        return board;
    }

    private List<Coord> getNeighborhood(Board<Set<Integer>> board, Coord position) {
        List<Coord> neighborhood = new ArrayList<>();
        for (Integer u : neighborValues) {
            for (Integer v : neighborValues) {
                if (u != 0 || v != 0) {
                    Coord neighbor = new Coord(position.getX() + u, position.getY() + v);
                    if (board.onBoard(neighbor)) {
                        neighborhood.add(neighbor);
                    }
                }
            }
        }
        return neighborhood;
    }

    private void propagate(Board<Set<Integer>> board, Coord position, List<Coord> checked) {
        List<Coord> neighborhood = getNeighborhood(board, position);
        for (Coord neighbor : neighborhood) {
            if (!checked.contains(neighbor) && board.accessCell(neighbor).size() > 1) {
                Coord diff = new Coord(neighbor.getX() - position.getX(), neighbor.getY() - position.getY());
                Set<Integer> newStates = states.getStates(this, board, position, diff);
                if (!newStates.equals(board.accessCell(neighbor))) {
                    board.setValue(neighbor, newStates);
                    checked.add(neighbor);
                    propagate(board, neighbor, checked);
                }
            }
        }
    }

    private static int getLength(Set<Integer> cell) {
        return cell.size() > 1 ? cell.size() : Integer.MAX_VALUE;
    }

    private List<Coord> lengthsArgmin(Integer[][] lengths, Coord dimensions, Integer minimum) {
        List<Coord> argmin = new ArrayList<>();
        for (int y = 0; y < dimensions.getY(); ++y) {
            for (int x = 0; x < dimensions.getX(); ++x) {
                if (lengths[y][x].equals(minimum)) {
                    argmin.add(new Coord(x, y));
                }
            }
        }
        return argmin;
    }

    private List<Coord> minimumEntropy(Board<Set<Integer>> board) {
        Integer[][] lengths = new Integer[board.getHeight()][board.getWidth()];
        Integer minimum = Integer.MAX_VALUE;
        for (int y = 0; y < board.getHeight(); ++y) {
            for (int x = 0; x < board.getWidth(); ++x) {
                lengths[y][x] = getLength(board.accessCell(new Coord(x, y)));
                minimum = minimum > lengths[y][x] ? lengths[y][x] : minimum;
            }
        }
        return lengthsArgmin(lengths, board.getDimensions(), minimum);
    }

    private Coord choiceMinimumEntropy(Board<Set<Integer>> board) {
        List<Coord> entropy = minimumEntropy(board);
        return entropy.get(rand.nextInt(entropy.size()));
    }

    private boolean isCollapsed(Board<Set<Integer>> board) {
        for (Board.Row<Set<Integer>> row : board) {
            for (Set<Integer> cell : row) {
                if (cell.size() > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    private Board<Set<Integer>> checkConstrains(Board<Set<Integer>> board) {
        if (softConstrains.check(board)) {
            if (isCollapsed(board)) {
                return hardConstrains.check(board) ? board : null;
            }
            return collapse(board);
        }
        return null;
    }

    private Board<Set<Integer>> collapse(Board<Set<Integer>> board) {
        Coord position = choiceMinimumEntropy(board);
        List<Integer> superposition = new ArrayList<>(board.accessCell(position));
        Collections.shuffle(superposition);
        for (Integer state : superposition) {
            Board<Set<Integer>> tempBoard = board.clone();
            tempBoard.setValue(position, Collections.singleton(state));
            propagate(tempBoard, position, List.of(position));
            Board<Set<Integer>> result = checkConstrains(board);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private void preCollapse(Board<Set<Integer>> board, List<InitValue> initValueList) {
        for (InitValue initVal : initValueList) {
            board.setValue(initVal.getCoord(), Collections.singleton(initVal.getValue()));
            propagate(board, initVal.getCoord(), List.of(initVal.getCoord()));
        }
    }

    private Board<Integer> convertToCollapsed(Board<Set<Integer>> board) {
        Board<Integer> collapsed = new BoardDTO(board.getDimensions());
        for (int y = 0; y < board.getHeight(); ++y) {
            for (int x = 0; x < board.getWidth(); ++x) {
                Coord position = new Coord(x, y);
                collapsed.setValue(position, board.accessCell(position).iterator().next());
            }
        }
        return collapsed;
    }

    @Override
    public Board<Integer> solve(Board<Set<Integer>> emptyBoard, List<InitValue> initValueList) throws NoSolutionException {
        Board<Set<Integer>> board = generateBoard(emptyBoard);
        preCollapse(board, initValueList);
        board = collapse(board);
        if (board == null)
            throw new NoSolutionException();
        return convertToCollapsed(board);
    }

}
