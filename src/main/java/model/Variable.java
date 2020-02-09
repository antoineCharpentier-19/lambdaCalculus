package model;

public class Variable implements IrreductibleNode {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public String toString(boolean topLevel) {
        return name;
    }

    public Node replaceOcc(String argName, Node arg) {
        return name.equals(argName) ? arg : this;
    }
}
