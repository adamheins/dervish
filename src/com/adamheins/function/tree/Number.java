package com.adamheins.function.tree;

import java.util.Map;

import org.apfloat.Apfloat;

public class Number extends Function {
    
    public static final Number ZERO = new Number("0");
    public static final Number ONE = new Number("1");
   

    public Number(String value) {
        super(new Apfloat(value, PRECISION), Precedence.NUMBER, Associativity.LEFT, true);
    }
    
    
    /**
     * Internal constructor for performance purposes. Create a Number directly from an Apfloat, 
     * but avoid exposing the internal implementation (Apfloat).
     * 
     * @param numValue The value of the number.
     */
    Number(Apfloat value) {
        super(value, Precedence.NUMBER, Associativity.LEFT, true);
    }

    
    @Override
    public Function differentiateInternal(String var) {
        return Number.ZERO;
    }
    
    
    @Override
    public Function evaluate(Map<String, Function> varMap) {
        return this;
    }
    
    
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Number))
            return false;
        return value.equals(((Number)other).getValue());
    }
    
    
    @Override
    public String toString() {
        return ((Apfloat)value).toString(PRETTY);
    }
}
