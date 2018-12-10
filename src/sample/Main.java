package sample;

import Lexer.LexerScanner;
import Lexer.Token;
import Lexer.TokenType;
import MiniException.LexicalException;
import Parser.Grammar;
import Parser.Node;
import Parser.ParserException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main {


    public static void main(String[] args) throws IOException {


        Grammar grammar;
        try {

            HashMap<Integer, Integer> map = new HashMap<>();

            File file = new File("C:\\Users\\CrazyFlower\\Desktop\\grammar");
            InputStream inputStream = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            inputStream.read(bytes);

            String[] rules = new String(bytes, "UTF-8").split("\n");
            List<String> rule = new ArrayList<>();
            for (String line : rules) {
                if (!(line.trim().startsWith("//") || line.trim().equals("")))
                    rule.add(line.substring(0, line.length() - 1));
            }
            rules = new String[rule.size()];
            rule.toArray(rules);

            grammar = new Grammar("ClassDeclaration ",
                    "null constString constBoolean constInt constDouble constChar ConstValue method void super double int char boolean String Object [ ] this identifier == != <= >= < > || && ! + - * / % new . ; break return if then else for = , while public private static class extends { } ( )",
                    "ConstValue FieldDeclarationBody1 TypeWithIdentifier FieldDeclarationBody StaticModifier MethodModifier MethodModifiers AccessStaticModifier AccessModifier AccessModifiers MethodDeclarator StatementExpressionList MethodName StatementExpression AssignmentExpression AccessModifiers ConstructorModifier ConstructorDeclaration FieldVariableDeclaration FieldVariableDeclarations FieldModifier FieldDeclaration ClassBodyDeclaration ClassBodyDeclarations ClassBody ClassModifiers ClassDeclaration ParameterListDefination BreakStatement ReturnStatement StaticModifier AccessModifier MethodModifiers ParameterList ReturnType MethodBody MethodHead MethodDeclaration BlockStatements WhileStatement VariableDeclaration VariableDeclarations LocalVariableDeclaration LocalVariableDeclarationStatement ForUpdateStatement ForInitStatement IfStatement ForStatement BlankDimension ExpressionDimension ClassType Dimensions Arguments MethodInvocation ArgumentList ClassCreation ArrayAccess FieldAccess LeftValue ExpressionName AssignmentExpression PrimitiveType ReferenceType Type ArrayType Value Variable Expression NewOperation E1 E2 E3 E4 E5 E6 E7 E8 E9 E10 E11 E12 E13 E14 E15 AssignmentStatement FunctionCall ParameterList ArraySize Parameters Statement NormalStatement DefinitionStatement FunctionalStatement AlternativeStatement LoopStatement Statements Block BlockStatement Function MemberFunction StaticFunction ConstructorFunction Visitable DefinitionParameterList DefinitionParameters ClassDefination FunctionAndVariable Class ClassDefinitionStatement AssignmentStatementList",
                    rules);

            List<Token> tokens = LexerScanner.scan("public class A extends B{ \n" +
                    "   public void f1(int b){ \n" +
                    "       int c = 2;\n" +
                    "       return c;\n" +
                    "   } \n"+
                    "}");
            List<Node> nodes = new ArrayList<>();
            for (Token token : tokens)
            {
                nodes.add(new Node(grammar.getTerminalElementMap().get(token.getTokenType().getDescription())));
            }



            Node root = grammar.parse(nodes);

            System.out.println("OK");

        } catch (ParserException e) {
            e.printStackTrace();
        } catch (LexicalException e) {
            e.printStackTrace();
        }

    }
}
