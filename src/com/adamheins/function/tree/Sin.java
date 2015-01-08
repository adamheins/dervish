package com.adamheins.function.tree;

import java.util.Map;

public class Sin extends Function {

    public Sin() {
        super("sin", Precedence.TRIG, Associativity.RIGHT);
    }


    @Override
    public Function evaluate(Map<String, Function> varMap) {
        
        Function child = getFirstChild().evaluate(varMap);
        
        if (child instanceof Number) {
            return new Number(Double.toString(Math.sin(Double.parseDouble(child.getValue()))));
        }
        
        Function me = new Sin();
        me.setFirstChild(child);
        
        return me;
    }

    @Override
    public Function differentiate(String var) {
        
        Function mult = new Multiply();
        Function cos = new Cos();
        
        cos.setFirstChild(getFirstChild().evaluate());
        mult.setFirstChild(cos);
        mult.setSecondChild(getFirstChild().differentiate(var));
        
        return mult;
    }
}
