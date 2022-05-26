package backend.boards;

import backend.utility.Coord;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/**
 * Represents the board used to interchange data between different layers of the application
 */
public class BoardDTO implements Board<Integer> {
    private final Integer[][]                       board;
    private final Coord                             dimensions;

    /**
     * Creates a new BoardDTO object. Values of the board cells are uninitialized
     *
     * @param dimensions the board dimensions
     */
    public BoardDTO(Coord dimensions) {
        this.dimensions = dimensions;
        board = new Integer[dimensions.getY()][dimensions.getX()];
    }

    /**
     * Represents the row on the board
     */
    public static class Row extends Board.Row<Integer> {
        private final BoardDTO                      board;
        private final int                           rowID;

        /**
         * Creates a new Row object
         *
         * @param board the BoardDTO object
         * @param rowID the row's ID number
         */
        public Row(BoardDTO board, int rowID) {
            this.board = board;
            this.rowID = rowID;
        }

        /**
         * Returns an iterator to the elements of the row
         *
         * @return the iterator to the elements of the row
         */
        @Override
        public Iterator<Integer> iterator() {
            return Arrays.stream(board[rowID]).iterator();
        }

        /**
         * Returns an element lying under the given index
         *
         * @param index the index of the accessed element
         * @return the value of the element with the given index
         * @throws IndexOutOfBoundsException when given index is invalid
         */
        @Override
        public Integer get(int index) throws IndexOutOfBoundsException {
            return board[rowID][index];
        }

        /**
         * Sets the value of an element lying under the given index
         *
         * @param index the index of the accessed element
         * @param value the new value of the accessed element
         * @throws IndexOutOfBoundsException when given index is invalid
         */
        @Override
        public void set(int index, Integer value) throws IndexOutOfBoundsException {
            board[rowID][index] = value;
        }

        /**
         * Returns the size of the row
         *
         * @return the size of the row
         */
        @Override
        public int getSize() {
            return getWidth();
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
            if (!(object instanceof Row integers)) return false;
            return rowID == integers.rowID && board.equals(integers.board);
        }

        /**
         * Returns the hash code of the row
         *
         * @return the hash code of the row
         */
        @Override
        public int hashCode() {
            return Objects.hash(rowID);
        }

    }

    /**
     * Represents the column on the board
     */
    public static class Column extends Board.Column<Integer> {
        private final BoardDTO                      board;
        private final int                           columnID;

        /**
         * Creates a new Column object
         *
         * @param board the BoardDTO object
         * @param columnID the column's ID number
         */
        public Column(BoardDTO board, int columnID) {
            this.board = board;
            this.columnID = columnID;
        }

        /**
         * The column's iterator
         */
        public static class ColumnIterator implements Iterator<Integer> {
            private final BoardDTO                  board;
            private final int                       columnID;
            private int                             index = 0;

            /**
             * Creates a new ColumnIterator object
             *
             * @param board the BoardDTO object
             * @param columnID the column's ID number
             */
            public ColumnIterator(BoardDTO board, int columnID) {
                this.board = board;
                this.columnID = columnID;
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
            public Integer next() {
                return board[index++][columnID];
            }
        }

        /**
         * Returns an iterator to the elements of the column
         *
         * @return the iterator to the elements of the column
         */
        @Override
        public Iterator<Integer> iterator() {
            return this.new ColumnIterator();
        }

        /**
         * Returns an element lying under the given index
         *
         * @param index the index of the accessed element
         * @return the value of the element with the given index
         * @throws IndexOutOfBoundsException when given index is invalid
         */
        @Override
        public Integer get(int index) {
            return board[index][columnID];
        }

