package backend.boards;

import backend.utility.Coord;

import java.util.*;
import java.util.stream.Stream;

public class GeneratorBoard implements Board<Set<Integer>> {
    public static class Cell {
        public Set<Integer>             value;
        public List<Coord>              ship;

        public Cell(Set<Integer> value, List<Coord> ship) {
            this.value = value;
            this.ship = ship;
        }
    }

    private final Cell[][]              board;
    private final Coord                 dimensions;

    public GeneratorBoard(Coord dimensions) {
        board = new Cell[dimensions.y][dimensions.x];
        this.dimensions = dimensions;
    }

    @Override
    public void generateCell(Coord position, Set<Integer> value) {
        board[position.y][position.x] = new Cell(value, null);
    }

    @Override
    public Coord getDimensions() {
        return dimensions;
    }

    private boolean onBoard(Coord position) {
        int x = position.x, y = position.y;
        int lx = dimensions.x, ly = dimensions.y;
        return x >= 0 && y >= 0 && lx > x && ly > y;
    }

    private List<Coord> shipNeighborhood(Coord position) {
        int x = position.x, y = position.y;
        List<Coord> neighborhood = Arrays.asList(
                new Coord(x, y - 1),
                new Coord(x, y + 1),
                new Coord(x + 1, y),
                new Coord(x - 1, y)
        );
        neighborhood.removeIf(c-> !onBoard(c));
        return neighborhood;
    }

    private void emplaceOneShip(Coord position) {
        board[position.y][position.x].ship = List.of(position);
    }

    private void lengthenShip(Coord position, List<List<Coord>> ships) {
        List<Coord> ship = ships.get(0);
        ship.add(position);
        board[position.y][position.x].ship = ship;
    }

    private void concatenateShip(Coord position, List<List<Coord>> ships) {
        List<Coord> newShip = Stream.concat(ships.get(0).stream(), ships.get(1).stream()).toList();
        newShip.add(position);
        for (Coord shipElement : newShip) {
            board[shipElement.y][shipElement.x].ship = newShip;
        }
    }

    private List<List<Coord>> neighborhoodShips(Coord position) {
        List<Coord> neighborhood = shipNeighborhood(position);
        List<List<Coord>> ships = new ArrayList<>();
        for (Coord neighbor : neighborhood) {
            if (board[neighbor.y][neighbor.x].ship != null) {
                ships.add(board[neighbor.y][neighbor.x].ship);
            }
        }
        return ships;
    }

    private void emplaceShips(Coord position) {
        List<List<Coord>> ships = neighborhoodShips(position);
        switch (ships.size()) {
            case 0:
                emplaceOneShip(position);
            case 1:
                lengthenShip(position, ships);
            case 2:
                concatenateShip(position, ships);
        }
    }

    @Override
    public void setValue(Coord position, Set<Integer> value) {
        if (value.contains(2) || value.contains(3)) {
            emplaceShips(position);
        }
        board[position.y][position.x].value = value;
    }

    @Override
    public Set<Integer> accessCell(Coord position) {
        return board[position.y][position.x].value;
    }

    public Iterator<Cell[]> iterator() {
        return Arrays.stream(board).iterator();
    }

    public List<Coord> accessShip(Coord position) {
        return board[position.y][position.x].ship;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeneratorBoard that)) return false;
        return Arrays.deepEquals(board, that.board) && dimensions.equals(that.dimensions);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(dimensions);
        result = 31 * result + Arrays.deepHashCode(board);
        return result;
    }
}
