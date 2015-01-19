package com.adamheins.function.tree;

public class FunctionParser {
    
    
    int bracketCounter;
    
    public FunctionParser() {
        bracketCounter = 0;
    }
    
    
    public Function parse(String functionString) {
        
        FunctionBuilder fb = new FunctionBuilder();
        
        for (int index = 0; index < functionString.length(); ) {
            if (Character.isWhitespace(functionString.charAt(index))) {
                index++;
            } else if (functionString.charAt(index) == '+') {
                fb.add(new Plus(), bracketCounter);
                index++;
            } else if (functionString.charAt(index) == '-') {
                
                if (fb.current == null) {
                    fb.add(new Negative(), bracketCounter);
                } else {
                    Function prev = fb.current.function;
                    if (prev instanceof Number || prev instanceof Variable) {
                        fb.add(new Minus(), bracketCounter);
                    } else {
                        fb.add(new Negative(), bracketCounter);
                    }
                }
                index++;
            } else if (functionString.charAt(index) == '*') {
                fb.add(new Multiply(), bracketCounter);
                index++;
            } else if (functionString.charAt(index) == '/') {
                fb.add(new Divide(), bracketCounter);
                index++;
            } else if (functionString.charAt(index) == '^') {
                fb.add(new Exponent(), bracketCounter);
                index++;
            } else if (substringAt(functionString, "sin", index)) {
                fb.add(new Sin(), bracketCounter);
                index += 3;
            } else if (substringAt(functionString, "cos", index)) {
                fb.add(new Cos(), bracketCounter);
                index += 3;
            } else if (substringAt(functionString, "tan", index)) {
                fb.add(new Tan(), bracketCounter);
                index += 3;
            } else if (substringAt(functionString, "ln", index)) {
                fb.add(new Ln(), bracketCounter);
                index += 2;
            } else if (substringAt(functionString, "log", index)) {
                fb.add(new Log("10"), bracketCounter);
                index += 3;
            } else if (functionString.charAt(index) == ')') {
                bracketCounter--;
                index++;         
            } else if (functionString.charAt(index) == '(') {
                bracketCounter++;
                index++;
            } else if (isNumber(functionString.charAt(index))) {
                int start = index;
                index++;
                while (index < functionString.length() && isNumber(functionString.charAt(index)))
                    index++;
                fb.add(new Number(functionString.substring(start, index)), bracketCounter);
            } else {
                fb.add(new Variable(functionString.substring(index, index + 1)), bracketCounter);
                index++;
            }
        }
        
        return fb.getFunction();
    }
    
    
    private static boolean substringAt(String str, String sub, int start) {
        if (start + sub.length() >= str.length())
            return false;
        return str.substring(start, start + sub.length()).equals(sub);
    }
    
    
    private static boolean isNumber(char ch) {
        return (Character.isDigit(ch) || ch == '.');
    }
}
