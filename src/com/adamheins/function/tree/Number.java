package com.adamheins.function.tree;

import java.util.Map;

public class Number extends Function {

    public Number(String value) {
        super(value, Precedence.NUMBER, Associativity.LEFT);
    }

    
    @Override
    public void differentiate(String var, Function derivative) {
        
        // Derivative of a constant value is 0.
        derivative.add(new Number("0"));
    }
    
    
    @Override
    public Function evaluate(Map<String, Function> varMap) {
        
        // Return a copy of this number.
        return new Number(value);
    }
}
