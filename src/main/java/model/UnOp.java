package model;

import lombok.AllArgsConstructor;
import util.NodeUpdateObserver;

import java.util.function.Function;

@AllArgsConstructor
public class UnOp implements Node {

    public enum Op {
        NOT("not", a -> new BoolConstant(!((BoolConstant) a).getValue())),
        NEGATIVE("-", a -> new IntConstant(-((IntConstant) a).getValue())),
        NIL("nil", a->new BoolConstant(a instanceof IntNil)),
        HEAD("head", a->((IntCons) a).getHead()),
        TAIL("tail", a->((IntCons) a).getTail());
        private String stringVal;
        private  Function<Node, Node> converter;

        Op(String stringVal, Function<Node, Node> converter) {
            this.converter = converter;
            this.stringVal = stringVal;
        }

        private static Op opFromString(String s) {
            for (Op o : Op.values()) {
                if (o.stringVal.equals(s)) {
                    return o;
                }
            }
            throw new IllegalArgumentException(s + " is not a valid binary operator.");
        }
    }

    private Op op;
    private Node body;

    public UnOp(String op, Node body) {
        this.op = Op.opFromString(op);
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
        Node result = op.converter.apply(body.debugReduceByName(newVal -> new UnOp(op, newVal)));
        notifier.onUpdate(result);
        if(result instanceof RecursiveNode) result = ((RecursiveNode) result).getNode();
        return result;
    }

    public Node replaceOcc(String name, Node arg) {
        return new UnOp(op, body.replaceOcc(name, arg));
    }
}
