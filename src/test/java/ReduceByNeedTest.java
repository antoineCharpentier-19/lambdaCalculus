import model.Application;
import model.BiOp;
import model.Lambda;
import model.Node;
import org.junit.jupiter.api.Test;

import static util.TestUtils.node;

public class ReduceByNeedTest {

    @Test
    public void test() {
        Node n = new Application(
                new Lambda("x", new BiOp(new BiOp(node("x"), "+", node("x")), "+", node("x"))),
                new BiOp(node("1"), "+", node("1")));
                //new Application(new Lambda("x",new BiOp(node("x"), "*", node("x"))), node("3")));
        System.out.println(n.reduceByNeed(true).toString(true));
    }
}