        /**
         * Sets the value of an element lying under the given index
         *
         * @param index the index of the accessed element
         * @param value the new value of the accessed element
         * @throws IndexOutOfBoundsException when given index is invalid
         */
        @Override
        public void set(int index, Integer value) {
            board.board[index][columnID] = value;
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
         * Returns if two columns contains the same elements
         *
         * @param object the column object
         * @return whether two columns contains the same elements
         */
        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (!(object instanceof Column integers)) return false;
            return columnID == integers.columnID && board.equals(integers.board);
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
     * The transposed view to the board. Allows to access the board in the column-oriented
     * manner
     */
    public static class TransposedView extends Board.TransposedView<Integer> {
        private final BoardDTO                      board;

        /**
         * Creates a new TransposedView object
         *
         * @param board the board object
         */
        public TransposedView(BoardDTO board) {
            this.board = board;
        }

        /**
         * Returns an iterator to the columns of the board
         *
         * @return the iterator to the columns of the board
         */
        @Override
        public Iterator<Board.Column<Integer>> iterator() {
            return BoardDTO.this.columnIterator();
        }

        /**
         * Returns an iterator to the rows of the board
         *
         * @return the iterator to the rows of the board
         */
        @Override
        public Iterator<Board.Row<Integer>> rowIterator() {
            return BoardDTO.this.iterator();
        }

        /**
         * Generates the cell on the board
         *
         * @param position the position of the cell
         * @param value the initial value of the cell
         */
        @Override
        public void generateCell(Coord position, Integer value) {
            board.generateCell(position.transpose(), value);
        }

        /**
         * Sets the value of the cell
         *
         * @param position the position of the cell
         * @param value the new value of the cell
         */
        @Override
        public void setValue(Coord position, Integer value) {
            board.setValue(position.transpose(), value);
        }

        /**
         * Returns the value of the given cell
         *
         * @param position the position of the cell
         * @return the value of the given cell
         */
        @Override
        public Integer accessCell(Coord position) {
            return BoardDTO.this.accessCell(position.transpose());
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
        public Board<Integer> transpose() {
            return BoardDTO.this;
        }

        /**
         *
         * Returns if two transposed views contains the same elements
         *
         * @param object the transposed view object
         * @return whether two transposed views contains the same elements
         */
        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (!(object instanceof TransposedView columns)) return false;
            return board.equals(columns.board);
        }

        /**
         * Returns the hash code of the transposed view
         *
         * @return the hash code of the transposed view
         */
        @Override
        public int hashCode() {
            return Objects.hash(board);
        }
    }

    /**
     * Iterates through rows
     */
    public static class RowIterator implements Iterator<Board.Row<Integer>> {
        private final BoardDTO              board;
        private int                         index = 0;

        /**
         * Creates a new RowIterator object
         *
         * @param board the BoardDTO object
         */
        public RowIterator(BoardDTO board) {
            this.board = board;
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
        public Board.Row<Integer> next() {
            return BoardDTO.this.new Row(index++);
        }
    }

    /**
     * Iterates through columns
     */
    public static class ColumnIterator implements Iterator<Board.Column<Integer>> {
        private final BoardDTO              board;
        private int                         index = 0;

        /**
         * Creates a new ColumnIterator object
         *
         * @param board the BoardDTO object
         */
        public ColumnIterator(BoardDTO board) {
            this.board = board;
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

        /**
         * Returns if there is next column on the board
         *
         * @return whether there is next column on the board
         */
        @Override
        public boolean hasNext() {
            return index < getWidth();
        }

        /**
         * Returns the next column from the board
         *
         * @return the next column from the board
         */
        @Override
        public Board.Column<Integer> next() {
            return BoardDTO.this.new Column(index++);
        }
    }

    /**
     * Generates the cell on the board
     *
     * @param position the position of the cell
     * @param value the initial value of the cell
     */
    @Override
    public void generateCell(Coord position, Integer value) {
        board[position.getY()][position.getX()] = value;
    }

    /**
     * Sets the value of the cell
     *
     * @param position the position of the cell
     * @param value the new value of the cell
     */
    @Override
    public void setValue(Coord position, Integer value) {
        generateCell(position, value);
    }

    /**
     * Returns the value of the given cell
     *
     * @param position the position of the cell
     * @return the value of the given cell
     */
    @Override
    public Integer accessCell(Coord position) {
        return board[position.getY()][position.getX()];
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
    public int getWidth() {
        return dimensions.getX();
    }

    /**
     * Returns the height of the board
     *
     * @return the height of the board
     */
    @Override
    public int getHeight() {
        return dimensions.getY();
    }

    /**
     * Returns an iterator to the rows of the board
     *
     * @return the iterator to the rows of the board
     */
    @Override
    public Iterator<Board.Row<Integer>> iterator() {
        return new RowIterator();
    }

    /**
     * Returns an iterator to the columns of the board
     *
     * @return the iterator to the columns of the board
     */
    @Override
    public Iterator<Board.Column<Integer>> columnIterator() {
        return new ColumnIterator();
    }

    /**
     * Returns the transposed version of this view (TransposedView)
     *
     * @return the transposed version of this view
     */
    @Override
    public Board.TransposedView<Integer> transpose() {
        return new TransposedView();
    }

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
     *
     * Returns if two boards contains the same elements
     *
     * @param object the board object
     * @return whether two boards contains the same elements
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof BoardDTO rows)) return false;
        return Arrays.deepEquals(board, rows.board) && dimensions.equals(rows.dimensions);
    }

    /**
     * Returns the hash code of the board
     *
     * @return the hash code of the board
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(dimensions);
        result = 31 * result + Arrays.deepHashCode(board);
        return result;
    }

    /**
     * Returns a deep-copy of this board object
     *
     * @return the deep-copy of this board object
     */
    public Board<Integer> clone() {
        // this method creates a new BoardDTO object from scratch - the super call is not necessary
        BoardDTO board = new BoardDTO(dimensions);
        for (int y = 0; y < dimensions.getY(); ++y) {
            for (int x = 0; x < dimensions.getX(); ++x) {
                board.setValue(new Coord(x, y), this.board[y][x]);
            }
        }
        return board;
    }

}
