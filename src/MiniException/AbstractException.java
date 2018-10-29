package MiniException;

abstract class AbstractException extends Exception {

    int row;

    int column;

    AbstractException(int row, int column) {
        this.row = row;
        this.column = column;
    }

    protected int getRow() {
        return row;
    }

    protected int getColumn() {
        return column;
    }
}
