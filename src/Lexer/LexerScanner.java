package Lexer;

import MiniException.LexicalException;

import java.util.ArrayList;
import java.util.List;

public class LexerScanner {

    public static List<Token> scan(String source) throws LexicalException {

        LexerReader reader = new LexerReader(source);
        StringBuilder builder = new StringBuilder();
        List<Token> result = new ArrayList<>();

        while (reader.previewNext() != '\04') {
            builder.setLength(0);
            start(reader, reader.getRow(), reader.getColumn(), builder, result);
        }

        return result;
    }

    static class LexerReader {

        char[] source;
        int row;
        int column;
        int index;

        LexerReader(String source) {
            this.source = source.toCharArray();
            row = 0;
            column = 0;
            index = 0;
        }

        char next() {
            if (index == source.length)
                return '\04';
            char result = source[index];
            index++;
            if (index < source.length && source[index] == '\n') {
                row++;
                column = 0;
            } else
                column++;
            return result;
        }

        char previewNext() {
            if (index == source.length)
                return '\04';
            return source[index];
        }

        int getRow() {
            return row;
        }

        int getColumn() {
            return column;
        }
    }

    private static void start(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) throws LexicalException {
        char ch = reader.previewNext();
        if (Character.isLetter(ch)) {
            reader.next();
            builder.append(ch);
            identifierOrKeyword(reader, startRow, startColumn, builder, tokens);
        } else if (Character.isDigit(ch)) {
            reader.next();
            builder.append(ch);
            constInteger(reader, startRow, startColumn, builder, tokens);
        } else if (ch == '.') {
            reader.next();
            builder.append(ch);
            dot(reader, startRow, startColumn, builder, tokens);
        } else if (ch == '/') {
            reader.next();
            builder.append(ch);
            operationDivide(reader, startRow, startColumn, builder, tokens);
        } else if (ch == '\'') {
            reader.next();
            builder.append(ch);
            constCharBegin(reader, startRow, startColumn, builder, tokens);
        } else if (ch == '\"') {
            reader.next();
            builder.append(ch);
            constStringBegin(reader, startRow, startColumn, builder, tokens);
        } else if (ch == '+') {
            reader.next();
            builder.append(ch);
            operationPlus(reader, startRow, startColumn, builder, tokens);
        } else if (ch == '-') {
            reader.next();
            builder.append(ch);
            operationMinus(reader, startRow, startColumn, builder, tokens);
        } else if (ch == '*') {
            reader.next();
            builder.append(ch);
            operationMultiply(reader, startRow, startColumn, builder, tokens);
        } else if (ch == '%') {
            reader.next();
            builder.append(ch);
            operationMode(reader, startRow, startColumn, builder, tokens);
        } else if (ch == '~') {
            reader.next();
            builder.append(ch);
            operationBitNot(reader, startRow, startColumn, builder, tokens);
        } else if (ch == '|') {
            reader.next();
            builder.append(ch);
            operationBitOr(reader, startRow, startColumn, builder, tokens);
        } else if (ch == '&') {
            reader.next();
            builder.append(ch);
            operationBitAnd(reader, startRow, startColumn, builder, tokens);
        } else if (ch == '>') {
            reader.next();
            builder.append(ch);
            operationGreater(reader, startRow, startColumn, builder, tokens);
        } else if (ch == '<') {
            reader.next();
            builder.append(ch);
            operationLess(reader, startRow, startColumn, builder, tokens);
        } else if (ch == '=') {
            reader.next();
            builder.append(ch);
            operationAssignment(reader, startRow, startColumn, builder, tokens);
        } else if (ch == '!') {
            reader.next();
            builder.append(ch);
            operationBooleanNot(reader, startRow, startColumn, builder, tokens);
        } else if (ch == '{') {
            reader.next();
            builder.append(ch);
            operationLeftBigBracket(reader, startRow, startColumn, builder, tokens);
        } else if (ch == '}') {
            reader.next();
            builder.append(ch);
            operationRightBigBracket(reader, startRow, startColumn, builder, tokens);
        } else if (ch == '[') {
            reader.next();
            builder.append(ch);
            operationLeftMiddleBracket(reader, startRow, startColumn, builder, tokens);
        } else if (ch == ']') {
            reader.next();
            builder.append(ch);
            operationRightMiddleBracket(reader, startRow, startColumn, builder, tokens);
        } else if (ch == '(') {
            reader.next();
            builder.append(ch);
            operationLeftSmallBracket(reader, startRow, startColumn, builder, tokens);
        } else if (ch == ')') {
            reader.next();
            builder.append(ch);
            operationRightSmallBracket(reader, startRow, startColumn, builder, tokens);
        } else if (ch == ' ' || ch == '\n' || ch == '\t') {
            reader.next();
        } else if (ch == '\004') {
        }
        else
            throw new LexicalException(startRow, startColumn, LexicalException.Type.UNEXPECTED_CHAR, builder.append(ch).toString());
    }

