package com.adamheins.function.tree;

import java.util.Map;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

public class Cos extends Function {

    public Cos() {
        super("cos", Precedence.TRIG, Associativity.RIGHT);
    }

    @Override
    public Function evaluate(Map<String, Function> varMap) {
        Function child = getFirstChild().evaluate(varMap);
        
        if (child instanceof Number) {
            Apfloat value = new Apfloat(child.getValue());
            return new Number(ApfloatMath.cos(value).toString(PRETTY));
        }
        
        Function me = new Cos();
        me.setFirstChild(child);
        
        return me;
    }

    @Override
    public Function differentiateInternal(String var) {
        Function mult = new Multiply();
        Function sin = new Sin();
        Function neg = new Negative();
        
        sin.setFirstChild(getFirstChild().evaluate());
        neg.setFirstChild(sin);
        mult.setFirstChild(neg);
        mult.setSecondChild(getFirstChild().differentiateInternal(var));
        
        return mult;
    }
}
