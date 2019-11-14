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
        return ((Lambda) left.reduceByName(observer.map(obs -> newVal -> obs.onUpdate(new Application(newVal, right)))))
                        .betaReduce(right)
                        .reduceByName(observer);
    }

    public Node replaceOcc(String name, Node arg) {
        return new Application(left.replaceOcc(name, arg), right.replaceOcc(name, arg));
    }
}
