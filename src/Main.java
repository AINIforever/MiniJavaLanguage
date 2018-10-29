import Lexer.LexerScanner;
import Lexer.Token;
import MiniException.LexicalException;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        try {
            List<Token> tokens = LexerScanner.scan("+/ *-&*% >=  > != ! dsfkjsahk213 123jbfs // 12321 \n\n\n /*  \n\n21fslkf **/  \"1231232'1\"  ''");
            for (Token token: tokens)
                System.out.println(token);
        } catch (LexicalException e) {
            System.out.println(e.toString());
        }
    }
}
