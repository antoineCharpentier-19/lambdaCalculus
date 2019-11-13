package model;

import lombok.Data;
import util.NodeUpdateObserver;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class BiOp implements Node {
    private final Node left;
    private final Node right;
    private final Op op;

    private enum Op {
        PLUS("+", (a, b) -> new IntConstant(((IntConstant) a).getValue() + ((IntConstant) b).getValue())),
        MINUS("-", (a, b) -> new IntConstant(((IntConstant) a).getValue() - ((IntConstant) b).getValue())),
        TIMES("*", (a, b) -> new IntConstant(((IntConstant) a).getValue() * ((IntConstant) b).getValue())),
        DIVIDE("/", (a, b) -> new IntConstant(((IntConstant) a).getValue() / ((IntConstant) b).getValue())),
        OR("||", (a, b) -> new BoolConstant(((BoolConstant) a).getValue() || ((BoolConstant) b).getValue())),
        AND("&&", (a, b) -> new BoolConstant(((BoolConstant) a).getValue() && ((BoolConstant) b).getValue())),
        XOR("xor", (a, b) -> new BoolConstant(((BoolConstant) a).getValue() != ((BoolConstant) b).getValue())),
        EQUAL("==", (a, b) -> new BoolConstant(a.equals(b))),
        CONS(":", (a, b) -> new Cons((IntConstant) a, (LCList) b)),
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
    public Node reduceByName() {
        Node reducedLeft = left.reduceByName();
        // special cases
        if (op == Op.AND && !((BoolConstant) reducedLeft).getValue() || op == Op.OR && ((BoolConstant) reducedLeft).getValue()) {
            return reducedLeft;
        }
        Node reducedRight = right.reduceByName();
        return op.converter.apply(reducedLeft, reducedRight);
    }

    @Override
    public Node debugReduceByName(NodeUpdateObserver notifier) {
        Node reducedLeft = left.debugReduceByName(newVal -> notifier.onUpdate(new BiOp(newVal, op, right)));
        // special cases
        if (op == Op.AND && !((BoolConstant) reducedLeft).getValue() || op == Op.OR && ((BoolConstant) reducedLeft).getValue()) {
            notifier.onUpdate(reducedLeft);
            return reducedLeft;
        }
        Node reducedRight = right.debugReduceByName(newVal -> notifier.onUpdate(new BiOp(reducedLeft, op, newVal)));
        Node result = op.converter.apply(reducedLeft, reducedRight);
        notifier.onUpdate(result);
        return result;
    }

    @Override
    public Node replaceOcc(String name, Node arg) {
        return new BiOp(left.replaceOcc(name, arg), op, right.replaceOcc(name, arg));
    }

    public String toString() {
        return "(" + left.toString() + op.stringVal + right.toString() + ")";
    }

}
