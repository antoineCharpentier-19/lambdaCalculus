package model;

import lombok.Getter;
import util.NodeUpdateObserver;

@Getter
public class Application implements Node {
    private final Node left;
    private final Node right;

    public Application(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return left.toString() + " " + right.toString();
    }

    @Override
    public Node reduceByName() { // TODO : add interface for reduced types
        return ((Lambda) left.reduceByName()).betaReduce(right).reduceByName();
    }

    @Override
    public Node debugReduceByName(NodeUpdateObserver notifier) {
        Node leftReduced = left.debugReduceByName(newVal -> notifier.onUpdate(new Application(newVal, right)));
        Node betaReduced = ((Lambda) leftReduced).betaReduce(right);
        notifier.onUpdate(betaReduced);
        return betaReduced.debugReduceByName(notifier);
    }

    public Node replaceOcc(String name, Node arg) {
        return new Application(left.replaceOcc(name, arg), right.replaceOcc(name, arg));
    }
}