    private static void identifierOrKeyword(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) throws LexicalException {
        char ch = reader.previewNext();
        if (Character.isLetter(ch) || Character.isDigit(ch)) {
            reader.next();
            builder.append(ch);
            identifierOrKeyword(reader, startRow, startColumn, builder, tokens);
        } else {
            String value = builder.toString();
            if (TokenType.keyWordTable.containsKey(value)) {
                generateToken(TokenType.keyWordTable.get(value), startRow, startColumn, builder, tokens);
            } else if (TokenType.reservedWordTable.containsKey(value)) {
                throw new LexicalException(startRow, startColumn, LexicalException.Type.RESERVED_KEYWORD, value);
            } else
                generateToken(TokenType.IDENTIFIER, startRow, startColumn, builder, tokens);
        }
    }

    private static void constInteger(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        char ch = reader.previewNext();
        if (Character.isDigit(ch)) {
            reader.next();
            builder.append(ch);
            constInteger(reader, startRow, startColumn, builder, tokens);
        } else if (ch == '.') {
            reader.next();
            builder.append(ch);
            constDouble(reader, startRow, startColumn, builder, tokens);
        } else {
            generateToken(TokenType.CONST_INTEGER, startRow, startColumn, builder, tokens);
        }
    }

    private static void constDouble(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        char ch = reader.previewNext();
        if (Character.isDigit(ch)) {
            reader.next();
            builder.append(ch);
            constDouble(reader, startRow, startColumn, builder, tokens);
        } else {
            generateToken(TokenType.CONST_DOUBLE, startRow, startColumn, builder, tokens);
        }
    }

    private static void dot(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        char ch = reader.previewNext();
        if (Character.isDigit(ch)) {
            reader.next();
            builder.append(ch);
            constDouble(reader, startRow, startColumn, builder, tokens);
        }
        else
            generateToken(TokenType.operatorTable.get("."), startRow, startColumn, builder, tokens);
    }

    private static void operationDivide(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) throws LexicalException {
        char ch = reader.previewNext();
        if (ch == '/') {
            reader.next();
            builder.append(ch);
            singleLineComment(reader, startRow, startColumn, builder, tokens);
        } else if (ch == '*') {
            reader.next();
            builder.append(ch);
            multiLineCommentBegin(reader, startRow, startColumn, builder, tokens);
        } else
            generateToken(TokenType.operatorTable.get("/"), startRow, startColumn, builder, tokens);
    }

    private static void singleLineComment(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        char ch = reader.previewNext();
        if (ch != '\n' && ch != '\04') {
            reader.next();
            builder.append(ch);
            singleLineComment(reader, startRow, startColumn, builder, tokens);
        }
        else {
            // single line ends
            reader.next();
            generateToken(TokenType.SINGLE_LINE_COMMENT, startRow, startColumn, builder, tokens);
        }
    }

    private static void multiLineCommentBegin(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) throws LexicalException {
        char ch = reader.previewNext();
        if (ch == '*') {
            reader.next();
            builder.append(ch);
            multiLineCommentEnd(reader, startRow, startColumn, builder, tokens);
        } else if (ch != '\04') {
            reader.next();
            builder.append(ch);
            multiLineCommentBegin(reader, startRow, startColumn, builder, tokens);
        } else {
            throw new LexicalException(startRow, startColumn, LexicalException.Type.INCOMPLETE_MULTI_LINE_COMMENT, builder.toString());
        }
    }

    private static void multiLineCommentEnd(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) throws LexicalException {
        char ch = reader.previewNext();
        if (ch == '/') {
            reader.next();
            builder.append(ch);
            generateToken(TokenType.MULTI_LINE_COMMENT, startRow, startColumn, builder, tokens);
        } else if (ch == '\04') {
            throw new LexicalException(startRow, startColumn, LexicalException.Type.INCOMPLETE_MULTI_LINE_COMMENT, builder.toString());
        } else {
            multiLineCommentBegin(reader, startRow, startColumn, builder, tokens);
        }
    }

    private static void constCharBegin(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) throws LexicalException {
        char ch = reader.previewNext();
        if (ch == '\04')
            throw new LexicalException(startRow, startColumn, LexicalException.Type.INCOMPLETE_CONST_CHAR, builder.toString());
        if (ch == '\n')
            throw new LexicalException(startRow, startColumn, LexicalException.Type.MULTI_LINE_CONST_CHAR, builder.toString());
        reader.next();
        builder.append(ch);
        constChar(reader, startRow, startColumn, builder, tokens);
    }

    private static void constChar(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) throws LexicalException {
        char ch = reader.previewNext();
        if (ch == '\04')
            throw new LexicalException(startRow, startColumn, LexicalException.Type.INCOMPLETE_CONST_CHAR, builder.toString());
        if (ch == '\n')
            throw new LexicalException(startRow, startColumn, LexicalException.Type.MULTI_LINE_CONST_CHAR, builder.toString());
        if (ch == '\'') {
            reader.next();
            builder.append(ch);
            constCharEnd(reader, startRow, startColumn, builder, tokens);
        } else
            throw new LexicalException(startRow, startColumn, LexicalException.Type.CONST_CHAR_TOO_MANY, builder.toString());
    }

