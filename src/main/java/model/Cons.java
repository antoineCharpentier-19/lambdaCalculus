package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import util.NodeUpdateObserver;

@Data
@AllArgsConstructor
public class Cons implements LCList {
    private Node head;
    private Node tail;

    @Override
    public String toString(){
        Node tmp = tail;
        String result = "[" + head;
        while(!(tmp instanceof Nil)){
            result += ", " + (tmp instanceof Cons ? ((Cons)tmp).getHead() : tmp);
            tmp = (tmp instanceof Cons ? ((Cons)tmp).getTail() : new Nil());
        }
        result +="]";
        return result;
    }

    @Override
    public Node replaceOcc(String name, Node arg) {
        return new Cons(head.replaceOcc(name, arg), tail.replaceOcc(name,arg));
    }

    @Override
    public Node reduceByName() {
        return new Cons(head.reduceByName(), tail.reduceByName());
    }

    @Override
    public Node debugReduceByName(NodeUpdateObserver notifier) {
        Node h = this.head.debugReduceByName(newVal -> notifier.onUpdate(new Cons(newVal, this.tail)));
        //Node tail = this.tail.debugReduceByName(newVal -> notifier.onUpdate(new IntCons(h, newVal)));
        Cons intCons = new Cons(h, tail);
        return intCons;
    }
}
