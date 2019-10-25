package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import util.NodeUpdateObserver;

import java.util.function.BiFunction;

@AllArgsConstructor
@Data
public class BinaryOp extends Node {
    private final Node left;
    private final Node right;
    private final Type op;

    public enum Type {
        PLUS("+", (a, b) -> new IntConstant(((IntConstant) a).getValue() + ((IntConstant) b).getValue())),
        MINUS("-", (a, b) -> new IntConstant( ((IntConstant) a).getValue() - ((IntConstant) b).getValue())),
        TIMES("*", (a, b) -> new IntConstant(((IntConstant) a).getValue() * ((IntConstant) b).getValue())),
        DIVIDE("/", (a, b) -> new IntConstant(((IntConstant) a).getValue() / ((IntConstant) b).getValue())),
        OR("||", (a, b) -> new BoolConstant(((BoolConstant) a).getValue() || ((BoolConstant) b).getValue())),
        AND("&&", (a, b) -> new BoolConstant(((BoolConstant) a).getValue() && ((BoolConstant) b).getValue())),
        XOR("xor", (a, b) -> new BoolConstant(((BoolConstant) a).getValue() != ((BoolConstant) b).getValue()));
        private String stringVal;
        private BiFunction<Node, Node, Node> converter;

        Type(String stringVal, BiFunction<Node, Node, Node> converter) {
            this.converter = converter;
            this.stringVal = stringVal;
        }
    }

    @Override
    public Node reduceByName() {
        Node reducedLeft = left.reduceByName();
        // special cases
        if (op == Type.AND && !((BoolConstant)reducedLeft).getValue() || op == Type.OR && ((BoolConstant)reducedLeft).getValue()) {
            return reducedLeft;
        }
        Node reducedRight = right.reduceByName();
        return op.converter.apply(reducedLeft, reducedRight);
    }

    @Override
    public Node debugReduceByName(NodeUpdateObserver notifier) {
        Node reducedLeft = left.debugReduceByName(newVal -> notifier.onUpdate(new BinaryOp(newVal, right, op)));
        // special cases
        if (op == Type.AND && !((BoolConstant)reducedLeft).getValue() || op == Type.OR && ((BoolConstant)reducedLeft).getValue()) {
            notifier.onUpdate(reducedLeft);
            return reducedLeft;
        }
        Node reducedRight = right.debugReduceByName(newVal -> notifier.onUpdate(new BinaryOp(reducedLeft, newVal, op)));
        Node result = op.converter.apply(reducedLeft, reducedRight);
        notifier.onUpdate(result);
        return result;
    }

    @Override
    public Node replaceOcc(String name, Node arg) {
        return new BinaryOp(left.replaceOcc(name, arg), right.replaceOcc(name, arg), op);
    }

    public String toString() {
        return "(" + left + op.stringVal + right + ")";
    }
}
