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
 */

public class FunctionMain {
        
    public static void main(String args[]) {
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        CommandParser parser = new CommandParser();
        
        try {
            while ((input = reader.readLine()) != null)
                System.out.print(parser.parse(input));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
