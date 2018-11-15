package Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public abstract class LRParser {

    protected HashMap<String, Integer>[] goToTable;
    protected HashMap<String, Action>[] actionTable;
    protected Grammar grammar;

    public LRParser(Grammar grammar) {
        this.grammar = grammar;
    }

    protected abstract void createGoToTable();

    private ASTreeNode rootNode;



    public boolean accept(ArrayList<String> inputs) {

        inputs.add("$");
        int index = 0;
        //状态栈
        Stack<String> stack = new Stack<>();
        //树节点栈
        Stack<ASTreeNode> treeNodeStack = new Stack<>();
        stack.add("0");
        treeNodeStack.add(new ASTreeNode("null"));
        while(index < inputs.size()){
            int state = Integer.valueOf(stack.peek());
            String nextInput = inputs.get(index);
            Action action = actionTable[state].get(nextInput);
            System.out.println(stack);
            System.out.println(action);
            if(action == null){
                return false;
            }else if(action.getType() == ActionType.SHIFT){
                stack.push(nextInput);
                ASTreeNode node = new ASTreeNode(nextInput);
                treeNodeStack.push(node);
                stack.push(action.getOperand()+"");
                index++;
            }else if(action.getType() == ActionType.REDUCE){

                int ruleIndex = action.getOperand();
                Rule rule = grammar.getRules().get(ruleIndex);
                String leftSide = rule.getLeftSide();
                int rightSideLength = rule.getRightSide().length;
                ASTreeNode childNode = null;
                ASTreeNode parentNode = new ASTreeNode(leftSide);
                for(int i = 0; i < 2 * rightSideLength; i++){
                    stack.pop();

                }
                for(int j = 0; j < rightSideLength; j++){
                    childNode = treeNodeStack.peek();
                    treeNodeStack.pop();
                    ArrayList<ASTreeNode> theList = parentNode.getChildNodeLists();
                    theList.add(childNode);
                    parentNode.setChildNodeLists(theList);
                }
                int nextState = Integer.valueOf(stack.peek());
                stack.push(leftSide);
                treeNodeStack.push(parentNode);
                int variableState = goToTable[nextState].get(leftSide);
                stack.push(variableState+"");


            }else if(action.getType() == ActionType.ACCEPT){
                rootNode = treeNodeStack.pop();
                System.out.println(rootNode);
                ASTreeNode.displaytree(rootNode,0);
                return true;
            }
        }
        return false;
    }

    public String showAnalyzeTable(ArrayList<String> inputs){
        String str = "Analyze Table: \n";
        str += "     State Stack     ";
        str += "     Action          ";

        return str;
    }

    public String goToTableStr() {
        String str = "Go TO Table : \n";
        str += "          ";
        for (String variable : grammar.getVariables()) {
            str += String.format("%-6s",variable);
        }
        str += "\n";

        for (int i = 0; i < goToTable.length; i++) {
            for (int j = 0; j < (grammar.getVariables().size()+1)*6+2; j++) {
                str += "-";
            }
            str += "\n";
            str += String.format("|%-6s|",i);
            for (String variable : grammar.getVariables()) {
                str += String.format("%6s",(goToTable[i].get(variable) == null ? "|" : goToTable[i].get(variable)+"|"));
            }
            str += "\n";
        }
        for (int j = 0; j < (grammar.getVariables().size()+1)*6+2; j++) {
            str += "-";
        }
        return str;
    }

    public String actionTableStr() {
        String str = "Action Table : \n";
        HashSet<String> terminals = new HashSet<>(grammar.getTerminals());
        terminals.add("$");
        str += "                ";
        for (String terminal : terminals) {
            str += String.format("%-10s" , terminal);
        }
        str += "\n";

        for (int i = 0; i < actionTable.length; i++) {
            for (int j = 0; j < (terminals.size()+1)*10+2; j++) {
                str += "-";
            }
            str += "\n";
            str += String.format("|%-10s|",i);
            for (String terminal : terminals) {
                str += String.format("%10s",(actionTable[i].get(terminal) == null ? "|" : actionTable[i].get(terminal) + "|"));
            }
            str += "\n";
        }
        for (int j = 0; j < (terminals.size()+1)*10+2; j++) {
            str += "-";
        }
        return str;
    }

    public Grammar getGrammar() {
        return grammar;
    }
}
