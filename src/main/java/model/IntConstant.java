package model;

import lombok.Getter;

import java.util.Objects;
import java.util.Set;

@Getter
public class IntConstant extends Node {

    private int value;

    public IntConstant(int value) {
        this.value = value;
    }

    @Override
    protected String toString(Set<RecursiveLambda> visited) {
        return Integer.toString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntConstant that = (IntConstant) o;
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
}
