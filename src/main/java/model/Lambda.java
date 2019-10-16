package model;

import util.NodeUpdateObserver;
import lombok.Getter;

@Getter
public class Lambda extends Node {
    private final String formalParam;
    private final Node body;

    public Lambda(String formalParam, Node body) {
        this.formalParam = formalParam;
        this.body = body;
    }

    @Override
    public String toString() {
        return "(\\" + formalParam + " -> " + body + ")";
    }

    @Override
    public Node reduceByName() {
        return this;
    }

    public Node replaceOcc(String name, Node arg) {
        if (!name.equals(formalParam)) { // name - capturing
            return new Lambda(formalParam, body.replaceOcc(name, arg));
        } else {
            return this;
        }
    }

    public Node betaReduce(Node arg) {
        return body.replaceOcc(formalParam, arg);
    }
}
