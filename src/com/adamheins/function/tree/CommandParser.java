package com.adamheins.function.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * help command
 * 
 * remember last command
 * bash style arrow kets
 * 
 * sub command may require more thought
 * optional brackets to surround expression containing spaces (hard)
 */

class MissingArgumentException extends Exception {
    
    private static final long serialVersionUID = -319843297438731309L;

    MissingArgumentException(String msg) {
        super(msg);
    }
}


class UnknownVariableException extends Exception {
    
    private static final long serialVersionUID = 7076065329659998594L;

    UnknownVariableException(String msg) {
        super(msg);
    }
}


class UndefinedVariableException extends Exception {

    private static final long serialVersionUID = 1070084253653017219L;

    UndefinedVariableException(String msg) {
        super(msg);
    }
}


class CyclicVariableException extends Exception {

    private static final long serialVersionUID = -2720890315075878056L;

    CyclicVariableException(String msg) {
        super(msg);
    }
}


class LastExpressionException extends Exception {
    
    private static final long serialVersionUID = -1728676203868117007L;

    LastExpressionException(String msg) {
        super(msg);
    }
}



class KeywordException extends Exception {
    
    private static final long serialVersionUID = -8715613918367932255L;

    KeywordException(String msg) {
        super(msg);
    }
}


public class CommandParser {
    
    // Map of user defined variables names and values.
    Map<String, Function> varMap;
    
    // List of variables that are being used by the program.
    List<String> varList;
    
    // Keywords, which cannot be the names of variables.
    private static final List<String> KEYWORDS = Arrays.asList("use", "forget", "eval", "sub", 
            "diff", "help", "exit", "set", "clear", "show", "log", "ln", "sin", "cos", "tan");
    
    // String representing the 'last' variable.
    private final String LAST = "$";
    
