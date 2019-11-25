import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.TestUtils.*;

public class ReduceByValueTest {

    @Test
    public void pureLambdaTest() {
        // (\x -> x ) (\y -> y )
        System.out.println("--------------------------");
        assertEquals("(\\y -> y)", lambdaTestNode1().reduceByValue(true).toString(false));

        // (\x -> (\y -> x) a) b
        System.out.println("--------------------------");
        assertEquals("b", lambdaTestNode2().reduceByValue(true).toString(false));

        // (\x -> (\y -> x)) b a
        System.out.println("--------------------------");
        assertEquals("b", lambdaTestNode3().reduceByValue(true).print());
    }

    @Test
    public void boolTest() {
        System.out.println("--------------------------");
        assertEquals("false", boolTestNode1().apply(true).reduceByValue(true).print());

        System.out.println("--------------------------");
        assertEquals("false", boolTestIfThenElse1().apply(true).reduceByValue(true).print());

        System.out.println("--------------------------");
        assertEquals("true", boolTestIfThenElse1().apply(false).reduceByValue(true).print());

        System.out.println("--------------------------");
        assertEquals("true", boolTestIfThenElse2().apply(true).reduceByValue(true).print());

        System.out.println("--------------------------");
        assertEquals("false", boolTestIfThenElse2().apply(false).reduceByValue(true).print());

        System.out.println("--------------------------");
        assertEquals("true", boolTestAnd().reduceByValue(true).print());

        System.out.println("--------------------------");
        assertEquals("true", boolTestOr().reduceByValue(true).print());
    }

    @Test
    public void IntTest() {
        // (\x -> (\y -> ((- 1)-y))) 2 3
        System.out.println("--------------------------");
        assertEquals("-4", intTest1().reduceByValue(true).print());

        // 8/4 + 1x3
        System.out.println("--------------------------");
        assertEquals("5", intTest2().reduceByValue(true).print());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5})
    void recursionTest(int number) {
        System.out.println("--------------------------");
        assertEquals(Integer.toString(factorial(number)), recursionTestFac(number).reduceByValue(true).print());
    }

    @Test
    public void listsTestRight() {
        System.out.println("--------------------------");
        assertEquals(node(6), foldRApplied().reduceByValue(true));

        System.out.println("--------------------------");
        assertEquals("((\\x -> x+x)(3) : (\\x -> x+x)(2) : (\\x -> x+x)(1) : [])", mapFoldRApplied().reduceByValue(true).print());
        System.out.println("--------------------------");
        // if we want to see "(6 : 4 : 2 : [])", we have to print this expression : mapFoldRApplied().reduceByValue(true).toString(true)
        assertEquals("(6 : 4 : 2 : [])", mapFoldRApplied().reduceByValue(true).toString(true));
    }

    @Test
    public void listsTestLeft() {
        System.out.println("--------------------------");
        assertEquals("6", foldLApplied().reduceByValue(true).print());

        System.out.println("--------------------------");
        assertEquals("(2 : 3 : 4 : [])", mapApplied1().reduceByValue(true).toString(true));
        System.out.println("--------------------------");
        assertEquals("((\\x -> x+1)(head ((1 : 2 : 3 : []))) : MAP(tail ((1 : 2 : 3 : [])))((\\x -> x+1)))", mapApplied1().reduceByValue(true).print());
    }

    @Test
    public void booleanListTest() {
        System.out.println("--------------------------");
        assertEquals("((\\x -> not (x))(head ((true : false : false : []))) : MAP(tail ((true : false : false : [])))((\\x -> not (x))))", mapAppliedOnBoolList().reduceByValue(true).print());
        System.out.println("--------------------------");
        assertEquals("(false : true : true : [])", mapAppliedOnBoolList().reduceByValue(true).toString(true));
    }
}

