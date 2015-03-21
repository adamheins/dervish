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
 * Exponentiation operator.
 *
 * @author Adam
 */
public class Exponent extends Function {

    public Exponent() {
        super("^", Precedence.EXPONENTIATION, Associativity.RIGHT, false);
    }


    @Override
    public Function evaluate(Map<String, Function> varMap) {

        Function first = getFirstChild().evaluate(varMap);
        Function second = getSecondChild().evaluate(varMap);

        if (first instanceof Number && second instanceof Number) {
            Apfloat firstValue = (Apfloat)first.getValue();
            Apfloat secondValue = (Apfloat)second.getValue();
            Apfloat result = ApfloatMath.pow(firstValue, secondValue);
            return new Number(precisionRound(result));
        }

        if (second.equals(Number.ZERO)) {
            return Number.ONE;
        } else if (second.equals(Number.ONE)) {
            return first;
        } else if (first.equals(Number.ZERO)) {
            return Number.ZERO;
        } else if (first.equals(Number.ONE)) {
            return Number.ONE;
        }

        Function me = new Exponent();
        me.setFirstChild(first);
        me.setSecondChild(second);

        return me;
    }


    @Override
    public Function differentiateInternal(String var) {

        // Formula for d/dx(f(x)^g(x)):
        // f(x)^g(x)*d/dx(g(x))*ln(f(x))+f(x)^(g(x)-1)*g(x)*d/dx(f(x))

        Function first = getFirstChild().evaluate();
        Function second = getSecondChild().evaluate();

        Function firstDer = first.differentiateInternal(var);
        Function secondDer = second.differentiateInternal(var);

        Function exp1 = new Exponent();
        exp1.setFirstChild(first);
        exp1.setSecondChild(second);

        Function ln = new Ln();
        ln.setFirstChild(first);

        Function mult1 = new Multiply();
        mult1.setFirstChild(secondDer);
        mult1.setSecondChild(ln);

        Function mult2 = new Multiply();
        mult2.setFirstChild(exp1);
        mult2.setSecondChild(mult1);


        Function minus = new Minus();
        minus.setFirstChild(second);
        minus.setSecondChild(Number.ONE);

        Function exp2 = new Exponent();
        exp2.setFirstChild(first);
        exp2.setSecondChild(minus);

        Function mult3 = new Multiply();
        mult3.setFirstChild(second);
        mult3.setSecondChild(firstDer);

        Function mult4 = new Multiply();
        mult4.setFirstChild(exp2);
        mult4.setSecondChild(mult3);


        Function derivative = new Plus();
        derivative.setFirstChild(mult2);
        derivative.setSecondChild(mult4);

        return derivative;
    }
}
