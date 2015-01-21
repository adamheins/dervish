package com.adamheins.function.tree;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*
 * use & forget commands
 * help command
 * 
 * chain commands - parse the command backwards
 * 
 * sub and eval as distinct commands
 *  eval <expression> sub <variable(s)/all>
 */

public class CommandParser {
    
    // Map of user defined variables names and values.
    Map<String, Function> varMap;
    
    
    CommandParser() {
        varMap = new HashMap<String, Function>();
    }
    
    
    private String eval(String[] tokens) {
        
        if (tokens.length == 1)
            return "Missing argument: eval <expression>.";
        
        FunctionParser fp = new FunctionParser();
        Function function = fp.parse(tokens[1]);
        return function.evaluate(varMap).toString();
    }
    
    
    private String diff(String[] tokens) {
        
        if (tokens.length < 3)
            return "Missing argument(s): diff <expression> <variable>.";
        
        FunctionParser parser = new FunctionParser();
        Function function = parser.parse(tokens[1]);
        return function.differentiate(tokens[2]).toString();
    }
    
    
    private String set(String[] tokens) {
        
        if (tokens.length < 3)
            return "Missing argument(s): set <variable> <expression>.";
        
        // For ease of parsing, limit the variables to only a signal character in length.
        if (tokens[1].length() > 1)
            return "Variable names may only be a single character long.";
        
        FunctionParser fp = new FunctionParser();
        Function varValue = fp.parse(tokens[2]);
        
        // Disallow recursive variable definitions.
        if (varValue.toString().contains(tokens[1]))
            return "Variable value cannot contain itself.";
        
        //TODO cycle detection in variable values
        
        varMap.put(tokens[1], varValue);
        return "";
    }
    
    
    private String clear(String[] tokens) {
        
        if (tokens.length < 2)
            return "Missing argument: clear <variables(s)>.";
        
        // Remove all variables from the list.
        if (tokens[1].equals("all")) {
            varMap = new HashMap<String, Function>();
            
        // Remove all specified variables from the list.
        } else {
            for (int i = 1; i < tokens.length; i++)
                varMap.remove(tokens[i]);
        }
        return "";
    }
    
    
    private String show(String[] tokens) {
        
        if (tokens.length < 2)
            return "Missing argument: show <variables(s)>.";
        
        String varList = "";
        if (tokens[1].equals("all")) {
            for (Map.Entry<String, Function> entry : varMap.entrySet()) {
                varList += entry.getKey() + " = " + entry.getValue() + "\n"; //TODO need something better than blind \n
            }
        } else {
            Arrays.sort(tokens);
            for (Map.Entry<String, Function> entry : varMap.entrySet()) {
                if (Arrays.binarySearch(tokens, entry.getKey()) >= 0)
                    varList += entry.getKey() + " = " + entry.getValue() + "\n";
            }
        }
        if (varList.equals(""))
            return "No variables defined.";
        return varList;
    }
    
    
    private String parseCommand(String command) {
        
        String tokens[] = command.split(" ");
        
        if (tokens.length == 0)
            return "";
        
        if (tokens[0].equals("eval")) {
            return eval(tokens);
        } else if (tokens[0].equals("diff")) {
            return diff(tokens);
        } else if (tokens[0].equals("set")) {
            return set(tokens);
        } else if (tokens[0].equals("clear")) {
            return clear(tokens);
        } else if (tokens[0].equals("show")) {
            return show(tokens);
        }
        
        return "Unknown command.";
    }
    
    
    public String parse(String command) {
        return parseCommand(command);
    }
}
