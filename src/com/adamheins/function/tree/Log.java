package com.adamheins.function.tree;

import java.util.Map;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

public class Log extends Function {

    // The base of the logarithm.
    String base;
    
    public Log(String base) {
        super("log<" + base + ">", Precedence.EXPONENTIATION, Associativity.RIGHT, false);
        this.base = base;
    }
    
    
    // Super-internal use only.
    // As opposed to regular internal use.
    protected Log(String base, String value) {
        super(value, Precedence.EXPONENTIATION, Associativity.RIGHT, false);
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
        
        Apfloat baseVal = new Apfloat(base, PRECISION);
        Apfloat coefficient = Apfloat.ONE.divide(ApfloatMath.log(baseVal));
        
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number(coefficient.toString(PRETTY)), 0);
        fb.add(new Multiply(), 0);
        fb.add(new Ln(), 0);
        fb.add(getFirstChild().evaluate(), 0);
        
        Function derivative = fb.getFunction().differentiateInternal(var);
        
        return derivative;
    }

}
