package com.adamheins.function.tree;

import java.math.BigDecimal;
import java.util.Map;

public class Minus extends Function {

    public Minus(int bracketDepth) {
        super("-", Precedence.ADDITION, Associativity.LEFT, bracketDepth);
    }
    

    @Override
    public Function evaluate(Map<String, Double> varMap) {
        
        // Evaluate children.
        Function first = getFirstChild().evaluate(varMap);
        Function second = getSecondChild().evaluate(varMap);
        
        // Check for number children, and evaluate.
        if (first instanceof Number && second instanceof Number) {
            BigDecimal firstValue = new BigDecimal(first.value);
            BigDecimal secondValue = new BigDecimal(second.value);
            BigDecimal result = firstValue.add(secondValue);
            return new Number(result.toString(), bracketDepth);
        }
        
        // If one of the children is equal to zero, there is no point having it in the expression.
        /*if (first instanceof Number) {
            BigDecimal firstValue = new BigDecimal(first.value);
            if (firstValue.equals(BigDecimal.ZERO)) {
                second.parent = this.parent;
                return second;
            }
        } else*/ if (second instanceof Number) {
            BigDecimal secondValue = new BigDecimal(second.value);
            if (secondValue.equals(BigDecimal.ZERO)) {
                first.parent = this.parent;
                return first;
            }
        }
        
        Function me = new Minus(bracketDepth);
        me.first = first;
        me.second = second;
        me.parent = this.parent;

        return me;
    }

    
    @Override
    public void differentiate(String var, Function function) {
        getFirstChild().differentiate(var, function);
        function.add(new Minus(bracketDepth));
        getSecondChild().differentiate(var, function);
    }
    
    
    @Override
    public String toString() {
        return "(" + getFirstChild().toString() + "-" + getSecondChild().toString() + ")";
    }
}
