package com.adamheins.function.tree;

import java.util.Map;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

public class Log extends Function {

    // The base of the logarithm.
    Apfloat base;
    
    public Log(String base) {
        super("log<" + base + ">", Precedence.EXPONENTIATION, Associativity.RIGHT, false);
        this.base = new Apfloat(base);
    }
    
    // Internal use.
    private Log(Apfloat base) {
        super("log<" + base.toString(PRETTY) + ">", Precedence.EXPONENTIATION, Associativity.RIGHT, false);
        this.base = base;
    }
    
    
    // Internal use.
    protected Log(String base, String value) {
        super(value, Precedence.EXPONENTIATION, Associativity.RIGHT, false);
        this.base = new Apfloat(base);
    }


    @Override
    public Function evaluate(Map<String, Function> varMap) {
        
        Function child = getFirstChild().evaluate(varMap);
        
        if (child instanceof Number) {
            Apfloat value = (Apfloat)child.getValue();
            return new Number(ApfloatMath.log(value, base));
        }
        
        Function me = new Log(base);
        me.setFirstChild(child);
        return me;
    }


    @Override
    public Function differentiateInternal(String var) {
        
        Apfloat coefficient = Apfloat.ONE.divide(ApfloatMath.log(base));
        
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number(coefficient.toString(PRETTY)), 0);
        fb.add(new Multiply(), 0);
        fb.add(new Ln(), 0);
        fb.add(getFirstChild().evaluate(), 0);
        
        Function derivative = fb.getFunction().differentiateInternal(var);
        
        return derivative;
    }

}
