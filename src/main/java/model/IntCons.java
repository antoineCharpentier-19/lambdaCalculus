package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IntCons implements Node, IntList {
    private Node head;
    private Node tail;

    @Override
    public String toString(){
        return "["+head+", "+tail+"]";
    }

    public Node replaceOcc(String name, Node arg) {
        return new IntCons(head.replaceOcc(name, arg), tail.replaceOcc(name,arg));
    }
}
