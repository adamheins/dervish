package com.adamheins.expression.tree;

public class Number extends Node {

    public Number(String value, int bracketDepth) {
        super(value, Precedence.NUMBER, Associativity.LEFT, bracketDepth);
    }

    @Override
    public double evaluate() {
        return Double.valueOf(value);
    }

}
