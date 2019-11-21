package util;

import model.*;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

public class TestUtils {
    private static BoolConstant f = new BoolConstant(false);
    private static BoolConstant t = new BoolConstant(true);

    public static Node node(String string) {
        if (StringUtils.isNumeric(string)) {
            return new IntConstant(Integer.parseInt(string));
        } else if (string.equalsIgnoreCase("false") || string.equalsIgnoreCase("true")) {
            return Boolean.parseBoolean(string) ? t : f;
        } else return new Variable(string);
    }

    public static Application multiApply(Node left, Node... args) {
        Node tmpLeft = left;
        Node tmpRight = args[0];
        for (int i = 1; i < args.length; i++) {
            tmpLeft = new Application(tmpLeft, tmpRight);
            tmpRight = args[i];
        }
        return new Application(tmpLeft, tmpRight);
    }

    public static Lambda multiLambda(String[] formalParams, Node body) {
        String tmpParam = formalParams[formalParams.length - 1];
        Node tmpBody = body;

        for (int i = formalParams.length - 2; i >= 0; i--) {
            tmpBody = new Lambda(tmpParam, tmpBody);
            tmpParam = formalParams[i];
        }
        return new Lambda(tmpParam, tmpBody);
    }

    public static Node node(int val) {
        return new IntConstant(val);
    }

    public static Node node(boolean val) {
        return new BoolConstant(val);
    }

    public static Node intList(int... ints) {
        Node output = new Nil();
        for (int i = ints.length - 1; i >= 0; i--) {
            output = new Cons(new IntConstant(ints[i]), output);
        }
        return output;
    }

    public static Node boolList(boolean... bools) {
        Node output = new Nil();
        for (int i = bools.length - 1; i >= 0; i--) {
            output = new Cons(new BoolConstant(bools[i]), output);
        }
        return output;
    }

    public static Application lambdaTestNode3() {
        return new Application(
                new Application(
                        new Lambda("x", new Lambda("y", new Variable("x"))),
                        new Variable("b")),
                new Variable("a")
        );
    }

    public static Application lambdaTestNode2() {
        return new Application(
                new Lambda(
                        "x",
                        new Application(
                                new Lambda("y", node("x")),
                                node("a"))),
                node("b"));
    }

    public static Node lambdaTestNode1() {
        return new Application(
                new Lambda("x", new Variable("x")),
                new Lambda("y", new Variable("y"))
        );
    }

    public static Node boolTestOr() {
        return new BiOp(
                new BiOp(node(true), "||", node(false)),
                "||",
                new BiOp(node(false), "||", node(false)));
    }

    public static Node boolTestAnd() {
        return new BiOp(
                new BiOp(node(true), "&&", node(true)),
                "&&",
                new BiOp(node(true), "&&", node(true))
        );
    }

    public static Function<Boolean, Node> boolTestIfThenElse2() {
        return x -> new IfThenElse(
                boolTestNode1().apply(x),
                node(false),
                node(true)
        );
    }

    public static Function<Boolean, Node> boolTestIfThenElse1() {
        return x -> new IfThenElse(
                new BoolConstant(x.booleanValue()),
                node(false),
                node(true));
    }

    public static Function<Boolean, Node> boolTestNode1() {
        return x -> new Application(new Lambda("x", new UnOp(UnOp.Op.NOT, new Variable("x"))), node(x));
    }

    public static BiOp intTest2() {
        return new BiOp(new BiOp(node(8), "/", node(4)), "+", new BiOp(node(1), "*", node(3)));
    }

    public static Application intTest1() {
        return multiApply(multiLambda(new String[]{"x", "y"}, new BiOp(new UnOp(UnOp.Op.NEGATIVE, new IntConstant(1)), "-", new Variable("y"))), node(2), node(3));
    }

    public static int factorial(int n) {
        if (n == 0) return 1;
        else return (n * factorial(n - 1));
    }

    public static Node recursionTestFac(int number) {
        RecursiveNode FAC = new RecursiveNode("FAC");
        Lambda fac = new Lambda("n",
                new IfThenElse(
                        new BiOp(node("n"), "==", node("0")),
                        node("1"),
                        new BiOp(node("n"), "*", new Application(FAC, new BiOp(node("n"), "-", node("1"))))));
        FAC.setNode(fac);
        return new Application(fac, node(number));
    }

    public static Node foldRApplied() {
        return multiApply(listsTestFoldR(), intList(1, 2, 3), node(0), multiLambda(new String[]{"x", "y"}, new BiOp(node("x"), "+", node("y"))));
    }

    public static Node mapFoldRApplied() {
        return multiApply(listsTestMapWithFoldR(), intList(3, 2, 1), new Lambda("x", new BiOp(node("x"), "+", node("x"))));
    }

    public static Lambda listsTestMapWithFoldR() {
        return multiLambda(new String[]{"l", "f"},
                multiApply(listsTestFoldR(),
                        node("l"),
                        new Nil(),
                        multiLambda(new String[]{"x", "y"},
                                new Cons(
                                        new Application(node("f"), node("x")), node("y")))));
    }

    public static RecursiveNode listsTestFoldR() {
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
        return FOLDR;
    }

    public static Node mapApplied1() {
        return multiApply(mapLambda(), intList(1, 2, 3), new Lambda("x", new BiOp(node("x"), "+", node(1))));
    }

    public static RecursiveNode mapLambda() {
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
        return MAP;
    }

    public static Node foldLApplied() {
        return multiApply(foldL(), intList(1, 2, 3), node(0), multiLambda(new String[]{"x", "y"}, new BiOp(node("x"), "+", node("y"))));
    }

    public static RecursiveNode foldL() {
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
        return FOLD;
    }

    public static Node mapAppliedOnBoolList() {
        return multiApply(mapLambda(), boolList(true, false, false), new Lambda("x", new UnOp("not", node("x"))));
    }
}
