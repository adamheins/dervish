package com.adamheins.diff.function;

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
