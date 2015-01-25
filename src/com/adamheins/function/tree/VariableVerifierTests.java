package com.adamheins.function.tree;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.Assert;

public class VariableVerifierTests {
    
    @Test
    public void testFalseWhenVariableIsItself() {
        String var = "x";

        Function func = new Variable(var);
        
        VariableVerifier verifier = new VariableVerifier(null);
        
        Assert.assertFalse(verifier.verify(var, func));
    }
    
    
    @Test
    public void testFalseWhenVariableContainsItself() {
        String var = "x";
        
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Variable(var), 0);
        fb.add(new Plus(), 0);
        fb.add(new Variable("y"), 0);
        Function func = fb.getFunction();
        
        VariableVerifier verifier = new VariableVerifier(null);
        
        Assert.assertFalse(verifier.verify(var, func));
    }
    
    
    @Test
    public void testFalseWithTwoVariableLoop() {
        String var1 = "x";
        String var2 = "y";

        Function func1 = new Variable("y");
        Function func2 = new Variable("x");
        
        Map<String, Function> varMap = new HashMap<String, Function>();
        varMap.put(var1, func1);
        
        VariableVerifier verifier = new VariableVerifier(varMap);
        
        Assert.assertFalse(verifier.verify(var2, func2));
    }
    
    
    @Test
    public void testFalseWithThreeVariableLoop() {
        String var1 = "x";
        String var2 = "y";
        String var3 = "z";

        Function func1 = new Variable("y");
        Function func2 = new Variable("z");
        Function func3 = new Variable("x");
        
        Map<String, Function> varMap = new HashMap<String, Function>();
        varMap.put(var1, func1);
        
        VariableVerifier verifier = new VariableVerifier(varMap);
        
        Assert.assertTrue(verifier.verify(var2, func2));
        varMap.put(var2, func2);
        
        Assert.assertFalse(verifier.verify(var3, func3));
    }
    
    
    @Test
    public void testTrueWithThreeVariables() {
        String var1 = "x";
        String var2 = "y";
        String var3 = "z";

        Function func1 = new Variable("y");
        Function func2 = new Variable("z");
        Function func3 = null;
        
        Map<String, Function> varMap = new HashMap<String, Function>();
        varMap.put(var1, func1);
        
        VariableVerifier verifier = new VariableVerifier(varMap);
        
        Assert.assertTrue(verifier.verify(var2, func2));
        varMap.put(var2, func2);
        
        Assert.assertTrue(verifier.verify(var3, func3));
    }
}
