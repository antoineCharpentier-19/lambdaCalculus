package model;

import Util.NodeUpdateObserver;
import lombok.Getter;

@Getter
public class IfThenElse extends Node {
    private final Node cond;
    private final Node left;
    private final Node right;

    public IfThenElse(Node cond, Node left, Node right) {
        this.cond = cond;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "if (" + cond + ") then " + left + " else " + right;
    }

    @Override
    public Node reduceByName() {
        return ((BoolConstant) cond.reduceByName()).getValue() ? left.reduceByName() : right.reduceByName();
    }

    @Override
    protected Node debugReduceByName(NodeUpdateObserver notifier) {
        BoolConstant newCond = (BoolConstant) cond.debugReduceByName(newVal -> notifier.onUpdate(new IfThenElse(newVal, left, right)));
        Node newLeft = left.debugReduceByName(newVal -> notifier.onUpdate(new IfThenElse(newCond, newVal, right)));
        Node newRight = right.debugReduceByName(newVal -> notifier.onUpdate(new IfThenElse(newCond, newLeft, newVal)));
        Node result = newCond.getValue() ? newLeft : newRight;
        notifier.onUpdate(result);

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
