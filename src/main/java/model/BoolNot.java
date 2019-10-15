package model;

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
        BoolConstant bool = (BoolConstant)body.reduceByName();
        return new BoolConstant(!bool.getValue());
    }

    public Node replaceOcc(String name, Node arg) {
        return new BoolNot(body.replaceOcc(name, arg));
    }
}
