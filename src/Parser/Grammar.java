package Parser;

import java.util.*;

public class Grammar {

    private Map<String, NotTerminalElement> notTerminalElementMap;

    private Map<String, TerminalElement> terminalElementMap;

    private List<Rule> rules;

    private List<LR1State> states;

    private String name;
    private String extensionalName;

    private static final String END_TERMINAL_STRING = "#";

    public Grammar(String name, String terminalText, String notTerminalText, String[] rulesText) throws ParserException {
        this.name = name.trim();
        notTerminalElementMap = getNotTerminalElements(notTerminalText);
        terminalElementMap = getTerminalElements(terminalText);
        rules = new ArrayList<>();

        //Add a rule such as S' -> S#
        extensionalName = this.name + "'";
        notTerminalElementMap.put(extensionalName, new NotTerminalElement(extensionalName));
        terminalElementMap.put("#", new TerminalElement("#"));
        addRule(extensionalName + " -> " + this.name);

        for (String ruleText : rulesText)
            addRule(ruleText);

        initStates();
    }

    private Map<String, NotTerminalElement> getNotTerminalElements(String notTerminalText) {
        notTerminalText = notTerminalText.trim();
        String[] result = notTerminalText.split(" ");
        Map<String, NotTerminalElement> notTerminalElementMap = new LinkedHashMap<String, NotTerminalElement>();
        for (String element : result)
            notTerminalElementMap.put(element, new NotTerminalElement(element));
        return notTerminalElementMap;
    }

    private Map<String, TerminalElement> getTerminalElements(String terminalText) {
        terminalText = terminalText.trim();
        String[] result = terminalText.split(" ");
        Map<String, TerminalElement> terminalElementHashMap = new LinkedHashMap<String, TerminalElement>();
        for (String element : result)
            terminalElementHashMap.put(element, new TerminalElement(element));
        return terminalElementHashMap;
    }

    private void addRule(String ruleText) throws ParserException {
        //找到 -> 的位置， 左右分割， 如果左边的在非终符号里找不到， 报错
        int index = ruleText.indexOf("->");
        String left = ruleText.substring(0, index).trim();
        NotTerminalElement leftNotTerminalElement = notTerminalElementMap.get(left);
        if (null == leftNotTerminalElement)
            throw new ParserException(0, 0, ParserException.GRAMMAR_EXCEPTION);

        //将右边的按|分割成多个生成式，  对于每个生成式， 按空格分割成Element，然后在集合里寻找， 并new Parser.Rule
        String rule = ruleText.substring(index + 2).trim();

        AbstractElement rightAbstractElement;
        List<AbstractElement> abstractElementList;

        String[] elements = rule.split(" ");
        abstractElementList = new ArrayList<AbstractElement>();
        for (String rightElement : elements) {
            rightAbstractElement = notTerminalElementMap.get(rightElement);
            if (null != rightAbstractElement) {
                abstractElementList.add(rightAbstractElement);
                continue;
            }
            rightAbstractElement = terminalElementMap.get(rightElement);
            if (null != rightAbstractElement) {
                abstractElementList.add(rightAbstractElement);
                continue;
            }
            throw new ParserException(0, 0, ParserException.WRONG_SYMBOL);
        }
        rules.add(new Rule(leftNotTerminalElement, abstractElementList, this.rules.size()));
    }

    public List<Rule> getRules() {
        return rules;
    }

