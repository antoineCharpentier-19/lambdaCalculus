package model;

public class BoolConstant extends Node {
    private final boolean value;

    public BoolConstant(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }

    public Node reduceByName() {
        return this;
    }

    public Node replaceOcc(String name, Node arg) {
        return this;
    }

    public boolean getValue() {
        return value;
    }
}
