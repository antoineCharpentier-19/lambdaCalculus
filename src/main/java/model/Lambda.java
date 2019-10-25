package model;

import util.NodeUpdateObserver;
import lombok.Getter;

import java.util.Set;

@Getter
public class Lambda extends Node {
    private final String formalParam;
    private final Node body;

    public Lambda(String formalParam, Node body) {
        this.formalParam = formalParam;
        this.body = body;
    }

    @Override
    protected String toString(Set<RecursiveLambda> visited) {
        return "(\\" + formalParam + " -> " + body.toString(visited) + ")";
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