    private void initStates() throws ParserException {
        states = new ArrayList<LR1State>();

        LR1Item startLR1Item = new LR1Item(rules.get(0), 0, terminalElementMap.get(END_TERMINAL_STRING));
        Set<LR1Item> startLR1ItemClosure = new HashSet<>();
        startLR1ItemClosure.add(startLR1Item);
        Util.closure(startLR1ItemClosure, rules);

        LR1State startState = new LR1State(startLR1ItemClosure, 0);
        states.add(startState);

        int index = 0, tempIndex;
        LR1State currentState, tempState;

        Set<LR1Item> tempLR1Item;
        List<AbstractElement> abstractElementList = new ArrayList<>(terminalElementMap.values());
        abstractElementList.addAll(notTerminalElementMap.values());
        while (index < states.size()) {
            currentState = states.get(index);
            for (AbstractElement tempAbstractElement : abstractElementList) {
                tempLR1Item = Util.go(currentState, tempAbstractElement, rules);
                if (tempLR1Item.size() == 0)
                    continue;
                tempState = new LR1State(tempLR1Item, states.size());
                tempIndex = states.indexOf(tempState);
                // -1 说明是新状态
                if (tempIndex == -1) {
                    tempIndex = states.size();
                    states.add(tempState);
                    if (null != currentState.addAction(tempAbstractElement, new Action(Action.SHIFT, tempIndex))) {
                        System.out.println(currentState);
                        throw new ParserException(0, 0, ParserException.GRAMMAR_EXCEPTION);
                    } else {
                        states.get(tempIndex).addFrom(currentState.index);
                    }
                } else {
                    if (null != currentState.addAction(tempAbstractElement, new Action(Action.SHIFT, tempIndex))) {
                        System.out.println(currentState);
                        throw new ParserException(0, 0, ParserException.GRAMMAR_EXCEPTION);
                    } else {
                        states.get(tempIndex).addFrom(currentState.index);
                    }
                }
            }
            index++;
        }

//        for (int i = 0, length = states.size(); i <length; i++)
//            System.out.println(states.get(i));

        Rule tempRule;
        for (LR1State lr1State : states) {
            for (LR1Item lr1Item : lr1State.lr1ItemSet) {
                if (null == lr1Item.getNextAbstractElement()) {
                    tempRule = lr1Item.rule;
                    // S' -> S, # accept
                    if (tempRule.leftNotTerminalElement.getText().equals(extensionalName) && lr1Item.nextInput.getText().equals(END_TERMINAL_STRING)) {
                        if (null != lr1State.addAction(lr1Item.nextInput, new Action(Action.ACCEPT, -1))) {
                            System.out.println(lr1State);
                            throw new ParserException(0, 0, ParserException.GRAMMAR_EXCEPTION);
                        }
                    } else {
                        if (null != lr1State.addAction(lr1Item.nextInput, new Action(Action.REDUCE, tempRule.index))) {
                            System.out.println(lr1State);
                            System.out.println(states.get(70));
                            throw new ParserException(0, 0, ParserException.GRAMMAR_EXCEPTION);
                        }
                    }
                }
            }
        }
    }

    public Node parse(List<Node> nodes) throws ParserException {
        nodes.add(new Node(terminalElementMap.get(END_TERMINAL_STRING)));
        Stack<Integer> stack = new Stack<Integer>();
        Stack<Node> nodeStack = new Stack<>();
        stack.push(0);
        AbstractElement nextAbstractElement;
        Action action;
        Node parent;
        Iterator<Node> iterator = nodes.iterator();
        Node node = iterator.next();
        while (node != null) {
            nextAbstractElement = node.abstractElement;
            action = states.get(stack.peek()).actionMap.get(nextAbstractElement);
            if (action == null) {
                throw new ParserException(0, 0, ParserException.WRONG_EXPRESSION);
            } else {
                switch (action.type) {
                    case Action.SHIFT:
                        stack.push(action.operand);
                        nodeStack.push(node);
                        node = iterator.next();
                        break;
                    case Action.REDUCE:
                        parent = new Node(rules.get(action.operand).leftNotTerminalElement);
                        for (int i = 0, length = rules.get(action.operand).rightAbstractElementList.size();
                             i < length; i++) {
                            parent.addSon(nodeStack.pop());
                            stack.pop();
                        }
                        nodeStack.push(parent);
                        action = states.get(stack.peek()).actionMap.get(parent.abstractElement);
                        stack.push(action.operand);
                        break;
                    case Action.ACCEPT:
                        System.out.println("OK");
                        return nodeStack.pop();
                }
            }
        }
        throw new ParserException(0, 0, ParserException.WRONG_EXPRESSION);
    }

    public Map<String, TerminalElement> getTerminalElementMap() {
        return terminalElementMap;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("     ");

        for (AbstractElement abstractElement : terminalElementMap.values())
            builder.append(String.format("%5s", abstractElement.toString()));

        for (AbstractElement abstractElement : notTerminalElementMap.values())
            builder.append(String.format("%5s", abstractElement.toString()));

        builder.append('\n');

        Action action;
        for (LR1State lr1State : states) {

            builder.append(String.format("%5d", lr1State.index));
            for (AbstractElement abstractElement : terminalElementMap.values()) {
                action = lr1State.actionMap.get(abstractElement);
                if (null == action)
                    builder.append("     ");
                else switch (action.type) {
                    case Action.SHIFT:
                        builder.append(String.format("%5s", "S" + action.operand));
                        break;
                    case Action.REDUCE:
                        builder.append(String.format("%5s", "r" + action.operand));
                        break;
                    case Action.ACCEPT:
                        builder.append("  acc");
                        break;
                }
            }

            for (AbstractElement abstractElement : notTerminalElementMap.values()) {
                action = lr1State.actionMap.get(abstractElement);
                if (null == action)
                    builder.append("     ");
                else if (action.type == Action.SHIFT) {
                    builder.append(String.format("%5d", action.operand));
                }
            }

            builder.append('\n');
        }

        return builder.toString();

    }
}
