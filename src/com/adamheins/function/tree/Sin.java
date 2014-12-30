package com.adamheins.function.tree;

public class Sin extends Function {

    public Sin(int bracketDepth) {
        super("sin", Precedence.TRIG, Associativity.RIGHT, bracketDepth);
    }

    @Override
    public double evaluate() {
        return Math.sin(getFirstChild().evaluate());
    }
}
