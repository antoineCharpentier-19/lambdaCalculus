package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import util.NodeUpdateObserver;

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
        notifier.onUpdate(lambda);
        return lambda.debugReduceByName(notifier);
    }

    @Override
    public Node replaceOcc(String name, Node arg) {
        return this;
    }

    @Override
    public String toString() {
        return name;
    }
}
