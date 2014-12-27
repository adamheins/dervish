package com.adamheins.function.tree;

import java.util.Map;

public class Variable extends Node {

    public Variable(String value, int bracketDepth) {
        super(value, Precedence.NUMBER, Associativity.LEFT, bracketDepth);
    }


    @Override
    public void differentiate(String var, ExpressionTree function) {
        
        // Derivative of a variable is 1.
        if (var.equals(value))
            function.add(new Number("1", bracketDepth));
        else
            function.add(new Number("0", bracketDepth));
    }
    
    
    @Override
    public Node evaluate(Map<String, Double> varMap) {
        
        // Replaces itself with the appropriate value, or just leaves itself in if a value for this
        // variable is not defined in the map.
        if (varMap != null && varMap.containsKey(value)) 
            return new Number(varMap.get(value).toString(), bracketDepth); // questionable using a <String, Double>
        return new Variable(value, bracketDepth);
    }
    
    
    @Override
    public String toString() {
        return value;
    }
}
