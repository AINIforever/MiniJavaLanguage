package Parser;

import java.util.List;

public class Rule {

    NotTerminalElement leftNotTerminalElement;
    List<AbstractElement> rightAbstractElementList;

    private int hashCode;
    int index;

    Rule(NotTerminalElement notTerminalElement, List<AbstractElement> abstractElementList, int index) {
        this.leftNotTerminalElement = notTerminalElement;
        this.rightAbstractElementList = abstractElementList;
        hashCode = leftNotTerminalElement.hashCode() + rightAbstractElementList.hashCode();
        this.index = index;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Rule))
            return false;
        if (obj == this)
            return true;
        Rule another = (Rule) obj;
        return this.leftNotTerminalElement.equals(another.leftNotTerminalElement) && this.rightAbstractElementList.equals(another.rightAbstractElementList);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(index);
        stringBuilder.append(leftNotTerminalElement.getText()).append(" -> ");
        for (AbstractElement abstractElement : rightAbstractElementList)
            stringBuilder.append(abstractElement.getText()).append(' ');
        return stringBuilder.toString();
    }
}
