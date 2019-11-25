package model;

import lombok.Data;
import util.LambdaCalculusUtils;
import util.NodeUpdateObserver;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class BiOp implements Node{
    private final Node left;
    private final Node right;
    private final Op op;

    private enum Op {
        PLUS("+", (a, b) -> new IntConstant(((IntConstant) a).getValue() + ((IntConstant) b).getValue())),
        MINUS("-", (a, b) -> new IntConstant(((IntConstant) a).getValue() - ((IntConstant) b).getValue())),
        TIMES("*", (a, b) -> new IntConstant(((IntConstant) a).getValue() * ((IntConstant) b).getValue())),
        DIVIDE("/", (a, b) -> new IntConstant(((IntConstant) a).getValue() / ((IntConstant) b).getValue())),
        LT("<", (a, b) -> BoolConstant.of(((IntConstant) a).getValue() < ((IntConstant) b).getValue())),
        OR("||", (a, b) -> BoolConstant.of(((BoolConstant) a).getValue() || ((BoolConstant) b).getValue())),
        AND("&&", (a, b) -> BoolConstant.of(((BoolConstant) a).getValue() && ((BoolConstant) b).getValue())),
        XOR("xor", (a, b) -> BoolConstant.of(((BoolConstant) a).getValue() != ((BoolConstant) b).getValue())),
        EQUAL("==", (a, b) -> BoolConstant.of(a.equals(b))),
        ;
        private String stringVal;
        private BiFunction<Node, Node, Node> converter;

        private static final Map<String, Op> BY_CODE_MAP =
                Collections.unmodifiableMap(Arrays.stream(Op.values()).collect(Collectors.toMap(op -> op.stringVal, Function.identity())));

        public static Op forCode(String code) {
            return BY_CODE_MAP.get(code);
        }

        Op(String stringVal, BiFunction<Node, Node, Node> converter) {
            this.converter = converter;
            this.stringVal = stringVal;
        }
    }

    public BiOp(Node left, String op, Node right) {
        this.left = left;
        this.right = right;
        this.op = Op.forCode(op);
    }

    public BiOp(Node left, Op op, Node right) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    @Override
    public Node reduceByName(Optional<NodeUpdateObserver> observer) {
        Node reducedLeft = left;
        while(!LambdaCalculusUtils.instanceOf(reducedLeft, IrreductibleNode.class))
            reducedLeft = reducedLeft.reduceByName(observer.map(obs -> newVal -> obs.onUpdate(new BiOp(newVal, op, right))));
        // special cases
        if (op == Op.AND && !((BoolConstant) reducedLeft).getValue() || op == Op.OR && ((BoolConstant) reducedLeft).getValue()) {
            Node finalReducedLeft = reducedLeft;
            observer.ifPresent(obs -> obs.onUpdate(finalReducedLeft));
            return reducedLeft;
        }
        Node reducedRight = right;
        while(!LambdaCalculusUtils.instanceOf(reducedRight, IrreductibleNode.class))
            reducedRight = reducedRight.reduceByName(observer.map(obs -> newVal -> obs.onUpdate(new BiOp(newVal, op, right))));
        Node result = op.converter.apply(reducedLeft, reducedRight);
        observer.ifPresent(obs -> obs.onUpdate(result));
        return result;
    }

    @Override
    public Node reduceByValue(Optional<NodeUpdateObserver> observer) {
        Node reducedLeft = left;
        while(!LambdaCalculusUtils.instanceOf(reducedLeft, IrreductibleNode.class))
            reducedLeft = reducedLeft.reduceByValue(observer.map(obs -> newVal -> obs.onUpdate(new BiOp(newVal, op, right))));
        // special cases
        if (op == Op.AND && !((BoolConstant) reducedLeft).getValue() || op == Op.OR && ((BoolConstant) reducedLeft).getValue()) {
            Node finalReducedLeft = reducedLeft;
            observer.ifPresent(obs -> obs.onUpdate(finalReducedLeft));
            return reducedLeft;
        }
        Node reducedRight = right;
        while(!LambdaCalculusUtils.instanceOf(reducedRight, IrreductibleNode.class))
            reducedRight = reducedRight.reduceByValue(observer.map(obs -> newVal -> obs.onUpdate(new BiOp(newVal, op, right))));
        Node result = op.converter.apply(reducedLeft, reducedRight);
        observer.ifPresent(obs -> obs.onUpdate(result));
        return result;
    }

    @Override
    public Node reduceByNeed(Optional<NodeUpdateObserver> observer) {
        Node reducedLeft = left.reduceByNeed(observer.map(obs -> newVal -> obs.onUpdate(new BiOp(newVal, op, right)))).unwrap();
        // special cases
        if (op == Op.AND && !((BoolConstant) reducedLeft).getValue() || op == Op.OR && ((BoolConstant) reducedLeft).getValue()) {
            Node finalReducedLeft = reducedLeft;
            observer.ifPresent(obs -> obs.onUpdate(finalReducedLeft));
            return reducedLeft;
        }
        Node finalReducedLeft1 = reducedLeft;
        Node reducedRight = right.reduceByNeed(observer.map(obs -> newVal -> obs.onUpdate(new BiOp(finalReducedLeft1, op, newVal)))).unwrap();

        Node result = op.converter.apply(reducedLeft, reducedRight);
        observer.ifPresent(obs -> obs.onUpdate(result));
        return result;
    }

    @Override
    public Node replaceOcc(String name, Node arg) {
        return new BiOp(left.replaceOcc(name, arg), op, right.replaceOcc(name, arg));
    }

    public String toString(boolean topLevel) {
        return left.toString(topLevel) + op.stringVal + right.toString(topLevel) ;
    }

}
