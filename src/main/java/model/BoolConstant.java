package model;

import java.util.Objects;

public class BoolConstant implements Node, IrreductibleNode {
    private boolean value;

    public static final BoolConstant f = new BoolConstant(false);
    public static final BoolConstant t = new BoolConstant(true);

    public static BoolConstant of(boolean bool) {
        return bool ? t : f;
    }

    private BoolConstant(boolean value) {
        this.value = value;
    }

    @Override
    public String toString(boolean topLevel) {
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
