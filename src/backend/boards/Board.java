package backend.boards;

import backend.utility.Coord;

import java.util.Iterator;

/**
 * Represents the board with the given field's base type
 *
 * @param <Value> the field's base type
 */
public interface Board<Value> extends Iterable<Board.Row<Value>> {

    /**
     * Represents the row on the board
     *
     * @param <Value> the row's element type
     */
    abstract class Row<Value> implements Iterable<Value> {

        /**
         * Returns an iterator to the elements of the row
         *
         * @return the iterator to the elements of the row
         */
        public abstract Iterator<Value> iterator();

        /**
         * Returns an element lying under the given index
         *
         * @param index the index of the accessed element
         * @return the value of the element with the given index
         * @throws IndexOutOfBoundsException when given index is invalid
         */
        public abstract Value get(int index) throws IndexOutOfBoundsException;

        /**
         * Sets the value of an element lying under the given index
         *
         * @param index the index of the accessed element
         * @param value the new value of the accessed element
         * @throws IndexOutOfBoundsException when given index is invalid
         */
        public abstract void set(int index, Value value) throws IndexOutOfBoundsException;

        /**
         * Returns the size of the row
         *
         * @return the size of the row
         */
        public abstract int getSize();

    }

    /**
     * Represents the column on the board
     *
     * @param <Value> the column's element type
     */
    abstract class Column<Value> implements Iterable<Value> {

        /**
         * Returns an iterator to the elements of the column
         *
         * @return the iterator to the elements of the column
         */
        public abstract Iterator<Value> iterator();

        /**
         * Returns an element lying under the given index
         *
         * @param index the index of the accessed element
         * @return the value of the element with the given index
         * @throws IndexOutOfBoundsException when given index is invalid
         */
        public abstract Value get(int index);

        /**
         * Sets the value of an element lying under the given index
         *
         * @param index the index of the accessed element
         * @param value the new value of the accessed element
         * @throws IndexOutOfBoundsException when given index is invalid
         */
        public abstract void set(int index, Value value);

        /**
         * Returns the size of the column
         *
         * @return the size of the column
         */
        public abstract int getSize();

    }

    /**
     * The transposed view to the board. Allows to access the board in the column-oriented
     * manner
     *
     * @param <Value> the field's base type
     */
    abstract class TransposedView<Value> implements Iterable<Column<Value>> {

        /**
         * Returns an iterator to the columns of the board
         *
         * @return the iterator to the columns of the board
         */
        public abstract Iterator<Column<Value>> iterator();

        /**
         * Returns an iterator to the rows of the board
         *
         * @return the iterator to the rows of the board
         */
        public abstract Iterator<Row<Value>> rowIterator();

        /**
         * Generates the cell on the board
         *
         * @param position the position of the cell
         * @param value the initial value of the cell
         */
        public abstract void generateCell(Coord position, Value value);

        /**
         * Sets the value of the cell
         *
         * @param position the position of the cell
         * @param value the new value of the cell
         */
        public abstract void setValue(Coord position, Value value);

        /**
         * Returns the value of the given cell
         *
         * @param position the position of the cell
         * @return the value of the given cell
         */
        public abstract Value accessCell(Coord position);

        /**
         * Returns the dimensions of the transposed view.
         * The x represents the height and y represents the width of the transposed board
         *
         * @return the dimensions of the transposed view
         */
        public abstract Coord getDimensions();

        /**
         * Returns the transposed version of this view (Old board)
         *
         * @return the transposed version of this view
         */
        public abstract Board<Value> transpose();

    }

    /**
     * Generates the cell on the board
     *
     * @param position the position of the cell
     * @param value the initial value of the cell
     */
    void generateCell(Coord position, Value value);

    /**
     * Sets the value of the cell
     *
     * @param position the position of the cell
     * @param value the new value of the cell
     */
    void setValue(Coord position, Value value);

    /**
     * Returns the value of the given cell
     *
     * @param position the position of the cell
     * @return the value of the given cell
     */
    Value accessCell(Coord position);

    /**
     * Checks if the given position is on the board
     *
     * @param position the checked position
     * @return whether the given position is on the board
     */
    boolean onBoard(Coord position);

    /**
     * Returns the dimensions of the board
     *
     * @return the dimensions of the board
     */
    Coord getDimensions();

    /**
     * Returns the width of the board
     *
     * @return the width of the board
     */
    int getWidth();

    /**
     * Returns the height of the board
     *
     * @return the height of the board
     */
    int getHeight();

    /**
     * Returns an iterator to the rows of the board
     *
     * @return the iterator to the rows of the board
     */
    Iterator<Row<Value>> iterator();

    /**
     * Returns an iterator to the columns of the board
     *
     * @return the iterator to the columns of the board
     */
    Iterator<Column<Value>> columnIterator();

    /**
     * Returns the transposed version of this view (TransposedView)
     *
     * @return the transposed version of this view
     */
    TransposedView<Value> transpose();

    /**
     * Returns a deep-copy of this board object
     *
     * @return the deep-copy of this board object
     */
    Board<Value> clone();

}
