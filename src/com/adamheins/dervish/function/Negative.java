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
 * Negation operator.
 *
 * @author Adam
 */
public class Negative extends Function {

    public Negative() {
        super("-", Precedence.MULTIPLICATION, Associativity.RIGHT, false);
    }


    @Override
    public Function evaluate(Map<String, Function> varMap) {

        Function child = getFirstChild().evaluate(varMap);

        if (child instanceof Number) {
            return new Number(((Apfloat) child.getValue()).negate());
        }

        Function me = new Negative();
        me.setFirstChild(child);

        return me;
    }


    @Override
    public Function differentiateInternal(String var) {

        Function childDer = getFirstChild().differentiateInternal(var);
        Function derivative = new Negative();
        derivative.setFirstChild(childDer);
        return derivative;
    }


    @Override
    public String toString() {
        return "-(" + getFirstChild().toString() + ")";
    }
}
