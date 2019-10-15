package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Variable extends Node {
    private String name;

    @Override
    public String toString() {
        return name + " ";
    }

    public Node reduceByName() {
        return this;
    }

    public Node replaceOcc(String argName, Node arg) {
        return name.equals(argName) ? arg : new Variable(name);
    }
}
