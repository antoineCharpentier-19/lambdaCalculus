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
    public Node reduceByName(NodeUpdateObserver n) {
        if(n!=null) n.onUpdate(node);
        return node.reduceByName(n);
    }

    @Override
    public String toString(boolean topLevel) {
        return name;
    }
}
