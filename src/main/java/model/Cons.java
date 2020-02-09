package model;

import util.NodeUpdateObserver;

public class Cons implements LCList {
    private Node head;
    private Node tail;

    public Cons(Node head, Node tail) {
        this.head = head;
        this.tail = tail;
    }

    public Node reduceByValue(NodeUpdateObserver observer) {
        Node reducedHead = head.reduceByValue(newVal -> observer.onUpdate(new Cons(newVal, tail)));
        Node reducedTail = tail.reduceByValue(newVal -> observer.onUpdate(new Cons(reducedHead, newVal)));
        return new Cons(reducedHead, reducedTail);
    }

    @Override
    public String toString(boolean topLevel){
        Node tmpHead = head;
        Node tmpTail = tail;
        if(topLevel) {
            while(! (tmpHead instanceof IrreductibleNode || tmpHead instanceof LCList)) tmpHead = tmpHead.reduceByName(a -> {}).unwrap();
            while(! (tmpTail instanceof IrreductibleNode || tmpTail instanceof LCList)) tmpTail = tmpTail.reduceByName(a -> {}).unwrap();
        }
        String result = "(" + tmpHead.toString(topLevel);
        while(tmpTail instanceof Cons){
            tmpHead = ((Cons) tmpTail).getHead();
            if(topLevel) {
                while(!(tmpHead instanceof IrreductibleNode)) tmpHead = tmpHead.reduceByName(a -> {}).unwrap();
            }
            result += " : " + (tmpHead).toString(false);
            tmpTail = ((Cons) tmpTail).getTail();
            if(topLevel) while(!(tmpTail instanceof IrreductibleNode || tmpTail instanceof LCList)) tmpTail = tmpTail.reduceByName(a -> {}).unwrap();
        }
        result += " : " + tmpTail.toString(false) + ")";
        return result;
    }

    @Override
    public Node replaceOcc(String name, Node arg) {
        return new Cons(head.replaceOcc(name, arg), tail.replaceOcc(name,arg));
    }

    public Node getHead() {
        return head;
    }

    public Node getTail() {
        return tail;
    }
}
