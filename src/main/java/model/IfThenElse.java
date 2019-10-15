package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IfThenElse extends Node {

    private Node cond;
    private Node left;
    private Node right;

    @Override
    public String toString() {
        return "if (" + cond + ") then " + left + " else " + right;
    }

    public Node reduceByName() {
        return ((BoolConstant)cond.reduceByName()).getValue() ? left.reduceByName() : right.reduceByName();
    }

    public Node replaceOcc(String name, Node arg) {
        return new IfThenElse(
                cond.replaceOcc(name, arg),
                left.replaceOcc(name, arg),
                right.replaceOcc(name, arg)
        );
    }

    public Node clone() {
        return new IfThenElse(cond.clone(), left.clone(), right.clone());
    }
}
