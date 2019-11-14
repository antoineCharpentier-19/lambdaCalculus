package model;

import lombok.Getter;
import util.NodeUpdateObserver;

@Getter
public class IfThenElse implements Node {
    private final Node cond;
    private final Node left;
    private final Node right;

    public IfThenElse(Node cond, Node left, Node right) {
        this.cond = cond;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString(boolean topLevel) {
        return "if (" + cond.toString(topLevel) + ") then " + left.toString(topLevel) + " else " + right.toString(topLevel);
    }

    @Override
    public Node reduceByName(NodeUpdateObserver n) {
        BoolConstant newCond = (BoolConstant) cond.reduceByName(newVal -> {if(n!=null) n.onUpdate(new IfThenElse(newVal, left, right));});
        Node newLeft = left.reduceByName(newVal -> {if(n!=null) n.onUpdate(new IfThenElse(newCond, newVal, right));});
        Node result;
        if (newCond.getValue()) {
            if(n!=null) n.onUpdate(newLeft);
            result = newLeft;
        } else {
            if(n!=null) n.onUpdate(right);
            result = right.reduceByName(n);
        }
        return result;
    }

    public Node replaceOcc(String name, Node arg) {
        return new IfThenElse(
                cond.replaceOcc(name, arg),
                left.replaceOcc(name, arg),
                right.replaceOcc(name, arg)
        );
    }
}
