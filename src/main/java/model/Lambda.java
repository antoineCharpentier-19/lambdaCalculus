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
        if (!name.equals(formalParam)) { // name - capturing
            return new Lambda(formalParam, body.replaceOcc(name, arg));
        } else {
            return clone();
        }
    }

    public Node betaReduce(Node arg) {
        return body.replaceOcc(formalParam, arg);
    }

    public Node clone() {
        return new Lambda(formalParam, body.clone());
    }
}
