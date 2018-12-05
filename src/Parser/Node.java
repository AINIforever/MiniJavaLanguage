package Parser;

import java.util.ArrayList;
import java.util.List;

public class Node {

    Node parent;
    List<Node> sons;
    AbstractElement abstractElement;

    public Node(AbstractElement abstractElement) {
        this.abstractElement = abstractElement;
        sons = new ArrayList<>();
    }

    public void addSon(Node node) {
        sons.add(0, node);
        node.parent = this;
    }

    public List<Node> getSons() {
        return sons;
    }

    public AbstractElement getAbstractElement() {
        return abstractElement;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Node))
            return false;
        Node another = (Node) obj;
        return parent.equals(another.parent) && sons.equals(another.sons) && another.abstractElement.equals(abstractElement);
    }

    @Override
    public String toString() {
        return abstractElement.toString();
    }
}
