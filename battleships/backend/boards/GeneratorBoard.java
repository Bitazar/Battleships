package backend.boards;

import backend.utility.Coord;

import java.util.*;
import java.util.stream.Stream;

public class GeneratorBoard implements Board<Set<Integer>> {
    private static class Cell {
        public Set<Integer>             value;
        public List<Coord>              ship;

        public Cell(Set<Integer> value, List<Coord> ship) {
            this.value = value;
            this.ship = ship;
        }
    }

    public static class Row extends Board.Row<Set<Integer>> {
        private final Cell[]            row;
        private final int               size;

        public Row(int size) {
            row = new Cell[size];
            this.size = size;
        }

        @Override
        public int getSize() {
            return size;
        }

        public static class RowIterator implements Iterator<Set<Integer>> {
            private final Row           row;
            private int                 index = 0;

            public RowIterator(Row row) {
                this.row = row;
            }

            @Override
            public boolean hasNext() {
                return index + 1 < row.getSize();
            }

            @Override
            public Set<Integer> next() {
                return row.row[index++].value;
            }

            @Override
            public void remove() throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Cannot remove element from the fixed size array");
            }
        }

        @Override
        public Iterator<Set<Integer>> iterator() {
            return new RowIterator(this);
        }

        @Override
        public Set<Integer> get(int index) throws IndexOutOfBoundsException {
            return row[index].value;
        }

        @Override
        public void set(int index, Set<Integer> value) throws IndexOutOfBoundsException {
            row[index].value = value;
        }

        public List<Coord> getShip(int index) throws IndexOutOfBoundsException {
            return row[index].ship;
        }

        public void setShip(int index, List<Coord> value) throws IndexOutOfBoundsException {
            row[index].ship = value;
        }

        public void generateCell(int index, Set<Integer> value) throws IndexOutOfBoundsException {
            row[index] = new Cell(value, null);
        }

    }

    public static class Column extends Board.Column<Set<Integer>> {
        private final GeneratorBoard        board;
        private final int                   columnID;

        public Column(GeneratorBoard board, int columnID) {
            this.board = board;
            this.columnID = columnID;
        }

        @Override
        public int getSize() {
            return board.getHeight();
        }

        public static class ColumnIterator implements Iterator<Set<Integer>> {
            private final Column            column;
            private int                     index = 0;

            public ColumnIterator(Column column) {
                this.column = column;
            }

            @Override
            public boolean hasNext() {
                return index + 1 < column.board.getHeight();
            }

            @Override
            public Set<Integer> next() {
                return column.board.rows[index++].get(column.columnID);
            }

            @Override
            public void remove() throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Cannot remove element from the fixed size array");
            }
        }

        @Override
        public Iterator<Set<Integer>> iterator() {
            return new ColumnIterator(this);
        }

        @Override
        public Set<Integer> get(int index) {
            return board.rows[index].get(columnID);
        }

        @Override
        public void set(int index, Set<Integer> value) {
            board.rows[index].set(columnID, value);
        }

        public List<Coord> getShip(int index) throws IndexOutOfBoundsException {
            return board.rows[index].getShip(columnID);
        }

        public void setShip(int index, List<Coord> value) throws IndexOutOfBoundsException {
            board.rows[index].setShip(columnID, value);
        }

        public void generateCell(int index, Set<Integer> value) throws IndexOutOfBoundsException {
            board.rows[index].generateCell(columnID, value);
        }

    }

    private final Row[]                 rows;
    private final Coord                 dimensions;

    public GeneratorBoard(Coord dimensions) {
        rows = new Row[dimensions.y];
        Arrays.fill(rows, new Row(dimensions.x));
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

    @Override
    public int getWidth() { return dimensions.x; }

    @Override
    public int getHeight() { return dimensions.y; }

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
