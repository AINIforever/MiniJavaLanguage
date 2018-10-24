import java.util.Hashtable;

public class Token {

    static{
        Hashtable<String, Integer> codeTable = new Hashtable<>();
        codeTable.put("class",1);
        codeTable.put("public",2);
        codeTable.put("private",3);
        codeTable.put("this",4);
        codeTable.put("static",5);
        codeTable.put("void",6);
        codeTable.put("for",7);
        codeTable.put("while",8);
        codeTable.put("if",9);
        codeTable.put("do",10);
        codeTable.put("break",11);
        codeTable.put("null",12);
        codeTable.put("new",13);
        codeTable.put("true",14);
        codeTable.put("false",15);
        codeTable.put("extends",16);
        codeTable.put("return",17);
        codeTable.put("System",101);
        codeTable.put("Object",102);
        codeTable.put("int",103);
        codeTable.put("string",104);
        codeTable.put("double",105);
        codeTable.put("char",106);
        codeTable.put("boolean",107);
        codeTable.put("infinite",300);
        codeTable.put("try",301);
        codeTable.put("catch",302);
        codeTable.put("throws",303);
        codeTable.put("float",304);
        codeTable.put("+",1001);
        codeTable.put("-",1002);
        codeTable.put("*",1003);
        codeTable.put("/",1004);
        codeTable.put("%",1005);
        codeTable.put("",1006);
        codeTable.put("&",1007);
        codeTable.put("|",1008);
        codeTable.put("&&",1009);
        codeTable.put("||",1010);
        codeTable.put("!",1011);
        codeTable.put("!",1011);
        codeTable.put("!",1011);
        codeTable.put("!",1011);
        codeTable.put("!",1011);
        codeTable.put("!",1011);
        codeTable.put("!",1011);
        codeTable.put("!",1011);
        codeTable.put("!",1011);
        codeTable.put("!",1011);
        codeTable.put("!",1011);
        codeTable.put("!",1011);
        codeTable.put("!",1011);
        codeTable.put("!",1011);
        codeTable.put("!",1011);
        codeTable.put("!",1011);
        codeTable.put("!",1011);
        codeTable.put("!",1011);
        codeTable.put("!",1011);
        codeTable.put("!",1011);


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
