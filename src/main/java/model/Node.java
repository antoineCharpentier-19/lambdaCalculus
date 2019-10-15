package model;

public abstract class Node {
    public abstract Node reduceByName();
    public abstract Node replaceOcc(String name, Node arg);
}
