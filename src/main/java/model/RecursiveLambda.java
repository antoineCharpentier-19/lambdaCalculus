package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import util.NodeUpdateObserver;

import java.util.Set;

@Data
@AllArgsConstructor
public class RecursiveLambda extends Node {
    private Lambda lambda;
    private String name;

    public RecursiveLambda(String name) {
        this.name = name;
    }

    @Override
    public Node reduceByName() {
        return lambda.reduceByName();
    }

    @Override
    protected Node debugReduceByName(NodeUpdateObserver notifier) {
        return lambda.debugReduceByName(notifier);
    }

    @Override
    public Node replaceOcc(String name, Node arg) {
        return lambda.replaceOcc(name, arg);
    }

    @Override
    protected String toString(Set<RecursiveLambda> visited) {
        return name;
    }
}
