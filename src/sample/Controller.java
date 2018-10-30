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
        String inputString = input.getText();
        String outputString = "";
        public void run(){
            try {
                List<Token> tokens = LexerScanner.scan(inputString);
                for (Token token: tokens){
                    System.out.println(token);
                    outputString += token.toString() + "\n";
                }


            } catch (LexicalException e) {
                System.out.println(e.toString());
            }
            output.setText(outputString);
        }
    }

    public void analyze(ActionEvent actionEvent) {
        Thread thread = new Thread(new MyRunnable());
        thread.start();

    }


}
