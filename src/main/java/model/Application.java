package model;

import Util.NodeUpdateObserver;
import lombok.Getter;

@Getter
public class Application extends Node {
    private final Node left;
    private final Node right;

    public Application(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return left + " " + right;
    }

    @Override
    public Node reduceByName() {
        return ((Lambda) left).betaReduce(right).reduceByName();
    }

    @Override
    protected Node debugReduceByName(NodeUpdateObserver notifier) {
        Node replaced = ((Lambda) left).betaReduce(right);
        notifier.onUpdate(replaced);
        return replaced.debugReduceByName(notifier);
    }

    public Node replaceOcc(String name, Node arg) {
        return new Application(left.replaceOcc(name, arg), right.replaceOcc(name, arg));
    }
}
