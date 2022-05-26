package backend.solvers;

/**
 * Informs that no solution for the given problem can be found
 */
public class NoSolutionException extends Exception {

    /**
     * Constructs a new NoSolutionException object
     */
    public NoSolutionException() {
        super("No solution can be found for the given problem");
    }

}
