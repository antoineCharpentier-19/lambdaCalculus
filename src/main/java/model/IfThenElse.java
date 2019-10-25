package model;

import util.NodeUpdateObserver;
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
        return "if (" + cond.toString() + ") then " + left.toString() + " else " + right.toString();
    }

    @Override
    public Node reduceByName() {
        return ((BoolConstant) cond.reduceByName()).getValue() ? left.reduceByName() : right.reduceByName();
    }

    @Override
    protected Node debugReduceByName(NodeUpdateObserver notifier) {
        BoolConstant newCond = (BoolConstant) cond.debugReduceByName(newVal -> notifier.onUpdate(new IfThenElse(newVal, left, right)));
        Node newLeft = left.debugReduceByName(newVal -> notifier.onUpdate(new IfThenElse(newCond, newVal, right)));
        Node result;
        if (newCond.getValue()) {
            result = newLeft;
        } else {
            notifier.onUpdate(right);
            result = right.debugReduceByName(notifier);
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
