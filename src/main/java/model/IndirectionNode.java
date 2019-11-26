package model;

import util.NodeUpdateObserver;

public class IndirectionNode implements Node{
    private Node wrapped;
    private boolean reduced = false;

    public IndirectionNode(Node wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public String toString(boolean topLevel) {
        return wrapped.toString(topLevel);
    }

    public Node reduceByNeed(NodeUpdateObserver observer) {
        boolean notify = !(wrapped instanceof IndirectionNode) && !(wrapped instanceof IrreductibleNode);
        if(!reduced) {
            wrapped = wrapped.reduceByNeed(a -> {});
            if(notify) observer.onUpdate(wrapped);
            reduced = true;
        }
        return this;
    }

    public Node reduceByName(NodeUpdateObserver observer) {
        boolean notify = !(wrapped instanceof IndirectionNode) && !(wrapped instanceof IrreductibleNode);
        if(!reduced) {
            wrapped = wrapped.reduceByName(a -> {});
            if(notify) observer.onUpdate(wrapped);
            reduced = true;
        }
        return this;
    }

    public Node unwrap() {
        return wrapped.unwrap();
    }
}
