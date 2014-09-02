package com.adamheins.expression.tree;

public class Plus extends Node {

    public Plus(int bracketDepth) {
        super("+", Precedence.ADDITION, Associativity.LEFT, bracketDepth);
    }

    @Override
    public double evaluate() {
        return getFirstChild().evaluate() + getSecondChild().evaluate();
    }

}
