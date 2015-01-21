package com.adamheins.function.tree;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String args[]) {
        FunctionParser fp = new FunctionParser();
        Function func1 = fp.parse("x^2");
        fp = new FunctionParser();
        Function func2 = fp.parse("y^2");
        
        Map<String, Function> varMap = new HashMap<String, Function>();
        VariableVerifier vv = new VariableVerifier(varMap);
        System.out.println(vv.verify("y", func1));
        varMap.put("y", func1);
        System.out.println(vv.verify("x", func2));
    }
}
