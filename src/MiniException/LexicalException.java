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
        public static final Type INCOMPLETE_MULTI_LINE_COMMENT = new Type(1003, "not found close symbol \"*/\".");
        public static final Type INCOMPLETE_CONST_CHAR = new Type(1004, "not found close symbol \"'\".");
        public static final Type MULTI_LINE_CONST_CHAR = new Type(1005, "const char should be in one line");
        public static final Type CONST_CHAR_TOO_MANY = new Type(1005, "one const char only has one char");
        public static final Type INCOMPLETE_CONST_STRING = new Type(1004, "not found close symbol \"\"\".");

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
