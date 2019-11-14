package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import util.NodeUpdateObserver;

@Getter
@Setter
@AllArgsConstructor
public class Cons implements LCList {
    private Node head;
    private Node tail;

    @Override
    public String toString(boolean topLevel){
        Node tmpHead = head;
        Node tmpTail = tail;
        if(topLevel) {
            tmpHead = head.reduceByName(null);
            tmpTail = tail.reduceByName(null);
        }
        String result = "(" + tmpHead.toString(topLevel);
        while(tmpTail instanceof Cons){
            result += " : " + (topLevel ? ((Cons) tmpTail).getHead().reduceByName(null) : ((Cons) tmpTail).getHead()).toString(false);
            tmpTail = ((Cons) tmpTail).getTail();
            if(topLevel) tmpTail = tmpTail.reduceByName(null);
        }
        result += " : " + tmpTail.toString(false) + ")";
        return result;
    }

    @Override
    public Node replaceOcc(String name, Node arg) {
        return new Cons(head.replaceOcc(name, arg), tail.replaceOcc(name,arg));
    }
}
