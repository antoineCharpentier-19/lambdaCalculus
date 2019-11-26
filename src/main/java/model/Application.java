package model;

import util.NodeUpdateObserver;

public class Application implements Node {
    private final Node left;
    private final Node right;

    public Application(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString(boolean topLevel) {
        return left.toString(topLevel) + "(" + right.toString(topLevel) + ")";
    }

    @Override
    public Node reduceByName(NodeUpdateObserver observer) {
        Node betaReduced = ((LambdaExpr) left.reduceByName(newVal -> observer.onUpdate(new Application(newVal, right))).unwrap()).betaReduce(right);
        observer.onUpdate(betaReduced);
        return betaReduced.reduceByName(observer);
    }

    @Override
    public Node reduceByValue(NodeUpdateObserver observer) {
        LambdaExpr l = ((LambdaExpr) left.reduceByValue(newVal -> observer.onUpdate(new Application(newVal, right))));
        Node betaReduced = l.betaReduce(right.reduceByValue(newVal -> observer.onUpdate(new Application(l, newVal))));
        observer.onUpdate(betaReduced);
        return betaReduced.reduceByValue(observer);
    }

    @Override
    public Node reduceByNeed(NodeUpdateObserver observer) {
        Node reducedLeft = left.reduceByNeed(newVal -> observer.onUpdate(new Application(newVal, right))).unwrap();
        Node betaReduced = ((LambdaExpr) reducedLeft).betaReduce(new IndirectionNode(right));
        observer.onUpdate(betaReduced);
        return betaReduced.reduceByNeed(observer);
    }

    public Node replaceOcc(String name, Node arg) {
        return new Application(left.replaceOcc(name, arg), right.replaceOcc(name, arg));
    }
}
