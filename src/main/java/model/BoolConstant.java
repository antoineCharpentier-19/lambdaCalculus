package model;

public class BoolConstant extends Node {
    private boolean value;

    public BoolConstant(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }

    @Override
    public Node reduceByName() { return this; }

    public Node replaceOcc(String name, Node arg) {
        return this;
    }

    public boolean getValue() {
        return value;
    }

    public BoolConstant opposite(){
        return new BoolConstant(!value);
    }
}
