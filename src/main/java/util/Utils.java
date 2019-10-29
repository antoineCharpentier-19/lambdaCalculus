package util;

import model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.HashMap;

public class Utils {
    public static Node node(String string) {
        if(StringUtils.isNumeric(string)) {
            return new IntConstant(Integer.parseInt(string));
        } else if (string.equalsIgnoreCase("false") || string.equalsIgnoreCase("true")) {
            return new BoolConstant(Boolean.parseBoolean(string));
        } else return new Variable(string);
    }

    public static Application multiApply(Node left, Node...args) {
        Node tmpLeft = left;
        Node tmpRight = args[0];
        for (int i = 1; i < args.length; i++) {
            tmpLeft = new Application(tmpLeft,tmpRight);
            tmpRight = args[i];
        }
        return new Application(tmpLeft,tmpRight);
    }

    public static Lambda multiLambda(String[] formalParams, Node body) {
            String tmpParam = formalParams[formalParams.length-1];
            Node tmpBody = body;

            for (int i = formalParams.length-2; i >= 0 ; i--) {
                tmpBody = new Lambda(tmpParam,tmpBody);
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

    public static Node intList(int ... ints){
        Node output = new IntNil();
        for(Integer i : ints) {
            output = new IntCons(new IntConstant(i), output);
        }
        return output;
    }
}
