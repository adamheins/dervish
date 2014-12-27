package com.adamheins.function.tree;

public class Cos extends Node {

    public Cos(int bracketDepth) {
        super("cos", Precedence.TRIG, Associativity.RIGHT, bracketDepth);
    }

    @Override
    public double evaluate() {
        return Math.cos(getFirstChild().evaluate());
    }
}
