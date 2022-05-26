package backend.boards;

import backend.utility.Coord;

import java.util.*;
import java.util.stream.Stream;

/**
 * Represents the board used by the solver and generator. Contains cells metadata
 */
public class GeneratorBoard implements Board<Set<Integer>> {

    /**
     * Represents a cell on the board. Contains metadata
     */
    private static class Cell {
        public Set<Integer>             value;
        public List<Coord>              ship;

        /**
         * Creates a new Cell object. Cell's value and metadata is uninitialized
         *
         * @param value the initial value
         * @param ship the initial metadata
         */
        public Cell(Set<Integer> value, List<Coord> ship) {
            this.value = value;
            this.ship = ship;
        }

        /**
         * Returns a deep-copy of this cell object
         *
         * @return the deep-copy of this cell object
         */
        @SuppressWarnings("MethodDoesntCallSuperMethod")
        public Cell clone() {
            Set<Integer> value = this.value == null ? null : new HashSet<>(this.value);
            return new Cell(value, null);
        }
    }

    /**
     * Represents the row on the board
     */
    public static class Row extends Board.Row<Set<Integer>> {
        private final Cell[]            row;
        private final int               size;

        /**
         * Creates a new Row object
         *
         * @param size the row's size
         */
        public Row(int size) {
            row = new Cell[size];
            for (int x = 0; x < size; ++x) {
                row[x] = new Cell(null, null);
            }
            this.size = size;
        }

        /**
         * Creates a new Row object
         *
         * @param row the cell's row
         * @param size the row's size
         */
        private Row(Cell[] row, int size) {
            this.row = row;
            this.size = size;
        }

        /**
         * Returns the row's size
         *
         * @return the row's size
         */
        @Override
        public int getSize() {
            return size;
        }

        /**
         * Iterates through the row. Returns only the cells values - not metadata
         */
        public static class RowIterator implements Iterator<Set<Integer>> {
            private final Row           row;
            private int                 index = 0;

            /**
             * Creates a new RowIterator object
             *
             * @param row the Row object
             */
            public RowIterator(Row row) {
                this.row = row;
            }

            /**
             * Returns if there is next element in the row
             *
             * @return whether there is next element in the row
             */
            @Override
            public boolean hasNext() {
                return index < row.getSize();
            }

            /**
             * Returns the next value in the row
             *
             * @return the next value in the row
             */
            @Override
            public Set<Integer> next() {
                return row.row[index++].value;
            }

            /**
             * Removes the element from the column. Always throws an exception
             * because the column's size is immutable
             *
             * @throws UnsupportedOperationException the exception thrown when
             * this method is called
             */
            @Override
            public void remove() throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Cannot remove element from the fixed size array");
            }
        }

        /**
         * Returns an iterator to the elements of the row
         *
         * @return the iterator to the elements of the row
         */
        @Override
        public Iterator<Set<Integer>> iterator() {
            return new RowIterator(this);
        }

        /**
         * Returns an element lying under the given index
         *
         * @param index the index of the accessed element
         * @return the value of the element with the given index
         * @throws IndexOutOfBoundsException when given index is invalid
         */
        @Override
        public Set<Integer> get(int index) throws IndexOutOfBoundsException {
            return row[index].value;
        }

        /**
         * Sets the value of an element lying under the given index
         *
         * @param index the index of the accessed element
         * @param value the new value of the accessed element
         * @throws IndexOutOfBoundsException when given index is invalid
         */
        @Override
        public void set(int index, Set<Integer> value) throws IndexOutOfBoundsException {
            row[index].value = value;
        }

        /**
         * Returns a metadata lying under the given index
         *
         * @param index the index of the accessed element
         * @return the value of the element with the given index
         * @throws IndexOutOfBoundsException when given index is invalid
         */
        public List<Coord> getShip(int index) throws IndexOutOfBoundsException {
            return row[index].ship;
        }

