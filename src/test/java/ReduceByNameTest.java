import model.*;
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
        assertEquals("(\\y -> y)", node.reduceByName().print());

        // (\x -> (\y -> x) a) b
        node = new Application(
                new Lambda(
                        "x",
                        new Application(
                                new Lambda("y", node("x")),
                                node("a"))),
                node("b"));
        System.out.println("--------------------------");
        assertEquals("b", node.reduceByName().print());

        // (\x -> (\y -> x)) b a
        node = new Application(
                new Application(
                        new Lambda("x", new Lambda("y", new Variable("x"))),
                        new Variable("b")),
                new Variable("a")
        );
        System.out.println("--------------------------");
        node.reduceByName();
    }

    @Test
    public void boolTest() {
        Function<Boolean, Node> node1 = x -> new Application(new Lambda("x", new UnOp(UnOp.Op.NOT, new Variable("x"))), node(x));
        Node applied1 = node1.apply(true);
        System.out.println("--------------------------");
        assertEquals("false", applied1.reduceByName().print());
        Function<Boolean, Node> ifThenElse1 = x -> new IfThenElse(
                new BoolConstant(x.booleanValue()),
                node(false),
                node(true));
        Node applied2True = ifThenElse1.apply(true);
        System.out.println("--------------------------");
        assertEquals("false", applied2True.reduceByName().print());
        Node applied2False = ifThenElse1.apply(false);
        System.out.println("--------------------------");
        assertEquals("true", applied2False.reduceByName().print());
        Function<Boolean, Node> ifThenElse2 = x -> new IfThenElse(
                node1.apply(x),
                node(false),
                node(true)
        );
        Node applied3True = ifThenElse2.apply(true);
        System.out.println("--------------------------");
        assertEquals("true", applied3True.reduceByName().print());
        Node applied3False = ifThenElse2.apply(false);
        System.out.println("--------------------------");
        assertEquals("false", applied3False.reduceByName().print());

        // ((true && true) && (true && true))
        System.out.println("--------------------------");
        Node andAndAndAnd = new BiOp(
                new BiOp(node(true), "&&", node(true)),
                "&&",
                new BiOp(node(true), "&&", node(true))
        );
        andAndAndAnd.reduceByName();

        System.out.println("--------------------------");
        Node or = new BiOp(
                new BiOp(node(true), "||", node(false)),
                "||",
                new BiOp(node(false), "||", node(false)));
        or.reduceByName();
    }

    @Test
    public void IntTest() {
        // (\x -> (\y -> ((- 1)-y))) 2 3
        Node node = multiApply(multiLambda(new String[]{"x", "y"}, new BiOp(new UnOp(UnOp.Op.NEGATIVE, new IntConstant(1)), "-", new Variable("y"))), node(2), node(3));
        node.reduceByName();

        System.out.println("--------------------------");
        // 8/4 + 1x3
        node = new BiOp(new BiOp(node(8), "/", node(4)), "+", new BiOp(node(1), "*", node(3))
        );
        node.reduceByName();
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
        nodeRecursion.reduceByName();
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

        assertEquals(node(6), nodeRecursion.reduceByName());

        Lambda map = multiLambda(new String[]{"l", "f"},
                multiApply(FOLDR,
                        node("l"),
                        new Nil(),
                        multiLambda(new String[]{"x", "y"},
                                new Cons(
                                        new Application(node("f"), node("x")), node("y")))));

        Node test = multiApply(map, intList(3, 2, 1), new Lambda("x", new BiOp(node("x"), "+", node("x"))));

        System.out.println(test.reduceByName());

        nodeRecursion.reduceByName();
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

//        nodeRecursion.reduceByName();

        System.out.println("--------------------------");

        RecursiveNode MAP = new RecursiveNode("MAP");
        Lambda map = multiLambda(
                new String[]{"l", "f"},
                new IfThenElse(
                        new UnOp("nil", node("l")),
                        new Nil(),
                        new Cons(
                                new Application(
                                        node("f"),
                                        new UnOp("head", node("l"))
                                ),
                                multiApply(MAP, new UnOp("tail", node("l")), node("f"))
                        )
                )
        );
        MAP.setNode(map);
        Node recursion = multiApply(MAP, intList(1, 2, 3), new Lambda("x", new BiOp(node("x"), "+", node(1))));
        System.out.println(recursion.reduceByName().toString(true));

        RecursiveNode INF_ONE = new RecursiveNode("INF");
        Node infOne = new Cons(node(1), INF_ONE);
        INF_ONE.setNode(infOne);

        System.out.println(new UnOp("tail", infOne).reduceByName());
        System.out.println(new UnOp("head", new UnOp("tail", new UnOp("tail", new UnOp("tail", infOne)))).reduceByName());

        new UnOp("head",
                new UnOp("tail",
                        new UnOp("tail",
                                multiApply(
                                        MAP,
                                        INF_ONE,
                                        new Lambda(
                                                "x",
                                                new BiOp(node("x"), "+", node(1))
                                        )
                                )))).reduceByName();

        RecursiveNode INF_INTS = new RecursiveNode("INF_INTS");
        Node intInts = new Cons(node(1),
                multiApply(
                        MAP, INF_INTS, new Lambda(
                                "x",
                                new BiOp(node("x"), "+", node(1))
                        )));
        INF_INTS.setNode(intInts);

        new UnOp("head", new UnOp("tail", new UnOp("tail", INF_INTS))).reduceByName();
    }

    @Test
    public void booleanListTest() {
        RecursiveNode MAP = new RecursiveNode("MAP");
        Lambda map = multiLambda(
                new String[]{"l", "f"},
                new IfThenElse(
                        new UnOp("nil", node("l")),
                        new Nil(),
                        new Cons(
                                new Application(
                                        node("f"),
                                        new UnOp("head", node("l"))
                                ),
                                multiApply(MAP, new UnOp("tail", node("l")), node("f"))
                        )
                )
        );
        MAP.setNode(map);
        Node recursion = multiApply(MAP, boolList(true, false, false), new Lambda("x", new UnOp("not", node("x"))));
        System.out.println(recursion.reduceByName().toString(true));
    }

    @Test
    public void printerTest() {
        Node list = new Cons(new BiOp(node(1), "+", node(2)),new BiOp(node(3), "+", node(4)));
        System.out.println(list.reduceByName(false).print());
    }
}
