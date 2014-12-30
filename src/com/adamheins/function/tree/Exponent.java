package com.adamheins.function.tree;

public class Exponent extends Function {

    public Exponent(int bracketDepth) {
        super("^", Precedence.EXPONENTIATION, Associativity.RIGHT, bracketDepth);
    }

    @Override
    public double evaluate() {
        return Math.pow(getFirstChild().evaluate(), getSecondChild().evaluate());
    }

}
