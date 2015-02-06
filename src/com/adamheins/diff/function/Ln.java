package com.adamheins.diff.function;

import java.math.RoundingMode;
import java.util.Map;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

public class Ln extends Log {

    public Ln() {
        super(Number.E.getValue().toString(), "ln");
    }
    
    
    @Override
    public Function evaluate(Map<String, Function> varMap) {
        
        Function child = getFirstChild().evaluate(varMap);
        
        if (child instanceof Number) {
            
            // Last digit needs to be rounded to get rid of imprecision errors.
            Apfloat value = ((Apfloat)child.getValue());
            return new Number(ApfloatMath.round(ApfloatMath.log(value), PRECISION - 1, RoundingMode.HALF_UP));
        }

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
