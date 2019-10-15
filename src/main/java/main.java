import model.Application;
import model.Lambda;
import model.Node;
import model.Variable;

public class main {
    public static void main(String[] args) {
        Node node =
                new Application(
                        new Lambda("x", new Variable("x")),
                        new Lambda("y", new Variable("y"))
                );

        System.out.println("node = " + node);

        System.out.println("the same node but reduced = " + node.reduceByName());
    }
}
