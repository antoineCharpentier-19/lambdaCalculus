package model;

import util.NodeUpdateObserver;

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
    public Node reduceByName(NodeUpdateObserver observer) {
        BoolConstant newCond = (BoolConstant) cond.reduceByName(newVal -> observer.onUpdate(new IfThenElse(newVal, left, right)));
        Node newLeft = left.reduceByName(newVal -> observer.onUpdate(new IfThenElse(newCond, newVal, right)));
        if (newCond.getValue()) {
            observer.onUpdate(newLeft);
            return newLeft;
        } else {
            observer.onUpdate(right);
            return right.reduceByName(observer);
        }
    }

    @Override
    public Node reduceByValue(NodeUpdateObserver observer) {
        BoolConstant newCond = (BoolConstant) cond.reduceByValue(newVal -> observer.onUpdate(new IfThenElse(newVal, left, right)));
        Node newLeft = left.reduceByValue(newVal -> observer.onUpdate(new IfThenElse(newCond, newVal, right)));
        if (newCond.getValue()) {
            observer.onUpdate(newLeft);
            return newLeft;
        } else {
            observer.onUpdate(right);
            return right.reduceByValue(observer);
        }
    }

    @Override
    public Node reduceByNeed(NodeUpdateObserver observer) {
        BoolConstant newCond = (BoolConstant) cond.reduceByNeed(newVal -> observer.onUpdate(new IfThenElse(newVal, left, right)));
        Node newLeft = left.reduceByNeed(newVal -> observer.onUpdate(new IfThenElse(newCond, newVal, right)));
        if (newCond.getValue()) {
            observer.onUpdate(newLeft);
            return newLeft;
        } else {
            observer.onUpdate(right);
            return right.reduceByNeed(observer);
        }
    }

    public Node replaceOcc(String name, Node arg) {
        return new IfThenElse(
                cond.replaceOcc(name, arg),
                left.replaceOcc(name, arg),
                right.replaceOcc(name, arg)
        );
    }
}
