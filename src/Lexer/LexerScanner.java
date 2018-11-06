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
        } else if (ch == '0') {
            reader.next();
            builder.append(ch);
            constOctIntegerBegin(reader, startRow, startColumn, builder, tokens);
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
            constCharSingleCharBegin(reader, startRow, startColumn, builder, tokens);
        } else if (ch == '\"') {
            reader.next();
            builder.append(ch);
            constStringBegin(reader, startRow, startColumn, builder, tokens, new StringBuilder());
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
        } else if (ch == ';') {
            reader.next();
            builder.append(ch);
            operationSemicolon(reader, startRow, startColumn, builder, tokens);
        }
        else if (ch == '\004') {
        }
        else
            throw new LexicalException(startRow, startColumn, LexicalException.Type.UNEXPECTED_CHAR, builder.append(ch).toString());
    }

    private static void constOctIntegerBegin(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) throws LexicalException {
        char ch = reader.previewNext();
        if (ch == 'x' || ch == 'X') {
            reader.next();
            builder.append(ch);
            constHexInteger(reader, startRow, startColumn, builder, tokens, new StringBuilder());
        } else if (ch >= '0' && ch <= '7') {
            reader.next();
            builder.append(ch);
            constOctInteger(reader, startRow, startColumn, builder, tokens, new StringBuilder(String.valueOf(ch)));
        } else
            generateToken(TokenType.CONST_INTEGER, startRow, startColumn, builder, tokens, 0);
    }

    private static void constOctInteger(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens, StringBuilder value) throws LexicalException {
        char ch = reader.previewNext();
        int temp;
        if (ch >= '0' && ch <= '7') {
            reader.next();
            builder.append(ch);
            constOctInteger(reader, startRow, startColumn, builder, tokens, value.append(ch));
        } else {
            try {
                temp = Integer.parseInt(value.toString(), 8);
            } catch (Exception e) {
                throw new LexicalException(startRow, startColumn, LexicalException.Type.INTEGER_OUT_OF_RANGE, builder.toString());
            }
            generateToken(TokenType.CONST_INTEGER, startRow, startColumn, builder, tokens, temp);
        }
    }

    private static void constHexInteger(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens, StringBuilder value) throws LexicalException {
        char ch = reader.previewNext();
        int temp;
        if (ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'f' || ch >= 'A' && ch <= 'F' ) {
            reader.next();
            builder.append(ch);
            constHexInteger(reader, startRow, startColumn, builder, tokens, value.append(ch));
        } else {
            if (value.length() == 0)
                throw new LexicalException(startRow, startColumn, LexicalException.Type.ILLEGAL_HEX_NUMBER, builder.toString());
            try {
                temp = Integer.parseInt(value.toString(), 16);
            } catch (Exception e) {
                throw new LexicalException(startRow, startColumn, LexicalException.Type.INTEGER_OUT_OF_RANGE, builder.toString());
            }
            generateToken(TokenType.CONST_INTEGER, startRow, startColumn, builder, tokens, temp);
        }
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
                generateToken(TokenType.keyWordTable.get(value), startRow, startColumn, builder, tokens, null);
            } else if (TokenType.reservedWordTable.containsKey(value)) {
                throw new LexicalException(startRow, startColumn, LexicalException.Type.RESERVED_KEYWORD, value);
            } else
                generateToken(TokenType.IDENTIFIER, startRow, startColumn, builder, tokens, null);
        }
    }

    private static void constInteger(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) throws LexicalException {
        char ch = reader.previewNext();
        int temp;
        if (Character.isDigit(ch)) {
            reader.next();
            builder.append(ch);
            constInteger(reader, startRow, startColumn, builder, tokens);
        } else if (ch == '.') {
            reader.next();
            builder.append(ch);
            constDouble(reader, startRow, startColumn, builder, tokens);
        } else {
            try {
                temp = Integer.parseInt(builder.toString());
            } catch (Exception e) {
                throw new LexicalException(startRow, startColumn, LexicalException.Type.INTEGER_OUT_OF_RANGE, builder.toString());
            }
            generateToken(TokenType.CONST_INTEGER, startRow, startColumn, builder, tokens, temp);
        }
    }

    private static void constDouble(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) throws LexicalException {
        char ch = reader.previewNext();
        double temp;
        if (Character.isDigit(ch)) {
            reader.next();
            builder.append(ch);
            constDouble(reader, startRow, startColumn, builder, tokens);
        } else {
            try {
                temp = Double.parseDouble(builder.toString());
            } catch (Exception e) {
                throw new LexicalException(startRow, startColumn, LexicalException.Type.DOUBLE_OUT_OF_RANGE, builder.toString());
            }
            generateToken(TokenType.CONST_DOUBLE, startRow, startColumn, builder, tokens, temp);
        }
    }

    private static void dot(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) throws LexicalException {
        char ch = reader.previewNext();
        if (Character.isDigit(ch)) {
            reader.next();
            builder.append(ch);
            constDouble(reader, startRow, startColumn, builder, tokens);
        }
        else
            generateToken(TokenType.operatorTable.get("."), startRow, startColumn, builder, tokens, null);
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
            generateToken(TokenType.operatorTable.get("/"), startRow, startColumn, builder, tokens, null);
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
            generateToken(TokenType.SINGLE_LINE_COMMENT, startRow, startColumn, builder, tokens, null);
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
            generateToken(TokenType.MULTI_LINE_COMMENT, startRow, startColumn, builder, tokens, null);
        } else if (ch == '\04') {
            throw new LexicalException(startRow, startColumn, LexicalException.Type.INCOMPLETE_MULTI_LINE_COMMENT, builder.toString());
        } else {
            multiLineCommentBegin(reader, startRow, startColumn, builder, tokens);
        }
    }

    private static void constCharSingleCharBegin(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) throws LexicalException {
        char ch = reader.previewNext();
        if (ch == '\04')
            throw new LexicalException(startRow, startColumn, LexicalException.Type.INCOMPLETE_CONST_CHAR, builder.toString());
        if (ch == '\n')
            throw new LexicalException(startRow, startColumn, LexicalException.Type.MULTI_LINE_CONST_CHAR, builder.toString());
        if (ch == '\\') {
            reader.next();
            builder.append(ch);
            constCharSingleCharTransferred(reader, startRow, startColumn, builder, tokens);
        } else if (ch == '\'')
            throw new LexicalException(startRow, startColumn, LexicalException.Type.CONST_CHAR_NOTHING, builder.append(ch).toString());
        else {
            reader.next();
            builder.append(ch);
            constChar(reader, startRow, startColumn, builder, tokens, ch);
        }
    }

    private static void constCharSingleCharTransferred(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) throws LexicalException {
        char ch = reader.previewNext();
        if (ch == 'b' || ch == 'f' || ch == 'r' || ch == 'n' || ch == 't' || ch == '\'' || ch == '\"' || ch == '\\') {
            char value = '\b';
            switch (ch) {
                case 'f':
                    value = '\f';
                    break;
                case 'r':
                    value = '\r';
                    break;
                case 'n':
                    value = '\n';
                    break;
                case 't':
                    value = '\t';
                    break;
                case '\\':
                    value = '\\';
                    break;
                case '\'':
                    value = '\'';
                    break;
                case '\"':
                    value = '\"';
                    break;
            }
            reader.next();
            builder.append(ch);
            constChar(reader, startRow, startColumn, builder, tokens, value);
        } else if (ch >= '0' && ch <= '7') {
            reader.next();
            builder.append(ch);
            constCharSingleCharTransferredDDD1(reader, startRow, startColumn, builder, tokens, (char) (ch - '0'));
        } else if (ch == 'u') {
            reader.next();
            builder.append(ch);
            constCharSingleCharTransferredUUUU(reader, startRow, startColumn, builder, tokens);
        } else
            throw new LexicalException(startRow, startColumn, LexicalException.Type.WRONG_TRANSFERRED_CHAR, builder.append(ch).toString());
    }

    private static void constCharSingleCharTransferredDDD1(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens, char number) throws LexicalException {
        char ch = reader.previewNext();
        if (ch >= '0' && ch <= '7') {
            reader.next();
            builder.append(ch);
            constCharSingleCharTransferredDDD2(reader, startRow, startColumn, builder, tokens, (char) (number * 8 + (ch - '0')));
        } else
            constChar(reader, startRow, startColumn, builder, tokens, number);
    }

    private static void constCharSingleCharTransferredDDD2(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens, char number) throws LexicalException {
        char ch = reader.previewNext();
        if (ch >= '0' && ch <= '7') {
            reader.next();
            builder.append(ch);
            constCharSingleCharTransferredDDD3(reader, startRow, startColumn, builder, tokens, (char) (number * 8 + (ch - '0')));
        } else
            constChar(reader, startRow, startColumn, builder, tokens, number);
    }

    private static void constCharSingleCharTransferredDDD3(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens, char number) throws LexicalException {
        if (number <= 127)
            constChar(reader, startRow, startColumn, builder, tokens, number);
        else
            throw new LexicalException(startRow, startColumn, LexicalException.Type.OCTAL_TRANSFERRED_TOO_BIG, builder.toString());
    }

    private static void constCharSingleCharTransferredUUUU(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) throws LexicalException {
        char ch = reader.previewNext();
        if (ch >= '0' && ch <= '9' || ch >= 'A' && ch <= 'F' || ch >= 'a' && ch <= 'f') {
            reader.next();
            builder.append(ch);
            if (ch <= '9')
                constCharSingleCharTransferredUUUU1(reader, startRow, startColumn, builder, tokens, (char) (ch - '0'));
            else
                constCharSingleCharTransferredUUUU1(reader, startRow, startColumn, builder, tokens, (char) (Character.toUpperCase(ch) - 'A' + 10));
        } else
            throw new LexicalException(startRow, startColumn, LexicalException.Type.WRONG_HEXADECIMAL_TRANSFERRED, builder.append(ch).toString());
    }

    private static void constCharSingleCharTransferredUUUU1(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens, char number) throws LexicalException {
        char ch = reader.previewNext();
        if (ch >= '0' && ch <= '9' || ch >= 'A' && ch <= 'F' || ch >= 'a' && ch <= 'f') {
            reader.next();
            builder.append(ch);
            if (ch <= '9')
                constCharSingleCharTransferredUUUU2(reader, startRow, startColumn, builder, tokens, (char) (ch - '0' + number * 16));
            else
                constCharSingleCharTransferredUUUU2(reader, startRow, startColumn, builder, tokens, (char) (Character.toUpperCase(ch) - 'A' + 10 + number * 16));
        } else
            throw new LexicalException(startRow, startColumn, LexicalException.Type.WRONG_HEXADECIMAL_TRANSFERRED, builder.append(ch).toString());
    }

    private static void constCharSingleCharTransferredUUUU2(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens, char number) throws LexicalException {
        char ch = reader.previewNext();
        if (ch >= '0' && ch <= '9' || ch >= 'A' && ch <= 'F' || ch >= 'a' && ch <= 'f') {
            reader.next();
            builder.append(ch);
            if (ch <= '9')
                constCharSingleCharTransferredUUUU3(reader, startRow, startColumn, builder, tokens, (char) (ch - '0' + number * 16));
            else
                constCharSingleCharTransferredUUUU3(reader, startRow, startColumn, builder, tokens, (char) (Character.toUpperCase(ch) - 'A' + 10 + number * 16));
        } else
            throw new LexicalException(startRow, startColumn, LexicalException.Type.WRONG_HEXADECIMAL_TRANSFERRED, builder.append(ch).toString());
    }

    private static void constCharSingleCharTransferredUUUU3(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens, char number) throws LexicalException {
        char ch = reader.previewNext();
        if (ch >= '0' && ch <= '9' || ch >= 'A' && ch <= 'F' || ch >= 'a' && ch <= 'f') {
            reader.next();
            builder.append(ch);
            if (ch <= '9')
                constCharSingleCharTransferredUUUU4(reader, startRow, startColumn, builder, tokens, (char) (ch - '0' + number * 16));
            else
                constCharSingleCharTransferredUUUU4(reader, startRow, startColumn, builder, tokens, (char) (Character.toUpperCase(ch) - 'A' + 10 + number * 16));
        } else
            throw new LexicalException(startRow, startColumn, LexicalException.Type.WRONG_HEXADECIMAL_TRANSFERRED, builder.append(ch).toString());
    }

    private static void constCharSingleCharTransferredUUUU4(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens, char number) throws LexicalException {
        constChar(reader, startRow, startColumn, builder, tokens, number);
    }

    private static void constChar(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens, char value) throws LexicalException {
        char ch = reader.previewNext();
        if (ch == '\04')
            throw new LexicalException(startRow, startColumn, LexicalException.Type.INCOMPLETE_CONST_CHAR, builder.toString());
        if (ch == '\n')
            throw new LexicalException(startRow, startColumn, LexicalException.Type.MULTI_LINE_CONST_CHAR, builder.toString());
        if (ch == '\'') {
            reader.next();
            builder.append(ch);
            constCharEnd(reader, startRow, startColumn, builder, tokens, value);
        } else
            throw new LexicalException(startRow, startColumn, LexicalException.Type.CONST_CHAR_TOO_MANY, builder.toString());
    }

    private static void constCharEnd(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens, char value) {
        generateToken(TokenType.CONST_CHAR, startRow, startColumn, builder, tokens, value);
    }

    private static void constStringBegin(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens, StringBuilder value) throws LexicalException {
        char ch = reader.previewNext();
        if (ch == '\004')
            throw new LexicalException(startRow, startColumn, LexicalException.Type.INCOMPLETE_CONST_STRING, builder.toString());
        if (ch == '\n')
            throw new LexicalException(startRow, startColumn, LexicalException.Type.MULTI_LINE_CONST_STRING, builder.toString());
        if (ch == '\"') {
            reader.next();
            builder.append(ch);
            constStringEnd(reader, startRow, startColumn, builder, tokens, value);
        } else if (ch == '\\') {
            reader.next();
            builder.append(ch);
            constStringSingleCharTransferred(reader, startRow, startColumn, builder, tokens, value);
        }
        else {
            reader.next();
            builder.append(ch);
            constStringBegin(reader, startRow, startColumn, builder, tokens, value.append(ch));
        }
    }

    private static void constStringSingleCharTransferred(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens, StringBuilder value) throws LexicalException {
        char ch = reader.previewNext();
        if (ch == 'b' || ch == 'f' || ch == 'r' || ch == 'n' || ch == 't' || ch == '\'' || ch == '\"' || ch == '\\') {
            char temp = '\b';
            switch (ch) {
                case 'f':
                    temp = '\f';
                    break;
                case 'r':
                    temp = '\r';
                    break;
                case 'n':
                    temp = '\n';
                    break;
                case 't':
                    temp = '\t';
                    break;
                case '\\':
                    temp = '\\';
                    break;
                case '\'':
                    temp = '\'';
                    break;
                case '\"':
                    temp = '\"';
                    break;
            }
            reader.next();
            builder.append(ch);
            constStringBegin(reader, startRow, startColumn, builder, tokens, value.append(temp));
        } else if (ch >= '0' && ch <= '7') {
            reader.next();
            builder.append(ch);
            constStringSingleCharTransferredDDD1(reader, startRow, startColumn, builder, tokens, value, (char) (ch - '0'));
        } else if (ch == 'u') {
            reader.next();
            builder.append(ch);
            constStringSingleCharTransferredUUUU(reader, startRow, startColumn, builder, tokens, value);
        } else
            throw new LexicalException(startRow, startColumn, LexicalException.Type.WRONG_TRANSFERRED_CHAR, builder.append(ch).toString());
    }

    private static void constStringSingleCharTransferredDDD1(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens, StringBuilder value, char number) throws LexicalException {
        char ch = reader.previewNext();
        if (ch >= '0' && ch <= '7') {
            reader.next();
            builder.append(ch);
            constStringSingleCharTransferredDDD2(reader, startRow, startColumn, builder, tokens, value, (char) (number * 8 + ch - '0'));
        } else
            constStringBegin(reader, startRow, startColumn, builder, tokens, value.append(number));
    }

    private static void constStringSingleCharTransferredDDD2(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens, StringBuilder value, char number) throws LexicalException {
        char ch = reader.previewNext();
        if (ch >= '0' && ch <= '7') {
            reader.next();
            builder.append(ch);
            constStringSingleCharTransferredDDD3(reader, startRow, startColumn, builder, tokens, value, (char) (number * 8 + ch - '0'));
        } else
            constStringBegin(reader, startRow, startColumn, builder, tokens, value.append(number));
    }

    private static void constStringSingleCharTransferredDDD3(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens, StringBuilder value, char number) throws LexicalException {
        if (number <= 127)
            constStringBegin(reader, startRow, startColumn, builder, tokens, value.append(number));
        else
            throw new LexicalException(startRow, startColumn, LexicalException.Type.OCTAL_TRANSFERRED_TOO_BIG, builder.toString());
    }

    private static void constStringSingleCharTransferredUUUU(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens, StringBuilder value) throws LexicalException {
        char ch = reader.previewNext();
        if (ch >= '0' && ch <= '9' || ch >= 'A' && ch <= 'F' || ch >= 'a' && ch <= 'f') {
            int bit;
            if (ch <= '9')
                bit = ch - '0';
            else
                bit = Character.toUpperCase(ch) - 'A' + 10;
            reader.next();
            builder.append(ch);
            constStringSingleCharTransferredUUUU1(reader, startRow, startColumn, builder, tokens, value, (char) bit);
        } else
            throw new LexicalException(startRow, startColumn, LexicalException.Type.WRONG_HEXADECIMAL_TRANSFERRED, builder.append(ch).toString());
    }

    private static void constStringSingleCharTransferredUUUU1(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens, StringBuilder value, char number) throws LexicalException {
        char ch = reader.previewNext();
        if (ch >= '0' && ch <= '9' || ch >= 'A' && ch <= 'F' || ch >= 'a' && ch <= 'f') {
            int bit;
            if (ch <= '9')
                bit = ch - '0';
            else
                bit = Character.toUpperCase(ch) - 'A' + 10;
            reader.next();
            builder.append(ch);
            constStringSingleCharTransferredUUUU2(reader, startRow, startColumn, builder, tokens, value, (char) (number * 16 + bit));
        } else
            throw new LexicalException(startRow, startColumn, LexicalException.Type.WRONG_HEXADECIMAL_TRANSFERRED, builder.append(ch).toString());
    }

    private static void constStringSingleCharTransferredUUUU2(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens, StringBuilder value, char number) throws LexicalException {
        char ch = reader.previewNext();
        if (ch >= '0' && ch <= '9' || ch >= 'A' && ch <= 'F' || ch >= 'a' && ch <= 'f') {
            int bit;
            if (ch <= '9')
                bit = ch - '0';
            else
                bit = Character.toUpperCase(ch) - 'A' + 10;
            reader.next();
            builder.append(ch);
            constStringSingleCharTransferredUUUU3(reader, startRow, startColumn, builder, tokens, value, (char) (number * 16 + bit));
        } else
            throw new LexicalException(startRow, startColumn, LexicalException.Type.WRONG_HEXADECIMAL_TRANSFERRED, builder.append(ch).toString());
    }

    private static void constStringSingleCharTransferredUUUU3(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens, StringBuilder value, char number) throws LexicalException {
        char ch = reader.previewNext();
        if (ch >= '0' && ch <= '9' || ch >= 'A' && ch <= 'F' || ch >= 'a' && ch <= 'f') {
            int bit;
            if (ch <= '9')
                bit = ch - '0';
            else
                bit = Character.toUpperCase(ch) - 'A' + 10;
            reader.next();
            builder.append(ch);
            constStringSingleCharTransferredUUUU4(reader, startRow, startColumn, builder, tokens, value, (char) (number * 16 + bit));
        } else
            throw new LexicalException(startRow, startColumn, LexicalException.Type.WRONG_HEXADECIMAL_TRANSFERRED, builder.append(ch).toString());
    }

    private static void constStringSingleCharTransferredUUUU4(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens, StringBuilder value, char number) throws LexicalException {
        constStringBegin(reader, startRow, startColumn, builder, tokens, value.append(number));
    }

    private static void constStringEnd(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens, StringBuilder value) {
        generateToken(TokenType.CONST_STRING, startRow, startColumn, builder, tokens, value.toString());
    }

    private static void operationPlus(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("+"), startRow, startColumn, builder, tokens, null);
    }

    private static void operationMinus(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("-"), startRow, startColumn, builder, tokens, null);
    }

    private static void operationMultiply(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("*"), startRow, startColumn, builder, tokens, null);
    }

    private static void operationMode(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("%"), startRow, startColumn, builder, tokens, null);
    }

    private static void operationAssignment(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        char ch = reader.previewNext();
        if (ch == '=' ) {
            reader.next();
            builder.append(ch);
            operationEqual(reader, startRow, startColumn, builder, tokens);
        }
        else
            generateToken(TokenType.operatorTable.get("="), startRow, startColumn, builder, tokens, null);
    }

    private static void operationEqual(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("=="), startRow, startColumn, builder, tokens, null);
    }

    private static void operationLess(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        char ch = reader.previewNext();
        if (ch == '=' ) {
            reader.next();
            builder.append(ch);
            operationLessOrEqual(reader, startRow, startColumn, builder, tokens);
        }
        else
            generateToken(TokenType.operatorTable.get("<"), startRow, startColumn, builder, tokens, null);
    }

    private static void operationLessOrEqual(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("<="), startRow, startColumn, builder, tokens, null);
    }

    private static void operationGreater(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        char ch = reader.previewNext();
        if (ch == '=' ) {
            reader.next();
            builder.append(ch);
            operationGreaterOrEqual(reader, startRow, startColumn, builder, tokens);
        }
        else
            generateToken(TokenType.operatorTable.get(">"), startRow, startColumn, builder, tokens, null);
    }

    private static void operationGreaterOrEqual(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get(">="), startRow, startColumn, builder, tokens, null);
    }

    private static void operationBooleanNot(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        char ch = reader.previewNext();
        if (ch == '=' ) {
            reader.next();
            builder.append(ch);
            operationBooleanNotEqual(reader, startRow, startColumn, builder, tokens);
        }
        else
            generateToken(TokenType.operatorTable.get("!"), startRow, startColumn, builder, tokens, null);
    }

    private static void operationBooleanNotEqual(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("!="), startRow, startColumn, builder, tokens, null);
    }

    private static void operationBitOr(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        char ch = reader.previewNext();
        if (ch == '|' ) {
            reader.next();
            builder.append(ch);
            operationBooleanOr(reader, startRow, startColumn, builder, tokens);
        }
        else
            generateToken(TokenType.operatorTable.get("|"), startRow, startColumn, builder, tokens, null);
    }

    private static void operationBooleanOr(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("||"), startRow, startColumn, builder, tokens, null);
    }

    private static void operationBitAnd(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        char ch = reader.previewNext();
        if (ch == '&' ) {
            reader.next();
            builder.append(ch);
            operationBooleanAnd(reader, startRow, startColumn, builder, tokens);
        }
        else
            generateToken(TokenType.operatorTable.get("&"), startRow, startColumn, builder, tokens, null);
    }

    private static void operationBooleanAnd(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("&&"), startRow, startColumn, builder, tokens, null);
    }

    private static void operationBitNot(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("~"), startRow, startColumn, builder, tokens, null);
    }

    private static void operationLeftBigBracket(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("{"), startRow, startColumn, builder, tokens, null);
    }

    private static void operationRightBigBracket(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("}"), startRow, startColumn, builder, tokens, null);
    }

    private static void operationLeftMiddleBracket(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("["), startRow, startColumn, builder, tokens, null);
    }

    private static void operationRightMiddleBracket(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("]"), startRow, startColumn, builder, tokens, null);
    }

    private static void operationLeftSmallBracket(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get("("), startRow, startColumn, builder, tokens, null);
    }

    private static void operationRightSmallBracket(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get(")"), startRow, startColumn, builder, tokens, null);
    }

    private static void operationSemicolon(LexerReader reader, int startRow, int startColumn, StringBuilder builder, List<Token> tokens) {
        generateToken(TokenType.operatorTable.get(";"), startRow, startColumn, builder, tokens, null);
    }

    private static void generateToken(TokenType tokenType, int startRow, int startColumn, StringBuilder builder, List<Token> tokens, Object value) {
        String text = builder.toString();
        tokens.add(Token.creator(text, tokenType, startRow, startColumn, value));
    }

}
