import model.*;

import static util.TestUtils.*;

public class Main {
    public static void main(String[] args) {

        System.out.println("#################################################");
        System.out.println("Test du nombre de réductions en fonction du type");
        System.out.println("#################################################");
        System.out.println("-------Réduction par nom-------");
        System.out.println();
        testComparaison().reduceByName(true);
        System.out.println();
        System.out.println("-------Réduction par valeur-------");
        System.out.println();
        testComparaison().reduceByValue(true);
        System.out.println();
        System.out.println("-------Réduction par nécessité-------");
        System.out.println();
        testComparaison().reduceByNeed(true);
        System.out.println();
        System.out.println();
        System.out.println();

        System.out.println("#################################################");
        System.out.println("Comportement des listes selon le type de réduction");
        System.out.println("#################################################");

        Node list = new Cons(new BiOp(node(1), "+", node(2)), new Cons(new BiOp(node(3), "+", node(4)), new Nil()));
        System.out.println();
        System.out.println("-------Réduction par nom-------");
        System.out.println();
        list.reduceByName(true);
        System.out.println();
        System.out.println("-------Réduction par valeur-------");
        System.out.println();
        list.reduceByValue(true);

        System.out.println("#############################################################");
        System.out.println("-----------------Exemples de Call by name--------------------");
        System.out.println("#############################################################");
        System.out.println();

        System.out.println("### Pur lambda");
        System.out.println();
        lambdaTestNode1().reduceByName(true);
        System.out.println();

        System.out.println("### Booléens");
        System.out.println();
        boolTestOr().reduceByName(true);
        System.out.println("-------");
        boolTestIfThenElse2().apply(false).reduceByName(true);
        System.out.println();

        System.out.println("### Entiers");
        System.out.println();
        intTest1().reduceByName(true);
        System.out.println("-------");
        intTest2().reduceByName(true);
        System.out.println("-------");
        tripleTimes().reduceByName(true);
        System.out.println();

        System.out.println("### Récursion");
        System.out.println();
        multiApply(pow(), node("2"), node("3")).reduceByName(true);
        System.out.println();

        System.out.println("### Listes");
        System.out.println();
        boolTestIfThenElse2().apply(false).reduceByName(true);
        System.out.println();
        System.out.println();
        new Application(sortLambda(), intList(3,2,1)).reduceByName(true);
        System.out.println();

        System.out.println("#############################################################");
        System.out.println("-----------------Exemples de Call by value--------------------");
        System.out.println("#############################################################");
        System.out.println();

        System.out.println("### Pur lambda");
        System.out.println();
        lambdaTestNode1().reduceByValue(true);
        System.out.println();

        System.out.println("### Booléens");
        System.out.println();
        boolTestOr().reduceByValue(true);
        System.out.println("-------");
        boolTestIfThenElse2().apply(false).reduceByValue(true);
        System.out.println();

        System.out.println("### Entiers");
        System.out.println();
        intTest1().reduceByValue(true);
        System.out.println("-------");
        intTest2().reduceByValue(true);
        System.out.println("-------");
        tripleTimes().reduceByValue(true);
        System.out.println();

        System.out.println("### Récursion");
        System.out.println();
        multiApply(pow(), node("2"), node("3")).reduceByValue(true);
        System.out.println();

        System.out.println("### Listes");
        System.out.println();
        boolTestIfThenElse2().apply(false).reduceByValue(true);
        System.out.println();
        System.out.println();
        new Application(sortLambda(), intList(3,2,1)).reduceByValue(true);
        System.out.println();


        System.out.println("#############################################################");
        System.out.println("-----------------Exemples de Call by need--------------------");
        System.out.println("#############################################################");
        System.out.println();

        System.out.println("### Pur lambda");
        System.out.println();
        lambdaTestNode1().reduceByNeed(true);
        System.out.println();

        System.out.println("### Booléens");
        System.out.println();
        boolTestOr().reduceByNeed(true);
        System.out.println("-------");
        boolTestIfThenElse2().apply(false).reduceByNeed(true);
        System.out.println();

        System.out.println("### Entiers");
        System.out.println();
        intTest1().reduceByNeed(true);
        System.out.println("-------");
        intTest2().reduceByNeed(true);
        System.out.println("-------");
        tripleTimes().reduceByNeed(true);
        System.out.println();

        System.out.println("### Récursion");
        System.out.println();
        multiApply(pow(), node("2"), node("3")).reduceByNeed(true);
        System.out.println();

        System.out.println("### Listes");
        System.out.println();
        boolTestIfThenElse2().apply(false).reduceByNeed(true);
        System.out.println();
        System.out.println();
        new Application(sortLambda(), intList(3,2,1)).reduceByNeed(true);
        System.out.println();



    }
}
