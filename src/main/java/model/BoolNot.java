package model;

import util.NodeUpdateObserver;
import lombok.Getter;

@Getter
public class BoolNot extends Node {
    private final Node body;

    public BoolNot(Node body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "not " + body;
    }

    public Node reduceByName() {
        return ((BoolConstant)body.reduceByName()).opposite();
    }

    @Override
    protected Node debugReduceByName(NodeUpdateObserver notifier) {
        BoolConstant result = ((BoolConstant)body.debugReduceByName(BoolNot::new)).opposite();
        notifier.onUpdate(result);
        return result;
    }

    public Node replaceOcc(String name, Node arg) {
        return new BoolNot(body.replaceOcc(name, arg));
    }
}
