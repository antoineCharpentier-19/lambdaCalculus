import model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.TestUtils.*;
import static util.TestUtils.mapFoldRApplied;

public class ReduceByNeedTest {

    @Test
    public void test() {
        Node n = new Application(
                new Lambda("x", new BiOp(new BiOp(node("x"), "+", node("x")), "+", node("x"))),
                new Application(new Lambda("x", new BiOp(node("x"), "*", node("x"))), node("3")));
        n.reduceByNeed(true);
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
        System.out.println(mapAppliedOnBoolList().reduceByNeed(true).toString(true));
    }

    @Test
    public void insertSortTest() {
        /*
        myInsert' :: Int -> [Int] -> [Int]
        myInsert' x y = if length y == 0
                        then [x]
                        else if x < head y
                            then x : y
                            else
                                (head y) : myInsert' x (tail y)
         */

        RecursiveNode INSERT = new RecursiveNode("INSERT");
        Lambda insert = multiLambda(new String[]{"x", "y"},
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
        System.out.println(node.reduceByValue(true).toString(true));

        /*
         mySort'' :: [Int] -> [Int]
         mySort'' x =   if length x == 0
                        then []
                        else if length x == 1
                            then x
                            else myInsert' (head x) (mySort'' (tail x))
         */
        RecursiveNode SORT = new RecursiveNode("SORT");
        Lambda sort = new Lambda(
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
        node = new Application(SORT, intList(2,321,4,66,3,21));
        System.out.println("--------------------------");
        System.out.println(node.reduceByValue(true).toString(true));
    }
}
