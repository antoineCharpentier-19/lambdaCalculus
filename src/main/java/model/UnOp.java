package model;

import lombok.AllArgsConstructor;
import util.NodeUpdateObserver;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
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

    @Override
    public String toString(boolean topLevel) {
        return op.stringVal + " (" + body.toString(topLevel) + ")";
    }

    @Override
    public Node reduceByName(Optional<NodeUpdateObserver> observer) {
        Optional<NodeUpdateObserver> nodeUpdateObserver = observer.map(obs -> newVal -> obs.onUpdate(new UnOp(op, newVal)));
        Node reducedBody = body.reduceByName(nodeUpdateObserver);
        if (op == Op.TAIL || op == Op.HEAD) {
            while (!(reducedBody instanceof Cons))
                reducedBody = reducedBody.reduceByName(nodeUpdateObserver).unwrap();
        }
        Node result = op.converter.apply(reducedBody);
        observer.ifPresent(obs -> obs.onUpdate(result));
        return result;
    }

    @Override
    public Node reduceByValue(Optional<NodeUpdateObserver> observer) {
        Optional<NodeUpdateObserver> nodeUpdateObserver = observer.map(obs -> newVal -> obs.onUpdate(new UnOp(op, newVal)));
        Node reducedBody = body;
        while(!(reducedBody instanceof IrreductibleNode))
            reducedBody = reducedBody.reduceByValue(nodeUpdateObserver);
        // TODO - tail on an infinite list should not happen
//        if (op == Op.TAIL || op == Op.HEAD) {
//            while (!(reducedBody instanceof Cons))
//                reducedBody = reducedBody.reduceByValue(nodeUpdateObserver);
//        }
        Node result = op.converter.apply(reducedBody);
        observer.ifPresent(obs -> obs.onUpdate(result));
        return result;
    }

    @Override
    public Node reduceByNeed(Optional<NodeUpdateObserver> observer) {
        Optional<NodeUpdateObserver> nodeUpdateObserver = observer.map(obs -> newVal -> obs.onUpdate(new UnOp(op, newVal)));
        Node reducedBody = body.reduceByNeed(nodeUpdateObserver).unwrap();
        while(!(reducedBody instanceof IrreductibleNode))
            reducedBody = reducedBody.reduceByValue(nodeUpdateObserver);
        Node result = op.converter.apply(reducedBody);
        observer.ifPresent(obs -> obs.onUpdate(result));
        return result;
    }


    public Node replaceOcc(String name, Node arg) {
        return new UnOp(op, body.replaceOcc(name, arg));
    }
}
