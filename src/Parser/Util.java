package Parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Util {

    public static Set<LR1Item> go(LR1State lr1State, AbstractElement abstractElement, List<Rule> ruleList) {
        Set<LR1Item> lr1ItemSet = new HashSet<>();
        for (LR1Item lr1Item : lr1State.lr1ItemSet) {
            if (abstractElement == lr1Item.getNextAbstractElement())
                lr1ItemSet.add(lr1Item.getNextLR1Item());
        }
        closure(lr1ItemSet, ruleList);
        return lr1ItemSet;
    }

    public static void closure(Set<LR1Item> lr1ItemSet, List<Rule> ruleList) {
        List<LR1Item> lr1ItemList = new ArrayList<>(lr1ItemSet);
        LR1Item lr1Item, tempLR1Item;
        List<AbstractElement> itemRuleRightAbstractElementList;
        List<TerminalElement> firstTerminalElementList;
        AbstractElement nextNotTerminalElement;
        int dotPointer;
        for (int i = 0; i < lr1ItemList.size(); i++) {
            lr1Item = lr1ItemList.get(i);
            dotPointer = lr1Item.dotPointer;
            itemRuleRightAbstractElementList = lr1Item.rule.rightAbstractElementList;
            if (dotPointer < itemRuleRightAbstractElementList.size()
                    && (nextNotTerminalElement = itemRuleRightAbstractElementList.get(dotPointer)).getType() == AbstractElement.NOT_TERMINAL_ELEMENT) {

                if (dotPointer + 1 < itemRuleRightAbstractElementList.size())
                    firstTerminalElementList = first(itemRuleRightAbstractElementList.get(dotPointer + 1), ruleList);
                else
                    firstTerminalElementList = first(lr1Item.nextInput, ruleList);

                for (TerminalElement terminalElement : firstTerminalElementList) {

                    for (Rule rule : ruleList)
                        if (rule.leftNotTerminalElement.equals(nextNotTerminalElement)) {
                            tempLR1Item = new LR1Item(rule, 0, terminalElement);
                            if (!lr1ItemSet.contains(tempLR1Item)) {
                                lr1ItemSet.add(tempLR1Item);
                                lr1ItemList.add(tempLR1Item);
                            }
                        }

                }

            }
        }
    }

    public static List<TerminalElement> first(AbstractElement abstractElement, List<Rule> ruleList) {
        List<TerminalElement> result = new ArrayList<>();
        if (abstractElement.getType() == AbstractElement.TERMINAL_ELEMENT) {
            result.add((TerminalElement) abstractElement);
            return result;
        }

        List<NotTerminalElement> visitedNotTerminalElementList = new ArrayList<>();
        visitedNotTerminalElementList.add((NotTerminalElement) abstractElement);
        Set<NotTerminalElement> visitedNotTerminalElementSet = new HashSet<>();
        visitedNotTerminalElementSet.add((NotTerminalElement) abstractElement);
        int index = 0;
        NotTerminalElement notTerminalElement;
        AbstractElement rightFirstElement;
        while (index < visitedNotTerminalElementList.size()) {
            notTerminalElement = visitedNotTerminalElementList.get(index);
            for (Rule rule : ruleList)
                if (rule.leftNotTerminalElement.equals(notTerminalElement)) {
                    rightFirstElement = rule.rightAbstractElementList.get(0);
                    if (rightFirstElement.getType() == AbstractElement.TERMINAL_ELEMENT)
                        result.add((TerminalElement) rightFirstElement);
                    else if (!visitedNotTerminalElementSet.contains((NotTerminalElement) rightFirstElement)) {
                        visitedNotTerminalElementList.add((NotTerminalElement) rightFirstElement);
                        visitedNotTerminalElementSet.add((NotTerminalElement) rightFirstElement);
                    }
                }
            index ++;
        }
        return result;
    }

}
