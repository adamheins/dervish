/*
 * Copyright (c) 2015 Adam Heins
 *
 * This file is part of the Dervish project, which is distributed under the MIT
 * license. For the full terms, see the included LICENSE file.
 */

package com.adamheins.dervish.function;

import java.util.Map;

import org.apfloat.Apfloat;

/**
 * A function that is a constant number.
 *
 * @author Adam
 */
public class Number extends Function {

    public static final Number ZERO = new Number(Apfloat.ZERO);
    public static final Number ONE = new Number(Apfloat.ONE);


    public Number(String value) {
        super(new Apfloat(value, INTERNAL_PRECISION), Precedence.NUMBER,
                Associativity.LEFT, true);
    }


    /**
     * Internal constructor for performance purposes. Create a Number directly
     * from an Apfloat, but avoid exposing the internal implementation
     * (Apfloat).
     *
     * @param numValue The value of the number.
     */
    Number(Apfloat value) {
        super(value, Precedence.NUMBER, Associativity.LEFT, true);
    }


    @Override
    public Function evaluate(Map<String, Function> varMap) {
        return this;
    }


    @Override
    public Function differentiateInternal(String var) {
        return Number.ZERO;
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
