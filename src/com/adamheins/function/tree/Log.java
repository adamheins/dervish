package com.adamheins.function.tree;

import java.util.Map;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

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
            Apfloat value = new Apfloat(child.getValue());
            Apfloat baseValue = new Apfloat(base);
            return new Number(ApfloatMath.log(value, baseValue).toString(PRETTY));
        }
        
        Function me = new Log(base);
        me.setFirstChild(child);
        
        return me;
    }


    @Override
    public Function differentiateInternal(String var) {
        
        // Transform to equivalent form using only natural logarithm.
        Function coeff = new Number(Double.toString(1/Math.log(Double.parseDouble(base))));
        Function ln = new Ln();
        ln.setFirstChild(getFirstChild().evaluate());
        Function mult = new Multiply();
        mult.setFirstChild(coeff);
        mult.setSecondChild(ln);
        
        return mult.differentiateInternal(var);
    }

}
