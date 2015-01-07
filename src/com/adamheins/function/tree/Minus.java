package com.adamheins.function.tree;

import java.math.BigDecimal;
import java.util.Map;

public class Minus extends Function {

    public Minus() {
        super("-", Precedence.ADDITION, Associativity.LEFT);
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
            BigDecimal result = firstValue.subtract(secondValue);
            return new Number(result.toString());
        }
        
        // TODO need a unary negative sign to handle case where first number is zero.
        
        if (second instanceof Number) {
            BigDecimal secondValue = new BigDecimal(second.value);
            if (secondValue.equals(BigDecimal.ZERO))
                return first;
        }
        
        Function me = new Plus();
        me.setFirstChild(first);
        me.setSecondChild(second);
        
        return me;
    }


    @Override
    public Function differentiate(String var) {
        Function derivative = new Minus();
        derivative.setFirstChild(getFirstChild().differentiate(var));
        derivative.setSecondChild(getSecondChild().differentiate(var));
        return derivative;
    }
}
