package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import util.NodeUpdateObserver;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
public class Cons implements LCList {
    private Node head;
    private Node tail;

    public Node reduceByValue(Optional<NodeUpdateObserver> observer) {
        Node reducedHead = head.reduceByValue(observer.map(obs -> newVal -> obs.onUpdate(new Cons(newVal, tail))));
        Node reducedTail = tail.reduceByValue(observer.map(obs -> newVal -> obs.onUpdate(new Cons(reducedHead, newVal))));
        return new Cons(reducedHead, reducedTail);
    }

    @Override
    public String toString(boolean topLevel){
        Node tmpHead = head;
        Node tmpTail = tail;
        if(topLevel) {
            while(! (tmpHead instanceof IrreductibleNode || tmpHead instanceof LCList)) tmpHead = tmpHead.reduceByName(Optional.empty()).unwrap();
            while(! (tmpTail instanceof IrreductibleNode || tmpTail instanceof LCList)) tmpTail = tmpTail.reduceByName(Optional.empty()).unwrap();
        }
        String result = "(" + tmpHead.toString(topLevel);
        while(tmpTail instanceof Cons){
            tmpHead = ((Cons) tmpTail).getHead();
            if(topLevel) {
                while(!(tmpHead instanceof IrreductibleNode)) tmpHead = tmpHead.reduceByName(Optional.empty()).unwrap();
            }
            result += " : " + (tmpHead).toString(false);
            tmpTail = ((Cons) tmpTail).getTail();
            if(topLevel) while(!(tmpTail instanceof IrreductibleNode || tmpTail instanceof LCList)) tmpTail = tmpTail.reduceByName(Optional.empty()).unwrap();
        }
        result += " : " + tmpTail.toString(false) + ")";
        return result;
    }

    @Override
    public Node replaceOcc(String name, Node arg) {
        return new Cons(head.replaceOcc(name, arg), tail.replaceOcc(name,arg));
    }
}
