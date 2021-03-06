package model;

public class LambdaExpr implements IrreductibleNode {
    private final String formalParam;
    private final Node body;

    public LambdaExpr(String formalParam, Node body) {
        this.formalParam = formalParam;
        this.body = body;
    }

    @Override
    public String toString(boolean topLevel) {
        return "(\\" + formalParam + " -> " + body.toString(topLevel) + ")";
    }

    @Override
    public Node replaceOcc(String name, Node arg) {
        // name - capturing
        if (!name.equals(formalParam)) return new LambdaExpr(formalParam, body.replaceOcc(name, arg));
        else return this;
    }

    public Node betaReduce(Node arg) {
        return body.replaceOcc(formalParam, arg);
    }
}
