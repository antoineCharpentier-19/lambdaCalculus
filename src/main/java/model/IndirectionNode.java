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
        if(!reduced) {
            wrapped = wrapped.reduceByValue(Optional.empty());
            reduced = true;
        }
        return this;
    }
}
