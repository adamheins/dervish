package com.adamheins.function.tree;

import java.util.Map;

public class Ln extends Log {

    public Ln() {
        super("2.718281828459045", "ln");
    }
    
    
    @Override
    public Function evaluate(Map<String, Function> varMap) {
        
        Function child = getFirstChild().evaluate(varMap);
        
        if (child instanceof Number)
            return new Number(Double.toString(Math.log(Double.parseDouble(child.getValue()))));
        
        Function me = new Ln();
        me.setFirstChild(child);
        
        return me;
    }
    
    
    @Override 
    public Function differentiateInternal(String var) {
        
        Function divide = new Divide();
        divide.setFirstChild(getFirstChild().differentiateInternal(var));
        divide.setSecondChild(getFirstChild().evaluate());
        
        return divide;
    }
}
