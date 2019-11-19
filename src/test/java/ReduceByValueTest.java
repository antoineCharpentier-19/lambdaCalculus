import model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.Utils.node;

public class ReduceByValueTest {

    @Test
    public void pureLambdaTest() {
        Node node = new Application(
                new Lambda("x", new Variable("x")),
                new Lambda("y", new Variable("y"))
        );

        // (\x -> x ) (\y -> y )
        System.out.println("--------------------------");
        assertEquals("(\\y -> y)", node.reduceByValue().toString(false));


        // (\x -> (\y -> x) a) b
        node = new Application(
                new Lambda(
                        "x",
                        new Application(
                                new Lambda("y", node("x")),
                                node("a"))),
                node("b"));
        System.out.println("--------------------------");
        assertEquals("b", node.reduceByValue().toString(false));

        // (\x -> (\y -> x)) b a
        node = new Application(
                new Application(
                        new Lambda("x", new Lambda("y", new Variable("x"))),
                        new Variable("b")),
                new Variable("a")
        );
        System.out.println("--------------------------");
        node.reduceByValue();
    }
}

