package Parser;

public class ParserException extends Exception {

    static private class ParserExceptionType {

        int id;
        String message;

        private ParserExceptionType(int id, String message) {
            this.id = id;
            this.message = message;
        }


    }

    static final ParserExceptionType GRAMMAR_EXCEPTION = new ParserExceptionType(2001, "Parser.Grammar is wrong.");
    static final ParserExceptionType WRONG_EXPRESSION = new ParserExceptionType(2003, "The expression is wrong.");
    static final ParserExceptionType WRONG_SYMBOL = new ParserExceptionType(2002, "The symbol is't terminal symbol nor not-terminal symbol.");

    private int startRow;
    private int startColumn;

    private ParserExceptionType parserExceptionType;

    ParserException(int startRow, int startColumn, ParserExceptionType parserExceptionType) {
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.parserExceptionType = parserExceptionType;
    }

    @Override
    public String toString() {
        return String.format("Parser Error %d, At(%d, %d) : %s", parserExceptionType.id, startRow, startColumn, parserExceptionType.message);
    }
}
