package backend.src.solvers;

public class NoSolutionException extends Exception {

    public NoSolutionException() {
        super("No solution can be found for the given problem");
    }

}
