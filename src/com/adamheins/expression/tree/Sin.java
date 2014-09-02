package com.adamheins.expression.tree;

public class Sin extends Node {

    public Sin(int bracketDepth) {
        super("sin", Precedence.TRIG, Associativity.RIGHT, bracketDepth);
    }

    @Override
    public double evaluate() {
        return Math.sin(getFirstChild().evaluate());
    }
}
