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
        Node tmp = tail;
        String result = "[" + head;
        while(!(tmp instanceof IntNil)){
            result += ", " + ((IntCons)tmp).getHead();
            tmp = ((IntCons)tmp).getTail();
        }
        result +="]";
        return result;
    }

    public Node replaceOcc(String name, Node arg) {
        return new IntCons(head.replaceOcc(name, arg), tail.replaceOcc(name,arg));
    }
}
