import model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.TestUtils.*;

public class ReduceByNameTest {

    @Test
    public void pureLambdaTest() {
        // (\x -> x ) (\y -> y )
        System.out.println("--------------------------");
        assertEquals("(\\y -> y)", lambdaTestNode1().reduceByName(true).toString(false));

        // (\x -> (\y -> x) a) b
        System.out.println("--------------------------");
        assertEquals("b", lambdaTestNode2().reduceByName(true).toString(false));

        // (\x -> (\y -> x)) b a
        System.out.println("--------------------------");
        assertEquals("b", lambdaTestNode3().reduceByName(true).print());
    }

    @Test
    public void boolTest() {
        System.out.println("--------------------------");
        assertEquals("false", boolTestNode1().apply(true).reduceByName(true).print());

        System.out.println("--------------------------");
        assertEquals("false", boolTestIfThenElse1().apply(true).reduceByName(true).print());

        System.out.println("--------------------------");
        assertEquals("true", boolTestIfThenElse1().apply(false).reduceByName(true).print());

        System.out.println("--------------------------");
        assertEquals("true", boolTestIfThenElse2().apply(true).reduceByName(true).print());

        System.out.println("--------------------------");
        assertEquals("false", boolTestIfThenElse2().apply(false).reduceByName(true).print());

        System.out.println("--------------------------");
        assertEquals("true", boolTestAnd().reduceByName(true).print());

        System.out.println("--------------------------");
        assertEquals("true", boolTestOr().reduceByName(true).print());
    }

    @Test
    public void IntTest() {
        // (\x -> (\y -> ((- 1)-y))) 2 3
        System.out.println("--------------------------");
        assertEquals("-4", intTest1().reduceByName(true).print());

        // 8/4 + 1x3
        System.out.println("--------------------------");
        assertEquals("5", intTest2().reduceByName(true).print());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5})
    void recursionTest(int number) {
        System.out.println("--------------------------");
        assertEquals(Integer.toString(factorial(number)), recursionTestFac(number).reduceByName(true).print());
    }

    @Test
    public void listsTestRight() {
        System.out.println("--------------------------");
        assertEquals(node(6), foldRApplied().reduceByName(true));

        System.out.println("--------------------------");
        assertEquals("((\\x -> x+x)(head ((3 : 2 : 1 : []))) : FOLD(tail ((3 : 2 : 1 : [])))([])((\\x -> (\\y -> ((\\x -> x+x)(x) : y)))))", mapFoldRApplied().reduceByName(true).print());
        assertEquals("(6 : 4 : 2 : [])", mapFoldRApplied().reduceByName(true).toString(true));
    }

    @Test
    public void listsTestLeft() {

        System.out.println("--------------------------");
        assertEquals("6", foldLApplied().reduceByName(true).print());

        System.out.println("--------------------------");
        assertEquals("(2 : 3 : 4 : [])", mapApplied1().reduceByName(true).toString(true));
        System.out.println("--------------------------");
        assertEquals("((\\x -> x+1)(head ((1 : 2 : 3 : []))) : MAP(tail ((1 : 2 : 3 : [])))((\\x -> x+1)))", mapApplied1().reduceByName(true).print());

        System.out.println("--------------------------");
        RecursiveNode INF_ONE = new RecursiveNode("INF");
        Node infOne = new Cons(node(1), INF_ONE);
        INF_ONE.setNode(infOne);

        assertEquals("INF", new UnOp("tail", infOne).reduceByName(true).print());
        System.out.println("--------------------------");
        assertEquals("1", new UnOp("head", new UnOp("tail", new UnOp("tail", new UnOp("tail", infOne)))).reduceByName(true).print());
        System.out.println("--------------------------");

        new UnOp("head",
                new UnOp("tail",
                        new UnOp("tail",
                                multiApply(
                                        mapLambda(),
                                        INF_ONE,
                                        new Lambda(
                                                "x",
                                                new BiOp(node("x"), "+", node(1))
                                        )
                                )))).reduceByName(true);

        RecursiveNode INF_INTS = new RecursiveNode("INF_INTS");
        Node intInts = new Cons(node(1),
                multiApply(
                        mapLambda(), INF_INTS, new Lambda(
                                "x",
                                new BiOp(node("x"), "+", node(1))
                        )));
        INF_INTS.setNode(intInts);

        new UnOp("head", new UnOp("tail", new UnOp("tail", INF_INTS))).reduceByName(true).print();
    }

    @Test
    public void booleanListTest() {
        System.out.println("--------------------------");
        assertEquals("((\\x -> not (x))(head ((true : false : false : []))) : MAP(tail ((true : false : false : [])))((\\x -> not (x))))", mapAppliedOnBoolList().reduceByName(true).print());
        System.out.println("--------------------------");
        assertEquals("(false : true : true : [])", mapAppliedOnBoolList().reduceByName(true).toString(true));
    }

    @Test
    public void printerTest() {
        Node list = new Cons(new BiOp(node(1), "+", node(2)), new BiOp(node(3), "+", node(4)));
        System.out.println(list.reduceByName(true).print());
    }
}
