package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Lambda extends Node {
    String name;
    Node body;

    @Override
    public String toString() {
        return "(\\" + name + " -> " + body + ")";
    }
}
