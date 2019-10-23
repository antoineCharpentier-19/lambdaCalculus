package model;

import lombok.Getter;
import util.NodeUpdateObserver;

import java.util.function.BiFunction;

@Getter
public class IntBinary extends Node {

    public enum Type {
        PLUS("+", (a, b) -> new IntConstant(a + b)),
        MINUS("-", (a, b) -> new IntConstant(a - b)),
        TIMES("*", (a, b) -> new IntConstant(a * b)),
        DIVIDE("/", (a, b) -> new IntConstant(a / b));
        private String stringVal;
        private BiFunction<Integer, Integer, IntConstant> converter;

        Type(String stringVal, BiFunction<Integer, Integer, IntConstant> converter) {
            this.converter = converter;
            this.stringVal = stringVal;
        }
    }

    private final Node left;
    private final Node right;
    private final Type op;

    public IntBinary(Node left, Node right, Type op) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    @Override
    public Node reduceByName() {
        IntConstant reducedLeft = (IntConstant) left.reduceByName();
        IntConstant reducedRight = (IntConstant) right.reduceByName();
        return op.converter.apply(reducedLeft.getValue(), reducedRight.getValue());
    }

    @Override
    public Node debugReduceByName(NodeUpdateObserver notifier) {
        IntConstant reducedLeft = (IntConstant) left.debugReduceByName(newVal -> notifier.onUpdate(new IntBinary(newVal, right, op)));
        IntConstant reducedRight = (IntConstant) right.debugReduceByName(newVal -> notifier.onUpdate(new IntBinary(reducedLeft, newVal, op)));
        IntConstant result = op.converter.apply(reducedLeft.getValue(), reducedRight.getValue());
        notifier.onUpdate(result);
        return result;
    }

    @Override
    public Node replaceOcc(String name, Node arg) {
        return new IntBinary(left.replaceOcc(name, arg), right.replaceOcc(name, arg), op);
    }

    @Override
    public String toString() {
        return "(" + left + " " + op.stringVal + " " + right + ")";
    }
}
