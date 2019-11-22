import model.Application;
import model.BiOp;
import model.Lambda;
import model.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.TestUtils.mapAppliedOnBoolList;
import static util.TestUtils.node;

public class ReduceByNeedTest {

    @Test
    public void test() {
        Node n = new Application(
                new Lambda("x", new BiOp(new BiOp(node("x"), "+", node("x")), "+", node("x"))),
                new Application(new Lambda("x",new BiOp(node("x"), "*", node("x"))), node("3")));
        n.reduceByNeed(true);
    }

    @Test
    public void booleanListTest() {
        System.out.println("--------------------------");
        mapAppliedOnBoolList().reduceByNeed(true).print();
        System.out.println("--------------------------");
        assertEquals("(false : true : true : [])", mapAppliedOnBoolList().reduceByNeed(true).toString(true));
    }
}
