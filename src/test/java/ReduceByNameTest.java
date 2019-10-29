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
import static util.Utils.*;

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
                new Lambda(
                        "x",
                        new Application(
                                new Lambda("y", node("x")),
                                node("a"))),
                node("b"));
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
        Function<Boolean, Node> node1 = x -> new Application(new Lambda("x", new UnOp(UnOp.Op.NOT, new Variable("x"))), node(x));
        Node applied1 = node1.apply(true);
        System.out.println("--------------------------");
        assertEquals("false", applied1.debugReduceByName().toString());
        Function<Boolean, Node> ifThenElse1 = x -> new IfThenElse(
                new BoolConstant(x.booleanValue()),
                node(false),
                node(true));
        Node applied2True = ifThenElse1.apply(true);
        System.out.println("--------------------------");
        assertEquals("false", applied2True.debugReduceByName().toString());
        Node applied2False = ifThenElse1.apply(false);
        System.out.println("--------------------------");
        assertEquals("true", applied2False.debugReduceByName().toString());
        Function<Boolean, Node> ifThenElse2 = x -> new IfThenElse(
                node1.apply(x),
                node(false),
                node(true)
        );
        Node applied3True = ifThenElse2.apply(true);
        System.out.println("--------------------------");
        assertEquals("true", applied3True.debugReduceByName().toString());
        Node applied3False = ifThenElse2.apply(false);
        System.out.println("--------------------------");
        assertEquals("false", applied3False.debugReduceByName().toString());

        // ((true && true) && (true && true))
        System.out.println("--------------------------");
        Node andAndAndAnd = new BiOp(
                new BiOp(node(true), "&&", node(true)),
                "&&",
                new BiOp(node(true), "&&", node(true))
        );
        andAndAndAnd.debugReduceByName();

        System.out.println("--------------------------");
        Node or = new BiOp(
                new BiOp(node(true), "||", node(false)),
                "||",
                new BiOp(node(false), "||", node(false)));
        or.debugReduceByName();
    }

    @Test
    public void IntTest() {
        // (\x -> (\y -> ((- 1)-y))) 2 3
        Node node = multiApply(multiLambda(new String[]{"x", "y"}, new BiOp(new UnOp(UnOp.Op.NEGATIVE, new IntConstant(1)), "-", new Variable("y"))), node(2), node(3));
        node.debugReduceByName();

        System.out.println("--------------------------");
        // 8/4 + 1x3
        node = new BiOp(new BiOp(node(8), "/", node(4)), "+", new BiOp(node(1), "*", node(3))
        );
        node.debugReduceByName();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5})
        // six numbers
    void recursionTest(int number) {

        RecursiveNode FAC = new RecursiveNode("FAC");
        Lambda fac = new Lambda("n",
                new IfThenElse(
                        new BiOp(node("n"), "==", node("0")),
                        node("1"),
                        new BiOp(node("n"), "*", new Application(FAC, new BiOp(node("n"), "-", node("1"))))));
        FAC.setNode(fac);

        Node nodeRecursion = new Application(
                fac,
                node(number)
        );
        nodeRecursion.debugReduceByName();
    }

    @Test
    public void listsTestRight() {
        RecursiveNode FOLDR = new RecursiveNode("FOLD");
        Lambda foldr = multiLambda(new String[]{"l", "p", "f"},
                new IfThenElse(
                        new UnOp("nil", node("l")),
                        node("p"),
                        multiApply(
                                node("f"),
                                new UnOp("head", node("l")),
                                multiApply(FOLDR,
                                        new UnOp("tail", node("l")),
                                        node("p"),
                                        node("f"))
                        )));

        FOLDR.setNode(foldr);

        Node nodeRecursion = multiApply(foldr, intList(1, 2, 3), node(0), multiLambda(new String[]{"x", "y"}, new BiOp(node("x"), "+", node("y"))));

        assertEquals(node(6), nodeRecursion.debugReduceByName());

        Lambda map = multiLambda(new String[]{"l", "f"},
                multiApply(FOLDR,
                        node("l"),
                        new IntNil(),
                        multiLambda(new String[]{"x", "y"},
                                new BiOp(
                                        new Application(node("f"), node("x")), ":", node("y")))));

        Node test = multiApply(map, intList(3, 2, 1), new Lambda("x", new BiOp(node("x"), "+", node("x"))));

        System.out.println(test.debugReduceByName());

        nodeRecursion.debugReduceByName();
    }

    @Test
    public void listsTestLeft() {
        RecursiveNode FOLD = new RecursiveNode("FOLD");
        Lambda fold = multiLambda(new String[]{"l", "p", "f"},
                new IfThenElse(
                        new UnOp("nil", node("l")),
                        node("p"),
                        multiApply(
                                node("f"),
                                node("p"),
                                multiApply(FOLD, new UnOp("tail", node("l")), new UnOp("head", node("l")), node("f")))));

        FOLD.setNode(fold);

        Node nodeRecursion = multiApply(fold, intList(1, 2, 3), node(0), multiLambda(new String[]{"x", "y"}, new BiOp(node("x"), "+", node("y"))));

        nodeRecursion.debugReduceByName();

        System.out.println("--------------------------");

        RecursiveNode MAP = new RecursiveNode("MAP");
        Lambda map = multiLambda(
                new String[]{"l", "f"},
                new IfThenElse(
                        new UnOp("nil", node("l")),
                        new IntNil(),
                        new IntCons(
                                new Application(
                                        node("f"),
                                        new UnOp("head", node("l"))
                                ),
                                multiApply(MAP, new UnOp("tail", node("l")), node("f"))
                        )
                )
        );
        MAP.setNode(map);
        Node recursion = multiApply(map, intList(1, 2, 3), new Lambda("x", new BiOp(node("x"), "+", node(1))));
        recursion.debugReduceByName();
    }

    @Test
    public void appTest() {
        // (\x -> (\y -> (\z -> (x+(y+z))))) 1 2 3
        System.out.println(multiApply(multiLambda(new String[]{"x", "y", "z"}, new BiOp(node("x"), "+", new BiOp(node("y"), "+", node("z")))), node("1"), node("2"), node("3")));
    }

}
