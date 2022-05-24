package backend.boards;

import backend.utility.Coord;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class BoardDTO implements Board<Integer> {
    private final Integer[][]                       board;
    private final Coord                             dimensions;

    public BoardDTO(Coord dimensions) {
        this.dimensions = dimensions;
        board = new Integer[dimensions.getY()][dimensions.getX()];
    }

    public static class Row extends Board.Row<Integer> {
        private final BoardDTO                      board;
        private final int                           rowID;

        public Row(BoardDTO board, int rowID) {
            this.board = board;
            this.rowID = rowID;
        }

        @Override
        public Iterator<Integer> iterator() {
            return Arrays.stream(board.board[rowID]).iterator();
        }

        @Override
        public Integer get(int index) throws IndexOutOfBoundsException {
            return board.board[rowID][index];
        }

        @Override
        public void set(int index, Integer value) throws IndexOutOfBoundsException {
            board.board[rowID][index] = value;
        }

        @Override
        public int getSize() {
            return board.getWidth();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Row integers)) return false;
            return rowID == integers.rowID && board.equals(integers.board);
        }

        @Override
        public int hashCode() {
            return Objects.hash(board, rowID);
        }
    }

    public static class Column extends Board.Column<Integer> {
        private final BoardDTO                      board;
        private final int                           columnID;

        public Column(BoardDTO board, int columnID) {
            this.board = board;
            this.columnID = columnID;
        }

        public static class ColumnIterator implements Iterator<Integer> {
            private final BoardDTO                  board;
            private final int                       columnID;
            private int                             index = 0;

            public ColumnIterator(BoardDTO board, int columnID) {
                this.board = board;
                this.columnID = columnID;
            }

            @Override
            public void remove() throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Cannot remove element from the fixed size array");
            }

            @Override
            public boolean hasNext() {
                return index < board.getHeight();
            }

            @Override
            public Integer next() {
                return board.board[index++][columnID];
            }
        }

        @Override
        public Iterator<Integer> iterator() {
            return new ColumnIterator(board, columnID);
        }

        @Override
        public Integer get(int index) {
            return board.board[index][columnID];
        }

        @Override
        public void set(int index, Integer integer) {
            board.board[index][columnID] = integer;
        }

        @Override
        public int getSize() {
            return board.getHeight();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Column integers)) return false;
            return columnID == integers.columnID && board.equals(integers.board);
        }

        @Override
        public int hashCode() {
            return Objects.hash(board, columnID);
        }
    }

    public static class TransposedView extends Board.TransposedView<Integer> {
        private final BoardDTO                      board;

        public TransposedView(BoardDTO board) {
            this.board = board;
        }

        @Override
        public Iterator<Board.Column<Integer>> iterator() {
            return board.columnIterator();
        }

        @Override
        public Iterator<Board.Row<Integer>> rowIterator() {
            return board.iterator();
        }

        @Override
        public void generateCell(Coord position, Integer integer) {
            board.generateCell(position.transpose(), integer);
        }

        @Override
        public void setValue(Coord position, Integer integer) {
            board.setValue(position.transpose(), integer);
        }

        @Override
        public Integer accessCell(Coord position) {
            return board.accessCell(position.transpose());
        }

        @Override
        public Coord getDimensions() {
            return board.dimensions.transpose();
        }

        @Override
        public Board<Integer> transpose() {
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

    public static class RowIterator implements Iterator<Board.Row<Integer>> {
        private final BoardDTO              board;
        private int                         index = 0;

        public RowIterator(BoardDTO board) {
            this.board = board;
        }

        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Cannot remove element from the fixed size array");
        }

        @Override
        public boolean hasNext() {
            return index < board.getHeight();
        }

        @Override
        public Board.Row<Integer> next() {
            return new Row(board, index++);
        }
    }

    public static class ColumnIterator implements Iterator<Board.Column<Integer>> {
        private final BoardDTO              board;
        private int                         index = 0;

        public ColumnIterator(BoardDTO board) {
            this.board = board;
        }

        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Cannot remove element from the fixed size array");
        }

        @Override
        public boolean hasNext() {
            return index < board.getWidth();
        }

        @Override
        public Board.Column<Integer> next() {
            return new Column(board, index++);
        }
    }

    @Override
    public void generateCell(Coord position, Integer integer) {
        board[position.getY()][position.getX()] = integer;
    }

    @Override
    public void setValue(Coord position, Integer integer) {
        generateCell(position, integer);
    }

    @Override
    public Integer accessCell(Coord position) {
        return board[position.getY()][position.getX()];
    }

    @Override
    public Coord getDimensions() {
        return dimensions;
    }

    @Override
    public int getWidth() {
        return dimensions.getX();
    }

    @Override
    public int getHeight() {
        return dimensions.getY();
    }

    @Override
    public Iterator<Board.Row<Integer>> iterator() {
        return new RowIterator(this);
    }

    @Override
    public Iterator<Board.Column<Integer>> columnIterator() {
        return new ColumnIterator(this);
    }

    @Override
    public Board.TransposedView<Integer> transpose() {
        return new TransposedView(this);
    }

    @Override
    public boolean onBoard(Coord position) {
        int x = position.getX(), y = position.getY();
        int lx = dimensions.getX(), ly = dimensions.getY();
        return x >= 0 && y >= 0 && lx > x && ly > y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardDTO rows)) return false;
        return Arrays.deepEquals(board, rows.board) && dimensions.equals(rows.dimensions);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(dimensions);
        result = 31 * result + Arrays.deepHashCode(board);
        return result;
    }

    public Board<Integer> clone() {
        BoardDTO board = new BoardDTO(dimensions);
        for (int y = 0; y < dimensions.getY(); ++y) {
            for (int x = 0; x < dimensions.getX(); ++x) {
                board.setValue(new Coord(x, y), this.board[y][x]);
            }
        }
        return board;
    }

}
