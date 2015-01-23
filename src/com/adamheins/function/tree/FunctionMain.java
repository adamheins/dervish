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
        
        System.out.println("function shell");
        
        try {
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
                System.out.print("> ");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
