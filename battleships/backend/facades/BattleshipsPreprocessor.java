package backend.facades;

import backend.utility.Coord;
import backend.utility.InitValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BattleshipsPreprocessor {
    private final Coord                         dimensions;

    public BattleshipsPreprocessor(Coord dimensions) {
        this.dimensions = dimensions;
    }

    private boolean onBoard(Coord position) {
        return position.x >= 0 && position.x < dimensions.x && position.y >= 0 && position.y < dimensions.y;
    }

    private List<InitValue> leftVectorized(Coord position) {
        int x = position.x, y = position.y;
        List<InitValue> neighborhood = Arrays.asList(
            new InitValue(new Coord(x, y - 1), 1),
            new InitValue(new Coord(x + 1, y), 1),
            new InitValue(new Coord(x, y + 1), 1),
            new InitValue(new Coord(x - 1, y), 2)
        );
        neighborhood.removeIf(c-> !onBoard(c.getCoord()));
        return neighborhood;
    }

    private List<InitValue> upVectorized(Coord position) {
        int x = position.x, y = position.y;
        List<InitValue> neighborhood = Arrays.asList(
                new InitValue(new Coord(x - 1, y), 1),
                new InitValue(new Coord(x + 1, y), 1),
                new InitValue(new Coord(x, y + 1), 1),
                new InitValue(new Coord(x, y - 1), 2)
        );
        neighborhood.removeIf(c-> !onBoard(c.getCoord()));
        return neighborhood;
    }

    private List<InitValue> rightVectorized(Coord position) {
        int x = position.x, y = position.y;
        List<InitValue> neighborhood = Arrays.asList(
                new InitValue(new Coord(x, y - 1), 1),
                new InitValue(new Coord(x - 1, y), 1),
                new InitValue(new Coord(x, y + 1), 1),
                new InitValue(new Coord(x + 1, y), 2)
        );
        neighborhood.removeIf(c-> !onBoard(c.getCoord()));
        return neighborhood;
    }

    private List<InitValue> downVectorized(Coord position) {
        int x = position.x, y = position.y;
        List<InitValue> neighborhood = Arrays.asList(
                new InitValue(new Coord(x, y - 1), 1),
                new InitValue(new Coord(x - 1, y), 1),
                new InitValue(new Coord(x + 1, y), 1),
                new InitValue(new Coord(x, y + 1), 2)
        );
        neighborhood.removeIf(c-> !onBoard(c.getCoord()));
        return neighborhood;
    }

    private List<InitValue> singleVectorized(Coord position) {
        int x = position.x, y = position.y;
        List<InitValue> neighborhood = Arrays.asList(
                new InitValue(new Coord(x, y - 1), 1),
                new InitValue(new Coord(x - 1, y), 1),
                new InitValue(new Coord(x + 1, y), 1),
                new InitValue(new Coord(x, y + 1), 1)
        );
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
