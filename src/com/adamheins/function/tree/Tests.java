package com.adamheins.function.tree;

import org.junit.Assert;
import org.junit.Test;

public class Tests {
    
    
    @Test
    public void testBuildOneNumber() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("1"), 0);
        
        Function result = fb.getFunction();
        
        Assert.assertEquals("1", result.toString());
    }
    
    
    @Test
    public void testDifferentiateOneNumber() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("1"), 0);
        
        Function result = fb.getFunction().differentiate(null);
        
        Assert.assertEquals("0", result.toString());
    }
    
    
    @Test
    public void testDifferentiateOneVariable() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Variable("x"), 0);
        
        Function result = fb.getFunction().differentiate("x");
        
        Assert.assertEquals("1", result.toString());
    }

    
    @Test
    public void testAdditionIntegers() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("1"), 0);
        fb.add(new Plus(), 0);
        fb.add(new Number("2"), 0);
        
        Function result = fb.getFunction();
        
        Assert.assertEquals("3", result.toString());
    }
    
    
    @Test
    public void testAdditionFloats() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("1.5"), 0);
        fb.add(new Plus(), 0);
        fb.add(new Number("2.5"), 0);
        
        Function result = fb.getFunction();
        
        Assert.assertEquals("4", result.toString());
    }
    
    
    @Test
    public void testAdditionOneVariable() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("3"), 0);
        fb.add(new Plus(), 0);
        fb.add(new Variable("x"), 0);
        
        Function result = fb.getFunction();
        
        Assert.assertEquals("3+x", result.toString());
    }
    
    
    @Test
    public void testAdditionTwoVariables() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Variable("x"), 0);
        fb.add(new Plus(), 0);
        fb.add(new Variable("y"), 0);
        
        Function result = fb.getFunction();
        
        Assert.assertEquals("x+y", result.toString());
    }
    
    
    @Test
    public void testAdditionZeroTerm() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Variable("x"), 0);
        fb.add(new Plus(), 0);
        fb.add(new Number("0"), 0);
        
        Function result = fb.getFunction();
        
        Assert.assertEquals("x", result.toString());
    }
    
    
    @Test
    public void testAdditionTwoZeros() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("0"), 0);
        fb.add(new Plus(), 0);
        fb.add(new Number("0"), 0);
        
        Function result = fb.getFunction();
        
        Assert.assertEquals("0", result.toString());
    }
    
}
