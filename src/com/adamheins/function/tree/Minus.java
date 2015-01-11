package com.adamheins.function.tree;

import java.util.Map;

import org.apfloat.Apfloat;

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
            Apfloat firstValue = new Apfloat(first.getValue());
            Apfloat secondValue = new Apfloat(second.getValue());
            Apfloat result = firstValue.subtract(secondValue);
            return new Number(result.toString(PRETTY));
        }
        
        if (first instanceof Number) {
            Apfloat firstValue = new Apfloat(first.getValue());
            if (firstValue.equals(Apfloat.ZERO)) {
                Function neg = new Negative();
                neg.setFirstChild(second);
                return neg;
            }
        } else if (second instanceof Number) {
            Apfloat secondValue = new Apfloat(second.getValue());
            if (secondValue.equals(Apfloat.ZERO))
                return first;
        }
        
        Function me = new Minus();
        me.setFirstChild(first);
        me.setSecondChild(second);
        
        return me;
    }


    @Override
    public Function differentiateInternal(String var) {
        Function derivative = new Minus();
        derivative.setFirstChild(getFirstChild().differentiateInternal(var));
        derivative.setSecondChild(getSecondChild().differentiateInternal(var));
        return derivative;
    }
}
