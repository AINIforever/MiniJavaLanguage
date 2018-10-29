import Lexer.LexerScanner;
import Lexer.Token;
import MiniException.LexicalException;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        try {
            LexerScanner lexerScanner = new LexerScanner("abc class try");
            List<Token> tokens = lexerScanner.getTokens();
            for (Token token: tokens)
                System.out.println(token);
        } catch (LexicalException e) {
            System.out.println(e.toString());
        }
    }
}
