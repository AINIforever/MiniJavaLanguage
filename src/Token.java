import java.util.Hashtable;

public class Token {


    public class TokenType{
        private Hashtable<String, Integer> keyWordTable = new Hashtable<>();
        private Hashtable<String, Integer> reservedWordTable = new Hashtable<>();
        private Hashtable<String, Integer> operatorTable = new Hashtable<>();
        public TokenType(){
            keyWordTable.put("class",1);
            keyWordTable.put("public",2);
            keyWordTable.put("private",3);
            keyWordTable.put("this",4);
            keyWordTable.put("static",5);
            keyWordTable.put("void",6);
            keyWordTable.put("for",7);
            keyWordTable.put("while",8);
            keyWordTable.put("if",9);
            keyWordTable.put("do",10);
            keyWordTable.put("break",11);
            keyWordTable.put("null",12);
            keyWordTable.put("new",13);
            keyWordTable.put("true",14);
            keyWordTable.put("false",15);
            keyWordTable.put("extends",16);
            keyWordTable.put("return",17);
            keyWordTable.put("System",101);
            keyWordTable.put("Object",102);
            keyWordTable.put("int",103);
            keyWordTable.put("string",104);
            keyWordTable.put("double",105);
            keyWordTable.put("char",106);
            keyWordTable.put("boolean",107);

            reservedWordTable.put("infinite",300);
            reservedWordTable.put("try",301);
            reservedWordTable.put("catch",302);
            reservedWordTable.put("throws",303);
            reservedWordTable.put("float",304);

            operatorTable.put("+",1001);
            operatorTable.put("-",1002);
            operatorTable.put("*",1003);
            operatorTable.put("/",1004);
            operatorTable.put("%",1005);
            operatorTable.put("~",1006);
            operatorTable.put("&",1007);
            operatorTable.put("|",1008);
            operatorTable.put("&&",1009);
            operatorTable.put("||",1010);
            operatorTable.put("!",1011);
            operatorTable.put(">",1012);
            operatorTable.put("<",1013);
            operatorTable.put("=",1014);
            operatorTable.put("==",1015);
            operatorTable.put(">=",1016);
            operatorTable.put("<=",1017);
            operatorTable.put("!=",1018);
            operatorTable.put("{",1019);
            operatorTable.put("}",1020);
            operatorTable.put("[",1021);
            operatorTable.put("]",1022);
            operatorTable.put("(",1023);
            operatorTable.put(")",1024);
            operatorTable.put(";",1025);
            operatorTable.put(".",1026);
        }

    }


    private String name;

    private TokenType tokenType;

    private int row;
    private int column;

    public static Token creator(String s, TokenType tokenType, int row, int column) {
        return new Token(s, tokenType, row, column);
    }

    private Token(String s, TokenType tokenType, int row, int column) {
        this.name = s;
        this.tokenType = tokenType;
        this.row = row;
        this.column = column;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + "  " + tokenType.toString();
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Token toNegative() {
        if (name.startsWith("-"))
            name = name.substring(1);
        else
            name = "-" + name;
        return this;
    }
}
