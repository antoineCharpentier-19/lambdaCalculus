package util;

import model.Node;

@FunctionalInterface
public interface NodeUpdateObserver {
    void onUpdate(Node newVal);
}
