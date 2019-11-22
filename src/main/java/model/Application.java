package model;

import lombok.Getter;
import util.NodeUpdateObserver;

import java.util.Optional;

@Getter
public class Application implements Node {
    private final Node left;
    private final Node right;

    public Application(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString(boolean topLevel) {
        return left.toString(topLevel) + "(" + right.toString(topLevel) + ")";
    }

    @Override
    public Node reduceByName(Optional<NodeUpdateObserver> observer) {
        Node betaReduced = ((Lambda) left.reduceByName(observer.map(obs -> newVal -> obs.onUpdate(new Application(newVal, right))))).betaReduce(right);
        observer.ifPresent(o -> o.onUpdate(betaReduced));
        return betaReduced.reduceByName(observer);
    }

    @Override
    public Node reduceByValue(Optional<NodeUpdateObserver> observer) {
        Lambda l = ((Lambda) left.reduceByValue(observer.map(obs -> newVal -> obs.onUpdate(new Application(newVal, right)))));
        Node betaReduced = l.betaReduce(right.reduceByValue(observer.map(obs -> newVal -> obs.onUpdate(new Application(l, newVal)))));
        observer.ifPresent(o -> o.onUpdate(betaReduced));
        return betaReduced.reduceByValue(observer);
    }

    @Override
    public Node reduceByNeed(Optional<NodeUpdateObserver> observer) {
        Node betaReduced = ((Lambda) left.reduceByNeed(observer.map(obs -> newVal -> obs.onUpdate(new Application(newVal, right))))).betaReduce(new IndirectionNode(right));
        observer.ifPresent(o -> o.onUpdate(betaReduced));
        return betaReduced.reduceByNeed(observer);
    }

    public Node replaceOcc(String name, Node arg) {
        return new Application(left.replaceOcc(name, arg), right.replaceOcc(name, arg));
    }
}
