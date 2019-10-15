package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BoolNot extends Node {

    private Node body;

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

    public Node clone() {
        return new BoolNot(body.clone());
    }
}
