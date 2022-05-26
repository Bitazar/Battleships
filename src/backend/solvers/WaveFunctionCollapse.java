package backend.solvers;

import backend.boards.Board;
import backend.constrains.Constrains;
import backend.heuristic.Heuristic;
import backend.states.States;
import backend.utility.Coord;
import backend.utility.InitValue;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Solves the board problem using the wave function collapse algorithm. Adapted to
 * the integer problems only
 *
 * @param <BoardValue> the board's field type
 * @param <StateValue> the board's state type
 */
public class WaveFunctionCollapse<BoardValue extends Collection<?>, StateValue> extends Solver<BoardValue, StateValue> {
    private final States<BoardValue, StateValue>                    states;
    private final Heuristic<BoardValue>                             heuristic;

    private final static Set<Integer>                               neighborValues = Stream.of(-1, 0, 1).collect(Collectors.toCollection(HashSet::new));

    /**
     * Creates a new WaveFunctionCollapse object
     *
     * @param softConstrains the soft constraints
     * @param hardConstrains the hard constraints
     * @param constrains the board's states constraints
     * @param states the state choosing functor
     */
    public WaveFunctionCollapse(Constrains<BoardValue> softConstrains, Constrains<BoardValue> hardConstrains, Map<StateValue, Map<Coord, BoardValue>> constrains, States<BoardValue, StateValue> states, Heuristic<BoardValue> heuristic) {
        super(softConstrains, hardConstrains, constrains);
        this.states = states;
        this.heuristic = heuristic;
    }

    /**
     * Returns the board's states constraints
     *
     * @return the board's states constraints
     */
    public States<BoardValue, StateValue> getStates() {
        return states;
    }

    /**
     * Fills the board with the superposition states
     *
     * @param board the board object
     */
    private void generateBoard(Board<BoardValue> board) {
        Set<StateValue> initialSuperstate = constrains.keySet();
        for (int y = 0; y < board.getHeight(); ++y) {
            for (int x = 0; x < board.getWidth(); ++x) {
                board.generateCell(new Coord(x, y), states.generateState(initialSuperstate));
            }
        }
    }

    /**
     * Returns the neighborhood of the given cell
     *
     * @param board the board object
     * @param position the cell's position
     * @return the list of the cell's neighbors
     */
    private List<Coord> getNeighborhood(Board<BoardValue> board, Coord position) {
        List<Coord> neighborhood = new ArrayList<>();
        for (Integer u : neighborValues) {
            for (Integer v : neighborValues) {
                if (u != 0 || v != 0) {
                    Coord neighbor = new Coord(position.x() + u, position.y() + v);
                    if (board.onBoard(neighbor)) {
                        neighborhood.add(neighbor);
                    }
                }
            }
        }
        return neighborhood;
    }

    /**
     * Propagates the collapse of the state forward
     *
     * @param board the board object
     * @param position the checked cell's position
     * @param checked the list of the positions of the checked cell's
     */
    private void propagate(Board<BoardValue> board, Coord position, List<Coord> checked) {
        List<Coord> neighborhood = getNeighborhood(board, position);
        for (Coord neighbor : neighborhood) {
            if (!checked.contains(neighbor) && board.accessCell(neighbor).size() > 1) {
                Coord diff = new Coord(neighbor.x() - position.x(), neighbor.y() - position.y());
                BoardValue newStates = states.updateStates(this, board, position, diff);
                if (!newStates.equals(board.accessCell(neighbor))) {
                    board.setValue(neighbor, newStates);
                    checked.add(neighbor);
                    propagate(board, neighbor, checked);
                }
            }
        }
    }

    /**
     * Checks if the board is already collapsed
     *
     * @param board the board object
     * @return whether the board is collapsed
     */
    private boolean isCollapsed(Board<BoardValue> board) {
        for (var row : board) {
            for (var cell : row) {
                if (cell.size() > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks the board constrains. If board is valid then returns it. When not then
     * returns null
     *
     * @param board the board object
     * @return the valid board
     */
    private Board<BoardValue> checkConstrains(Board<BoardValue> board) {
        if (softConstrains.check(board)) {
            if (isCollapsed(board)) {
                return hardConstrains.check(board) ? board : null;
            }
            return collapse(board);
        }
        return null;
    }

    /**
     * Collapses one of the states of the board. If board cannot be collapsed further, and it
     * is invalid then returns null
     *
     * @param board the board object
     * @return the collapsed board
     */
    private Board<BoardValue> collapse(Board<BoardValue> board) {
        Coord position = heuristic.choose(board);
        List<StateValue> superposition = states.updateStates(board.accessCell(position));
        Collections.shuffle(superposition);
        for (StateValue state : superposition) {
            Board<BoardValue> tempBoard = board.clone();
            tempBoard.setValue(position, states.collapseState(state));
            propagate(tempBoard, position, new ArrayList<>(List.of(position)));
            Board<BoardValue> result = checkConstrains(tempBoard);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * Prepares the board for the main collapsing process
     *
     * @param board the board object
     * @param initValueList the initial values of the board
     */
    private void preCollapse(Board<BoardValue> board, List<InitValue<StateValue>> initValueList) {
        for (var initVal : initValueList) {
            board.setValue(initVal.coord(), states.collapseState(initVal.value()));
            propagate(board, initVal.coord(), new ArrayList<>(List.of(initVal.coord())));
        }
    }

    /**
     * Converts the board to the collapsed one
     *
     * @param board the board object
     * @param collapsed the empty collapsed board
     * @return the converted and collapsed board
     */
    private Board<StateValue> convertToCollapsed(Board<BoardValue> board, Board<StateValue> collapsed) {
        for (int y = 0; y < board.getHeight(); ++y) {
            for (int x = 0; x < board.getWidth(); ++x) {
                Coord position = new Coord(x, y);
                collapsed.setValue(position, states.updateStates(board.accessCell(position)).iterator().next());
            }
        }
        return collapsed;
    }

    /**
     * Solves the given board using the wave function collapse algorithm
     *
     * @param board the empty board
     * @param collapsedBoard the solved board
     * @param initValueList the initial value list for the problem
     * @return the solved board
     * @throws NoSolutionException if no solution can be found
     */
    @Override
    public Board<StateValue> solve(Board<BoardValue> board, Board<StateValue> collapsedBoard, List<InitValue<StateValue>> initValueList) throws NoSolutionException {
        generateBoard(board);
        preCollapse(board, initValueList);
        board = collapse(board);
        if (board == null)
            throw new NoSolutionException();
        return convertToCollapsed(board, collapsedBoard);
    }

}
