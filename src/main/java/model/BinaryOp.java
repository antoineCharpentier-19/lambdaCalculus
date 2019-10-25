package model;

import lombok.Data;
import util.NodeUpdateObserver;

import java.util.function.BiFunction;

@Data
public class BinaryOp extends Node {
    private final Node left;
    private final Node right;
    private final Operator op;

    public BinaryOp(Operator op, Node left, Node right) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public enum Operator {
        PLUS("+", (a, b) -> new IntConstant(((IntConstant) a).getValue() + ((IntConstant) b).getValue())),
        MINUS("-", (a, b) -> new IntConstant( ((IntConstant) a).getValue() - ((IntConstant) b).getValue())),
        TIMES("*", (a, b) -> new IntConstant(((IntConstant) a).getValue() * ((IntConstant) b).getValue())),
        DIVIDE("/", (a, b) -> new IntConstant(((IntConstant) a).getValue() / ((IntConstant) b).getValue())),
        OR("||", (a, b) -> new BoolConstant(((BoolConstant) a).getValue() || ((BoolConstant) b).getValue())),
        AND("&&", (a, b) -> new BoolConstant(((BoolConstant) a).getValue() && ((BoolConstant) b).getValue())),
        XOR("xor", (a, b) -> new BoolConstant(((BoolConstant) a).getValue() != ((BoolConstant) b).getValue()));
        private String stringVal;
        private BiFunction<Node, Node, Node> converter;

        Operator(String stringVal, BiFunction<Node, Node, Node> converter) {
            this.converter = converter;
            this.stringVal = stringVal;
        }
    }

    @Override
    public Node reduceByName() {
        Node reducedLeft = left.reduceByName();
        // special cases
        if (op == Operator.AND && !((BoolConstant)reducedLeft).getValue() || op == Operator.OR && ((BoolConstant)reducedLeft).getValue()) {
            return reducedLeft;
        }
        Node reducedRight = right.reduceByName();
        return op.converter.apply(reducedLeft, reducedRight);
    }

    @Override
    public Node debugReduceByName(NodeUpdateObserver notifier) {
        Node reducedLeft = left.debugReduceByName(newVal -> notifier.onUpdate(new BinaryOp(op, newVal, right)));
        // special cases
        if (op == Operator.AND && !((BoolConstant)reducedLeft).getValue() || op == Operator.OR && ((BoolConstant)reducedLeft).getValue()) {
            notifier.onUpdate(reducedLeft);
            return reducedLeft;
        }
        Node reducedRight = right.debugReduceByName(newVal -> notifier.onUpdate(new BinaryOp(op, reducedLeft, newVal)));
        Node result = op.converter.apply(reducedLeft, reducedRight);
        notifier.onUpdate(result);
        return result;
    }

    @Override
    public Node replaceOcc(String name, Node arg) {
        return new BinaryOp(op, left.replaceOcc(name, arg), right.replaceOcc(name, arg));
    }

    public String toString() {
        return "(" + left + op.stringVal + right + ")";
    }
}
