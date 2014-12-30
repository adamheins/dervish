package com.adamheins.function.tree;

import java.util.HashMap;
import java.util.Map;

/*
 * root issue
 * possible to add values without storing precedence?
 * difference between the way differentiation and evaluation is performed
 * possibly add a boolean negative field to variables OR simply add a unary neg operator
 * problem with using BigDecimal is that it doesn't support trig/log etc
 * 
 * possibly use some kind of FunctionIterator when parsing
 */

public class Main {

    public static void main(String[] args) {
        
        FunctionBuilder fb = new FunctionBuilder();
        
        fb.add(new Number("5"), 0);
        fb.add(new Plus(), 0);
        fb.add(new Number("2"), 0);
        fb.add(new Plus(), 0);
        fb.add(new Variable("x"), 0);
        
        Function function = fb.getRoot();
        
        Map<String, Function> varMap = new HashMap<>();
        varMap.put("x", new Number("3"));
        
        System.out.println(function.evaluate(varMap));
    }

}
