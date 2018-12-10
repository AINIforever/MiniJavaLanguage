package Lexer;

import java.util.Hashtable;

public class TokenType {



    private String description;

    private int number;

    private TokenType(String description, int number) {
        this.description = description;
        this.number = number;
    }

    static final Hashtable<String, TokenType> keyWordTable;
    static final Hashtable<String, TokenType> reservedWordTable;
    static final Hashtable<String, TokenType> operatorTable;

    static {
        keyWordTable = new Hashtable<>();
        keyWordTable.put("class", new TokenType("class", 1));
        keyWordTable.put("public", new TokenType("public", 2));
        keyWordTable.put("private", new TokenType("private", 3));
        keyWordTable.put("this", new TokenType("this", 4));
        keyWordTable.put("static", new TokenType("static", 5));
        keyWordTable.put("void", new TokenType("void", 6));
        keyWordTable.put("for", new TokenType("for", 7));
        keyWordTable.put("while", new TokenType("while", 8));
        keyWordTable.put("if", new TokenType("if", 9));
        keyWordTable.put("do", new TokenType("do", 10));
        keyWordTable.put("break", new TokenType("break", 11));
        keyWordTable.put("null", new TokenType("null", 12));
        keyWordTable.put("new", new TokenType("new", 13));
        keyWordTable.put("true", new TokenType("constBoolean", 14));
        keyWordTable.put("false", new TokenType("constBoolean", 15));
        keyWordTable.put("extends", new TokenType("extends", 16));
        keyWordTable.put("return", new TokenType("return", 17));
        keyWordTable.put("System", new TokenType("System", 101));
        keyWordTable.put("Object", new TokenType("Object", 102));
        keyWordTable.put("int", new TokenType("int", 103));
        keyWordTable.put("String", new TokenType("String", 104));
        keyWordTable.put("double", new TokenType("double", 105));
        keyWordTable.put("char", new TokenType("char", 106));
        keyWordTable.put("boolean", new TokenType("boolean", 107));

        reservedWordTable = new Hashtable<>();
        reservedWordTable.put("infinite", new TokenType("infinite", 300));
        reservedWordTable.put("try", new TokenType("try", 301));
        reservedWordTable.put("catch", new TokenType("catch", 302));
        reservedWordTable.put("throws", new TokenType("throws", 303));
        reservedWordTable.put("float", new TokenType("float", 304));

        operatorTable = new Hashtable<>();
        operatorTable.put("+", new TokenType("+", 1001));
        operatorTable.put("-", new TokenType("-", 1002));
        operatorTable.put("*", new TokenType("*", 1003));
        operatorTable.put("/", new TokenType("/", 1004));
        operatorTable.put("%", new TokenType("%", 1005));
        operatorTable.put("~", new TokenType("~", 1006));
        operatorTable.put("&", new TokenType("&", 1007));
        operatorTable.put("|", new TokenType("|", 1008));
        operatorTable.put("&&", new TokenType("&&", 1009));
        operatorTable.put("||", new TokenType("||", 1010));
        operatorTable.put("!", new TokenType("!", 1011));
        operatorTable.put(">", new TokenType(">", 1012));
        operatorTable.put("<", new TokenType("<", 1013));
        operatorTable.put("=", new TokenType("=", 1014));
        operatorTable.put("==", new TokenType("==", 1015));
        operatorTable.put(">=", new TokenType(">=", 1016));
        operatorTable.put("<=", new TokenType("<=", 1017));
        operatorTable.put("!=", new TokenType("!=", 1018));
        operatorTable.put("{", new TokenType("{", 1019));
        operatorTable.put("}", new TokenType("}", 1020));
        operatorTable.put("[", new TokenType("[", 1021));
        operatorTable.put("]", new TokenType("]", 1022));
        operatorTable.put("(", new TokenType("(", 1023));
        operatorTable.put(")", new TokenType(")", 1024));
        operatorTable.put(";", new TokenType(";", 1025));
        operatorTable.put(".", new TokenType(".", 1026));
        operatorTable.put(",", new TokenType(",", 1027));
    }

    public static final TokenType IDENTIFIER = new TokenType("identifier", 1100);
    public static final TokenType CONST_INTEGER = new TokenType("constInt", 1101);
    public static final TokenType CONST_DOUBLE = new TokenType("constDouble", 1102);
    public static final TokenType CONST_BOOL = new TokenType("constBoolean", 1102);
    public static final TokenType SINGLE_LINE_COMMENT = new TokenType("Single line comment", 1106);
    public static final TokenType MULTI_LINE_COMMENT = new TokenType("Multi line comment", 1107);
    public static final TokenType CONST_CHAR = new TokenType("constChar", 1104);
    public static final TokenType CONST_STRING = new TokenType("constString", 1105);

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("%-30s %-8d", description, number);
    }
}

