package com.adamheins.function.tree;

import java.util.Map;

public class Number extends Node {

    public Number(String value, int bracketDepth) {
        super(value, Precedence.NUMBER, Associativity.LEFT, bracketDepth);
    }

    
    @Override
    public void differentiate(String var, ExpressionTree derivative) {
        
        // Derivative of a constant value is 0.
        derivative.add(new Number("0", bracketDepth));
    }
    
    
    @Override
    public Node evaluate(Map<String, Double> varMap) {
        return new Number(value, bracketDepth);
    }

    
    @Override
    public String toString() {
        return value;
    }
}
