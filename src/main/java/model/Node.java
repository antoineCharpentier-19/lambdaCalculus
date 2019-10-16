package model;

import util.NodeUpdateObserver;

public abstract class Node {

    public abstract Node reduceByName();

    public Node debugReduceByName() {
        System.out.println(this);
        return debugReduceByName(newVal -> System.out.println(newVal));
    }

    protected Node debugReduceByName(NodeUpdateObserver notifier) {
        return reduceByName(); // default implementation : nothing :)
    };

    public abstract Node replaceOcc(String name, Node arg);

    public abstract String toString();
}