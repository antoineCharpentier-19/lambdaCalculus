package model;

import util.NodeUpdateObserver;

import java.util.function.BiFunction;
import java.util.function.Function;

public class UnaryOp extends Node {

    public enum Type {
        NOT("not", a -> new BoolConstant(!((BoolConstant) a).getValue())),
        NEGATIVE("-", a -> new IntConstant(-((IntConstant) a).getValue()));
        private String stringVal;
        private  Function<Node, Node> converter;

        Type(String stringVal, Function<Node, Node> converter) {
            this.converter = converter;
            this.stringVal = stringVal;
        }
    }

    private Type op;
    private Node body;

    public UnaryOp(Node body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "not " + body;
    }

    public Node reduceByName() {
        return ((BoolConstant)body.reduceByName()).opposite();
    }

    @Override
    protected Node debugReduceByName(NodeUpdateObserver notifier) {
        Node result = (body.debugReduceByName(UnaryOp::new));
        notifier.onUpdate(result);
        return result;
    }

    public Node replaceOcc(String name, Node arg) {
        return new BoolNot(body.replaceOcc(name, arg));
    }
}
