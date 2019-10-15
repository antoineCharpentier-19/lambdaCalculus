package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Application extends Node {
    private Node left;
    private Node right;

    @Override
    public String toString() {
        return left + " " + right;
    }

    public Node reduceByName() {
        if (left instanceof Lambda) {
            return left.replaceOcc(((Lambda) left).getFormalParam(), right).reduceByName();
        } else {
            return left.reduceByName();
        }
    }

    public Node replaceOcc(String name, Node arg) {
        return new Application(left.replaceOcc(name, arg), right.replaceOcc(name, arg));
    }
}
