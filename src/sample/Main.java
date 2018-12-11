package sample;

import Lexer.LexerScanner;
import Lexer.Token;
import Lexer.TokenType;
import MiniException.LexicalException;
import Parser.Grammar;
import Parser.Node;
import Parser.ParserException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class Main extends Application {


    private static int radius = 30;
    private static int distance = 30;
    private static int verticalDistance = 65;

    private static Font drawTextFont = Font.font(20);


    public static void main(String[] args) {
        launch(args);
    }

    public static void drawNode(Pane pane, Node node, int left, int high, Map<Node, Integer> map, int parentX, int parentY) {


        int x = left + map.get(node) / 2;
        int y = high;

        if (high != radius) {
            Line line = new Line(x, y, parentX, parentY);
            pane.getChildren().add(line);
        }

        Circle circle = new Circle(radius, Color.rgb(255, 153, 0, 1.0));
        circle.setTranslateX(x);
        circle.setTranslateY(y);

        Text text = new Text(node.getAbstractElement().getText());
        text.setFont(drawTextFont);
        text.setTranslateX(x - text.getLayoutBounds().getWidth() / 2);
        text.setTranslateY(y + (text.getBaselineOffset()  * 2 - text.getLayoutBounds().getHeight()) / 2);

        int tempLeft = 0;
        for (Node son : node.getSons()) {
            drawNode(pane, son, left + tempLeft, high + verticalDistance, map, x, y);
            tempLeft += map.get(son) + distance;
        }
        pane.getChildren().add(circle);
        pane.getChildren().add(text);
    }

    public static void drawTree(Pane pane, Node node) {


        Map<Node, Integer> nodeWidthMap = new HashMap<>();

        calculateWidth(nodeWidthMap, node);

        drawNode(pane, node, 0, radius, nodeWidthMap, 0, 0);
    }

    public static void calculateWidth(Map<Node, Integer> map, Node parent) {

        List<Node> sons = parent.getSons();
        if (sons.size() == 0) {
            map.put(parent, radius * 2);
            return;
        }

        int width = 0;

        for (Node son : parent.getSons()) {
            calculateWidth(map, son);
            width += map.get(son) + distance;
        }

        map.put(parent, width - distance);
    }


    Grammar grammar;
    @Override
    public void start(Stage primaryStage) throws Exception {


        try {
            File file = new File("C:\\Users\\CrazyFlower\\Desktop\\grammar");
            InputStream inputStream = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            inputStream.read(bytes);

            String[] rules = new String(bytes, "UTF-8").split("\r\n");
            List<String> rule = new ArrayList<>();
            for (String line : rules) {
                if (!(line.trim().startsWith("//") || line.trim().equals("")))
                    rule.add(line);
            }
            rules = new String[rule.size()];
            rule.toArray(rules);

            grammar = new Grammar("ClassDeclaration ",
                    "double int char boolean String Object ( ) [ ] { } constInt constDouble constBoolean constString constChar null = || && == != <= >= < > + - * / % ! identifier . this super , ; return break private public static void new class for while if then else extends",
                    "Type PrimitiveType ReferenceType ClassType ArrayType LeftValue ConstValue Expression AssignmentStatement AssignmentExpression E1 E2 E3 E4 E5 E6 E7 E8 E9 E10 E11 E12 E13 E14 FieldAccess MethodInvocation Dimensions ExpressionDimension BlankDimension ClassCreation ArgumentList Arguments ArrayAccess Statement StatementExpression IfStatement ForStatement ForInitStatement WhileStatement StatementExpressionList ForUpdateStatement StatementExpressionList LocalVariableDeclarationStatement VariableDeclarations VariableDeclaration ReturnStatement BreakStatement Block BlockStatements MethodDeclaration MethodHead MethodDeclarator TypeWithIdentifier ReturnType ParameterListDefination ParameterList MethodBody ClassDeclaration ClassModifiers ClassBody ClassBodyDeclarations ClassBodyDeclaration FieldDeclaration FieldDeclarationBody AccessModifiers AccessModifier AccessStaticModifier ConstructorDeclaration ConstructorModifier WithoutSubstatementStatement EmptyStatement ExpressionStatement IfThenStatement IfThenElseStatement NoShortIfIfThenElseStatement NoShortIfStatement NoShortIfWhileStatement NoShortIfForStatement BlockStatement",
                    rules);

        } catch (ParserException e) {
            e.printStackTrace();
        }

        Pane pane = new Pane();
        TextArea textArea = new TextArea();
        textArea.setFont(new Font(16.0));
        textArea.prefWidthProperty().bind(pane.widthProperty().subtract(100));
        textArea.prefHeightProperty().bind(pane.heightProperty());
        textArea.setPadding(new Insets(0, 0, 0, 0));
        Button button = new Button();
        button.setMinWidth(84);
        button.setMaxWidth(84);
        button.setMinHeight(60);
        button.setMaxHeight(60);
        button.setText("Analyse");
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(540);
        pane.getChildren().addAll(textArea, button);
        button.layoutXProperty().bind(textArea.widthProperty().add(9));
        Scene scene = new Scene(pane);
        pane.prefHeightProperty().bind(scene.heightProperty());
        pane.prefWidthProperty().bind(scene.widthProperty());
        primaryStage.setScene(scene);
        primaryStage.show();

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {

                    List<Token> tokens = LexerScanner.scan(textArea.getText());
                    List<Node> nodes = new ArrayList<>();
                    for (Token token : tokens)
                    {
                        nodes.add(new Node(grammar.getTerminalElementMap().get(token.getTokenType().getDescription())));
                    }

                    Node root = grammar.parse(nodes);

                    Pane pane = new Pane();
                    ScrollPane scrollPane = new ScrollPane();
                    scrollPane.setContent(pane);
                    drawTree(pane, root);
                    Stage stage = new Stage();
                    Scene scene = new Scene(scrollPane);
                    stage.setScene(scene);
                    stage.show();


                    System.out.println("OK");
                } catch (LexicalException e) {
                    e.printStackTrace();
                } catch (ParserException e) {
                    e.printStackTrace();
                }
            }
        });




    }
}
