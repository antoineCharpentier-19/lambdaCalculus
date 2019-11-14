package model;

public class Nil implements LCList {
    @Override
    public String toString(boolean topLevel){
        return "[]";
    }
}
