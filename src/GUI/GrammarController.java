package GUI;

import Parser.Grammar;
import Parser.SLRParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GrammarController implements Initializable {
    @FXML
    private Label error;

    @FXML
    private TextArea input;

    public static SLRParser slrParser;

    @FXML
    private void handleStart(ActionEvent event) throws IOException {

        boolean canBeParse = true;
        String grammarText = input.getText();
        Grammar grammar = new Grammar(grammarText);
        slrParser = new SLRParser(grammar);
        slrParser.parserSLR1();

        if(!canBeParse){
            error.setText("The grammar can not be parsed. choose a different parser or grammar");
            error.setVisible(true);
        }else{
            Button button = (Button)event.getSource();
            Stage stage = (Stage)button.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("Analyze.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        error.setVisible(false);

    }
}
