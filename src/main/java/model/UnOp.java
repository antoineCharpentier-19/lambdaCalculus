package model;

import util.NodeUpdateObserver;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UnOp implements Node {

    public enum Op {
        NOT("not", a -> BoolConstant.of(!((BoolConstant) a).getValue())),
        NEGATIVE("-", a -> new IntConstant(-((IntConstant) a).getValue())),
        NIL("nil", a -> BoolConstant.of(a instanceof Nil)),
        HEAD("head", a -> ((Cons) a).getHead()),
        TAIL("tail", a -> ((Cons) a).getTail());
        private String stringVal;
        private Function<Node, Node> converter;
        private static final Map<String, Op> BY_CODE_MAP =
                Collections.unmodifiableMap(Arrays.stream(Op.values()).collect(Collectors.toMap(op -> op.stringVal, Function.identity())));

        Op(String stringVal, Function<Node, Node> converter) {
            this.converter = converter;
            this.stringVal = stringVal;
        }

        private static Op forCode(String code) {
            return BY_CODE_MAP.get(code);
        }
    }

    private Op op;
    private Node body;

    public UnOp(String op, Node body) {
        this.op = Op.forCode(op);
        this.body = body;
    }

    public UnOp(Op op, Node body) {
        this.op = op;
        this.body = body;
    }

    @Override
    public String toString(boolean topLevel) {
        return op.stringVal + " (" + body.toString(topLevel) + ")";
    }

    @Override
    public Node reduceByName(NodeUpdateObserver observer) {
        NodeUpdateObserver nodeUpdateObserver = newVal -> observer.onUpdate(new UnOp(op, newVal));
        Node reducedBody = body.reduceByName(nodeUpdateObserver);
        while(!(reducedBody instanceof IrreductibleNode))
            reducedBody = reducedBody.reduceByName(nodeUpdateObserver).unwrap();
        Node result = op.converter.apply(reducedBody);
        observer.onUpdate(result);
        return result.reduceByName(observer);
    }

    @Override
    public Node reduceByValue(NodeUpdateObserver observer) {
        NodeUpdateObserver nodeUpdateObserver = newVal -> observer.onUpdate(new UnOp(op, newVal));
        Node reducedBody = body;
        while(!(reducedBody instanceof IrreductibleNode))
            reducedBody = reducedBody.reduceByValue(nodeUpdateObserver).unwrap();
        Node result = op.converter.apply(reducedBody);
        observer.onUpdate(result);
        return result.reduceByValue(observer);
    }

    @Override
    public Node reduceByNeed(NodeUpdateObserver observer) {
        NodeUpdateObserver nodeUpdateObserver = newVal -> observer.onUpdate(new UnOp(op, newVal));
        Node reducedBody = body.reduceByNeed(nodeUpdateObserver).unwrap();
        while(!(reducedBody instanceof IrreductibleNode))
            reducedBody = reducedBody.reduceByNeed(nodeUpdateObserver);
        Node result = op.converter.apply(reducedBody);
        observer.onUpdate(result);
        return result.reduceByNeed(observer);
    }


    public Node replaceOcc(String name, Node arg) {
        return new UnOp(op, body.replaceOcc(name, arg));
    }
}
