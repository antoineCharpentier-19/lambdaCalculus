package model;

import util.NodeUpdateObserver;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.BiFunction;

@AllArgsConstructor
@Getter
public class BoolBinary extends Node {
    private Node left;
    private Node right;
    private Type op;

    public static enum Type {
        OR("||", (a,b) -> new BoolConstant(a || b)),
        AND("&&", (a,b) -> new BoolConstant(a && b)),
        XOR("xor", (a,b) -> new BoolConstant(a != b));
        private String stringVal;
        private BiFunction<Boolean, Boolean, BoolConstant> converter;
        Type(String stringVal, BiFunction<Boolean, Boolean, BoolConstant> converter) {
            this.converter = converter;
            this.stringVal = stringVal;
        }
    }

    @Override
    public Node reduceByName() {
        Node reducedLeft = left.reduceByName();
        boolean leftVal = ((BoolConstant) reducedLeft).getValue();
        // special cases
        if(op == Type.AND && !leftVal) return new BoolConstant(false);
        if(op == Type.OR && leftVal) return new BoolConstant(true);

        Node reducedRight = right.reduceByName();
        boolean rightVal = ((BoolConstant) reducedRight).getValue();
        return op.converter.apply(leftVal, rightVal);
    }

    @Override
    public Node debugReduceByName(NodeUpdateObserver notifier) {
        Node reducedLeft = left.debugReduceByName(newVal->notifier.onUpdate(new BoolBinary(newVal,right, op)));
        boolean leftVal = ((BoolConstant) reducedLeft).getValue();
        // special cases
        if(op == Type.AND && !leftVal) {
            Node result = new BoolConstant(false);
            notifier.onUpdate(result);
            return result;
        }
        if(op == Type.OR && leftVal) {
            Node result = new BoolConstant(true);
            notifier.onUpdate(result);
            return result;
        }

        Node reducedRight = right.debugReduceByName(newVal->notifier.onUpdate(new BoolBinary(reducedLeft,newVal, op)));
        boolean rightVal = ((BoolConstant) reducedRight).getValue();

        BoolConstant result =  op.converter.apply(leftVal, rightVal);
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
