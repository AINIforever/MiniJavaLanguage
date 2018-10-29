package Lexer;

public class Token {

    private String value;

    private TokenType tokenType;

    private int row;
    private int column;

    public static Token creator(String s, TokenType tokenType, int row, int column) {
        return new Token(s, tokenType, row, column);
    }

    private Token(String s, TokenType tokenType, int row, int column) {
        this.value = s;
        this.tokenType = tokenType;
        this.row = row;
        this.column = column;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%-20s %s", value, tokenType.toString());
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Token toNegative() {
        if (value.startsWith("-"))
            value = value.substring(1);
        else
            value = "-" + value;
        return this;
    }

}
