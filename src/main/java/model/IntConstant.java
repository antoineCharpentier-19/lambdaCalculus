package model;

import lombok.Getter;

import java.util.Objects;

@Getter
public class IntConstant implements Node {

    private int value;

    public IntConstant(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
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
}
