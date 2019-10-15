package model;

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

    public Node reduceByName() {
        return ((BoolConstant) cond.reduceByName()).getValue() ? left.reduceByName() : right.reduceByName();
    }

    public Node replaceOcc(String name, Node arg) {
        return new IfThenElse(
                cond.replaceOcc(name, arg),
                left.replaceOcc(name, arg),
                right.replaceOcc(name, arg)
        );
    }
}
