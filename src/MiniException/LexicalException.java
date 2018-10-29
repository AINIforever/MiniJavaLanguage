package MiniException;

public class LexicalException extends AbstractException {

    public static class Type {

        private int type;

        private String description;

        private Type(int type, String description) {
            this.type = type;
            this.description = description;
        }

        public static final Type UNEXPECTED_CHAR = new Type(1001, "unexpected char.");
        public static final Type RESERVED_KEYWORD = new Type(1002, "is a reserved keyword.");
    }

    private Type type;

    private String value;

    public LexicalException(int row, int column, Type type, String value) {
        super(row, column);
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("Lexer ERROR %d (%d, %d): \"%s\" %s", type.type, getRow(), getColumn(), value, type.description);
    }
}
