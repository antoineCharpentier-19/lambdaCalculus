package model;

import lombok.Getter;

@Getter
public class Variable implements Node {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Node replaceOcc(String argName, Node arg) {
        return name.equals(argName) ? arg : this;
    }
}
