package backend.boards;

import backend.utility.Coord;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class BoardDTO implements Board<Integer> {
    private final Integer[][]                     board;
    private final Coord                           dimensions;

    public BoardDTO(Coord dimensions) {
        board = new Integer[dimensions.y][dimensions.x];
        this.dimensions = dimensions;
    }

    @Override
    public void generateCell(Coord position, Integer value) {
        board[position.y][position.x] = value;
    }

    @Override
    public void setValue(Coord position, Integer value) {
        board[position.y][position.x] = value;
    }

    @Override
    public Coord getDimensions() {
        return dimensions;
    }

    @Override
    public Integer accessCell(Coord position) {
        return board[position.y][position.x];
    }

    public Iterator<Integer[]> iterator() {
        return Arrays.stream(board).iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardDTO boardDTO)) return false;
        return Arrays.deepEquals(board, boardDTO.board) && dimensions.equals(boardDTO.dimensions);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(dimensions);
        result = 31 * result + Arrays.deepHashCode(board);
        return result;
    }
}
