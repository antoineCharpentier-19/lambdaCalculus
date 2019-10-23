package model;

import lombok.Getter;

@Getter
public class IntConstant extends Node {

    private int value;

    public IntConstant(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public Node reduceByName() { return this; }

    public Node replaceOcc(String name, Node arg) {
        return this;
    }
}
