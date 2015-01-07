package com.adamheins.function.tree;

import java.util.Map;

public class Divide extends Function {

    public Divide() {
        super("/", Precedence.MULTIPLICATION, Associativity.LEFT);
    }
    

    @Override
    public Function evaluate(Map<String, Function> varMap) {
        
        Function first = getFirstChild().evaluate(varMap);
        Function second = getSecondChild().evaluate(varMap);
        
        if (first instanceof Number && second instanceof Number) {
            double firstValue = Double.parseDouble(first.value);
            double secondValue = Double.parseDouble(second.value);
            return new Number(Double.toString(firstValue / secondValue));
        }
        
        if (first instanceof Multiply) {
           // TODO eliminate common terms from numerator and denominator
        }
        
        Function me = new Divide();
        me.setFirstChild(first);
        me.setSecondChild(second);
        
        return me;
    }

    
    @Override
    public Function differentiate(String var) {
        Function derivative = new Divide();
        
        Function minus = new Minus();
        
        Function mult1 = new Multiply();
        Function mult2 = new Multiply();
        Function mult3 = new Multiply();
        
        mult1.setFirstChild(getFirstChild().differentiate(var));
        mult1.setSecondChild(getSecondChild());
        
        mult2.setFirstChild(getFirstChild());
        mult2.setSecondChild(getSecondChild().differentiate(var));
        
        minus.setFirstChild(mult1);
        minus.setSecondChild(mult2);
        
        mult3.setFirstChild(getSecondChild());
        mult3.setSecondChild(getSecondChild());
        
        derivative.setFirstChild(minus);
        derivative.setSecondChild(mult3);

        return derivative;
    }

}
