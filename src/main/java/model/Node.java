package model;

import util.NodeUpdateObserver;

import java.util.Optional;

public interface Node {

    default Node reduceByName(boolean print) {
        if (print) System.out.println(this.toString(false));
        return reduceByName(print ? Optional.of(newVal -> System.out.println(newVal.toString(false))) : Optional.empty());
    }

    default Node reduceByName(Optional<NodeUpdateObserver> observer) {
        return this;
    }

    default Node reduceByValue(boolean print) {
        if (print) System.out.println(this.toString(false));
        return reduceByValue(print ? Optional.of(newVal -> System.out.println(newVal.toString(false))) : Optional.empty());
    }

    default Node reduceByValue(Optional<NodeUpdateObserver> observer) {
        return this;
    }

    default Node reduceByNeed(boolean print) {
        if (print) System.out.println(this.toString(false));
        return reduceByNeed(print ? Optional.of(newVal -> System.out.println(newVal.toString(false))) : Optional.empty());
    }

    default Node reduceByNeed(Optional<NodeUpdateObserver> observer) {
        return this;
    }

    default Node replaceOcc(String name, Node arg) {
        return this;
    }

    default String print() {
        return toString(false);
    }

    String toString(boolean topLevel);
}