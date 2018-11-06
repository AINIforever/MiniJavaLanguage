package Lexer;

public class Token {

    private String text;

    private TokenType tokenType;

    private Object value;

    private int row;
    private int column;

    public static Token creator(String s, TokenType tokenType, int row, int column, Object value) {
        return new Token(s, tokenType, row, column, value);
    }

    private Token(String text, TokenType tokenType, int row, int column, Object value) {
        this.text = text;
        this.tokenType = tokenType;
        this.row = row;
        this.column = column;
        this.value = value;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return String.format("%-20s %-10s %-10s", text, null == value ? "":value.toString(), tokenType.toString());
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

}
