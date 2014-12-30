package com.adamheins.function.tree;

import java.math.BigDecimal;
import java.util.Map;

public class Multiply extends Function {

    public Multiply(int bracketDepth) {
        super("*", Precedence.MULTIPLICATION, Associativity.LEFT, bracketDepth);
    }

    
    @Override
    public void differentiate(String var, Function function) {
        
        getFirstChild().differentiate(var, function);
        function.add(new Multiply(bracketDepth));
        function.add(getSecondChild());
        
        function.add(new Plus(bracketDepth));
        
        function.add(getFirstChild());
        function.add(new Multiply(bracketDepth));
        getSecondChild().differentiate(var, function);
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
            return new Number(result.toString(), bracketDepth);
        }
        
        // If one of the children is equal to zero, there is no point having it in the expression.
        if (first instanceof Number) {
            BigDecimal firstValue = new BigDecimal(first.value);
            if (firstValue.equals(BigDecimal.ZERO))
                return new Number("0", bracketDepth);
        } else if (second instanceof Number) {
            BigDecimal secondValue = new BigDecimal(second.value);
            if (secondValue.equals(BigDecimal.ZERO))
                return new Number("0", bracketDepth);
        }

        
        Function me = new Multiply(bracketDepth);
        me.first = first;
        me.second = second;
        me.parent = this.parent;

        return me;
    }
}
