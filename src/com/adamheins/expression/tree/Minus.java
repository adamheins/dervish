package com.adamheins.expression.tree;

public class Minus extends Node {

    public Minus(int bracketDepth) {
        super("-", Precedence.ADDITION, Associativity.LEFT, bracketDepth);
    }

    @Override
    public double evaluate() {
        return getFirstChild().evaluate() - getSecondChild().evaluate();
    }
}
