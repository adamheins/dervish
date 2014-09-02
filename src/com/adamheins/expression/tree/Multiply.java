package com.adamheins.expression.tree;

public class Multiply extends Node {

    public Multiply(int bracketDepth) {
        super("*", Precedence.MULTIPLICATION, Associativity.LEFT, bracketDepth);
    }

    @Override
    public double evaluate() {
        return getFirstChild().evaluate() * getSecondChild().evaluate();
    }

}
