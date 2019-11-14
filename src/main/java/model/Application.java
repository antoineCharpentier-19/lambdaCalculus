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
    public String toString(boolean topLevel) {
        return left.toString(topLevel) + "(" + right.toString(topLevel) + ")";
    }

    @Override
    public Node reduceByName(NodeUpdateObserver n) {
        if(n != null) {
            Node leftReduced = left.reduceByName(newVal -> n.onUpdate(new Application(newVal, right)));
            Node betaReduced = ((Lambda) leftReduced).betaReduce(right);
            n.onUpdate(betaReduced);
            return betaReduced.reduceByName(n);
        } else return ((Lambda) left.reduceByName(null)).betaReduce(right).reduceByName(null);
    }

    public Node replaceOcc(String name, Node arg) {
        return new Application(left.replaceOcc(name, arg), right.replaceOcc(name, arg));
    }
}
