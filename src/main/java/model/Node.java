package model;

import util.NodeUpdateObserver;

import java.util.HashSet;
import java.util.Set;

public abstract class Node {

    public abstract Node reduceByName();

    public Node debugReduceByName() {
        System.out.println(this);
        return debugReduceByName(newVal -> System.out.println(newVal.toString(new HashSet<>())));
    }

    protected Node debugReduceByName(NodeUpdateObserver notifier) {
        return reduceByName(); // default implementation : nothing :)
    };

    public abstract Node replaceOcc(String name, Node arg);

    public String toString() {
        return toString(new HashSet<>());
    }

    protected abstract String toString(Set<RecursiveLambda> visited);
}