package model;

import util.NodeUpdateObserver;
import lombok.Getter;

import java.util.Set;

@Getter
public class Application extends Node {
    private final Node left;
    private final Node right;

    public Application(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    @Override
    protected String toString(Set<RecursiveLambda> visited) {
        return left.toString(visited) + " " + right.toString(visited);
    }

    @Override
    public Node reduceByName() { // TODO : add interface for reduced types
        return ((Lambda) left.reduceByName()).betaReduce(right).reduceByName();
    }

    @Override
    protected Node debugReduceByName(NodeUpdateObserver notifier) {
        Node leftReduced = left.debugReduceByName(newVal -> notifier.onUpdate(new Application(newVal, right)));
        Node betaReduced = ((Lambda) leftReduced).betaReduce(right);
        notifier.onUpdate(betaReduced);
        return betaReduced.debugReduceByName(notifier);
    }

    public Node replaceOcc(String name, Node arg) {
        return new Application(left.replaceOcc(name, arg), right.replaceOcc(name, arg));
    }
}
