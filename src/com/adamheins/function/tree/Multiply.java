package com.adamheins.function.tree;

import java.util.Map;

import org.apfloat.Apfloat;

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
            return new Number(firstVal.multiply(secondVal));
        }
        
        // If one of the children is equal to zero, there is no point having it in the expression.
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
        
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(getFirstChild().differentiateInternal(var), 0);
        fb.add(new Multiply(), 0);
        fb.add(getSecondChild(), 0);
        fb.add(new Plus(), 0);
        fb.add(getFirstChild(), 0);
        fb.add(new Multiply(), 0);
        fb.add(getSecondChild().differentiateInternal(var), 0);
        
        Function derivative = fb.getFunction();
        
        return derivative;
    }
}
