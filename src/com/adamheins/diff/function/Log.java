package com.adamheins.diff.function;

import java.math.RoundingMode;
import java.util.Map;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

public class Log extends Function {

    // The base of the logarithm.
    Apfloat base;
    
    public Log(String base) {
        super("log<" + base + ">", Precedence.EXPONENTIATION, Associativity.RIGHT, false);
        this.base = new Apfloat(base, PRECISION + 1);
    }
    
    // Internal use.
    private Log(Apfloat base) {
        super("log<" + base.toString(PRETTY) + ">", Precedence.EXPONENTIATION, Associativity.RIGHT, false);
        this.base = base;
    }
    
    
    // Internal use.
    protected Log(String base, String value) {
        super(value, Precedence.EXPONENTIATION, Associativity.RIGHT, false);
        this.base = new Apfloat(base, PRECISION + 1);
    }


    @Override
    public Function evaluate(Map<String, Function> varMap) {
        
        Function child = getFirstChild().evaluate(varMap);
        
        if (child instanceof Number) {

            // Hacky way of dealing with precision problems.
            Apfloat value = ((Apfloat)child.getValue()).precision(PRECISION + 1);
            return new Number(ApfloatMath.round(ApfloatMath.log(value, base), PRECISION, RoundingMode.HALF_UP));
        }
        
        Function me = new Log(base);
        me.setFirstChild(child);
        return me;
    }


    @Override
    public Function differentiateInternal(String var) {
        
        Apfloat coefficient = Apfloat.ONE.divide(ApfloatMath.log(base));
        
        Function ln = new Ln();
        ln.setFirstChild(getFirstChild());
        
        Function derivative = new Multiply();
        derivative.setFirstChild(new Number(coefficient.toString(PRETTY)));
        derivative.setSecondChild(ln);
        
        return derivative;
    }

}