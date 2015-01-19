package com.adamheins.function.tree;

import java.util.Map;

import org.apfloat.Apfloat;

public class Plus extends Function {

    public Plus() {
        super("+", Precedence.ADDITION, Associativity.LEFT, true);
    }
    
    
    @Override
    public Function evaluate(Map<String, Function> varMap) {
        
        // Evaluate children.
        Function first = getFirstChild().evaluate(varMap);
        Function second = getSecondChild().evaluate(varMap);
        
        // Check for number children, and evaluate.
        if (first instanceof Number && second instanceof Number) {
            
            Apfloat firstVal = new Apfloat(first.getValue(), PRECISION);
            Apfloat secondVal = new Apfloat(second.getValue(), PRECISION);
            return new Number(firstVal.add(secondVal).toString(PRETTY));
        }
        
        // If one of the children is equal to zero, there is no point having it in the expression.
        if (first instanceof Number) {
            Apfloat firstVal = new Apfloat(first.getValue());
            if (firstVal.equals(Apfloat.ZERO))
                return second;
        } else if (second instanceof Number) {
            Apfloat secondVal = new Apfloat(second.getValue());
            if (secondVal.equals(Apfloat.ZERO))
                return first;
        }
        
        Function me = new Plus();
        me.setFirstChild(first);
        me.setSecondChild(second);
        
        return me;
    }
    
    
    @Override
    public Function differentiateInternal(String var) {
        
        Function derivative = new Plus();
        derivative.setFirstChild(getFirstChild().differentiateInternal(var));
        derivative.setSecondChild(getSecondChild().differentiateInternal(var));
        return derivative;
    }
}
