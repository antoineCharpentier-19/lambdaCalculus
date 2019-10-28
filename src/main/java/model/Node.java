package model;

import util.NodeUpdateObserver;

public interface Node {

    default Node reduceByName() {
        return this;
    }

    default Node debugReduceByName() {
        System.out.println(this);
        return debugReduceByName(newVal -> System.out.println(newVal.toString()));
    }

    default Node debugReduceByName(NodeUpdateObserver notifier) {
        return reduceByName(); // default implementation : nothing :)
    }

    default Node replaceOcc(String name, Node arg) {
        return this;
    }

    String toString();
}