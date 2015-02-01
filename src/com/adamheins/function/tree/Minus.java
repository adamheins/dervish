package com.adamheins.function.tree;

import java.util.Map;

import org.apfloat.Apfloat;

public class Minus extends Function {

    public Minus() {
        super("-", Precedence.ADDITION, Associativity.LEFT, false);
    }


    @Override
    public Function evaluate(Map<String, Function> varMap) {
        
        // Evaluate children.
        Function first = getFirstChild().evaluate(varMap);
        Function second = getSecondChild().evaluate(varMap);
        
        // Check for number children, and evaluate.
        if (first instanceof Number && second instanceof Number) {
            Apfloat firstValue = (Apfloat)first.getValue();
            Apfloat secondValue = (Apfloat)second.getValue();
            return new Number(firstValue.subtract(secondValue));
        }
        
        if (first.equals(Number.ZERO)) {
            return second;
        } else if (second.equals(Number.ZERO)) {
            return first;
        }
        
        Function me = new Minus();
        me.setFirstChild(first);
        me.setSecondChild(second);
        
        return me;
    }


    @Override
    public Function differentiateInternal(String var) {
        Function derivative = new Minus();
        derivative.setFirstChild(getFirstChild().differentiateInternal(var));
        derivative.setSecondChild(getSecondChild().differentiateInternal(var));
        return derivative;
    }
}
