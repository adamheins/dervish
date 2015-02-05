package com.adamheins.diff.parser;

import java.util.List;

import com.adamheins.diff.builder.FunctionBuilder;
import com.adamheins.diff.function.*;
import com.adamheins.diff.function.Number;


public class FunctionParser {
    
    List<String> varList;
    int bracketCounter;


    public FunctionParser(List<String> varList) {
        bracketCounter = 0;
        this.varList = varList;
    }
    
    
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
            } else if (isNumber(functionString.charAt(index))) {
                int start = index;
                index++;
                while (index < functionString.length() && isNumber(functionString.charAt(index)))
                    index++;
                func = new Number(functionString.substring(start, index));
            } else {
                boolean flag = false;
                for (String var : varList) {
                    if (substringAt(functionString, var, index)) {
                        func = new Variable(functionString.substring(index, index + var.length()));
                        index += var.length();
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    throw new ParsingException("Unrecognized character <" + functionString.charAt(index) + "> while parsing.");
                }
            }

            fb.add(func, bracketCounter);
        }
        return fb.getFunction();
    }
    
    
    private static boolean substringAt(String str, String sub, int start) {
        if (start + sub.length() > str.length())
            return false;
        return str.substring(start, start + sub.length()).equals(sub);
    }
    
    
    private static boolean isNumber(char ch) {
        return (Character.isDigit(ch) || ch == '.');
    }
}
