package com.adamheins.function.tree;

import java.util.Map;

public class Variable extends Function {

    public Variable(String value) {
        super(value, Precedence.NUMBER, Associativity.LEFT);
    }


    @Override
    public void differentiate(String var, Function function) {
        
        // Derivative of a variable is 1.
        if (var.equals(value))
            function.add(new Number("1"));
        else
            function.add(new Number("0"));
    }
    
    
    @Override
    public Function evaluate(Map<String, Function> varMap) {
        
        // Replaces itself with the appropriate value, or just leaves itself in if a value for this
        // variable is not defined in the map.
        if (varMap != null && varMap.containsKey(value)) 
            return varMap.get(value).evaluate(varMap); // need to deal with recursion
        
        // If this variable is not defined in the map, just return a new copy of itself.
        return new Variable(value);
    }
}