        /**
         * Sets the metadata of an element lying under the given index
         *
         * @param index the index of the accessed element
         * @param value the new value of the accessed element
         * @throws IndexOutOfBoundsException when given index is invalid
         */
        public void setShip(int index, List<Coord> value) throws IndexOutOfBoundsException {
            row[index].ship = value;
        }

        /**
         * Generates a new cell under the given index
         *
         * @param index the generated cell's index
         * @param value the cell's initial value
         * @throws IndexOutOfBoundsException when the given index is invalid
         */
        public void generateCell(int index, Set<Integer> value) throws IndexOutOfBoundsException {
            row[index] = new Cell(value, null);
        }

        /**
         * Returns if two rows contains the same elements
         *
         * @param object the row object
         * @return whether two rows contains the same elements
         */
        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (!(object instanceof Row row1)) return false;
            return size == row1.size && Arrays.equals(row, row1.row);
        }

        /**
         * Returns the hash code of the column
         *
         * @return the hash code of the column
         */
        @Override
        public int hashCode() {
            int result = Objects.hash(size);
            result = 31 * result + Arrays.hashCode(row);
            return result;
        }

        /**
         * Returns a deep-copy of this row object
         *
         * @return the deep-copy of this row object
         */
        @SuppressWarnings("MethodDoesntCallSuperMethod")
        public Row clone() {
            Cell[] array = new Cell[size];
            for (int x = 0; x < size; ++x) {
                array[x] = row[x].clone();
            }
            return new Row(array, size);
        }
    }

    /**
     * Represents the column on the board
     */
    public class Column extends Board.Column<Set<Integer>> {
        private final int                   columnID;

        /**
         * Creates a new Column object
         *
         * @param columnID the column's ID number
         */
        public Column(int columnID) {
            this.columnID = columnID;
        }

        /**
         * Returns the size of the column
         *
         * @return the size of the column
         */
        @Override
        public int getSize() {
            return getHeight();
        }

        /**
         * The column's iterator
         */
        public class ColumnIterator implements Iterator<Set<Integer>> {
            private int                     index = 0;

            /**
             * Creates a new ColumnIterator object
             */
            public ColumnIterator() {}

            /**
             * Returns if there is next element in the column
             *
             * @return whether there is next element in the column
             */
            @Override
            public boolean hasNext() {
                return index < getHeight();
            }

            /**
             * Returns the next value in the column
             *
             * @return the next value in the column
             */
            @Override
            public Set<Integer> next() {
                return rows[index++].get(columnID);
            }

            /**
             * Removes the element from the column. Always throws an exception
             * because the column's size is immutable
             *
             * @throws UnsupportedOperationException the exception thrown when
             * this method is called
             */
            @Override
            public void remove() throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Cannot remove element from the fixed size array");
            }
        }

        /**
         * Returns an iterator to the elements of the column
         *
         * @return the iterator to the elements of the column
         */
        @Override
        public Iterator<Set<Integer>> iterator() {
            return new ColumnIterator();
        }

        /**
         * Returns an element lying under the given index
         *
         * @param index the index of the accessed element
         * @return the value of the element with the given index
         * @throws IndexOutOfBoundsException when given index is invalid
         */
        @Override
        public Set<Integer> get(int index) {
            return rows[index].get(columnID);
        }

        /**
         * Sets the value of an element lying under the given index
         *
         * @param index the index of the accessed element
         * @param value the new value of the accessed element
         * @throws IndexOutOfBoundsException when given index is invalid
         */
        @Override
        public void set(int index, Set<Integer> value) {
            rows[index].set(columnID, value);
        }

        /**
         * Returns a metadata lying under the given index
         *
         * @param index the index of the accessed element
         * @return the value of the element with the given index
         * @throws IndexOutOfBoundsException when given index is invalid
         */
        public List<Coord> getShip(int index) throws IndexOutOfBoundsException {
            return rows[index].getShip(columnID);
        }

        /**
         * Sets the metadata of an element lying under the given index
         *
         * @param index the index of the accessed element
         * @param value the new value of the accessed element
         * @throws IndexOutOfBoundsException when given index is invalid
         */
        public void setShip(int index, List<Coord> value) throws IndexOutOfBoundsException {
            rows[index].setShip(columnID, value);
        }

        /**
         * Generates a new cell under the given index
         *
         * @param index the generated cell's index
         * @param value the cell's initial value
         * @throws IndexOutOfBoundsException when the given index is invalid
         */
        public void generateCell(int index, Set<Integer> value) throws IndexOutOfBoundsException {
            rows[index].generateCell(columnID, value);
        }

        /**
         * Returns if two columns contains the same elements
         *
         * @param object the column object
         * @return whether two columns contains the same elements
         */
        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            Column sets = (Column) object;
            return columnID == sets.columnID;
        }

        /**
         * Returns the hash code of the column
         *
         * @return the hash code of the column
         */
        @Override
        public int hashCode() {
            return Objects.hash(columnID);
        }

    }

    /**
     * Iterates through rows
     */
    public class RowIterator implements Iterator<Board.Row<Set<Integer>>> {
        private int                         index = 0;

        /**
         * Creates a new RowIterator object
         */
        public RowIterator() {}

        /**
         * Returns if there is next row on the board
         *
         * @return whether there is next row on the board
         */
        @Override
        public boolean hasNext() {
            return index < getHeight();
        }

        /**
         * Returns the next row from the board
         *
         * @return the next row from the board
         */
        @Override
        public Board.Row<Set<Integer>> next() {
            return rows[index++];
        }

        /**
         * Removes the row from the board. Always throws an exception
         * because the column's size is immutable
         *
         * @throws UnsupportedOperationException the exception thrown when
         * this method is called
         */
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Cannot remove element from the fixed size array");
        }
    }

    /**
     * Iterates through columns
     */
    public class ColumnIterator implements Iterator<Board.Column<Set<Integer>>> {
        private int                         index = 0;
        /**
         * Creates a new ColumnIterator object
         */
        public ColumnIterator() {}

        /**
         * Returns the next column from the board
         *
         * @return the next column from the board
         */
        @Override
        public boolean hasNext() {
            return index < getWidth();
        }

        /**
         * Returns if there is next column on the board
         *
         * @return whether there is next column on the board
         */
        @Override
        public Board.Column<Set<Integer>> next() {
            return new Column(index++);
        }

        /**
         * Removes the column from the board. Always throws an exception
         * because the column's size is immutable
         *
         * @throws UnsupportedOperationException the exception thrown when
         * this method is called
         */
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Cannot remove element from the fixed size array");
        }
    }

    /**
     * The transposed view to the board. Allows to access the board in the column-oriented
     * manner
     */
    public class TransposedView extends Board.TransposedView<Set<Integer>> {

        /**
         * Creates a new TransposedView object
         */
        public TransposedView() {}

        /**
         * Returns an iterator to the columns of the board
         *
         * @return the iterator to the columns of the board
         */
        @Override
        public Iterator<Board.Column<Set<Integer>>> iterator() {
            return GeneratorBoard.this.columnIterator();
        }

        /**
         * Returns an iterator to the rows of the board
         *
         * @return the iterator to the rows of the board
         */
        @Override
        public Iterator<Board.Row<Set<Integer>>> rowIterator() {
            return GeneratorBoard.this.iterator();
        }

        /**
         * Generates the cell on the board
         *
         * @param position the position of the cell
         * @param value the initial value of the cell
         */
        @Override
        public void generateCell(Coord position, Set<Integer> value) {
            GeneratorBoard.this.generateCell(position.transpose(), value);
        }

        /**
         * Sets the value of the cell
         *
         * @param position the position of the cell
         * @param value the new value of the cell
         */
        @Override
        public void setValue(Coord position, Set<Integer> value) {
            GeneratorBoard.this.setValue(position.transpose(), value);
        }

        /**
         * Returns the value of the given cell
         *
         * @param position the position of the cell
         * @return the value of the given cell
         */
        @Override
        public Set<Integer> accessCell(Coord position) {
            return GeneratorBoard.this.accessCell(position.transpose());
        }

        /**
         * Returns the dimensions of the transposed view.
         * The x represents the height and y represents the width of the transposed board
         *
         * @return the dimensions of the transposed view
         */
        @Override
        public Coord getDimensions() {
            return dimensions.transpose();
        }

        /**
         * Returns the transposed version of this view (Old board)
         *
         * @return the transposed version of this view
         */
        @Override
        public Board<Set<Integer>> transpose() {
            return GeneratorBoard.this;
        }

    }

    private final Row[]                 rows;
    private final Coord                 dimensions;

    /**
     * Creates a new GeneratorBoard object
     *
     * @param dimensions the board's dimensions
     */
    public GeneratorBoard(Coord dimensions) {
        rows = new Row[dimensions.getY()];
        for (int y = 0; y < dimensions.getY(); ++y) {
            rows[y] = new Row(dimensions.getX());
        }
        this.dimensions = dimensions;
    }

    /**
     * Creates a new GeneratorBoard object
     *
     * @param rows the board's rows
     * @param dimensions the board's dimensions
     */
    private GeneratorBoard(Row[] rows, Coord dimensions) {
        this.rows = rows;
        this.dimensions = dimensions;
    }

    /**
     * Generates the cell on the board
     *
     * @param position the position of the cell
     * @param value the initial value of the cell
     */
    @Override
    public void generateCell(Coord position, Set<Integer> value) {
        rows[position.getY()].generateCell(position.getX(), value);
    }

    /**
     * Returns the dimensions of the board
     *
     * @return the dimensions of the board
     */
    @Override
    public Coord getDimensions() {
        return dimensions;
    }

    /**
     * Returns the width of the board
     *
     * @return the width of the board
     */
    @Override
    public int getWidth() { return dimensions.getX(); }

    /**
     * Returns the height of the board
     *
     * @return the height of the board
     */
    @Override
    public int getHeight() { return dimensions.getY(); }

    /**
     * Checks if the given position is on the board
     *
     * @param position the checked position
     * @return whether the given position is on the board
     */
    @Override
    public boolean onBoard(Coord position) {
        int x = position.getX(), y = position.getY();
        int lx = dimensions.getX(), ly = dimensions.getY();
        return x >= 0 && y >= 0 && lx > x && ly > y;
    }

    /**
     * Returns the neighborhood of the given position
     *
     * @param position the position
     * @return the neighborhood of the given position
     */
    private List<Coord> getNeighborhood(Coord position) {
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

    /**
     * Emplace ship on the board
     *
     * @param position the ship position
     */
    private void emplaceShip(Coord position) {
        List<Coord> positionList = new ArrayList<>();
        positionList.add(position);
        rows[position.getY()].setShip(position.getX(), positionList);
    }

    /**
     * Lengthens already existing ship
     *
     * @param position the ship position
     * @param ships the ships metadata
     */
    private void lengthenShip(Coord position, List<List<Coord>> ships) {
        List<Coord> ship = ships.get(0);
        ship.add(position);
        rows[position.getY()].setShip(position.getX(), ship);
    }

    /**
     * Concatenates ships into a bigger one
     *
     * @param position the position of the concatenation
     * @param ships the ships metadata
     */
    private void concatenateShips(Coord position, List<List<Coord>> ships) {
        List<Coord> newShip = new ArrayList<>(Stream.concat(ships.get(0).stream(), ships.get(1).stream()).toList());
        newShip.add(position);
        for (Coord shipElement : newShip) {
            rows[shipElement.getY()].setShip(shipElement.getX(), newShip);
        }
    }

    /**
     * Returns the neighborhood ships of the given field
     *
     * @param position the field's position
     * @return the neighborhood ships of the given field
     */
    private List<List<Coord>> getNeighborhoodShips(Coord position) {
        List<Coord> neighborhood = getNeighborhood(position);
        List<List<Coord>> ships = new ArrayList<>();
        for (Coord neighbor : neighborhood) {
            if (accessShip(neighbor) != null) {
                ships.add(accessShip(neighbor));
            }
        }
        return ships;
    }

    /**
     * Places ship field onto the board
     *
     * @param position the ship field's position
     */
    private void placeShipField(Coord position) {
        List<List<Coord>> ships = getNeighborhoodShips(position);
        switch (ships.size()) {
            case 0 -> emplaceShip(position);
            case 1 -> lengthenShip(position, ships);
            case 2 -> concatenateShips(position, ships);
        }
    }

    /**
     * Sets the value of the cell
     *
     * @param position the position of the cell
     * @param value the new value of the cell
     */
    @Override
    public void setValue(Coord position, Set<Integer> value) {
        if (value.contains(2) || value.contains(3)) {
            placeShipField(position);
        }
        rows[position.getY()].set(position.getX(), value);
    }

    /**
     * Returns the value of the given cell
     *
     * @param position the position of the cell
     * @return the value of the given cell
     */
    @Override
    public Set<Integer> accessCell(Coord position) {
        return rows[position.getY()].get(position.getX());
    }

    /**
     * Returns an iterator to the rows of the board
     *
     * @return the iterator to the rows of the board
     */
    @Override
    public Iterator<Board.Row<Set<Integer>>> iterator() {
        return new RowIterator();
    }

    /**
     * Returns an iterator to the columns of the board
     *
     * @return the iterator to the columns of the board
     */
    @Override
    public Iterator<Board.Column<Set<Integer>>> columnIterator() {
        return new ColumnIterator();
    }

    /**
     * Returns the transposed version of this view (TransposedView)
     *
     * @return the transposed version of this view
     */
    @Override
    public Board.TransposedView<Set<Integer>> transpose() {
        return new TransposedView();
    }

    /**
     * Returns the metadata of the given cell
     *
     * @param position the position of the cell
     * @return the value of the given cell
     */
    public List<Coord> accessShip(Coord position) {
        return rows[position.getY()].getShip(position.getX());
    }

    /**
     * Returns if two boards contains the same elements
     *
     * @param object the board object
     * @return whether two boards contains the same elements
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof GeneratorBoard that)) return false;
        return Arrays.equals(rows, that.rows) && dimensions.equals(that.dimensions);
    }

    /**
     * Returns the hash code of the board
     *
     * @return the hash code of the board
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(dimensions);
        result = 31 * result + Arrays.hashCode(rows);
        return result;
    }

    /**
     * Returns the ships placed on the board
     *
     * @return the ships placed on the board
     */
    private Set<List<Coord>> getBoardShips() {
        Set<List<Coord>> ships = new HashSet<>();
        for (int y = 0; y < dimensions.getY(); ++y) {
            for (int x = 0; x < dimensions.getX(); ++x) {
                if (this.rows[y].getShip(x) != null) {
                    ships.add(this.rows[y].getShip(x));
                }
            }
        }
        return ships;
    }

    /**
     * Sets the ships on the board
     *
     * @param rows the board's rows
     * @param ships the ships metadata
     */
    private void setBoardShips(Row[] rows, Set<List<Coord>> ships) {
        for (List<Coord> ship : ships) {
            List<Coord> shipCopy = new ArrayList<>(ship);
            for (Coord coord : ship) {
                rows[coord.getY()].setShip(coord.getX(), shipCopy);
            }
        }
    }

    /**
     * Returns a deep-copy of this board object
     *
     * @return the deep-copy of this board object
     */
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public Board<Set<Integer>> clone() {
        Row[] rows = new Row[dimensions.getY()];
        for (int y = 0;y < dimensions.getY(); ++y) {
            rows[y] = this.rows[y].clone();
        }
        setBoardShips(rows, getBoardShips());
        return new GeneratorBoard(rows, new Coord(dimensions.getX(), dimensions.getY()));
    }

}
