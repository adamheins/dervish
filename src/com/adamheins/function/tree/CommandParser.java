package com.adamheins.function.tree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Thrown when a command is missing arguments.
 */
class MissingArgumentException extends Exception {
    
    private static final long serialVersionUID = -319843297438731309L;

    MissingArgumentException(String msg) {
        super(msg);
    }
}


/**
 * Thrown when the user attempts to use a variable that is not being used by the
 * program.
 */
class UnknownVariableException extends Exception {
    
    private static final long serialVersionUID = 7076065329659998594L;

    UnknownVariableException(String msg) {
        super(msg);
    }
}


/**
 * Thrown when the user attempts use the value of a variable when that variable
 * does not have one.
 */
class UndefinedVariableException extends Exception {

    private static final long serialVersionUID = 1070084253653017219L;

    UndefinedVariableException(String msg) {
        super(msg);
    }
}


/**
 * Thrown when setting a variable to a particular value would cause a cycle in 
 * defined variables.
 */
class CyclicVariableException extends Exception {

    private static final long serialVersionUID = -2720890315075878056L;

    CyclicVariableException(String msg) {
        super(msg);
    }
}


/**
 * Thrown when the user attempts to manipulate the LAST variable.
 */
class LastFunctionException extends Exception {
    
    private static final long serialVersionUID = -1728676203868117007L;

    LastFunctionException(String msg) {
        super(msg);
    }
}


/**
 * Thrown when a variable uses a reserved keyword as a name.
 */
class IllegalVariableNameException extends Exception {
    
    private static final long serialVersionUID = -988684259468261864L;

    IllegalVariableNameException(String msg) {
        super(msg);
    }
}


/**
 * Parses and interprets input for function scripts.
 * 
 * @author Adam
 */
public class CommandParser {
    
    // Map of user defined variables names and values.
    Map<String, Function> varMap;
    
    // List of variables that are being used by the program.
    List<String> varList;
    
    // Keywords, which cannot be the names of variables.
    private static final List<String> KEYWORDS = Arrays.asList("use", "forget", 
            "eval", "sub", "diff", "help", "exit", "set", "clear", "show", 
            "log", "ln", "sin", "cos", "tan");
    
    // String representing the 'last' variable.
    private final String LAST = "$";
    
