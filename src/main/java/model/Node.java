package model;

import util.NodeUpdateObserver;

public interface Node {

    default Node reduceByName(boolean print) {
        if (print) System.out.println(this.toString(false));
        return reduceByName(print ? newVal -> System.out.println(newVal.toString(false)) : null);
    }

    default Node reduceByName(NodeUpdateObserver n) {
        return this;
    }

    //helper
    default Node reduceByName() {
        return reduceByName(true);
    }

    default Node replaceOcc(String name, Node arg) {
        return this;
    }

    default String print() {
        return toString(false);
    }

    String toString(boolean topLevel);
}