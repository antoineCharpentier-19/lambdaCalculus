package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import util.NodeUpdateObserver;

import java.util.Optional;

public class IndirectionNode implements Node{
    @Getter
    @Setter
    private Node wrapped;
    private boolean reduced = false;

    public IndirectionNode(Node wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public String toString(boolean topLevel) {
        return wrapped.toString(topLevel);
    }

    public Node reduceByNeed(Optional<NodeUpdateObserver> observer) {
        boolean notify = !(wrapped instanceof IndirectionNode) && !(wrapped instanceof IrreductibleNode);
        if(!reduced) {
            wrapped = wrapped.reduceByNeed(Optional.empty());
            if(notify) observer.ifPresent(o->o.onUpdate(wrapped));
            reduced = true;
        }
        return this;
    }

    public Node reduceByName(Optional<NodeUpdateObserver> observer) {
        boolean notify = !(wrapped instanceof IndirectionNode) && !(wrapped instanceof IrreductibleNode);
        if(!reduced) {
            wrapped = wrapped.reduceByName(Optional.empty());
            if(notify) observer.ifPresent(o->o.onUpdate(wrapped));
            reduced = true;
        }
        return this;
    }

    public Node unwrap() {
        return wrapped.unwrap();
    }
}
