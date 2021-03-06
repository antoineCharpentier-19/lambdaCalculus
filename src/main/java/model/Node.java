package model;

import util.NodeUpdateObserver;

import java.util.concurrent.atomic.AtomicInteger;

public interface Node {

    default Node reduceByName(boolean print) {
        if (print) System.out.println(this.toString(false));
        AtomicInteger c = new AtomicInteger(0);
        Node output = reduceByName(print ? newVal -> {System.out.println(newVal.toString(false)); c.incrementAndGet();} : a -> c.incrementAndGet());
        System.out.println(c.intValue() + " réductions en call by Name.");
        return output;
    }

    default Node reduceByName(NodeUpdateObserver observer) {
        return this;
    }

    default Node reduceByValue(boolean print) {
        if (print) System.out.println(this.toString(false));
        AtomicInteger c = new AtomicInteger(0);
        Node output = reduceByValue(print ? newVal -> {System.out.println(newVal.toString(false)); c.incrementAndGet();} : a -> c.incrementAndGet());
        System.out.println(c.intValue() + " réductions en call by Value.");
        return output;
    }

    default Node reduceByValue(NodeUpdateObserver observer) {
        return this;
    }

    default Node reduceByNeed(boolean print) {
        if (print) System.out.println(this.toString(false));
        AtomicInteger c = new AtomicInteger(0);
        Node output = reduceByNeed(print ? newVal -> {System.out.println(newVal.toString(false)); c.incrementAndGet();} : a -> c.incrementAndGet());
        System.out.println(c.intValue() + " réductions en call by Need.");
        return output;
    }

    default Node reduceByNeed(NodeUpdateObserver observer) {
        return this;
    }

    default Node replaceOcc(String name, Node arg) {
        return this;
    }

    default String print() {
        return toString(false);
    }

    default Node unwrap() {
        return this;
    }

    String toString(boolean topLevel);
}