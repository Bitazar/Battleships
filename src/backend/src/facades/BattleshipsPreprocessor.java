package backend.src.facades;

import backend.src.utility.Coord;
import backend.src.utility.InitValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//3 -> kraniec skierowany w lewo
//4 -> kraniec skierowany w górę
//5 -> kraniec skierowany w prawo
//6 -> kraniec skierowany w dół
//7 -> samotna łódz

public class BattleshipsPreprocessor {
    private final Coord                         dimensions;

    public BattleshipsPreprocessor(Coord dimensions) {
        this.dimensions = dimensions;
    }

    private boolean onBoard(Coord position) {
        return position.getX() >= 0 && position.getX() < dimensions.getX() && position.getY() >= 0 && position.getY() < dimensions.getY();
    }

    private List<InitValue> leftVectorized(Coord position) {
        int x = position.getX(), y = position.getY();
        List<InitValue> neighborhood = new ArrayList<>(Arrays.asList(
            new InitValue(new Coord(x, y - 1), 1),
            new InitValue(new Coord(x + 1, y), 1),
            new InitValue(new Coord(x, y + 1), 1),
            new InitValue(new Coord(x - 1, y), 2)
        ));
        neighborhood.removeIf(c-> !onBoard(c.getCoord()));
        return neighborhood;
    }

    private List<InitValue> upVectorized(Coord position) {
        int x = position.getX(), y = position.getY();
        List<InitValue> neighborhood = new ArrayList<>(Arrays.asList(
                new InitValue(new Coord(x - 1, y), 1),
                new InitValue(new Coord(x + 1, y), 1),
                new InitValue(new Coord(x, y + 1), 1),
                new InitValue(new Coord(x, y - 1), 2)
        ));
        neighborhood.removeIf(c-> !onBoard(c.getCoord()));
        return neighborhood;
    }

    private List<InitValue> rightVectorized(Coord position) {
        int x = position.getX(), y = position.getY();
        List<InitValue> neighborhood = new ArrayList<>(Arrays.asList(
                new InitValue(new Coord(x, y - 1), 1),
                new InitValue(new Coord(x - 1, y), 1),
                new InitValue(new Coord(x, y + 1), 1),
                new InitValue(new Coord(x + 1, y), 2)
        ));
        neighborhood.removeIf(c-> !onBoard(c.getCoord()));
        return neighborhood;
    }

    private List<InitValue> downVectorized(Coord position) {
        int x = position.getX(), y = position.getY();
        List<InitValue> neighborhood = new ArrayList<>(Arrays.asList(
                new InitValue(new Coord(x, y - 1), 1),
                new InitValue(new Coord(x - 1, y), 1),
                new InitValue(new Coord(x + 1, y), 1),
                new InitValue(new Coord(x, y + 1), 2)
        ));
        neighborhood.removeIf(c-> !onBoard(c.getCoord()));
        return neighborhood;
    }

    private List<InitValue> singleVectorized(Coord position) {
        int x = position.getX(), y = position.getY();
        List<InitValue> neighborhood = new ArrayList<>(Arrays.asList(
                new InitValue(new Coord(x, y - 1), 1),
                new InitValue(new Coord(x - 1, y), 1),
                new InitValue(new Coord(x + 1, y), 1),
                new InitValue(new Coord(x, y + 1), 1)
        ));
        neighborhood.removeIf(c-> !onBoard(c.getCoord()));
        return neighborhood;
    }

    private List<InitValue> vectorized(Coord position, Integer value) {
        return switch (value) {
            case 3 -> leftVectorized(position);
            case 4 -> upVectorized(position);
            case 5 -> rightVectorized(position);
            case 6 -> downVectorized(position);
            case 7 -> singleVectorized(position);
            default -> new ArrayList<>();
        };
    }

    public List<InitValue> preprocess(List<InitValue> initValueList) {
        List<InitValue> extendedValues = new ArrayList<>();
        for (InitValue value : initValueList) {
            if (value.getValue() > 2) {
                extendedValues.add(new InitValue(value.getCoord(), 2));
                extendedValues.addAll(vectorized(value.getCoord(), value.getValue()));
            } else {
                extendedValues.add(value);
            }
        }
        return extendedValues;
    }
}
