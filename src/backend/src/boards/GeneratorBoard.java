package backend.src.boards;

import backend.src.utility.Coord;

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

        public Cell clone() {
            List<Coord> ship = this.ship == null ? null : new ArrayList<>(this.ship);
            Set<Integer> value = this.value == null ? null : new HashSet<>(this.value);
            return new Cell(value, ship);
        }
    }

    public static class Row extends Board.Row<Set<Integer>> {
        private final Cell[]            row;
        private final int               size;

        public Row(int size) {
            row = new Cell[size];
            for (int x = 0; x < size; ++x) {
                row[x] = new Cell(null, null);
            }
            this.size = size;
        }

        private Row(Cell[] row, int size) {
            this.row = row;
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
                return index < row.getSize();
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Row row1)) return false;
            return size == row1.size && Arrays.equals(row, row1.row);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(size);
            result = 31 * result + Arrays.hashCode(row);
            return result;
        }

        public Row clone() {
            Cell[] array = new Cell[size];
            for (int x = 0; x < size; ++x) {
                array[x] = row[x].clone();
            }
            return new Row(array, size);
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
                return index < column.board.getHeight();
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Column column)) return false;
            return columnID == column.columnID && board.equals(column.board);
        }

        @Override
        public int hashCode() {
            return Objects.hash(board, columnID);
        }
    }

    public static class RowIterator implements Iterator<Board.Row<Set<Integer>>> {
        private final GeneratorBoard        board;
        private int                         index = 0;

        public RowIterator(GeneratorBoard board) {
            this.board = board;
        }

        @Override
        public boolean hasNext() {
            return index < board.getHeight();
        }

        @Override
        public Board.Row<Set<Integer>> next() {
            return board.rows[index++];
        }

        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Cannot remove element from the fixed size array");
        }
    }

    public static class ColumnIterator implements Iterator<Board.Column<Set<Integer>>> {
        private final GeneratorBoard        board;
        private int                         index = 0;

        public ColumnIterator(GeneratorBoard board) {
            this.board = board;
        }

        @Override
        public boolean hasNext() {
            return index < board.getWidth();
        }

        @Override
        public Board.Column<Set<Integer>> next() {
            return new Column(board, index++);
        }

        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Cannot remove element from the fixed size array");
        }
    }

    public static class TransposedView extends Board.TransposedView<Set<Integer>> {
        private final GeneratorBoard    board;

        public TransposedView(GeneratorBoard board) {
            this.board = board;
        }

        @Override
        public Iterator<Board.Column<Set<Integer>>> iterator() {
            return board.columnIterator();
        }

        @Override
        public Iterator<Board.Row<Set<Integer>>> rowIterator() {
            return board.iterator();
        }

        @Override
        public void generateCell(Coord position, Set<Integer> value) {
            board.generateCell(position.transpose(), value);
        }

        @Override
        public void setValue(Coord position, Set<Integer> value) {
            board.setValue(position.transpose(), value);
        }

        @Override
        public Set<Integer> accessCell(Coord position) {
            return board.accessCell(position.transpose());
        }

        @Override
        public Coord getDimensions() {
            return board.dimensions.transpose();
        }

        @Override
        public Board<Set<Integer>> transpose() {
            return board;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TransposedView columns)) return false;
            return board.equals(columns.board);
        }

        @Override
        public int hashCode() {
            return Objects.hash(board);
        }
    }

    private final Row[]                 rows;
    private final Coord                 dimensions;

    public GeneratorBoard(Coord dimensions) {
        rows = new Row[dimensions.getY()];
        for (int y = 0; y < dimensions.getY(); ++y) {
            rows[y] = new Row(dimensions.getX());
        }
        this.dimensions = dimensions;
    }

    private GeneratorBoard(Row[] rows, Coord dimensions) {
        this.rows = rows;
        this.dimensions = dimensions;
    }

    @Override
    public void generateCell(Coord position, Set<Integer> value) {
        rows[position.getY()].generateCell(position.getX(), value);
    }

    @Override
    public Coord getDimensions() {
        return dimensions;
    }

    @Override
    public int getWidth() { return dimensions.getX(); }

    @Override
    public int getHeight() { return dimensions.getY(); }

    @Override
    public boolean onBoard(Coord position) {
        int x = position.getX(), y = position.getY();
        int lx = dimensions.getX(), ly = dimensions.getY();
        return x >= 0 && y >= 0 && lx > x && ly > y;
    }

    private List<Coord> shipNeighborhood(Coord position) {
        int x = position.getX(), y = position.getY();
        List<Coord> neighborhood = new ArrayList<>(Arrays.asList(
                new Coord(x, y - 1),
                new Coord(x, y + 1),
                new Coord(x + 1, y),
                new Coord(x - 1, y)
        ));
        neighborhood.removeIf(c-> !onBoard(c));
        return neighborhood;
    }

    private void emplaceOneShip(Coord position) {
        List<Coord> positionList = new ArrayList<>();
        positionList.add(position);
        rows[position.getY()].setShip(position.getX(), positionList);
    }

    private void lengthenShip(Coord position, List<List<Coord>> ships) {
        List<Coord> ship = ships.get(0);
        ship.add(position);
        rows[position.getY()].setShip(position.getX(), ship);
    }

    private void concatenateShip(Coord position, List<List<Coord>> ships) {
        List<Coord> newShip = new ArrayList<>(Stream.concat(ships.get(0).stream(), ships.get(1).stream()).toList());
        newShip.add(position);
        for (Coord shipElement : newShip) {
            rows[shipElement.getY()].setShip(shipElement.getX(), newShip);
        }
    }

    private List<List<Coord>> neighborhoodShips(Coord position) {
        List<Coord> neighborhood = shipNeighborhood(position);
        List<List<Coord>> ships = new ArrayList<>();
        for (Coord neighbor : neighborhood) {
            if (accessShip(neighbor) != null) {
                ships.add(accessShip(neighbor));
            }
        }
        return ships;
    }

    private void emplaceShips(Coord position) {
        List<List<Coord>> ships = neighborhoodShips(position);
        switch (ships.size()) {
            case 0 -> emplaceOneShip(position);
            case 1 -> lengthenShip(position, ships);
            case 2 -> concatenateShip(position, ships);
        }
    }

    @Override
    public void setValue(Coord position, Set<Integer> value) {
        if (value.contains(2) || value.contains(3)) {
            emplaceShips(position);
        }
        rows[position.getY()].set(position.getX(), value);
    }

    @Override
    public Set<Integer> accessCell(Coord position) {
        return rows[position.getY()].get(position.getX());
    }

    @Override
    public Iterator<Board.Row<Set<Integer>>> iterator() {
        return new RowIterator(this);
    }

    @Override
    public Iterator<Board.Column<Set<Integer>>> columnIterator() {
        return new ColumnIterator(this);
    }

    @Override
    public Board.TransposedView<Set<Integer>> transpose() {
        return new TransposedView(this);
    }

    public List<Coord> accessShip(Coord position) {
        return rows[position.getY()].getShip(position.getX());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeneratorBoard that)) return false;
        return Arrays.equals(rows, that.rows) && dimensions.equals(that.dimensions);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(dimensions);
        result = 31 * result + Arrays.hashCode(rows);
        return result;
    }

    public Board<Set<Integer>> clone() {
        Row[] rows = new Row[dimensions.getY()];
        for (int y = 0;y < dimensions.getY(); ++y) {
            rows[y] = this.rows[y].clone();
        }
        return new GeneratorBoard(rows, new Coord(dimensions.getX(), dimensions.getY()));
    }

}
