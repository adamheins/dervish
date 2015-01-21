package com.adamheins.function.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    
    List<String> varList;
    
    
    CommandParser() {
        varMap = new HashMap<String, Function>();
        varList = new ArrayList<String>();
    }
    
    
    private String use(String[] tokens) {
        if (tokens.length < 2)
            return "Missing argument: use <variables(s)>.";
        for (int i = 1; i < tokens.length; ++i) {
            varList.add(tokens[i]);
        }
        return "";
    }
    
    
    private String forget(String[] tokens) {
        if (tokens.length < 2)
            return "Missing argument: use <variables(s)>.";
        
        // Clear all variables.
        if (tokens[1].equals("all")) {
            varList = new ArrayList<String>();
            varMap = new HashMap<String, Function>();
        }
        
        // Clear individual variables.
        for (int i = 1; i < tokens.length; ++i) {
            varList.remove(tokens[i]);
            if (varMap.containsKey(tokens[i]))
                varMap.remove(tokens[i]);
        }
        
        return "";
    }
    
    
    private String sub(String[] tokens) {
        
        if (tokens.length == 1)
            return "Missing argument: eval <expression>.";
        
        FunctionParser fp = new FunctionParser(varList);
        Function function;
        try {
            function = fp.parse(tokens[1]);
        } catch (Exception e) {
            return "Parsing error.";
        }
        
        if (tokens.length == 2)
            return function.evaluate().toString();
        if (tokens[2].equals("all"))
            return function.evaluate(varMap).toString();
        
        Map<String, Function> varSubMap = new HashMap<String, Function>();
        for (int i = 2; i < tokens.length; ++i) {
            if (!varMap.containsKey(tokens[i]))
                return "Error! Variable '" + tokens[i] + "' has no value.";
            varSubMap.put(tokens[i], varMap.get(tokens[i]));
        }
        return function.evaluate(varSubMap).toString();
    }
    
    
    private String eval(String[] tokens) {
        
        if (tokens.length == 1)
            return "Missing argument: eval <expression>.";
        
        FunctionParser fp = new FunctionParser(varList);
        Function function;
        try {
            function = fp.parse(tokens[1]);
        } catch (Exception e) {
            return "Parsing error.";
        }
        return function.evaluate().toString();
    }
    
    
    private String diff(String[] tokens) {
        
        if (tokens.length < 3)
            return "Missing argument(s): diff <expression> <variable>.";
        
        FunctionParser parser = new FunctionParser(varList);
        Function function;
        try {
            function = parser.parse(tokens[1]);
        } catch (Exception e) {
            return "Parsing error.";
        }
        return function.differentiate(tokens[2]).toString();
    }
    
    
    private String set(String[] tokens) {
        
        if (tokens.length < 3)
            return "Missing argument(s): set <variable> <expression>.";
        
        // For ease of parsing, limit the variables to only a signal character in length.
        if (tokens[1].length() > 1)
            return "Variable names may only be a single character long.";
        
        if (!varList.contains(tokens[1]))
            return "Unknown variable! Declare variables with 'use <variable(s)>' first.";
        
        FunctionParser fp = new FunctionParser(varList);
        Function varValue;
        try {
            varValue = fp.parse(tokens[2]);
        } catch (Exception e) {
            return "Parsing error.";
        }
        
        // Disallow recursive variable definitions.
        if (varValue.toString().contains(tokens[1]))
            return "Variable value cannot contain itself.";
        
        // Check for potentional cycle in the variable map.
        VariableVerifier verifier = new VariableVerifier(varMap);
        if (!verifier.verify(tokens[1], varValue))
            return "Error: variable definition contains cycle.";
        
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
        
        if (varList.isEmpty())
            return "No variables defined.";
        
        String str = "Using:\n";
        for (String var : varList) {
            str += var + " ";
        }
        str += "\nValued:\n";

        if (tokens[1].equals("all")) {
            for (Map.Entry<String, Function> entry : varMap.entrySet()) {
                str += entry.getKey() + " = " + entry.getValue() + "\n"; //TODO need something better than blind \n
            }
        } else {
            Arrays.sort(tokens);
            for (Map.Entry<String, Function> entry : varMap.entrySet()) {
                if (Arrays.binarySearch(tokens, entry.getKey()) >= 0)
                    str += entry.getKey() + " = " + entry.getValue() + "\n";
            }
        }

        return str.trim();
    }
    
    
    private String parseCommand(String command) {
        
        String tokens[] = command.split(" ");
        
        if (tokens.length == 0)
            return "";
        
        if (tokens[0].equals("use")) {
            return use(tokens);
        } else if (tokens[0].equals("forget")) {
            return forget(tokens);
        } else if (tokens[0].equals("sub")) {
            return sub(tokens);
        } else if (tokens[0].equals("eval")) {
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
