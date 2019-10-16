package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import util.NodeUpdateObserver;

import java.util.function.BiFunction;

@AllArgsConstructor
@Getter
public class BoolBinary extends Node {
    private Node left;
    private Node right;
    private Type op;

    public enum Type {
        OR("||", (a, b) -> new BoolConstant(a || b)),
        AND("&&", (a, b) -> new BoolConstant(a && b)),
        XOR("xor", (a, b) -> new BoolConstant(a != b));
        private String stringVal;
        private BiFunction<Boolean, Boolean, BoolConstant> converter;

        Type(String stringVal, BiFunction<Boolean, Boolean, BoolConstant> converter) {
            this.converter = converter;
            this.stringVal = stringVal;
        }
    }

    @Override
    public Node reduceByName() {
        BoolConstant reducedLeft = (BoolConstant) left.reduceByName();
        // special cases
        if (op == Type.AND && !reducedLeft.getValue() || op == Type.OR && reducedLeft.getValue())
            return reducedLeft;
        BoolConstant reducedRight = (BoolConstant) right.reduceByName();
        return op.converter.apply(reducedLeft.getValue(), reducedRight.getValue());
    }

    @Override
    public Node debugReduceByName(NodeUpdateObserver notifier) {
        BoolConstant reducedLeft = (BoolConstant) left.debugReduceByName(newVal -> notifier.onUpdate(new BoolBinary(newVal, right, op)));
        // special cases
        if (op == Type.AND && !reducedLeft.getValue() || op == Type.OR && reducedLeft.getValue()) {
            notifier.onUpdate(reducedLeft);
            return reducedLeft;
        }
        BoolConstant reducedRight = (BoolConstant) right.debugReduceByName(newVal -> notifier.onUpdate(new BoolBinary(reducedLeft, newVal, op)));
        BoolConstant result = op.converter.apply(reducedLeft.getValue(), reducedRight.getValue());
        notifier.onUpdate(result);
        return result;
    }

    @Override
    public Node replaceOcc(String name, Node arg) {
        return new BoolBinary(left.replaceOcc(name, arg), right.replaceOcc(name, arg), op);
    }

    @Override
    public String toString() {
        return "(" + left + " " + op.stringVal + " " + right + ")";
    }
}
