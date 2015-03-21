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
 * Logarithm operator.
 *
 * @author Adam
 */
public class Log extends Function {

    // The base of the logarithm.
    Apfloat base;

    public Log(String base) {
        super("log<" + base + ">", Precedence.EXPONENTIATION,
                Associativity.RIGHT, false);
        this.base = new Apfloat(base, INTERNAL_PRECISION);
    }


    private Log(Apfloat base) {
        super("log<" + base.toString(PRETTY) + ">", Precedence.EXPONENTIATION,
                Associativity.RIGHT, false);
        this.base = base;
    }


    protected Log(String base, String value) {
        super(value, Precedence.EXPONENTIATION, Associativity.RIGHT, false);
        this.base = new Apfloat(base, INTERNAL_PRECISION);
    }


    @Override
    public Function evaluate(Map<String, Function> varMap) {

        Function child = getFirstChild().evaluate(varMap);

        if (child instanceof Number) {
            Apfloat value = ((Apfloat)child.getValue());
            Apfloat result = ApfloatMath.log(value, base);
            return new Number(precisionRound(result));
        }

        Function me = new Log(base);
        me.setFirstChild(child);
        return me;
    }


    @Override
    public Function differentiateInternal(String var) {

        Apfloat coefficient = Apfloat.ONE.divide(ApfloatMath.log(base));

        Function ln = new Ln();
        ln.setFirstChild(getFirstChild());

        Function derivative = new Multiply();
        derivative.setFirstChild(new Number(coefficient.toString(PRETTY)));
        derivative.setSecondChild(ln);

        return derivative;
    }
}
