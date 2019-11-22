package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import util.NodeUpdateObserver;

import java.util.Optional;

@Data
@AllArgsConstructor
public class RecursiveNode implements Node {
    private Node node;
    private String name;

    public RecursiveNode(String name) {
        this.name = name;
    }

    @Override
    public Node reduceByName(Optional<NodeUpdateObserver> observer) {
        observer.ifPresent(obs -> obs.onUpdate(node));
        return node.reduceByName(observer);
    }

    @Override
    public Node reduceByValue(Optional<NodeUpdateObserver> observer) {
        observer.ifPresent(obs -> obs.onUpdate(node));
        return node.reduceByValue(observer);
    }

    @Override
    public Node reduceByNeed(Optional<NodeUpdateObserver> observer) {
        observer.ifPresent(obs -> obs.onUpdate(node));
        return node.reduceByNeed(observer);
    }

    @Override
    public String toString(boolean topLevel) {
        return name;
    }
}
