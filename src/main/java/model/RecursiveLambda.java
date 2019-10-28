package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import util.NodeUpdateObserver;

@Data
@AllArgsConstructor
public class RecursiveLambda implements Node {
    private Lambda lambda;
    private String name;

    public RecursiveLambda(String name) {
        this.name = name;
    }

    @Override
    public Node reduceByName() {
        return lambda;
    }

    @Override
    public Node debugReduceByName(NodeUpdateObserver notifier) {
        notifier.onUpdate(lambda);
        return lambda;
    }

    @Override
    public String toString() {
        return name;
    }
}
