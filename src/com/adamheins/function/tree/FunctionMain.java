package com.adamheins.function.tree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
        
        // If there is an arg, it should be a file name.
        boolean fromFile = args.length > 0;
        
        // Create a reader for either stdin or a file.
        BufferedReader reader = null;
        if (fromFile)
            try {
                reader = new BufferedReader(new FileReader(args[0]));
            } catch (FileNotFoundException e) {
                System.out.println("File '" + args[0] + "' not found.");
            }
        else
            reader = new BufferedReader(new InputStreamReader(System.in));
        
        String input;
        CommandParser parser = new CommandParser();
        
        System.out.println("function shell");
        
        try {
            if (!fromFile)
                System.out.print("> ");
            while ((input = reader.readLine()) != null) {

                // Stop execution after user enters 'exit'.
                if (input.length() >= 4 && input.substring(0, 4).toLowerCase().equals("exit"))
                    break;
                try {
                    String result = parser.parse(input);
                    if (!result.equals(""))
                        System.out.println(result);
                } catch (MissingArgumentException e) {
                    System.out.println(e.getMessage());
                } catch (UndefinedVariableException e) {
                    System.out.println(e.getMessage());
                } catch (ParsingException e) {
                    System.out.println(e.getMessage());
                } catch (UnknownVariableException e) {
                    System.out.println(e.getMessage());
                } catch (CyclicVariableException e) {
                    System.out.println(e.getMessage());
                } catch (LastExpressionException e) {
                    System.out.println(e.getMessage());
                }
                if (!fromFile)
                    System.out.print("> ");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
