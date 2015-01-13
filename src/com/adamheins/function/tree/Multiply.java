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
            Apfloat firstVal = new Apfloat(first.getValue());
            Apfloat secondVal = new Apfloat(second.getValue());
            return new Number(firstVal.multiply(secondVal).toString(PRETTY));
        }
        
        // If one of the children is equal to zero, there is no point having it in the expression.
        if (first instanceof Number) {
            Apfloat firstVal = new Apfloat(first.getValue());
            if (firstVal.equals(Apfloat.ZERO))
                return new Number("0");
            if (firstVal.equals(Apfloat.ONE))
                return second;
        } else if (second instanceof Number) {
            Apfloat secondVal = new Apfloat(second.getValue());
            if (secondVal.equals(Apfloat.ZERO))
                return new Number("0");
            if (secondVal.equals(Apfloat.ONE))
                return first;
        }
        
        Function me = new Multiply();
        me.first = first;
        me.second = second;

        return me;
    }
    
    
    @Override
    public Function differentiateInternal(String var) {
        
        Function derivative = new Plus();
        Function mult1 = new Multiply();
        Function mult2 = new Multiply();
        
        mult1.setFirstChild(getFirstChild().differentiateInternal(var));
        mult1.setSecondChild(getSecondChild());
        
        mult2.setFirstChild(getFirstChild());
        mult2.setSecondChild(getSecondChild().differentiateInternal(var));
        
        derivative.setFirstChild(mult1);
        derivative.setSecondChild(mult2);
        
        return derivative;
    }
}
