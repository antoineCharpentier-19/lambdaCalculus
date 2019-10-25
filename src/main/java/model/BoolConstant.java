package model;

import java.util.Objects;
import java.util.Set;

public class BoolConstant extends Node {
    private boolean value;

    public BoolConstant(boolean value) {
        this.value = value;
    }

    @Override
    protected String toString(Set<RecursiveLambda> visited) {
        return Boolean.toString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoolConstant that = (BoolConstant) o;
        return getValue() == that.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

    @Override
    public Node reduceByName() { return this; }

    public Node replaceOcc(String name, Node arg) {
        return this;
    }

    public boolean getValue() {
        return value;
    }
}
