package com.adamheins.expression.tree;

public class Divide extends Node {

    public Divide(int bracketDepth) {
        super("/", Precedence.MULTIPLICATION, Associativity.LEFT, bracketDepth);
    }

    @Override
    public double evaluate() {
        return getFirstChild().evaluate() / getSecondChild().evaluate();
    }

}
