package com.adamheins.diff.parser;

import java.util.List;

import com.adamheins.diff.builder.FunctionBuilder;
import com.adamheins.diff.function.*;
import com.adamheins.diff.function.Number;


/**
 * A parser that converts strings to Function objects.
 *
 * @author Adam
 */
public class FunctionParser {

    // List of variables that may appear in the function.
    List<String> varList;

    // Keeps track of how deep within sets of brackets the function currently
    // is. Equal to the number of open brackets minus the number of close
    // brackets that have occurred in the function so far.
    int bracketCounter;


    /**
     * Creates a new FunctionParser object.
     *
     * @param varList The list of variables that may appear in this function.
     */
    public FunctionParser(List<String> varList) {
        bracketCounter = 0;
        this.varList = varList;
    }


    /**
     * Parses a Function object from a string.
     *
     * @param functionString The string representing the function.
     *
     * @return The parsed Function object.
     *
     * @throws ParsingException If there is a syntax error in the function.
     */
    public Function parse(String functionString) throws ParsingException {

        FunctionBuilder fb = new FunctionBuilder();
        Function func = null;

        for (int index = 0; index < functionString.length(); ) {
            if (Character.isWhitespace(functionString.charAt(index))) {
                index++;
                continue;
            } else if (functionString.charAt(index) == '+') {
                func = new Plus();
                index++;
            } else if (functionString.charAt(index) == '-') {

                // Determine if '-' represents minus or negative.
                if (index == 0) {
                    func = new Negative();
                } else {
                    if (func instanceof Number || func instanceof Variable) {
                        func = new Minus();
                    } else {
                        func = new Negative();
                    }
                }
                index++;
            } else if (functionString.charAt(index) == '*') {
                func = new Multiply();
                index++;
            } else if (functionString.charAt(index) == '/') {
                func = new Divide();
                index++;
            } else if (functionString.charAt(index) == '^') {
                func = new Exponent();
                index++;
            } else if (substringAt(functionString, "sin", index)) {
                func = new Sin();
                index += 3;
            } else if (substringAt(functionString, "cos", index)) {
                func = new Cos();
                index += 3;
            } else if (substringAt(functionString, "tan", index)) {
                func = new Tan();
                index += 3;
            } else if (substringAt(functionString, "ln", index)) {
                func = new Ln();
                index += 2;
            } else if (substringAt(functionString, "log", index)) {
                func = new Log("10");
                index += 3;
            } else if (functionString.charAt(index) == ')') {
                bracketCounter--;
                index++;
                continue;
            } else if (functionString.charAt(index) == '(') {
                bracketCounter++;
                index++;
                continue;
            } else if (functionString.charAt(index) == 'e') {
                func = Constant.E;
                index++;
            } else if (substringAt(functionString, "pi", index)) {
                func = Constant.PI;
                index += 2;
            }else if (isNumber(functionString.charAt(index))) {
                int start = index;
                index++;
                while (index < functionString.length()
                        && isNumber(functionString.charAt(index)))
                    index++;
                func = new Number(functionString.substring(start, index));
            } else {
                boolean varflag = false;
                for (String var : varList) {
                    if (substringAt(functionString, var, index)) {
                        func = new Variable(functionString.substring(index,
                                index + var.length()));
                        index += var.length();
                        varflag = true;
                        break;
                    }
                }
                if (!varflag) {
                    throw new ParsingException("Unrecognized character <"
                           + functionString.charAt(index) + "> while parsing.");
                }
            }

            fb.add(func, bracketCounter);
        }

        // Check if brackets balance in the function.
        if (bracketCounter != 0)
            throw new ParsingException("Unbalanced brackets.");

        return fb.getFunction();
    }


    /**
     * Checks if the substring exists at the given place in the string.
     *
     * @param str The original string.
     * @param sub The substring.
     * @param start The starting index of the substring.
     *
     * @return True if the substring occurs at the start location, false
     *         otherwise.
     */
    private static boolean substringAt(String str, String sub, int start) {
        if (start + sub.length() > str.length())
            return false;
        return str.substring(start, start + sub.length()).equals(sub);
    }


    /**
     * Checks if a character could be part of a positive floating point value.
     *
     * @param ch The char to check.
     *
     * @return True if the character could be part of a number, false otherwise.
     */
    private static boolean isNumber(char ch) {
        return (Character.isDigit(ch) || ch == '.');
    }
}
