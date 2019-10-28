package model;

import java.util.Objects;

public class BoolConstant implements Node {
    private boolean value;

    public BoolConstant(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
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

    public boolean getValue() {
        return value;
    }
}
