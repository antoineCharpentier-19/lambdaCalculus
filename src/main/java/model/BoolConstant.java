package model;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class BoolConstant extends Node {

    private boolean value;

    @Override
    public String toString() {
        return value + "";
    }

    public Node reduceByName() {
        return clone();
    }

    public Node replaceOcc(String name, Node arg) {
        return clone();
    }

    public Node clone() {
        return new BoolConstant(value);
    }

    public boolean getValue() {
        return value;
    }
}
