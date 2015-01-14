package com.adamheins.function.tree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
        
        /*
        FunctionBuilder fb = new FunctionBuilder();
        
        fb.add(new Number("2"), 0);
        fb.add(new Plus(), 0);
        fb.add(new Number("3"), 0);
        fb.add(new Multiply(), 0);
        fb.add(new Number("4"), 0);
        
        Function function = fb.getFunction();
        
        Map<String, Function> varMap = new HashMap<>();
        varMap.put("x", new Number("1000"));

        System.out.println(function.evaluate());*/
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        String input;
        FunctionParser parser = new FunctionParser();
        
        try {
            while ((input = reader.readLine()) != null)
                System.out.println(parser.parse(input));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
