import model.*;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

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
        assertEquals("(\\y -> y)", node.reduceByName().toString());

        node = new Lambda("x", new Lambda("y", new Variable("x")));
        assertEquals("(\\x -> x)", node.reduceByName().toString());

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

        Function<Boolean, Node> ifthenelse1 = x ->
                new IfThenElse(new BoolConstant(x.booleanValue()),
                        new BoolConstant(false),
                        new BoolConstant(true)
                );

        assertEquals("false", ifthenelse1.apply(true).reduceByName().toString());
        assertEquals("true", ifthenelse1.apply(false).reduceByName().toString());
    }
}
