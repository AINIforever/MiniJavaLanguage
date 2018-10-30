package sample;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import javafx.event.ActionEvent;

import Lexer.LexerScanner;
import Lexer.Token;
import MiniException.LexicalException;

import java.util.List;

public class Controller {

    public Button analyze;
    public TextArea output;
    public TextArea input;

    public class MyRunnable implements Runnable {

        public void run() {

            String inputString = input.getText();
            StringBuilder stringBuilder = new StringBuilder();

            try {
                List<Token> tokens = LexerScanner.scan(inputString);
                for (Token token: tokens)
                    stringBuilder.append(token.toString()).append('\n');
            } catch (LexicalException e) {
                stringBuilder.setLength(0);
                stringBuilder.append(e.toString());
            }
            output.setText(stringBuilder.toString());
        }
    }

    public void analyze(ActionEvent actionEvent) {
        Thread thread = new Thread(new MyRunnable());
        thread.start();
    }


}
