package com.adamheins.diff.function;

import java.util.Map;

import org.apfloat.Apfloat;

/**
 * Multiplication operator.
 * 
 * @author Adam
 */
public class Multiply extends Function {

    public Multiply() {
        super("*", Precedence.MULTIPLICATION, Associativity.LEFT, true);
    }
    
    
    @Override
    public Function evaluate(Map<String, Function> varMap) {
        
        // Evaluate children.
        Function first = getFirstChild().evaluate(varMap);
        Function second = getSecondChild().evaluate(varMap);
        
        // Check for number children, and evaluate.
        if (first instanceof Number && second instanceof Number) {
            Apfloat firstVal = (Apfloat)first.getValue();
            Apfloat secondVal = (Apfloat)second.getValue();
            Apfloat result = firstVal.multiply(secondVal);
            return new Number(result);
        }
        
        if (first.equals(Number.ZERO) || second.equals(Number.ZERO)) {
            return Number.ZERO;
        } else if (first.equals(Number.ONE)) {
            return second;
        } else if (second.equals(Number.ONE)) {
            return first;
        }
        
        Function me = new Multiply();
        me.first = first;
        me.second = second;

        return me;
    }
    
    
    @Override
    public Function differentiateInternal(String var) {
        
        Function mult1 = new Multiply();
        mult1.setFirstChild(getFirstChild().differentiateInternal(var));
        mult1.setSecondChild(getSecondChild());
        
        Function mult2 = new Multiply();
        mult2.setFirstChild(getFirstChild());
        mult2.setSecondChild(getSecondChild().differentiateInternal(var));
        
        Function derivative = new Plus();
        derivative.setFirstChild(mult1);
        derivative.setSecondChild(mult2);
        
        return derivative;
    }
}
