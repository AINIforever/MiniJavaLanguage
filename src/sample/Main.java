package sample;

import Lexer.LexerScanner;
import Lexer.Token;
import MiniException.LexicalException;
import Parser.Grammar;
import Parser.Node;
import Parser.ParserException;
import Parser.TerminalElement;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class Main extends Application {


    private static int verticalRadius = 20;
    private static int horizontalRadius = 18;
    private static int horizontalDistance = 30;
    private static int verticalDistance = 52;
    private static Color ELLIPSE_COLOR = Color.rgb(255, 153, 0);

    private static Font drawTextFont = Font.font(20);


    public static void main(String[] args) {
        launch(args);
    }

    public static void drawNode(Pane pane, Node node, double left, double height, Map<Node, Double> map, double parentX, double parentY) {

        double x = left + map.get(node) / 2;
        double y = height;

        if (!(parentX == 0 && parentY == 0)) {
            Line line = new Line(x, y, parentX, parentY);
            pane.getChildren().add(line);
        }

        Text text = new Text(node.getAbstractElement().getText());
        text.setFont(drawTextFont);
        text.setTranslateX(x - text.getLayoutBounds().getWidth() / 2);
        text.setTranslateY(y + (text.getBaselineOffset() * 2 - text.getLayoutBounds().getHeight()) / 2);

        Ellipse ellipse = new Ellipse(x, y, text.getLayoutBounds().getWidth() / 2 + horizontalRadius, verticalRadius);
        ellipse.setFill(ELLIPSE_COLOR);

        double tempLeft = 0;
        for (Node son : node.getSons())
            tempLeft += map.get(son);
        tempLeft += Math.max(0, node.getSons().size() -1) * horizontalDistance;
        if (tempLeft < map.get(node))
            tempLeft = (map.get(node) - tempLeft) / 2;
        else
            tempLeft = 0;

        for (Node son : node.getSons()) {
            drawNode(pane, son, left + tempLeft, height + verticalDistance, map, x, y);
            tempLeft += map.get(son) + horizontalDistance;
        }

        pane.getChildren().add(ellipse);
        pane.getChildren().add(text);
    }

    public static void drawTree(Pane pane, Node node) {
        Map<Node, Double> nodeWidthMap = new HashMap<>();

        calculateWidth(nodeWidthMap, node);

        drawNode(pane, node, 0, verticalRadius, nodeWidthMap, 0, 0);
    }

    public static void calculateWidth(Map<Node, Double> map, Node parent) {

        List<Node> sons = parent.getSons();
        double sonWidth = 0, parentWidth;

        for (Node son : sons) {
            calculateWidth(map, son);
            sonWidth += map.get(son);
        }
        sonWidth += Math.max(sons.size() - 1, 0) * horizontalDistance;

        Text text = new Text();
        text.setText(parent.getAbstractElement().getText());
        text.setFont(drawTextFont);
        parentWidth = text.getLayoutBounds().getWidth() + horizontalRadius * 2;


        map.put(parent, Math.max(parentWidth, sonWidth));
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

        button.setOnMouseClicked(event -> {
            try {

                List<Token> tokens = LexerScanner.scan(textArea.getText());
                List<Node> nodes = new ArrayList<>();
                Map<String, TerminalElement> map = grammar.getTerminalElementMap();
                for (Token token : tokens)
                {
                    if (token.getTokenType().getNumber() != 1106 && token.getTokenType().getNumber() != 1107)
                        nodes.add(new Node(map.get(token.getTokenType().getDescription())));
                }

                Node root = grammar.parse(nodes);

                Pane pane1 = new Pane();
                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setContent(pane1);
                drawTree(pane1, root);
                Stage stage = new Stage();
                Scene scene1 = new Scene(scrollPane);
                stage.setScene(scene1);
                stage.show();


                System.out.println("OK");
            } catch (LexicalException e) {
                e.printStackTrace();
            } catch (ParserException e) {
                e.printStackTrace();
            }
        });




    }
}
