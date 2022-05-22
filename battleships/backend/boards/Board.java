package backend.boards;

import backend.utility.Coord;

import java.util.Iterator;

public interface Board<Value> {
    abstract class Row<Value> {

        public abstract Iterator<Value> iterator();

        public abstract Value get(int index);

        public abstract void set(int index, Value value);

    }

    abstract class Column<Value> {

        public abstract Iterator<Value> iterator();

        public abstract Value get(int index);

        public abstract void set(int index, Value value);

    }

    abstract class TransposedView<Value> {

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

    Coord getDimensions();

    Iterator<Row<Value>> iterator();

    Iterator<Column<Value>> columnIterator();

    TransposedView<Value> transpose();

};
