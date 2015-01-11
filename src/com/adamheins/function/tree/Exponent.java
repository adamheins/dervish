package com.adamheins.function.tree;

import java.util.Map;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

public class Exponent extends Function {

    public Exponent() {
        super("^", Precedence.EXPONENTIATION, Associativity.RIGHT);
    }


    @Override
    public Function evaluate(Map<String, Function> varMap) {
        
        Function first = getFirstChild().evaluate(varMap);
        Function second = getSecondChild().evaluate(varMap);
        
        if (first instanceof Number && second instanceof Number) {
            Apfloat firstValue = new Apfloat(first.getValue());
            Apfloat secondValue = new Apfloat(second.getValue());
            return new Number(ApfloatMath.pow(firstValue, secondValue).toString(PRETTY));
        }
        
        if (first instanceof Number) {
            Apfloat firstValue = new Apfloat(first.getValue());
            if (firstValue.equals(Apfloat.ZERO))
                return new Number("0");
            // TODO add case for base = 1
        }
        
        // Value is always 1 if the exponent is 0.
        // Ignore exponents with value 1.
        if (second instanceof Number) {
           if ((new Apfloat(second.getValue())).equals(Apfloat.ZERO))
               return new Number("1");
           if ((new Apfloat(second.getValue())).equals(Apfloat.ONE))
               return first;
        }
        
        Function me = new Exponent();
        me.setFirstChild(first);
        me.setSecondChild(second);
        
        return me;
    }

    
    @Override
    public Function differentiateInternal(String var) {
        
        // Formula for d/dx( f(x)^g(x) ):
        // f(x)^g(x) * d/dx( g(x) ) * ln( f(x) ) + f(x)^( g(x)-1 ) * g(x) * d/dx( f(x) )
        
        Function first = getFirstChild().evaluate();
        Function second = getSecondChild().evaluate();
        
        Function firstDer = first.differentiateInternal(var);
        Function secondDer = second.differentiateInternal(var);
        
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(first, 0);
        fb.add(new Exponent(), 0);
        fb.add(second, 0);
        fb.add(new Multiply(), 0);
        fb.add(secondDer, 0);
        fb.add(new Multiply(), 0);
        fb.add(new Ln(), 0);
        fb.add(first, 0);
        fb.add(new Plus(), 0);
        fb.add(first, 0);
        fb.add(new Exponent(), 0);
        fb.add(second, 1);
        fb.add(new Minus(), 1);
        fb.add(new Number("1"), 1);
        fb.add(new Multiply(), 0);
        fb.add(second, 0);
        fb.add(new Multiply(), 0);
        fb.add(firstDer, 0);
        
        return fb.getFunction();
    }

}
