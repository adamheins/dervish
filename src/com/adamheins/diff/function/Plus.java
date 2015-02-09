package com.adamheins.diff.function;

import java.util.Map;

import org.apfloat.Apfloat;

/**
 * Addition operator.
 *
 * @author Adam
 */
public class Plus extends Function {

    public Plus() {
        super("+", Precedence.ADDITION, Associativity.LEFT, true);
    }


    @Override
    public Function evaluate(Map<String, Function> varMap) {

        // Evaluate children.
        Function first = getFirstChild().evaluate(varMap);
        Function second = getSecondChild().evaluate(varMap);

        // Check for number children, and evaluate.
        if (first instanceof Number && second instanceof Number) {
            Apfloat firstVal = (Apfloat)first.getValue();
            Apfloat secondVal = (Apfloat)second.getValue();
            return new Number(firstVal.add(secondVal));
        }

        // Get rid of unnecessary zero terms.
        if (first.equals(Number.ZERO))
            return second;
        if (second.equals(Number.ZERO))
            return first;

        // Return copy of the Plus.
        Function me = new Plus();
        me.setFirstChild(first);
        me.setSecondChild(second);

        return me;
    }


    @Override
    public Function differentiateInternal(String var) {

        Function derivative = new Plus();
        derivative.setFirstChild(getFirstChild().differentiateInternal(var));
        derivative.setSecondChild(getSecondChild().differentiateInternal(var));
        return derivative;
    }
}
