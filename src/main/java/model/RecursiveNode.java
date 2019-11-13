package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import util.NodeUpdateObserver;

@Data
@AllArgsConstructor
public class RecursiveNode implements Node {
    private Node node;
    private String name;

    public RecursiveNode(String name) {
        this.name = name;
    }

    @Override
    public Node reduceByName() {
        return node;
    }

    @Override
    public Node debugReduceByName(NodeUpdateObserver notifier) {
        notifier.onUpdate(node);
        return node.debugReduceByName(notifier);
    }

    @Override
    public String toString() {
        return name;
    }
}
