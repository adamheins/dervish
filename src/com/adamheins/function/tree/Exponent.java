package com.adamheins.function.tree;

import java.math.BigDecimal;
import java.util.Map;

public class Exponent extends Function {

    public Exponent() {
        super("^", Precedence.EXPONENTIATION, Associativity.RIGHT);
    }


    @Override
    public Function evaluate(Map<String, Function> varMap) {
        
        Function first = getFirstChild().evaluate(varMap);
        Function second = getSecondChild().evaluate(varMap);
        
        if (first instanceof Number && second instanceof Number) {
            double firstValue = Double.parseDouble(first.value);
            double secondValue = Double.parseDouble(second.value);
            return new Number(Double.toString(Math.pow(firstValue, secondValue)));
        }
        
        // Value is always 1 if the exponent is 0.
        // TODO unless the base is also 0, in which case it is undefined.
        if (second instanceof Number) {
           if ((new BigDecimal(second.getValue())).equals(BigDecimal.ZERO))
               return new Number("1");
        }
        
        Function me = new Exponent();
        me.setFirstChild(first);
        me.setSecondChild(second);
        
        return me;
    }

    
    @Override
    public Function differentiate(String var) {
        //f(x)^g(x) * d/dx( g(x) ) * ln( f(x) ) + f(x)^( g(x)-1 ) * g(x) * d/dx( f(x) )
        
        
        
        
        Function first = getFirstChild().evaluate();
        Function second = getSecondChild().evaluate();
        
        Function firstDer = first.differentiate(var);
        Function secondDer = second.differentiate(var);
        
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(first, 0);
        fb.add(new Exponent(), 0);
        fb.add(second, 0);
        
        fb.add(new Multiply(), 0);
        fb.add(secondDer, 0);
        
        Function exp1 = new Exponent();
        exp1.setFirstChild(first);
        exp1.setSecondChild(second);
        
        Function 
        
        Function minus = new Minus();
        minus.setFirstChild(second);
        minus.setSecondChild(new Number("1"));
        
        Function plus = new Plus();
        
        return null;
    }

}
