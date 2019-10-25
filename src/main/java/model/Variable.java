package model;

import lombok.Getter;

import java.util.Set;

@Getter
public class Variable extends Node {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    protected String toString(Set<RecursiveLambda> visited) {
        return name;
    }

    public Node reduceByName() {  // TODO : create irreductible super class
        throw new UnsupportedOperationException("This should not happen : variable should be ");
    }

    public Node replaceOcc(String argName, Node arg) {
        return name.equals(argName) ? arg : this;
    }

}
