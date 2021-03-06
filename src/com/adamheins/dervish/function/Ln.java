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
 * Natural logarithm operator.
 *
 * @author Adam
 */
public class Ln extends Log {

    public Ln() {
        super(Constant.E.getValue().toString(), "ln");
    }


    @Override
    public Function evaluate(Map<String, Function> varMap) {

        Function child = getFirstChild().evaluate(varMap);

        if (child instanceof Number) {
            Apfloat result = ApfloatMath.log(((Apfloat)child.getValue()));
            return new Number(precisionRound(result));
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
