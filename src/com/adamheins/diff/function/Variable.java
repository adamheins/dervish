package com.adamheins.diff.function;

import java.util.Map;

/**
 * A variable function.
 *
 * @author Adam
 */
public class Variable extends Function {

    public Variable(String value) {
        super(value, Precedence.NUMBER, Associativity.LEFT, true);
    }


    @Override
    public Function differentiateInternal(String var) {

        // Derivative of a variable is 1.
        if (var.equals(value))
            return Number.ONE;
        return Number.ZERO;
    }


    @Override
    public Function evaluate(Map<String, Function> varMap) {

        // Replaces itself with the appropriate value, or just leaves itself in
        // if a value for this variable is not defined in the map.
        if (varMap != null && varMap.containsKey(value))
            return varMap.get(value).evaluate(varMap);

        // If this variable is not defined in the map, just return itself.
        return this;
    }


    @Override
    public String toString() {
        return value.toString();
    }
}
