package model;

import lombok.Getter;
import util.NodeUpdateObserver;

import java.util.Optional;

@Getter
public class IfThenElse implements Node {
    private final Node cond;
    private final Node left;
    private final Node right;

    public IfThenElse(Node cond, Node left, Node right) {
        this.cond = cond;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString(boolean topLevel) {
        return "if (" + cond.toString(topLevel) + ") then " + left.toString(topLevel) + " else " + right.toString(topLevel);
    }

    @Override
    public Node reduceByName(Optional<NodeUpdateObserver> observer) {
        BoolConstant newCond = (BoolConstant) cond.reduceByName(observer.map(obs -> newVal -> obs.onUpdate(new IfThenElse(newVal, left, right))));
        Node newLeft = left.reduceByName(observer.map(obs -> newVal -> obs.onUpdate(new IfThenElse(newCond, newVal, right))));
        if (newCond.getValue()) {
            observer.ifPresent(obs -> obs.onUpdate(newLeft));
            return newLeft;
        } else {
            observer.ifPresent(obs -> obs.onUpdate(right));
            return right.reduceByName(observer);
        }
    }

    @Override
    public Node reduceByValue(Optional<NodeUpdateObserver> observer) {
        BoolConstant newCond = (BoolConstant) cond.reduceByValue(observer.map(obs -> newVal -> obs.onUpdate(new IfThenElse(newVal, left, right))));
        Node newLeft = left.reduceByValue(observer.map(obs -> newVal -> obs.onUpdate(new IfThenElse(newCond, newVal, right))));
        if (newCond.getValue()) {
            observer.ifPresent(obs -> obs.onUpdate(newLeft));
            return newLeft;
        } else {
            observer.ifPresent(obs -> obs.onUpdate(right));
            return right.reduceByValue(observer);
        }
    }

    @Override
    public Node reduceByNeed(Optional<NodeUpdateObserver> observer) {
        BoolConstant newCond = (BoolConstant) cond.reduceByNeed(observer.map(obs -> newVal -> obs.onUpdate(new IfThenElse(newVal, left, right))));
        Node newLeft = left.reduceByNeed(observer.map(obs -> newVal -> obs.onUpdate(new IfThenElse(newCond, newVal, right))));
        if (newCond.getValue()) {
            observer.ifPresent(obs -> obs.onUpdate(newLeft));
            return newLeft;
        } else {
            observer.ifPresent(obs -> obs.onUpdate(right));
            return right.reduceByNeed(observer);
        }
    }

    public Node replaceOcc(String name, Node arg) {
        return new IfThenElse(
                cond.replaceOcc(name, arg),
                left.replaceOcc(name, arg),
                right.replaceOcc(name, arg)
        );
    }
}
