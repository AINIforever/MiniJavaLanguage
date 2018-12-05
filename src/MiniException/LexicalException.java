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
        public static final Type MULTI_LINE_CONST_CHAR = new Type(1005, "const char should be in one line.");
        public static final Type MULTI_LINE_CONST_STRING = new Type(1006, "const string should be in one line.");
        public static final Type CONST_CHAR_TOO_MANY = new Type(1007, "one const char only has one char.");
        public static final Type INCOMPLETE_CONST_STRING = new Type(1008, "not found close symbol \"\"\".");
        public static final Type CONST_CHAR_NOTHING = new Type(1009, "const char should have one char.");
        public static final Type WRONG_TRANSFERRED_CHAR = new Type(1010, "don't have this transferred char.");
        public static final Type OCTAL_TRANSFERRED_TOO_BIG = new Type(1011, "octal transferred char should be in 0-127.");
        public static final Type WRONG_HEXADECIMAL_TRANSFERRED = new Type(1012, "hexadecimal transferred must have 4 numbers");
        public static final Type INTEGER_OUT_OF_RANGE = new Type(1013, "integer number too large");
        public static final Type DOUBLE_OUT_OF_RANGE = new Type(1014, "double number too large");
        public static final Type UNEXPECTED_CHAR_IN_OCT_NUMBER = new Type(1015, "unexpected char in oct number");
        public static final Type ILLEGAL_HEX_NUMBER = new Type(1016, "illegal hex number");

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
