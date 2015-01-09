package com.adamheins.function.tree;

import java.util.HashMap;
import java.util.Map;

/*
 * Change public facing differentiation function to always call evaluate()
 * Use Apfloat library for internal calculations.
 * Re-add polymorphic toString() behaviour.
 * Add polymorphic calculation behaviour.
 * Add an "exact" mode which avoids evaluating expressions that results in inexact values.
 * Use a FunctionBuilder to create differentiation trees.
 */

public class Main {

    public static void main(String[] args) {
        
        FunctionBuilder fb = new FunctionBuilder();
        
        fb.add(new Variable("x"), 0);
        fb.add(new Exponent(), 0);
        fb.add(new Number("2"), 0);
        //fb.add(new Exponent(), 0);
        //fb.add(new Number("2"), 0);
        //fb.add(new Multiply(), 0);
        //fb.add(new Variable("x"), 0);
        //fb.add(new Multiply(), 0);
        //fb.add(new Variable("y"), 0);
        
        Function function = fb.getFunction();
        
        Map<String, Function> varMap = new HashMap<>();
        varMap.put("x", new Number("1000"));

        System.out.println(function.differentiate("x").evaluate());
        //System.out.println(function.getVariables());
    }

}