    private String HELP_TEXT;
    
    
    CommandParser() {
        varMap = new HashMap<String, Function>();
        varList = new ArrayList<String>();
        varList.add(LAST);
        
        // Load text from help file.
        try {
            HELP_TEXT = (new String(Files.readAllBytes(Paths.get("help.txt")))).trim();
        } catch (IOException e) {
            HELP_TEXT = "Help file could not be loaded.";
        }
    }
    
    
    /**
     * Command for specifying variables that are being used by the program.
     * 
     * @param tokens List of tokens from the command string.
     * 
     * @return A string representing the evaluated function.
     * 
     * @throws MissingArgumentException If no variables are listed.
     * @throws IllegalVariableNameException If keywords are used as variable 
     *         names.
     */
    private void use(List<String> tokens) throws MissingArgumentException, 
            IllegalVariableNameException {
        
        if (tokens.size() < 2)
            throw new MissingArgumentException("Missing argument: use <variables(s)>.");
        
        tokens.remove(0);
        
        List<String> illegalVariables = new ArrayList<String>();
        
        // Add new variables to the list.
        for (String token : tokens) {
            if (isComposedOfLetters(token) && !KEYWORDS.contains(token))
                varList.add(token);
            else
                illegalVariables.add(token);
        }
        
        // Throw exception if keywords were used as variable names.
        if (!illegalVariables.isEmpty()) {
            String msg = "Variable names ";
            for (String var : illegalVariables)
                msg += "'" + var + "',";
            msg += "are reserved keywords and cannot be used as variables.";
            throw new IllegalVariableNameException(msg);
        }
    }
    
    
    /**
     * Command for forgetting variables that are being used by the program.
     * 
     * @param tokens List of tokens from the command string.
     * 
     * @return A string representing the evaluated function.
     * 
     * @throws MissingArgumentException
     */
    private void forget(List<String> tokens) throws MissingArgumentException {
        if (tokens.size() < 2)
            throw new MissingArgumentException("Missing argument: use <variables(s)>.");
        
        // Remove 'forget' token.
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
    }
    
    
    /**
     * Substitutes variables into a function.
     * 
     * @param tokens The list of tokens from the command string.
     * 
     * @return The evaluated function with all specified variables substituted 
     *         into it.
     * 
     * @throws MissingArgumentException If no function is given.
     * @throws UndefinedVariableException If the user attempts to sub in a 
     *         variable that has no value.
     * @throws ParsingException If the function fails to parse.
     */
    private String sub(List<String> tokens) throws MissingArgumentException, 
            UndefinedVariableException, ParsingException {
        
        if (tokens.size() == 1)
            throw new MissingArgumentException("Missing argument: sub <function>.");
        
        // Remove the 'sub' token.
        tokens.remove(0);
        
        FunctionParser fp = new FunctionParser(varList);
        Function function = fp.parse(tokens.get(0));
        
        // Remove the <function> token.
        tokens.remove(0);
        
        // Specifying no variables is the same as specifying 'all': all 
        // variables get subbed in.
        if (tokens.isEmpty())
            return function.evaluate(varMap).toString();
        if (tokens.get(0).equals("all"))
            return function.evaluate(varMap).toString();
        
        // Create a new map that is a subset of varMap, such that it contains 
        // only the variables indicated by the user.
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
    
    
    /**
     * Command for evaluating a function.
     * 
     * @param tokens List of tokens from the command string.
     * 
     * @return A string representing the evaluated function.
     * 
     * @throws MissingArgumentException If no function is given.
     * @throws ParsingException If the given function fails to parse.
     */
    private String eval(List<String> tokens) throws MissingArgumentException, ParsingException {
        
        if (tokens.size() == 1)
            throw new MissingArgumentException("Missing argument: eval <function>.");
        
        String funcStr = tokens.get(1);
        
        // Parse the function.
        FunctionParser fp = new FunctionParser(varList);
        Function function = fp.parse(funcStr);
        
        // If the LAST value exists, substitute it. The LAST variable is always substituted.
        if (varMap.containsKey(LAST))
            function = function.evaluate(getLastVariableMap());
        
        // Set the value of the LAST variable to this result.
        varMap.put(LAST, function);
        
        return function.toString();
    }
    
    
    /**
     * Command to differentiate a function with respect to a given variable.
     * 
     * @param tokens List of tokens from the command string.
     * 
     * @return A string representing the derivative of the function.
     * 
     * @throws MissingArgumentException If the function or variable are missing.
     * @throws ParsingException If the given function fails to parse.
     */
    private String diff(List<String> tokens) throws MissingArgumentException, ParsingException {
        
        if (tokens.size() < 3)
            throw new MissingArgumentException("Missing argument(s): diff <function> <variable>.");
        
        String funcStr = tokens.get(1);
        
        // Parse the function.
        FunctionParser parser = new FunctionParser(varList);
        Function function = parser.parse(funcStr);
        
        // Substitute the LAST variable into the function.
        if (varMap.containsKey(LAST))
            function = function.evaluate(getLastVariableMap());
        
        // Differentiate the function.
        Function derivative = function.differentiate(tokens.get(2));
        
        // Update LAST variable value.
        varMap.put(LAST, derivative);
        
        return derivative.toString();
    }
    
    
    /**
     * Command to differentiate a function with respect to a given variable.
     * 
     * @param tokens List of tokens from the command string.
     * 
     * @throws MissingArgumentException If the variable or assigned function is missing.
     * @throws UnknownVariableException If the user attempts to assign a variable that is not being
     *      being used by the program.
     * @throws ParsingException If the assigned function fails to parse.
     * @throws CyclicVariableException If the variable assignment would cause a cycle in the
     *      variable definitions.
     * @throws UndefinedVariableException If the user has also called the 'sub' command in the same
     *         line and has attempted to sub in variable with no value.
     * @throws LastFunctionException If the user attempted to clear the LAST variable.
     * @throws IllegalVariableNameException If the variable name is an illegal keyword.
     */
    private void set(List<String> tokens) throws MissingArgumentException, 
            UnknownVariableException, ParsingException, CyclicVariableException, 
            UndefinedVariableException, LastFunctionException, IllegalVariableNameException {
        
        if (tokens.size() < 3)
            throw new MissingArgumentException("Missing argument(s): set <variable> <function>.");
        
        if (!varList.contains(tokens.get(1)))
            throw new UnknownVariableException("Unknown variable! Declare variables with 'use <variable(s)>' first.");
        
        String var = tokens.get(1);
        String funcStr = tokens.get(2);
        
        // Evaluate subsequent commands. The 'set' command allows other commands to be chained after
        // it, such that the user can assign variables to a derivative of a function easily in
        // one line.
        if (tokens.size() > 3) {
            tokens.remove(0);
            tokens.remove(0);
            funcStr = parseCommand(tokens);
        }
        
        // Parse the function that is to be the variable's value.
        FunctionParser fp = new FunctionParser(varList);
        Function varValue = fp.parse(funcStr);

        // Check for potentional cycle in the variable map.
        VariableVerifier verifier = new VariableVerifier(varMap);
        if (!verifier.verify(var, varValue))
            throw new CyclicVariableException("Variable definition contains cycle.");
        
        varMap.put(var, varValue);
    }
    
    
    /**
     * Command to clear the values of all variables. This does not remove them from the list of
     * variables being used by the program. Use the 'forget' command for that.
     * 
     * @param tokens List of tokens from the command string.
     * 
     * @throws MissingArgumentException If no variables are specified to be cleared.
     * @throws LastFunctionException If the user tries to clear the value of the LAST variable.
     */
    private void clear(List<String> tokens) throws MissingArgumentException, 
            LastFunctionException {
        
        if (tokens.size() < 2)
            throw new MissingArgumentException("Missing argument: clear <variables(s)>.");
        
        // Remove the 'clear' token so we can iterate over all tokens later.
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
                throw new LastFunctionException("Cannot clear LAST variable ($).");
            for (String token : tokens) {
                varMap.remove(token);
            }
        }
    }
    
    
    /**
     * Creates a list of specified variables being used by the program and their values, if they
     * have any.
     * 
     * @param tokens List of tokens from the command string.
     * 
     * @return The list of variables being used by the program.
     */
    private String show(List<String> tokens) {
        
        // Response to the show command. Variables without values are listed first, and then
        // variables with values.
        String response = "";
        
        // No variables are defined, return empty string.
        if (varList.isEmpty())
            return "";
        
        // Add variables from the list, but that don't have values.
        if (tokens.size() == 1 || tokens.get(1).equals("all")) {
            for (String var : varList) {
                if (!varMap.containsKey(var))
                    response += var + "\n";
            }
        } else {
            for (String var : varList) {
                if (tokens.contains(var) && !varMap.containsKey(var))
                    response += var + "\n";
            }
        }
        
        // If the map is empty, we're done now.
        if (varMap.isEmpty())
            return response;

        // Add variables that have values and are in the map.
        if (tokens.size() == 1 || tokens.get(1).equals("all")) {
            for (Map.Entry<String, Function> entry : varMap.entrySet()) {
                response += entry.getKey() + " = " + entry.getValue() + "\n";
            }
        } else {
            for (Map.Entry<String, Function> entry : varMap.entrySet()) {
                if (tokens.contains(entry.getKey()))
                    response += entry.getKey() + " = " + entry.getValue() + "\n";
            }
        }

        return response;
    }
    
    
    /**
     * Parses the list of tokens and delegates to the correct function.
     * 
     * @param tokens List of tokens from the command string.
     * 
     * @return The response to the command.
     * 
     * @throws MissingArgumentException If arguments are missing from the command.
     * @throws UndefinedVariableException If a variable has no value where one is expected.
     * @throws ParsingException If a function fails to parse.
     * @throws UnknownVariableException If the user attempts to use a variable that is not in list
     *         of variables being used by the program.
     * @throws CyclicVariableException If a variable assignment would cause a cyclic relationship
     *         between variable definitions.
     * @throws LastFunctionException If the user attempts to modify or delete the LAST variable.
     * @throws IllegalVariableNameException If the user attempts to name a variable with a
     *         reserved word.
     */
    private String parseCommand(List<String> tokens) throws MissingArgumentException, 
            UndefinedVariableException, ParsingException, UnknownVariableException, 
            CyclicVariableException, LastFunctionException, IllegalVariableNameException {
        
        if (tokens.get(0).equals("use")) {
            use(tokens);
            return "";
        } else if (tokens.get(0).equals("forget")) {
            forget(tokens);
            return "";
        } else if (tokens.get(0).equals("sub")) {
            return sub(tokens);
        } else if (tokens.get(0).equals("eval")) {
            return eval(tokens);
        } else if (tokens.get(0).equals("diff")) {
            return diff(tokens);
        } else if (tokens.get(0).equals("set")) {
            set(tokens);
            return "";
        } else if (tokens.get(0).equals("clear")) {
            clear(tokens);
            return "";
        } else if (tokens.get(0).equals("show")) {
            return show(tokens);
        } else if (tokens.get(0).equals("help")) {
            return HELP_TEXT;
        } else if (tokens.get(0).equals("prec")) {
            
        }
        
        return "Unknown command.";
    }
    
    
    /**
     * Parses the command.
     * 
     * @param command The string to parse.
     * 
     * @return A response to the command.
     * 
     * @throws MissingArgumentException If arguments are missing from the command.
     * @throws UndefinedVariableException If a variable has no value where one is expected.
     * @throws ParsingException If a function fails to parse.
     * @throws UnknownVariableException If the user attempts to use a variable that is not in list
     *         of variables being used by the program.
     * @throws CyclicVariableException If a variable assignment would cause a cyclic relationship
     *         between variable definitions.
     * @throws LastFunctionException If the user attempts to modify or delete the LAST variable.
     * @throws IllegalVariableNameException If the user attempts to name a variable with a
     *         reserved word.
     */
    public String parse(String command) throws MissingArgumentException, UndefinedVariableException,
            ParsingException, UnknownVariableException, CyclicVariableException, 
            LastFunctionException, IllegalVariableNameException {
        
        List<String> tokens = stringArrayToList(command.toLowerCase().split(" "));
        
        if (tokens.size() == 0)
            return "";
        return parseCommand(tokens).trim();
    }
    
    
    /**
     * Creates a map with only the LAST variable.
     * 
     * @return The map containing only the LAST variable and its value.
     */
    private Map<String, Function> getLastVariableMap() {
        Map<String, Function> lastMap = new HashMap<String, Function>();
        lastMap.put(LAST, varMap.get(LAST));
        return lastMap;
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
    private static boolean isComposedOfLetters(String str) {
        for (int i = 0; i < str.length(); ++i) {
            if (!Character.isAlphabetic(str.charAt(i)))
                return false;
        }
        return true;
    }
}
