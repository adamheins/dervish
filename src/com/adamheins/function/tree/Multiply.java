package com.adamheins.function.tree;

import java.math.BigDecimal;
import java.util.Map;

public class Multiply extends Node {

    public Multiply(int bracketDepth) {
        super("*", Precedence.MULTIPLICATION, Associativity.LEFT, bracketDepth);
    }

    
    @Override
    public void differentiate(String var, ExpressionTree function) {
        
        getFirstChild().differentiate(var, function);
        function.add(new Multiply(bracketDepth));
        function.add(getSecondChild());
        
        function.add(new Plus(bracketDepth));
        
        function.add(getFirstChild());
        function.add(new Multiply(bracketDepth));
        getSecondChild().differentiate(var, function);
    }
    
    
    @Override
    public Node evaluate(Map<String, Double> varMap) {
        
        // Evaluate children.
        Node first = getFirstChild().evaluate(varMap);
        Node second = getSecondChild().evaluate(varMap);
        
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

        
        Node me = new Multiply(bracketDepth);
        me.first = first;
        me.second = second;
        me.parent = this.parent;

        return me;
    }
}
