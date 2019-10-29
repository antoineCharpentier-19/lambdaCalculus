package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import util.NodeUpdateObserver;

@Data
@AllArgsConstructor
public class IntCons implements IntList {
    private Node head;
    private Node tail;

    @Override
    public String toString(){
        Node tmp = tail;
        String result = "[" + head;
        while(!(tmp instanceof IntNil)){
            result += ", " + (tmp instanceof IntCons ? ((IntCons)tmp).getHead() : tmp);
            tmp = (tmp instanceof IntCons ? ((IntCons)tmp).getTail() : new IntNil());
        }
        result +="]";
        return result;
    }

    @Override
    public Node replaceOcc(String name, Node arg) {
        return new IntCons(head.replaceOcc(name, arg), tail.replaceOcc(name,arg));
    }

    @Override
    public Node reduceByName() {
        return new IntCons(head.reduceByName(), tail.reduceByName());
    }

    @Override
    public Node debugReduceByName(NodeUpdateObserver notifier) {
        Node h = this.head.debugReduceByName(newVal -> notifier.onUpdate(new IntCons(newVal, this.tail)));
        Node tail = this.tail.debugReduceByName(newVal -> notifier.onUpdate(new IntCons(h, newVal)));
        IntCons intCons = new IntCons(h, tail);
        return intCons;
    }
}
