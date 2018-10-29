package Lexer;

import MiniException.LexicalException;

import java.util.ArrayList;

public class LexerScanner {

    String resource;

    int row;

    int column;

    int index;

    int startRow;

    int startColumn;

    StringBuilder builder;

    private ArrayList<Token> tokens;

    public LexerScanner(String resource) throws LexicalException {
        this.resource = resource;
        row = 0;
        column = -1;
        index = -1;
        builder = new StringBuilder();
        tokens = new ArrayList<>();
        start(getNextChar());
    }

    private char getNextChar() {
        if (index > -1 && resource.charAt(index) == '\n') {
            row++;
            column = -1;
        }
        column++;
        index++;
        if (index == resource.length())
            return '\04';
        return resource.charAt(index);
    }

    private void start(char ch) throws LexicalException {
        startRow = row;
        startColumn = column;
        builder.setLength(0);
        if (Character.isLetter(ch)) {
            builder.append(ch);
            identifierOrKeyword(getNextChar());
        } else {
            if (ch == ' ')
                start(getNextChar());
        }
    }

    private void identifierOrKeyword(char ch) throws LexicalException {
        if (Character.isLetter(ch) || Character.isDigit(ch)) {
            builder.append(ch);
            identifierOrKeyword(getNextChar());
        } else {
            generateIdentifierOrKeyword();
            start(ch);
        }
    }

    private void generateIdentifierOrKeyword() throws LexicalException {
        String value = builder.toString();
        if (TokenType.keyWordTable.containsKey(value)) {
            tokens.add(Token.creator(value, TokenType.keyWordTable.get(value), startRow, startColumn));
        } else if (TokenType.reservedWordTable.containsKey(value)) {
            throw new LexicalException(startRow, startColumn, LexicalException.Type.RESERVED_KEYWORD, value);
        } else
            tokens.add(Token.creator(value, TokenType.IDENTIFIER, startRow, startColumn));
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }
}
