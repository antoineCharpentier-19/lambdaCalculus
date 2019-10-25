package model;

import lombok.Getter;

@Getter
public class Variable extends Node {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public Node reduceByName() {
        return this;
    } // TODO : something is wrong with this / should not happen

    public Node replaceOcc(String argName, Node arg) {
        return name.equals(argName) ? arg : this;
    }

}
