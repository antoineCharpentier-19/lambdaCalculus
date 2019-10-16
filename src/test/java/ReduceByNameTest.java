import model.*;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReduceByNameTest {

    @Test
    public void pureLambdaTest() {
        Node node = new Application(
                new Lambda("x", new Variable("x")),
                new Lambda("y", new Variable("y"))
        );

        // (\x -> x ) (\y -> y )
        System.out.println("--------------------------");
        assertEquals("(\\y -> y)", node.debugReduceByName().toString());

        // (\x -> (\y -> x) a) b
        node = new Application(
                new Lambda("x",
                        new Application(
                                new Lambda("y", new Variable("x")),
                                new Variable("a"))),
                new Variable("b"));
        System.out.println("--------------------------");
        assertEquals("b", node.debugReduceByName().toString());

        // (\x -> (\y -> x)) b a
        node = new Application(
                new Application(
                        new Lambda("x", new Lambda("y", new Variable("x"))),
                        new Variable("b")),
                new Variable("a")
        );
        System.out.println("--------------------------");
        node.debugReduceByName();
    }

    @Test
    public void boolTest() {
        Function<Boolean, Node> node1 = x ->
                new Application(
                        new Lambda(
                                "x",
                                new BoolNot(new Variable("x"))
                        ),
                        new BoolConstant(x)
                );
        Node applied1 = node1.apply(true);
        System.out.println("--------------------------");
        assertEquals("false", applied1.debugReduceByName().toString());

        Function<Boolean, Node> ifThenElse1 = x ->
                new IfThenElse(new BoolConstant(x.booleanValue()),
                        new BoolConstant(false),
                        new BoolConstant(true)
                );
        Node applied2True = ifThenElse1.apply(true);
        System.out.println("--------------------------");
        assertEquals("false", applied2True.debugReduceByName().toString());
        Node applied2False = ifThenElse1.apply(false);
        System.out.println("--------------------------");
        assertEquals("true", applied2False.debugReduceByName().toString());

        Function<Boolean, Node> ifThenElse2 = x ->
                new IfThenElse(
                        node1.apply(x),
                        new BoolConstant(false),
                        new BoolConstant(true)
                );
        Node applied3True = ifThenElse2.apply(true);
        System.out.println("--------------------------");
        assertEquals("true", applied3True.debugReduceByName().toString());
        Node applied3False = ifThenElse2.apply(false);
        System.out.println("--------------------------");
        assertEquals("false", applied3False.debugReduceByName().toString());

        // ((true && true) && (true && true))
        System.out.println("--------------------------");
        Node andAndAndAnd = new BoolBinary(
                                new BoolBinary(new BoolConstant(true), new BoolConstant(true),
                                        BoolBinary.Type.AND),
                                new BoolBinary(new BoolConstant(true), new BoolConstant(true),
                                        BoolBinary.Type.AND),
                                BoolBinary.Type.AND);
        andAndAndAnd.debugReduceByName();

        System.out.println("--------------------------");
        Node or = new BoolBinary(
                new BoolBinary(new BoolConstant(true), new BoolConstant(false),
                        BoolBinary.Type.OR),
                new BoolBinary(new BoolConstant(false), new BoolConstant(false),
                        BoolBinary.Type.OR),
                BoolBinary.Type.OR);
        or.debugReduceByName();
    }
}
