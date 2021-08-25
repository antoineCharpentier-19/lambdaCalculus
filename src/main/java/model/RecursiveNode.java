package model;

import util.NodeUpdateObserver;

public class RecursiveNode implements Node {
    private Node node;
    private String name;

    public RecursiveNode(String name) {
        this.name = name;
    }

    @Override
    public Node reduceByName(NodeUpdateObserver observer) {
        observer.onUpdate(node);
        return node.reduceByName(observer);
    }

    @Override
    public Node reduceByValue(NodeUpdateObserver observer) {
        observer.onUpdate(node);
        return node.reduceByValue(observer);
    }

    @Override
    public Node reduceByNeed(NodeUpdateObserver observer) {
        observer.onUpdate(node);
        return node.reduceByNeed(observer);
    }

    @Override
    public String toString(boolean topLevel) {
        return name;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
