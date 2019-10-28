package model;

import lombok.AllArgsConstructor;
import util.NodeUpdateObserver;

import java.util.function.Function;

@AllArgsConstructor
public class UnaryOp implements Node {

    public enum Operator {
        NOT("not", a -> new BoolConstant(!((BoolConstant) a).getValue())),
        NEGATIVE("-", a -> new IntConstant(-((IntConstant) a).getValue())),
        NIL("nil", a->new BoolConstant(a instanceof IntNil)),
        HEAD("head", a->((IntCons) a).getHead()),
        TAIL("tail", a->((IntCons) a).getTail());
        private String stringVal;
        private  Function<Node, Node> converter;

        Operator(String stringVal, Function<Node, Node> converter) {
            this.converter = converter;
            this.stringVal = stringVal;
        }
    }

    private Operator op;
    private Node body;

    public UnaryOp(Node body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "(" + op.stringVal + " " + body.toString() + ")";
    }

    public Node reduceByName() {
        return op.converter.apply(body.reduceByName());
    }

    public Node debugReduceByName(NodeUpdateObserver notifier) {
        Node result = op.converter.apply(body.debugReduceByName(UnaryOp::new));
        notifier.onUpdate(result);
        return result;
    }

    public Node replaceOcc(String name, Node arg) {
        return new UnaryOp(op, body.replaceOcc(name, arg));
    }
}
