import model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.TestUtils.*;

public class ReduceByNeedTest {

    @Test
    public void test() {
//        tripleTimes().reduceByNeed(true);
//
        //multiApply(pow(), node("2"), node("3")).reduceByName(true);
        new Cons(new UnOp("head", new Cons(new BiOp(node(1), "+", node(2)), new Nil())), new Nil()).reduceByValue(true);
    }

    @Test
    public void listsTestRight() {
        System.out.println("--------------------------");
        assertEquals(node(6), foldRApplied().reduceByNeed(true));

        System.out.println("--------------------------");
        assertEquals("((\\x -> x+x)(head ((3 : 2 : 1 : []))) : FOLD(tail ((3 : 2 : 1 : [])))([])((\\x -> (\\y -> ((\\x -> x+x)(x) : y)))))", mapFoldRApplied().reduceByNeed(true).print());
        assertEquals("(6 : 4 : 2 : [])", mapFoldRApplied().reduceByNeed(true).toString(true));
    }

    @Test
    public void booleanListTest() {
        System.out.println("--------------------------");
        mapAppliedOnBoolList().reduceByNeed(true).print();
        System.out.println("--------------------------");
        assertEquals("(false : true : true : [])", mapAppliedOnBoolList().reduceByNeed(true).toString(true));
        System.out.println("--------------------------");
        System.out.println("top level printer : " + mapAppliedOnBoolList().reduceByNeed(true).toString(true));
    }

    @Test
    public void insertSortTest() {
        RecursiveNode INSERT = new RecursiveNode("INSERT");
        LambdaExpr insert = multiLambda(new String[]{"x", "y"},
                new IfThenElse(
                        new UnOp("nil", node("y")),
                        new Cons(node("x"), new Nil()),
                        new IfThenElse(
                                new BiOp(node("x"), "<", new UnOp("head", node("y"))),
                                new Cons(node("x"), node("y")),
                                new Cons(new UnOp("head", node("y")), multiApply(INSERT, node("x"), new UnOp("tail", node("y"))))
                        )
                )
        );
        INSERT.setNode(insert);
        Node node = multiApply(INSERT, node(2), intList(1, 2, 3));
        System.out.println("--------------------------");
        System.out.println("top level printer : " + node.reduceByNeed(true).toString(true));
        RecursiveNode SORT = new RecursiveNode("SORT");
        LambdaExpr sort = new LambdaExpr(
                "x",
                new IfThenElse(
                        new UnOp("nil", node("x")),
                        new Nil(),
                        new IfThenElse(
                                new UnOp("nil", new UnOp("tail", node("x"))),
                                node("x"),
                                multiApply(INSERT, new UnOp("head", node("x")), new Application(SORT, new UnOp("tail", node("x"))))
                        )
                )
        );
        SORT.setNode(sort);
        node = new Application(SORT, intList(3,2,1));
        System.out.println("--------------------------");
        System.out.println("top level printer : " + node.reduceByValue(true).toString(true));
    }
}
