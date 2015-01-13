package com.adamheins.function.tree;

import java.util.HashMap;
import java.util.Map;

/*
 * Re-add polymorphic toString() behaviour.
 * Add an "exact" mode which avoids evaluating expressions that results in inexact values.
 * Use a FunctionBuilder to create differentiation trees.
 * 
 * Add a field to Numbers containing an Apfloat value field.
 * Polymorphic way to check for number and do calculations
 * Simplify way to design each evaluation method.
 * 
 * Need a more robust equals method.
 */

public class Main {

    public static void main(String[] args) {
        
        FunctionBuilder fb = new FunctionBuilder();
        
        fb.add(new Number("5"), 0);
        fb.add(new Multiply(), 0);
        fb.add(new Variable("x"), 0);
        
        Function function = fb.getFunction();
        
        fb = new FunctionBuilder();
        fb.add(new Variable("x"), 0);
        fb.add(new Multiply(), 0);
        fb.add(new Number("5"), 0);
        
        Function function2 = fb.getFunction();
        
        Map<String, Function> varMap = new HashMap<>();
        varMap.put("x", new Number("1000"));

        System.out.println(function);
        System.out.println(function2);
        System.out.println(function.equals(function2));
    }

}
