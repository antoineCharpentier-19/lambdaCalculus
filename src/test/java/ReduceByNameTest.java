import model.Application;
import model.Lambda;
import model.Node;
import model.Variable;
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
}
