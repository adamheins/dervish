package com.adamheins.function.tree;

public class Divide extends Function {

    public Divide(int bracketDepth) {
        super("/", Precedence.MULTIPLICATION, Associativity.LEFT, bracketDepth);
    }

    @Override
    public String evaluate() {
        return getFirstChild().evaluate() + value + getSecondChild().evaluate();
    }

}
