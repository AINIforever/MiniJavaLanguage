package sample;

import Parser.Grammar;
import Parser.ParserException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        try {

            HashMap<Integer, Integer> map = new HashMap<>();

            File file = new File("C:\\Users\\CrazyFlower\\Desktop\\MiniJava Production.txt");
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

            Grammar grammar = new Grammar("Class",
                    "double int char boolean String Object [ ] this identifier == != <= >= < > || && ! + - * / % new . ; break return if then else for = , while public private static class extends { } ( )",
                    "Type BasicType CustomType ArrayType Value ConstValue Variable Expression NewOperation E1 E2 E3 E4 E5 E6 E7 E8 E9 AssignmentStatement FunctionCall ParameterList ArraySize Parameters Statement NormalStatement DefinitionStatement FunctionalStatement AlternativeStatement LoopStatement Statements Block BlockStatement Function MemberFunction StaticFunction ConstructorFunction Visitable DefinitionParameterList DefinitionParameters ClassDefination FunctionAndVariable Class ClassDefinitionStatement AssignmentStatementList",
                    rules);
        } catch (ParserException e) {
            e.printStackTrace();
        }

    }
}
