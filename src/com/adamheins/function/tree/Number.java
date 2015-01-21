package com.adamheins.function.tree;

import java.util.Map;

import org.apfloat.Apfloat;

public class Number extends Function {
    
    public static final Number ZERO = new Number("0");
    public static final Number ONE = new Number("1");
    
    Apfloat numValue;
   

    public Number(String value) {
        super(value, Precedence.NUMBER, Associativity.LEFT, true);
        numValue = new Apfloat(value, PRECISION);
    }
    
    
    /**
     * Internal constructor for performance purposes. Create a Number directly from an Apfloat, 
     * but avoid exposing the internal implementation (Apfloat).
     * 
     * @param numValue The value of the number.
     */
    Number(Apfloat numValue) {
        super(numValue.toString(PRETTY), Precedence.NUMBER, Associativity.LEFT, true);
        this.numValue = numValue;
    }

    
    @Override
    public Function differentiateInternal(String var) {
        
        // Derivative of a constant value is 0.
        return new Number("0");
    }
    
    
    @Override
    public Function evaluate(Map<String, Function> varMap) {
        
        // Return a copy of this number.
        return new Number(value);
    }
    
    
    /**
     * Gets the value of the number as a numberic type.
     * 
     * @return The value of the number as a numeric type.
     */
    Apfloat getNumericValue() {
        return numValue;
    }
    
    
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Number))
            return false;
        return numValue.equals(((Number)other).numValue);
    }
}
