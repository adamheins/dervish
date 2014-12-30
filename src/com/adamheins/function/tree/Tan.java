package com.adamheins.function.tree;

public class Tan extends Function {

    public Tan(int bracketDepth) {
        super("tan", Precedence.TRIG, Associativity.RIGHT, bracketDepth);
    }

    @Override
    public double evaluate() {
        return Math.tan(getFirstChild().evaluate());
    }
}
