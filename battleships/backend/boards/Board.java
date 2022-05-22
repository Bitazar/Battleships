package backend.boards;

import backend.utility.Coord;

import java.util.Iterator;

public interface Board<Value> {
    void generateCell(Coord position, Value value);

    void setValue(Coord position, Value value);

    Value accessCell(Coord position);

    Coord getDimensions();
};
