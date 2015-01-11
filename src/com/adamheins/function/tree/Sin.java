package com.adamheins.function.tree;

import java.util.Map;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

public class Sin extends Function {

    public Sin() {
        super("sin", Precedence.TRIG, Associativity.RIGHT);
    }


    @Override
    public Function evaluate(Map<String, Function> varMap) {
        
        Function child = getFirstChild().evaluate(varMap);
        
        if (child instanceof Number) {
            Apfloat value = new Apfloat(child.getValue());
            return new Number(ApfloatMath.sin(value).toString(PRETTY));
        }
        
        Function me = new Sin();
        me.setFirstChild(child);
        
        return me;
    }

    @Override
    public Function differentiateInternal(String var) {
        
        Function mult = new Multiply();
        Function cos = new Cos();
        
        cos.setFirstChild(getFirstChild().evaluate());
        mult.setFirstChild(cos);
        mult.setSecondChild(getFirstChild().differentiateInternal(var));
        
        return mult;
    }
}
