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
        return name;
    }

    public Node reduceByName() {
        return clone();
    }

    public Node replaceOcc(String argName, Node arg) {
        return name.equals(argName) ? arg : clone();
    }

    public Node clone() {
        return new Variable(name);
    }
}
