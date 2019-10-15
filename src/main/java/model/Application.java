package model;

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

    public Node reduceByName() {
        if (left instanceof Lambda) {
            Node replaced = ((Lambda) left).betaReduce(right);
            return replaced.reduceByName();
        } else {
            return left.reduceByName();
        }
    }

    public Node replaceOcc(String name, Node arg) {
        return new Application(left.replaceOcc(name, arg), right.replaceOcc(name, arg));
    }
}
