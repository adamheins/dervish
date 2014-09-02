package com.adamheins.expression.tree;

public class Exponent extends Node {

    public Exponent(int bracketDepth) {
        super("^", Precedence.EXPONENTIATION, Associativity.RIGHT, bracketDepth);
    }

    @Override
    public double evaluate() {
        return Math.pow(getFirstChild().evaluate(), getSecondChild().evaluate());
    }

}
