package com.adamheins.function.tree;

import java.math.BigDecimal;
import java.util.Map;

public class Plus extends Function {

    public Plus() {
        super("+", Precedence.ADDITION, Associativity.LEFT);
    }


    @Override
    public Function differentiate(String var) {
        
        Function derivative = new Plus();
        derivative.setFirstChild(getFirstChild().differentiate(var));
        derivative.setSecondChild(getSecondChild().differentiate(var));
        return derivative;
    }
    
    
    @Override
    public Function evaluate(Map<String, Function> varMap) {
        
        // Evaluate children.
        Function first = getFirstChild().evaluate(varMap);
        Function second = getSecondChild().evaluate(varMap);
        
        // Check for number children, and evaluate.
        if (first instanceof Number && second instanceof Number) {
            BigDecimal firstValue = new BigDecimal(first.value);
            BigDecimal secondValue = new BigDecimal(second.value);
            BigDecimal result = firstValue.add(secondValue);
            return new Number(result.toString());
        }
        
        // If one of the children is equal to zero, there is no point having it in the expression.
        if (first instanceof Number) {
            BigDecimal firstValue = new BigDecimal(first.value);
            if (firstValue.equals(BigDecimal.ZERO))
                return second;
        } else if (second instanceof Number) {
            BigDecimal secondValue = new BigDecimal(second.value);
            if (secondValue.equals(BigDecimal.ZERO))
                return first;
        }
        
        Function me = new Plus();
        me.setFirstChild(first);
        me.setSecondChild(second);
        
        return me;
    }
}
