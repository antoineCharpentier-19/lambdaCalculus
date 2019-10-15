package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Application extends Node {
    public Node left;
    public Node right;

    @Override
    public String toString() {
        return left + " " + right;
    }
}
