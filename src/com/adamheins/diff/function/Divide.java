package com.adamheins.diff.function;

import java.util.Map;

import org.apfloat.Apfloat;

public class Divide extends Function {

    public Divide() {
        super("/", Precedence.MULTIPLICATION, Associativity.LEFT, false);
    }
    

    @Override
    public Function evaluate(Map<String, Function> varMap) {
        
        Function first = getFirstChild().evaluate(varMap);
        Function second = getSecondChild().evaluate(varMap);
        
        if (first instanceof Number && second instanceof Number) {
            Apfloat firstValue = (Apfloat)first.getValue();
            Apfloat secondValue = (Apfloat)second.getValue();
            return new Number(firstValue.divide(secondValue));
        }
        
        if (second.equals(Number.ONE)) {
            return first;
        } else if (second.equals(Number.ZERO)) {
            throw new EvaluationException("Division by zero.");
        } else if (first.equals(Number.ZERO)) {
            return Number.ZERO;
        }
        
        Function me = new Divide();
        me.setFirstChild(first);
        me.setSecondChild(second);
        
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
        
        Function mult3 = new Multiply();
        mult3.setFirstChild(getSecondChild());
        mult3.setSecondChild(getSecondChild());
        
        Function minus = new Minus();
        minus.setFirstChild(mult1);
        minus.setSecondChild(mult2);
        
        Function derivative = new Divide();
        derivative.setFirstChild(minus);
        derivative.setSecondChild(mult3);
        
        return derivative;
    }

}
