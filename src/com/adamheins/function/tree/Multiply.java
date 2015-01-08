package com.adamheins.function.tree;

import java.math.BigDecimal;
import java.util.Map;

public class Multiply extends Function {

    public Multiply() {
        super("*", Precedence.MULTIPLICATION, Associativity.LEFT);
    }

    
    @Override
    public Function differentiate(String var) {
        
        Function derivative = new Plus();
        Function mult1 = new Multiply();
        Function mult2 = new Multiply();
        
        mult1.setFirstChild(getFirstChild().differentiate(var));
        mult1.setSecondChild(getSecondChild());
        
        mult2.setFirstChild(getFirstChild());
        mult2.setSecondChild(getSecondChild().differentiate(var));
        
        derivative.setFirstChild(mult1);
        derivative.setSecondChild(mult2);
        
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
            BigDecimal result = firstValue.multiply(secondValue);
            return new Number(result.toString());
        }
        
        // If one of the children is equal to zero, there is no point having it in the expression.
        if (first instanceof Number) {
            BigDecimal firstValue = new BigDecimal(first.value);
            if (firstValue.equals(BigDecimal.ZERO))
                return new Number("0");
            if (firstValue.equals(BigDecimal.ONE))
                return second;
        } else if (second instanceof Number) {
            BigDecimal secondValue = new BigDecimal(second.value);
            if (secondValue.equals(BigDecimal.ZERO))
                return new Number("0");
            if (secondValue.equals(BigDecimal.ONE))
                return first;
        }

        
        Function me = new Multiply();
        me.first = first;
        me.second = second;

        return me;
    }
}
