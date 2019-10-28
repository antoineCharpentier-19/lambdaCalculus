import model.*;
import model.BoolConstant;
import model.IntConstant;
import model.Lambda;
import model.Variable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
                                new UnaryOp(UnaryOp.Operator.NOT, new Variable("x"))
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
        Node andAndAndAnd = new BinaryOp(BinaryOp.Operator.AND,
                new BinaryOp(BinaryOp.Operator.AND, new BoolConstant(true), new BoolConstant(true)),
                new BinaryOp(BinaryOp.Operator.AND, new BoolConstant(true), new BoolConstant(true))
        );
        andAndAndAnd.debugReduceByName();

        System.out.println("--------------------------");
        Node or = new BinaryOp(BinaryOp.Operator.OR,
                new BinaryOp(BinaryOp.Operator.OR, new BoolConstant(true), new BoolConstant(false)),
                new BinaryOp(BinaryOp.Operator.OR, new BoolConstant(false), new BoolConstant(false)));
        or.debugReduceByName();
    }

    @Test
    public void IntTest() {
        // (\x -> + x (\y -> - 1 y)) 2 2
        Node node = new Application(
                new Application(
                        new Lambda(
                                "x",
                                new Lambda("y",
                                        new BinaryOp(
                                                BinaryOp.Operator.MINUS,
                                                new UnaryOp(UnaryOp.Operator.NEGATIVE, new IntConstant(1)),
                                                new Variable("y")
                                        ))
                        ),
                        new IntConstant(2)
                ),
                new IntConstant(2)
        );
        node.debugReduceByName();

        System.out.println("--------------------------");
        // 8/4 + 1x3
        node = new BinaryOp(
                BinaryOp.Operator.PLUS,
                new BinaryOp(BinaryOp.Operator.DIVIDE, new IntConstant(8), new IntConstant(4)),
                new BinaryOp(BinaryOp.Operator.TIMES, new IntConstant(1), new IntConstant(3))
        );
        node.debugReduceByName();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5})
        // six numbers
    void recursionTest(int number) {

        RecursiveLambda fac = new RecursiveLambda("FAC");
        Lambda lambda = new Lambda(
                "n",
                new IfThenElse(
                        new BinaryOp(BinaryOp.Operator.EQUAL, new Variable("n"), new IntConstant(0)),
                        new IntConstant(1),
                        new BinaryOp(BinaryOp.Operator.TIMES, new Variable("n"), new Application(fac,
                                new BinaryOp(BinaryOp.Operator.MINUS,
                                        new Variable("n"), new IntConstant(1))
                        )
                        )));
        fac.setLambda(lambda);
        Node nodeRecursion = new Application(
                lambda,
                new IntConstant(number)
        );
        nodeRecursion.debugReduceByName();
    }
}
