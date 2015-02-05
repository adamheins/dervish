package com.adamheins.diff.function;

import java.util.Map;

import org.apfloat.Apfloat;

public class Negative extends Function {

    public Negative() {
        super("-", Precedence.MULTIPLICATION, Associativity.RIGHT, false);
    }


    @Override
    public Function evaluate(Map<String, Function> varMap) {
        
        Function child = getFirstChild().evaluate(varMap);
        
        if (child instanceof Number) {
            return new Number(((Apfloat) child.getValue()).negate());
        }
        
        Function me = new Negative();
        me.setFirstChild(child);
        
        return me;
    }


    @Override
    public Function differentiateInternal(String var) {

        Function childDer = getFirstChild().differentiateInternal(var);
        Function derivative = new Negative();
        derivative.setFirstChild(childDer);
        return derivative;
    }
    
    
    @Override
    public String toString() {
        return "-(" + getFirstChild().toString() + ")";
    }

}
