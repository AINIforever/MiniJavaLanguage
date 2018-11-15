package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AnalyzeController implements Initializable {

    public Button showTree;
    @FXML
    private TextField input;

    @FXML
    private Label result;

    @FXML
    private TextArea output;

    @FXML
    private void handleGrammar(ActionEvent event){
        output.setText(GrammarController.slrParser.getGrammar()+"");
    }

    @FXML
    private void handleFirst(ActionEvent event){
        String str = "";
        for(String s : GrammarController.slrParser.getGrammar().getFirstSets().keySet()){
            str += s + " : " + GrammarController.slrParser.getGrammar().getFirstSets().get(s) + "\n";
        }
        output.setText(str);
    }

    @FXML
    private void handleFollow(ActionEvent event){
        String str = "";
        for(String s : GrammarController.slrParser.getGrammar().getFallowSets().keySet()){
            str += s + " : " + GrammarController.slrParser.getGrammar().getFallowSets().get(s) + "\n";
        }
        output.setText(str);
    }

    @FXML
    private void handleState(ActionEvent event){
        output.setText(GrammarController.slrParser.canonicalCollectionStr());
    }

    @FXML
    private void handleGoTo(ActionEvent event){
//        if(GrammarInputController.parserKind.equals("LR(0)") || GrammarInputController.parserKind.equals("SLR(1)")){
//            output.setText(GrammarInputController.lr0Parser.goToTableStr());
//        }else{
//            output.setText(GrammarInputController.lr1Parser.goToTableStr());
//        }
        output.setText(GrammarController.slrParser.goToTableStr());
    }

    @FXML
    private void handleAction(ActionEvent event){
        output.setText(GrammarController.slrParser.actionTableStr());
    }
    @FXML
    public void handleAnalyzeAction(ActionEvent actionEvent) {
        String str = input.getText();
        ArrayList<String> words = new ArrayList<>();
        String[] split = str.trim().split("\\s+");
        for(String s: split){
            words.add(s);
        }
        output.setText(GrammarController.slrParser.showAnalyzeTable(words));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        result.setVisible(false);
        showTree.setVisible(false);
        input.textProperty().addListener((observable, oldValue, newValue) -> {
            String str = input.getText();
            ArrayList<String> words = new ArrayList<>();
            String[] split = str.trim().split("\\s+");
            for(String s: split){
                words.add(s);
            }
            boolean accept = GrammarController.slrParser.accept(words);
            if(accept){
                result.setText("accepted");
                result.setTextFill(Color.GREEN);
                showTree.setVisible(true);
                result.setVisible(true);
            }else {
                result.setText("not accepted");
                result.setTextFill(Color.RED);
                result.setVisible(true);
            }
        });
        output.setText(GrammarController.slrParser.getGrammar()+"");
    }
}
