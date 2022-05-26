/**
 * @author Mateusz Jaracz
 */
package backend.facades;

import backend.utility.Coord;
import backend.utility.InitValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 0 -> nic
// 1 -> woda
// 2 -> nieskierowany
// 3 -> kraniec skierowany w lewo
// 4 -> kraniec skierowany w górę
// 5 -> kraniec skierowany w prawo
// 6 -> kraniec skierowany w dół
// 7 -> samotna łódź

/**
 * Preprocesses the given vectorized values into the valid initial list without
 * vectorized elements
 */
public class BattleshipsPreprocessor {
    private final Coord                         dimensions;

    /**
     * Constructs a new BattleshipsPreprocessor object
     *
     * @param dimensions the board dimensions
     */
    public BattleshipsPreprocessor(Coord dimensions) {
        this.dimensions = dimensions;
    }

    /**
     * Checks if the given cell's position is not on the board
     *
     * @param position the cell's position
     * @return whether the given cell's position is not on the board
     */
    private boolean notOnBoard(Coord position) {
        return position.x() < 0 || position.x() >= dimensions.x() || position.y() < 0 || position.y() >= dimensions.y();
    }

    /**
     * Devectorizes the left case
     *
     * @param position the cell's position
     * @return the list of the devectorized cell
     */
    private List<InitValue<Integer>> leftVectorized(Coord position) {
        int x = position.x(), y = position.y();
        List<InitValue<Integer>> neighborhood = new ArrayList<>(Arrays.asList(
            new InitValue<>(new Coord(x, y - 1), 1),
            new InitValue<>(new Coord(x + 1, y), 1),
            new InitValue<>(new Coord(x, y + 1), 1),
            new InitValue<>(new Coord(x - 1, y), 2)
        ));
        neighborhood.removeIf(c-> notOnBoard(c.coord()));
        return neighborhood;
    }

    /**
     * Devectorizes the up case
     *
     * @param position the cell's position
     * @return the list of the devectorized cell
     */
    private List<InitValue<Integer>> upVectorized(Coord position) {
        int x = position.x(), y = position.y();
        List<InitValue<Integer>> neighborhood = new ArrayList<>(Arrays.asList(
                new InitValue<>(new Coord(x - 1, y), 1),
                new InitValue<>(new Coord(x + 1, y), 1),
                new InitValue<>(new Coord(x, y + 1), 1),
                new InitValue<>(new Coord(x, y - 1), 2)
        ));
        neighborhood.removeIf(c-> notOnBoard(c.coord()));
        return neighborhood;
    }

    /**
     * Devectorizes the right case
     *
     * @param position the cell's position
     * @return the list of the devectorized cell
     */
    private List<InitValue<Integer>> rightVectorized(Coord position) {
        int x = position.x(), y = position.y();
        List<InitValue<Integer>> neighborhood = new ArrayList<>(Arrays.asList(
                new InitValue<>(new Coord(x, y - 1), 1),
                new InitValue<>(new Coord(x - 1, y), 1),
                new InitValue<>(new Coord(x, y + 1), 1),
                new InitValue<>(new Coord(x + 1, y), 2)
        ));
        neighborhood.removeIf(c-> notOnBoard(c.coord()));
        return neighborhood;
    }

    /**
     * Devectorizes the down case
     *
     * @param position the cell's position
     * @return the list of the devectorized cell
     */
    private List<InitValue<Integer>> downVectorized(Coord position) {
        int x = position.x(), y = position.y();
        List<InitValue<Integer>> neighborhood = new ArrayList<>(Arrays.asList(
                new InitValue<>(new Coord(x, y - 1), 1),
                new InitValue<>(new Coord(x - 1, y), 1),
                new InitValue<>(new Coord(x + 1, y), 1),
                new InitValue<>(new Coord(x, y + 1), 2)
        ));
        neighborhood.removeIf(c-> notOnBoard(c.coord()));
        return neighborhood;
    }

    /**
     * Devectorizes the single case
     *
     * @param position the cell's position
     * @return the list of the devectorized cell
     */
    private List<InitValue<Integer>> singleVectorized(Coord position) {
        int x = position.x(), y = position.y();
        List<InitValue<Integer>> neighborhood = new ArrayList<>(Arrays.asList(
                new InitValue<>(new Coord(x, y - 1), 1),
                new InitValue<>(new Coord(x - 1, y), 1),
                new InitValue<>(new Coord(x + 1, y), 1),
                new InitValue<>(new Coord(x, y + 1), 1)
        ));
        neighborhood.removeIf(c-> notOnBoard(c.coord()));
        return neighborhood;
    }

    /**
     * Devectorizes the given cell
     *
     * @param position the cell's position
     * @param value the cell's value
     * @return the list with devectorized initial values
     */
    private List<InitValue<Integer>> devectorize(Coord position, Integer value) {
        return switch (value) {
            case 3 -> leftVectorized(position);
            case 4 -> upVectorized(position);
            case 5 -> rightVectorized(position);
            case 6 -> downVectorized(position);
            case 7 -> singleVectorized(position);
            default -> new ArrayList<>();
        };
    }

    /**
     * Preprocesses the given initial values into the representation without vectorized
     * elements
     *
     * @param initValueList the list of initial values
     * @return the devectorizes list of initial values
     */
    public List<InitValue<Integer>> preprocess(List<InitValue<Integer>> initValueList) {
        List<InitValue<Integer>> extendedValues = new ArrayList<>();
        for (InitValue<Integer> value : initValueList) {
            if (value.value() > 2) {
                extendedValues.add(new InitValue<>(value.coord(), 2));
                extendedValues.addAll(devectorize(value.coord(), value.value()));
            } else {
                extendedValues.add(value);
            }
        }
        return extendedValues;
    }
}
