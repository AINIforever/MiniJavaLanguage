package Parser;

public class Action {

    public static final int SHIFT = 101;
    public static final int REDUCE = 102;
    public static final int ACCEPT = 103;

    int type;
    int operand;

    public Action(int type, int operand) {
        this.type = type;
        this.operand = operand;
    }

}
