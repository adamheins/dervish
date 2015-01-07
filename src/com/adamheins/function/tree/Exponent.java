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
        // TODO General form of derivative of power, where both base and exponent are functions
        // is tediously long. Implement.
        return null;
    }

}