    CommandParser() {
        varMap = new HashMap<String, Function>();
        varList = new ArrayList<String>();
        varList.add(LAST);
    }
    
    
    private String use(List<String> tokens) throws MissingArgumentException {
        if (tokens.size() < 2)
            throw new MissingArgumentException("Missing argument: use <variables(s)>.");
        tokens.remove(0);
        
        // Add new variables to the list.
        for (String token : tokens)
            if (isComposedOfLetters(token) && !KEYWORDS.contains(token))
                varList.add(token);
        return "";
    }
    
    
    private String forget(List<String> tokens) throws MissingArgumentException {
        if (tokens.size() < 2)
            throw new MissingArgumentException("Missing argument: use <variables(s)>.");
        tokens.remove(0);
        
        // Clear all variables.
        if (tokens.get(0).equals("all")) {
            varList = new ArrayList<String>();
            varList.add(LAST);
            varMap = new HashMap<String, Function>();
        }
        
        // Clear individual variables.
        for (String token : tokens) {
            if (!token.equals(LAST)) {
                varList.remove(token);
                if (varMap.containsKey(token))
                    varMap.remove(token);
            }
        }
        
        return "";
    }
    
    
    private String sub(List<String> tokens) throws MissingArgumentException, 
            UndefinedVariableException, ParsingException {
        
        if (tokens.size() == 1)
            throw new MissingArgumentException("Missing argument: eval <expression>.");
        
        tokens.remove(0);
        
        FunctionParser fp = new FunctionParser(varList);
        Function function = fp.parse(tokens.get(0));
        
        tokens.remove(0);
        
        if (tokens.isEmpty())
            return function.evaluate().toString();
        if (tokens.get(0).equals("all"))
            return function.evaluate(varMap).toString();
        
        Map<String, Function> varSubMap = new HashMap<String, Function>();
        for (int i = 0; i < tokens.size(); ++i) {
            if (!varMap.containsKey(tokens.get(i)))
                throw new UndefinedVariableException("Variable '" + tokens.get(i) + "' has no value.");
            varSubMap.put(tokens.get(i), varMap.get(tokens.get(i)));
        }
        function = function.evaluate(varSubMap);
        varMap.put(LAST, function);
        return function.toString();
    }
    
    
    private String eval(List<String> tokens) throws MissingArgumentException, ParsingException {
        
        if (tokens.size() == 1)
            throw new MissingArgumentException("Missing argument: eval <expression>.");
        
        tokens.remove(0);
        
        String exprStr = tokens.get(0);
        
        FunctionParser fp = new FunctionParser(varList);
        Function function = fp.parse(exprStr);
        if (varMap.containsKey(LAST)) {
            Map<String, Function> lastMap = new HashMap<String, Function>();
            lastMap.put(LAST, varMap.get(LAST));
            function = function.evaluate(lastMap);
        }
        varMap.put(LAST, function);
        return function.toString();
    }
    
    
    private String diff(List<String> tokens) throws MissingArgumentException, ParsingException {
        
        if (tokens.size() < 3)
            throw new MissingArgumentException("Missing argument(s): diff <expression> <variable>.");
        
        tokens.remove(0);
        
        String exprStr = tokens.get(0);
        
        FunctionParser parser = new FunctionParser(varList);
        Function function = parser.parse(exprStr);
        Function derivative = function.differentiate(tokens.get(1));
        varMap.put(LAST, derivative);
        return derivative.toString();
    }
    
    
    private String set(List<String> tokens) throws MissingArgumentException, 
            UnknownVariableException, ParsingException, CyclicVariableException, 
            UndefinedVariableException, LastExpressionException {
        
        if (tokens.size() < 3)
            throw new MissingArgumentException("Missing argument(s): set <variable> <expression>.");
        
        if (!varList.contains(tokens.get(1)))
            throw new UnknownVariableException("Unknown variable! Declare variables with 'use <variable(s)>' first.");
        
        String var = tokens.get(1);
        String funcStr = tokens.get(2);
        
        if (tokens.size() > 3) {
            tokens.remove(0);
            tokens.remove(0);
            funcStr = parseCommand(tokens);
        }
        
        FunctionParser fp = new FunctionParser(varList);
        Function varValue = fp.parse(funcStr);
        
        // Disallow recursive variable definitions.
        if (varValue.toString().contains(var))
            throw new CyclicVariableException("Variable value cannot contain itself.");
        
        // Check for potentional cycle in the variable map.
        VariableVerifier verifier = new VariableVerifier(varMap);
        if (!verifier.verify(var, varValue))
            throw new CyclicVariableException("Variable definition contains cycle.");
        
        varMap.put(var, varValue);
        return "";
    }
    
    
    private String clear(List<String> tokens) throws MissingArgumentException, LastExpressionException {
        
        if (tokens.size() < 2)
            throw new MissingArgumentException("Missing argument: clear <variables(s)>.");
        tokens.remove(0);
        
        // Remove all variables from the list.
        if (tokens.get(0).equals("all")) {
            if (varMap.containsKey(LAST)) {
                Function function = varMap.get(LAST);
                varMap.clear();
                varMap.put(LAST, function);
            } else {
                varMap.clear();
            }
            
        // Remove all specified variables from the list.
        } else {
            if (tokens.contains(LAST))
                throw new LastExpressionException("Cannot clear last expression variable ($).");
            for (String token : tokens) {
                varMap.remove(token);
            }
        }
        return "";
    }
    
    
    private String show(List<String> tokens) throws MissingArgumentException {
        
        //if (tokens.size() < 2)
        //    throw new MissingArgumentException("Missing argument: show <variables(s)>.");
        
        // No variables are defined, return empty string.
        if (varList.isEmpty())
            return "";
        
        String str = "Using:\n";
        for (String var : varList) {
            str += var + " ";
        }
        if (varMap.isEmpty())
            return str;
        
        str += "\nValued:\n";

        if (tokens.size() == 1 || tokens.get(1).equals("all")) {
            for (Map.Entry<String, Function> entry : varMap.entrySet()) {
                str += entry.getKey() + " = " + entry.getValue() + "\n";
            }
        } else {
            for (Map.Entry<String, Function> entry : varMap.entrySet()) {
                if (tokens.contains(entry.getKey()))
                    str += entry.getKey() + " = " + entry.getValue() + "\n";
            }
        }

        return str.trim();
    }
    
    
    private String parseCommand(List<String> tokens) throws MissingArgumentException, 
            UndefinedVariableException, ParsingException, UnknownVariableException, 
            CyclicVariableException, LastExpressionException {
        
        if (tokens.get(0).equals("use")) {
            return use(tokens);
        } else if (tokens.get(0).equals("forget")) {
            return forget(tokens);
        } else if (tokens.get(0).equals("sub")) {
            return sub(tokens);
        } else if (tokens.get(0).equals("eval")) {
            return eval(tokens);
        } else if (tokens.get(0).equals("diff")) {
            return diff(tokens);
        } else if (tokens.get(0).equals("set")) {
            return set(tokens);
        } else if (tokens.get(0).equals("clear")) {
            return clear(tokens);
        } else if (tokens.get(0).equals("show")) {
            return show(tokens);
        } else if (tokens.get(0).equals("help")) {
            return "not implemented";
        }
        
        return "Unknown command.";
    }
    
    
    public String parse(String command) throws MissingArgumentException, UndefinedVariableException,
            ParsingException, UnknownVariableException, CyclicVariableException, 
            LastExpressionException {
        
        List<String> tokens = stringArrayToList(command.toLowerCase().split(" "));
        
        if (tokens.size() == 0)
            return "";
        return parseCommand(tokens);
    }
    
    
    /**
     * Converts an array of strings to a mutable list.
     * 
     * @param arr The array of strings.
     * 
     * @return A list containing all elements of the array, in order.
     */
    private static List<String> stringArrayToList(String[] arr) {
        List<String> list = new ArrayList<String>();
        for (String ele : arr)
            list.add(ele);
        return list;
    }
    
    
    /**
     * Checks if a string is composed of letters.
     * 
     * @param str The string to check.
     * 
     * @return True if the string is composed of letters, false otherwise.
     */
    private boolean isComposedOfLetters(String str) {
        for (int i = 0; i < str.length(); ++i) {
            if (!Character.isAlphabetic(str.charAt(i)))
                return false;
        }
        return true;
    }
}
