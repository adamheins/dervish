/*
 * Copyright (c) 2015 Adam Heins
 *
 * This file is part of the Dervish project, which is distributed under the MIT
 * license. For the full terms, see the included LICENSE file.
 */

package com.adamheins.dervish.function;

import java.util.Map;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

/**
 * Trigonometric cosine operator.
 *
 * @author Adam
 */
public class Cos extends Function {

    public Cos() {
        super("cos", Precedence.TRIG, Associativity.RIGHT, false);
    }


    @Override
    public Function evaluate(Map<String, Function> varMap) {
        Function child = getFirstChild().evaluate(varMap);

        if (child instanceof Number) {
            Apfloat result = ApfloatMath.cos((Apfloat)child.getValue());
            return new Number(precisionRound(result));
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
