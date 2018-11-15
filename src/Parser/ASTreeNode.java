package Parser;

import java.util.ArrayList;

public class ASTreeNode {
    private ArrayList<ASTreeNode> childNodeLists;
    String data;

    public ASTreeNode(ArrayList<ASTreeNode> childNodeLists, String data){
        this.childNodeLists = childNodeLists;
        this.data = data;
    }

    public ASTreeNode(String data){
        this.data = data;
        this.childNodeLists = new ArrayList<ASTreeNode>();
    }

    public void setChildNodeLists(ArrayList<ASTreeNode> childNodeLists) {
        this.childNodeLists = childNodeLists;
    }

    public ArrayList<ASTreeNode> getChildNodeLists() {
        return childNodeLists;
    }

    @Override
    public String toString() {
        return data;
    }

    public static void displaytree(ASTreeNode f, int level) {       //递归显示树

        String preStr = "";
        for(int i=0; i<level; i++) {
            preStr += "    ";
        }

        for(int i=0; i<f.childNodeLists.size(); i++) {
            ASTreeNode t = f.childNodeLists.get(i);
            System.out.println(preStr + "-"+t.data);

            if(! t.childNodeLists.isEmpty()) {
                displaytree(t, level + 1);
            }
        }
    }
}
