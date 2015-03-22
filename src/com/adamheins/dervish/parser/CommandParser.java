/*
 * Copyright (c) 2015 Adam Heins
 *
 * This file is part of the Dervish project, which is distributed under the MIT
 * license. For the full terms, see the included LICENSE file.
 */

package com.adamheins.dervish.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adamheins.dervish.function.Function;


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
            "log", "ln", "sin", "cos", "tan", "e", "pi");

    // String representing the 'last' variable.
    private final String LAST = "$";

    // Text to display when the user enters 'help' in the console.
    private String HELP_TEXT;


    /**
     * Creates a new CommandParser object.
     */
    public CommandParser() {
        varMap = new HashMap<String, Function>();
        varList = new ArrayList<String>();
        varList.add(LAST);

        // Load text from help file.
        try {
            HELP_TEXT = (new String(Files.readAllBytes(Paths.get("res/help.txt"))))
                    .trim();
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
     * @throws ParsingException
     */
    private void use(List<String> tokens) throws ParsingException {

        if (tokens.size() < 2)
            throw new ParsingException("Missing argument: use <variables(s)>.");

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
            String msg = "Variable name(s) ";
            for (String var : illegalVariables)
                msg += "'" + var + "',";
            msg += " are reserved keywords and cannot be used as variables.";
            throw new ParsingException(msg);
        }
    }


    /**
     * Command for forgetting variables that are being used by the program.
     *
     * @param tokens List of tokens from the command string.
     *
     * @return A string representing the evaluated function.
     *
     * @throws ParsingException
     */
    private void forget(List<String> tokens) throws ParsingException {
        if (tokens.size() < 2)
            throw new ParsingException("Missing argument: use <variables(s)>.");

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
     * @throws ParsingException
     */
    private String sub(List<String> tokens) throws ParsingException {

        if (tokens.size() == 1)
            throw new ParsingException("Missing argument: sub <function>.");

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
                throw new ParsingException("Variable '" + tokens.get(i)
                        + "' has no value.");
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
     * @throws ParsingException
     */
    private String eval(List<String> tokens) throws ParsingException {

        if (tokens.size() == 1)
            throw new ParsingException("Missing argument: eval <function>.");

        String funcStr = tokens.get(1);

        // Parse the function.
        FunctionParser fp = new FunctionParser(varList);
        Function function = fp.parse(funcStr);

        // If the LAST value exists, substitute it. The LAST variable is always
        // substituted.
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
     * @throws ParsingException
     */
    private String diff(List<String> tokens) throws ParsingException {

        if (tokens.size() < 3)
            throw new ParsingException("Missing argument(s): diff <function>"
                    + " <variable>.");

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
     * @throws ParsingException
     */
    private void set(List<String> tokens) throws ParsingException {

        if (tokens.size() < 3)
            throw new ParsingException("Missing argument(s): set <variable>"
                    + " <function>.");

        if (!varList.contains(tokens.get(1)))
            throw new ParsingException("Unknown variable! Declare variables"
                    + " with 'use <variable(s)>' first.");

        String var = tokens.get(1);
        String funcStr = tokens.get(2);

        // Evaluate subsequent commands. The 'set' command allows other commands
        // to be chained after it, such that the user can assign variables to a
        // derivative of a function easily in
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
            throw new ParsingException("Variable definition contains cycle.");

        varMap.put(var, varValue);
    }


    /**
     * Command to clear the values of all variables. This does not remove them
     * from the list of variables being used by the program. Use the 'forget'
     * command for that.
     *
     * @param tokens List of tokens from the command string.
     *
     * @throws ParsingException
     */
    private void clear(List<String> tokens) throws ParsingException {

        if (tokens.size() < 2)
            throw new ParsingException("Missing argument: clear"
                    + " <variables(s)>.");

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
                throw new ParsingException("Cannot clear LAST variable ($).");
            for (String token : tokens) {
                varMap.remove(token);
            }
        }
    }


    /**
     * Creates a list of specified variables being used by the program and their
     * values, if they have any.
     *
     * @param tokens List of tokens from the command string.
     *
     * @return The list of variables being used by the program.
     */
    private String show(List<String> tokens) {

        // Response to the show command. Variables without values are listed
        // first, and then variables with values.
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
                    response += entry.getKey() + " = " + entry.getValue()
                            + "\n";
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
     * @throws ParsingException
     */
    private String parseCommand(List<String> tokens) throws ParsingException {

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
     * @throws ParsingException If the commands do not parse correctly.
     */
    public String parse(String command) throws ParsingException {

        List<String> tokens = stringArrayToList(command.toLowerCase()
                .split(" "));

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
