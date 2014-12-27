package com.adamheins.function.tree;

import java.util.HashMap;
import java.util.Map;

/*
 * 1. Zero terms need to be handled:
 * - if parent of a zero is a multiply operation, we can replace the whole multiply operation
 *   with a zero
 * - if parent of a zero is a plus/minus operation, we can replace the whole operation with the
 *   non-zero term
 *   
 * 2. Evaluate and toString() would once again be one and the same. Evaluate would give a string,
 *    which is the most simplified version of the function, given the Map of variables
 * 
 */

public class Main {

    public static void main(String[] args) {
        
        ExpressionTree expression = new ExpressionTree();
        expression.add(new Number("5", 0));
        expression.add(new Multiply(0));
        expression.add(new Number("2", 0));
        expression.add(new Plus(0));
        expression.add(new Variable("x", 0));
        
        Map<String, Double> varMap = new HashMap<>();
        varMap.put("x", 3.0);
        
        ExpressionTree result = expression.differentiate("x");
        System.out.println(result);
    }

}
