package com.adamheins.function.tree;

import java.util.HashMap;
import java.util.Map;

public class CommandParser {
    
    
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
        
        if (tokens.length < 2)
            return "Missing argument(s): diff <expression> <variable>.";
        
        FunctionParser parser = new FunctionParser();
        Function function = parser.parse(tokens[1]);
        return function.differentiate(tokens[2]).toString();
    }
    
    
    private String set(String[] tokens) {
        
        if (tokens.length < 2)
            return "Missing argument(s): <variable> <expression>.";
        
        FunctionParser fp = new FunctionParser();
        Function varValue = fp.parse(tokens[2]);
        varMap.put(tokens[1], varValue);
        return "";
    }
    
    
    private String clear(String[] tokens) {
        
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
        }
        
        return "Unknown command.";
    }
    
    
    public String parse(String command) {
        String response = parseCommand(command);
        if (response.equals(""))
            return response;
        return response + "\n";
    }
}
