package model;

import lombok.AllArgsConstructor;
import util.NodeUpdateObserver;

import java.util.function.BiFunction;
import java.util.function.Function;

@AllArgsConstructor
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
        return "(" + op.stringVal + " " + body + ")";
    }

    public Node reduceByName() {
        return op.converter.apply(body.reduceByName());
    }

    @Override
    protected Node debugReduceByName(NodeUpdateObserver notifier) {
        Node result = (body.debugReduceByName(UnaryOp::new));
        result = op.converter.apply(body.reduceByName());
        notifier.onUpdate(result);
        return result;
    }

    public Node replaceOcc(String name, Node arg) {
        return new UnaryOp(op, body.replaceOcc(name, arg));
    }
}
