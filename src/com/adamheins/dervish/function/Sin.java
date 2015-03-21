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
 * Trigonometic sine operator.
 *
 * @author Adam
 */
public class Sin extends Function {

    public Sin() {
        super("sin", Precedence.TRIG, Associativity.RIGHT, false);
    }


    @Override
    public Function evaluate(Map<String, Function> varMap) {

        Function child = getFirstChild().evaluate(varMap);

        if (child instanceof Number) {
            Apfloat result = ApfloatMath.sin((Apfloat)child.getValue());
            return new Number(precisionRound(result));
        }

        Function me = new Sin();
        me.setFirstChild(child);

        return me;
    }

    @Override
    public Function differentiateInternal(String var) {

        Function mult = new Multiply();
        Function cos = new Cos();

        cos.setFirstChild(getFirstChild().evaluate());
        mult.setFirstChild(cos);
        mult.setSecondChild(getFirstChild().differentiateInternal(var));

        return mult;
    }
}
