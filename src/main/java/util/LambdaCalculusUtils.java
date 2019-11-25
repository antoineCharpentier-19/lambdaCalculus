package util;

import model.IndirectionNode;
import model.IrreductibleNode;
import model.Node;

public class LambdaCalculusUtils {
    public static boolean instanceOf(Node node, Class<? extends Node> cls){
        return  cls.isInstance(node)
                || node instanceof IndirectionNode && cls.isInstance(((IndirectionNode) node).getWrapped());
    }
}
