package backend.src.boards;

import backend.src.utility.Coord;

import java.util.Iterator;

public interface Board<Value> extends Iterable<Board.Row<Value>> {
    abstract class Row<Value> implements Iterable<Value> {

        public abstract Iterator<Value> iterator();

        public abstract Value get(int index) throws IndexOutOfBoundsException;

        public abstract void set(int index, Value value) throws IndexOutOfBoundsException;

        public abstract int getSize();

    }

    abstract class Column<Value> implements Iterable<Value> {

        public abstract Iterator<Value> iterator();

        public abstract Value get(int index);

        public abstract void set(int index, Value value);

        public abstract int getSize();

    }

    abstract class TransposedView<Value> implements Iterable<Column<Value>> {

        public abstract Iterator<Column<Value>> iterator();

        public abstract Iterator<Row<Value>> rowIterator();

        public abstract void generateCell(Coord position, Value value);

        public abstract void setValue(Coord position, Value value);

        public abstract Value accessCell(Coord position);

        public abstract Coord getDimensions();

        public abstract Board<Value> transpose();

    }

    void generateCell(Coord position, Value value);

    void setValue(Coord position, Value value);

    Value accessCell(Coord position);

    boolean onBoard(Coord position);

    Coord getDimensions();

    int getWidth();

    int getHeight();

    Iterator<Row<Value>> iterator();

    Iterator<Column<Value>> columnIterator();

    TransposedView<Value> transpose();

    Board<Value> clone();

};
