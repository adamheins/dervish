package com.adamheins.function.tree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * Re-add polymorphic toString() behaviour.
 * Use a FunctionBuilder to create differentiation trees.
 * Add a field to Numbers containing an Apfloat value field.
 */


/**
 * Entry point for the interactive program.
 * 
 * @author Adam
 */
public class FunctionMain {
        
    public static void main(String args[]) {
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        CommandParser parser = new CommandParser();
        
        System.out.println("Function Shell started:");
        
        try {
            while ((input = reader.readLine()) != null) {
                
                // Stop execution after user enters 'exit'.
                if (input.length() >= 4 && input.substring(0, 4).equals("exit"))
                    break;
                String result = parser.parse(input);
                if (!result.equals(""))
                    System.out.println(result);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
