package Parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LR1State {

    Set<LR1Item> lr1ItemSet;

    int index;

    Map<AbstractElement, Action> actionMap;

    int hashCode;

    List<Integer> fromList;

    LR1State(Set<LR1Item> lr1ItemSet, int index) {
        this.lr1ItemSet = lr1ItemSet;
        this.index = index;
        this.actionMap = new HashMap<AbstractElement, Action>();
        hashCode = lr1ItemSet.hashCode() + index + actionMap.hashCode();
    }

    public Action addAction(AbstractElement abstractElement, Action action) {
        return actionMap.put(abstractElement, action);
    }

    void addFrom(int from) {
        fromList.add(from);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LR1State))
            return false;
        LR1State another = (LR1State) obj;
        return lr1ItemSet.equals(another.lr1ItemSet);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('I').append(index).append(':').append('\n');
        for (int i : fromList)
            builder.append(' ').append(i);
        builder.append('\n');
        for (LR1Item lr1Item : lr1ItemSet)
            builder.append(' ').append(lr1Item.toString()).append('\n');
        return builder.toString();
    }
}
