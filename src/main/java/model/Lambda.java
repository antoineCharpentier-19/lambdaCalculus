package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Lambda extends Node {
    private String formalParam;
    private Node body;

    @Override
    public String toString() {
        return "(\\" + formalParam + " -> " + body + ")";
    }

    public Node reduceByName() {
        return new Lambda(formalParam, body.reduceByName());
    }

    public Node replaceOcc(String name, Node arg) {
        return body.replaceOcc(name, arg);
    }
}
