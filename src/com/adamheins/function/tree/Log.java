package com.adamheins.function.tree;

import java.util.Map;

public class Log extends Function {

    
    String base;
    
    public Log(String base) {
        super("log<" + base + ">", Precedence.EXPONENTIATION, Associativity.RIGHT);
        this.base = base;
    }
    
    
    // Super-internal use only.
    // As opposed to regular internal use.
    Log(String base, String value) {
        super(value, Precedence.EXPONENTIATION, Associativity.RIGHT);
        this.base = base;
    }


    @Override
    public Function evaluate(Map<String, Function> varMap) {
        
        Function child = getFirstChild().evaluate(varMap);
        
        if (child instanceof Number) {
            double baseVal = Double.parseDouble(base);
            double val = Double.parseDouble(child.getValue());
            double result = Math.log(val) / Math.log(baseVal);
            return new Number(Double.toString(result));
        }
        
        Function me = new Log(base);
        me.setFirstChild(child);
        
        return me;
    }


    @Override
    public Function differentiate(String var) {
        
        // Transform to equivalent form using only natural logarithm.
        Function coeff = new Number(Double.toString(1/Math.log(Double.parseDouble(base))));
        Function ln = new Ln();
        ln.setFirstChild(getFirstChild().evaluate());
        Function mult = new Multiply();
        mult.setFirstChild(coeff);
        mult.setSecondChild(ln);
        
        return mult.differentiate(var);
    }

}
