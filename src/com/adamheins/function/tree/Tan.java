package com.adamheins.function.tree;

import java.util.Map;

public class Tan extends Function {

    public Tan() {
        super("tan", Precedence.TRIG, Associativity.RIGHT);
    }

    @Override
    public Function evaluate(Map<String, Function> varMap) {
        
        Function child = getFirstChild().evaluate(varMap);
        
        if(child instanceof Number) {
            return new Number(Double.toString(Math.tan(Double.parseDouble(child.getValue()))));
        }
        
        Function me = new Tan();
        me.setFirstChild(child);
        
        return me;
    }
    
    /*
     * Possibility for adding polymorphism to evaluation. Pass a reference to the current class
     * to the child, and then the child can use the calculate method if it is Number.
     */
    double calculate(double value) {
        return Math.tan(value);
    }

    
    @Override
    public Function differentiate(String var) {
        
        Function div = new Divide();
        div.setFirstChild(getFirstChild().differentiate(var));
        
        Function pow = new Exponent();
        Function child = getFirstChild().evaluate();
        Function cos = new Cos();
        cos.setFirstChild(child);
        
        pow.setFirstChild(cos);
        pow.setSecondChild(new Number("2"));
        div.setSecondChild(pow);
        
        return div;
    }
}
