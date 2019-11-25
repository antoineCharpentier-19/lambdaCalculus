package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import util.LambdaCalculusUtils;
import util.NodeUpdateObserver;

import java.util.Optional;

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
            while(!LambdaCalculusUtils.instanceOf(tmpHead, IrreductibleNode.class)) tmpHead = tmpHead.reduceByName(Optional.empty());
            while(!LambdaCalculusUtils.instanceOf(tmpTail, IrreductibleNode.class)) tmpTail = tmpTail.reduceByName(Optional.empty());
        }
        String result = "(" + tmpHead.toString(topLevel);
        while(tmpTail instanceof Cons){
            tmpHead = ((Cons) tmpTail).getHead();
            if(topLevel) {
                while(!(tmpHead instanceof IrreductibleNode)) tmpHead = tmpHead.reduceByName(Optional.empty());
            }
            result += " : " + (tmpHead).toString(false);
            tmpTail = ((Cons) tmpTail).getTail();
            if(topLevel) while(!(tmpTail instanceof IrreductibleNode)) tmpTail = tmpTail.reduceByName(Optional.empty());
        }
        result += " : " + tmpTail.toString(false) + ")";
        return result;
    }

    @Override
    public Node replaceOcc(String name, Node arg) {
        return new Cons(head.replaceOcc(name, arg), tail.replaceOcc(name,arg));
    }
}
