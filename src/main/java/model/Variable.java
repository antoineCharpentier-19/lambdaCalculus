package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Variable extends Node {
    String name;

    @Override
    public String toString() {
        return name + " ";
    }
}
