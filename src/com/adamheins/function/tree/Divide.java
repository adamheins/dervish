package com.adamheins.function.tree;

public class Divide extends Node {

    public Divide(int bracketDepth) {
        super("/", Precedence.MULTIPLICATION, Associativity.LEFT, bracketDepth);
    }

    @Override
    public String evaluate() {
        return getFirstChild().evaluate() + value + getSecondChild().evaluate();
    }

}
