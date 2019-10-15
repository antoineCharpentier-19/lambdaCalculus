import model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReduceByNameTest {

    @Test
    public void pureLambdaTest() {
        Node node =
                new Application(
                        new Lambda("x", new Variable("x")),
                        new Lambda("y", new Variable("y"))
                );
        // (\x -> x ) (\y -> y )
        assertEquals("(\\y -> y )", node.reduceByName().toString());

        node =
            new Application(
                    new Lambda("x", new Variable("x")),
                    new Lambda("y", new Variable("y"))
            );
    }

    @Test
    public void boolTest() {
        Node node1 =
                new Application(
                        new Lambda(
                                "x",
                                new BoolNot(new Variable("x"))
                        ),
                        new BoolConstant(true)
                );

        assertEquals("false", node1.reduceByName().toString());
    }
}
