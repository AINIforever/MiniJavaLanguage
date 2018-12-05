package Parser;

import java.util.List;

public class LR1Item {

    Rule rule;
    int dotPointer;
    AbstractElement nextInput;

    int hashCode;

    LR1Item(Rule rule, int dotPointer, AbstractElement nextInput) {
        this.rule = rule;
        this.dotPointer = dotPointer;
        this.nextInput = nextInput;
        hashCode = rule.hashCode() + dotPointer + nextInput.hashCode();
    }

    AbstractElement getNextAbstractElement() {
        List<AbstractElement> ruleRightAbstractElement = rule.rightAbstractElementList;
        return dotPointer < ruleRightAbstractElement.size() ? ruleRightAbstractElement.get(dotPointer) : null;
    }

    LR1Item getNextLR1Item() {
        return new LR1Item(rule, dotPointer + 1, nextInput);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LR1Item))
            return false;
        LR1Item another = (LR1Item) obj;
        return rule.equals(another.rule) && dotPointer == another.dotPointer && nextInput.equals(another.nextInput);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(rule.leftNotTerminalElement.toString()).append(" -> ");
        int i = 0;
        for (AbstractElement abstractElement : rule.rightAbstractElementList) {
            if (i == dotPointer)
                builder.append('·').append(' ');
            builder.append(abstractElement.toString()).append(' ');
            i++;
        }
        if (i == dotPointer)
            builder.append('·').append(' ');
        builder.append(',').append(nextInput.toString());
        return builder.toString();
    }
}