    private static void constCharEnd(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.CONST_CHAR, startRow, startColumn, builder, tokens);
    }

    private static void constStringBegin(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) throws LexicalException {
        char ch = reader.previewNext();
        if (ch == '\004') {
            throw new LexicalException(startRow, startColumn, LexicalException.Type.INCOMPLETE_CONST_STRING, builder.toString());
        } else if (ch == '\"') {
            reader.next();
            builder.append(ch);
            constStringEnd(reader, startRow, startColumn, builder, tokens);
        } else {
            reader.next();
            builder.append(ch);
            constStringBegin(reader, startRow, startColumn, builder, tokens);
        }
    }

    private static void constStringEnd(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.CONST_STRING, startRow, startColumn, builder, tokens);
    }

    private static void operationPlus(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("+"), startRow, startColumn, builder, tokens);
    }

    private static void operationMinus(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("-"), startRow, startColumn, builder, tokens);
    }

    private static void operationMultiply(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("*"), startRow, startColumn, builder, tokens);
    }

    private static void operationMode(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("%"), startRow, startColumn, builder, tokens);
    }

    private static void operationAssignment(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        char ch = reader.previewNext();
        if (ch == '=' ) {
            reader.next();
            builder.append(ch);
            operationEqual(reader, startRow, startColumn, builder, tokens);
        }
        else
            generateToken(TokenType.operatorTable.get("="), startRow, startColumn, builder, tokens);
    }

    private static void operationEqual(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("=="), startRow, startColumn, builder, tokens);
    }

    private static void operationLess(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        char ch = reader.previewNext();
        if (ch == '=' ) {
            reader.next();
            builder.append(ch);
            operationLessOrEqual(reader, startRow, startColumn, builder, tokens);
        }
        else
            generateToken(TokenType.operatorTable.get("<"), startRow, startColumn, builder, tokens);
    }

    private static void operationLessOrEqual(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("<="), startRow, startColumn, builder, tokens);
    }

    private static void operationGreater(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        char ch = reader.previewNext();
        if (ch == '=' ) {
            reader.next();
            builder.append(ch);
            operationGreaterOrEqual(reader, startRow, startColumn, builder, tokens);
        }
        else
            generateToken(TokenType.operatorTable.get(">"), startRow, startColumn, builder, tokens);
    }

    private static void operationGreaterOrEqual(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get(">="), startRow, startColumn, builder, tokens);
    }

    private static void operationBooleanNot(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        char ch = reader.previewNext();
        if (ch == '=' ) {
            reader.next();
            builder.append(ch);
            operationBooleanNotEqual(reader, startRow, startColumn, builder, tokens);
        }
        else
            generateToken(TokenType.operatorTable.get("!"), startRow, startColumn, builder, tokens);
    }

    private static void operationBooleanNotEqual(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("!="), startRow, startColumn, builder, tokens);
    }

    private static void operationBitOr(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        char ch = reader.previewNext();
        if (ch == '|' ) {
            reader.next();
            builder.append(ch);
            operationBooleanOr(reader, startRow, startColumn, builder, tokens);
        }
        else
            generateToken(TokenType.operatorTable.get("|"), startRow, startColumn, builder, tokens);
    }

    private static void operationBooleanOr(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("||"), startRow, startColumn, builder, tokens);
    }

    private static void operationBitAnd(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        char ch = reader.previewNext();
        if (ch == '&' ) {
            reader.next();
            builder.append(ch);
            operationBooleanAnd(reader, startRow, startColumn, builder, tokens);
        }
        else
            generateToken(TokenType.operatorTable.get("&"), startRow, startColumn, builder, tokens);
    }

    private static void operationBooleanAnd(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("&&"), startRow, startColumn, builder, tokens);
    }

    private static void operationBitNot(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("~"), startRow, startColumn, builder, tokens);
    }

    private static void operationLeftBigBracket(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("{"), startRow, startColumn, builder, tokens);
    }

    private static void operationRightBigBracket(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("}"), startRow, startColumn, builder, tokens);
    }

    private static void operationLeftMiddleBracket(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("["), startRow, startColumn, builder, tokens);
    }

    private static void operationRightMiddleBracket(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("]"), startRow, startColumn, builder, tokens);
    }

    private static void operationLeftSmallBracket(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("("), startRow, startColumn, builder, tokens);
    }

    private static void operationRightSmallBracket(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get(")"), startRow, startColumn, builder, tokens);
    }

    private static void generateToken(TokenType tokenType, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        String value = builder.toString();
        tokens.add(Token.creator(value, tokenType, startRow, startColumn));
    }

}
