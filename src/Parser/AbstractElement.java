package Parser;

public abstract class AbstractElement {

    public static final int TERMINAL_ELEMENT = 0;
    public static final int NOT_TERMINAL_ELEMENT = 1;

    private String text;
    private int type;

    private int hashCode;

    public AbstractElement(String text, int type) {
        this.text = text;
        this.type = type;
        hashCode = text.hashCode() + type;
    }

    public String getText() {
        return text;
    }

    public int getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof AbstractElement))
            return false;
        AbstractElement another = (AbstractElement) obj;
        return this.text.equals(another.text) && this.type == another.type;
    }

    @Override
    public String toString() {
        return text;
    }
}
