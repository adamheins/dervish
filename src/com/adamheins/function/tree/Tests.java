package com.adamheins.function.tree;

import org.junit.Assert;
import org.junit.Test;

public class Tests {
    
    
    @Test
    public void testBuildOneNumber() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("1"), 0);
        
        Function expected = fb.getFunction();
        Function actual = new Number("1");
        
        Assert.assertTrue(expected.equals(actual));
    }
    
    
    @Test
    public void testDifferentiateOneNumber() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("1"), 0);
        
        Function expected = fb.getFunction().differentiate(null);
        Function actual = new Number("0");
        
        Assert.assertTrue(expected.equals(actual));
    }
    
    
    @Test
    public void testDifferentiateOneVariable() {
        Function function = new Variable("x");
        
        Function expectedDerivative = function.differentiate("x");
        Function actualDerivative = new Number("1");
        
        Assert.assertTrue(expectedDerivative.equals(actualDerivative));
    }
    
    
    @Test
    public void testDifferentiateSimplePolynomial() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Variable("x"), 0);
        fb.add(new Exponent(), 0);
        fb.add(new Number("2"), 0);
        
        Function actual = fb.getFunction().differentiate("x");
        
        fb = new FunctionBuilder();
        fb.add(new Number("2"), 0);
        fb.add(new Multiply(), 0);
        fb.add(new Variable("x"), 0);
        
        Function expected = fb.getFunction();
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testDifferentiateAddition() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Variable("x"), 0);
        fb.add(new Plus(), 0);
        fb.add(new Variable("x"), 0);
        
        Function actual = fb.getFunction().differentiate("x");
        Function expected = new Number("2");
        
        Assert.assertEquals(expected, actual);
    }

    
    @Test
    public void testAdditionIntegers() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("1"), 0);
        fb.add(new Plus(), 0);
        fb.add(new Number("2"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("3");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testAdditionFloats() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("1.5"), 0);
        fb.add(new Plus(), 0);
        fb.add(new Number("2.5"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("4");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testAdditionOneVariable() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("3"), 0);
        fb.add(new Plus(), 0);
        fb.add(new Variable("x"), 0);
        
        // Using strings to compare results in this test since the function is not actual being
        // simplified by getting evaluated. It would not make sense to compare the function
        // to itself.
        String actual = fb.getFunction().toString();
        String expected = "3+x";
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testAdditionTwoVariables() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Variable("x"), 0);
        fb.add(new Plus(), 0);
        fb.add(new Variable("y"), 0);
        
        String actual = fb.getFunction().toString();
        String expected = "x+y";
        
        Assert.assertEquals(expected, actual);
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
        
        Function actual = fb.getFunction();
        Function expected = new Number("0");
        
        Assert.assertEquals(actual, expected);
    }
    
    
    @Test
    public void testMinusIntegers() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("2"), 0);
        fb.add(new Minus(), 0);
        fb.add(new Number("3"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("-1");
        
        Assert.assertEquals(actual, expected);
    }
    
    
    @Test
    public void testMinusFloats() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("4.2"), 0);
        fb.add(new Minus(), 0);
        fb.add(new Number("1.5"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("2.7");
        
        Assert.assertEquals(actual, expected);
    }
    
    
    @Test
    public void testMultiplyIntegers() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("2"), 0);
        fb.add(new Multiply(), 0);
        fb.add(new Number("3"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("6");
        
        Assert.assertEquals(actual, expected);
    }
    
    
    @Test
    public void testMultiplyZero() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("2"), 0);
        fb.add(new Multiply(), 0);
        fb.add(new Number("0"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("0");
        
        Assert.assertEquals(actual, expected);
    }
    
    
    @Test
    public void testMultiplyFloats() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("2.5"), 0);
        fb.add(new Multiply(), 0);
        fb.add(new Number("3.1"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("7.75");
        
        Assert.assertEquals(actual, expected);
    }
    
    
    @Test
    public void testDivideIntegers() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("6"), 0);
        fb.add(new Divide(), 0);
        fb.add(new Number("3"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("2");
        
        Assert.assertEquals(actual, expected);
    }
    
    
    @Test
    public void testDivideIntegersNonMultiples() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("15"), 0);
        fb.add(new Divide(), 0);
        fb.add(new Number("10"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("1.5");
        
        Assert.assertEquals(actual, expected);
    }
    
    
    @Test
    public void testDivideFloats() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("6.5"), 0);
        fb.add(new Divide(), 0);
        fb.add(new Number("2.5"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("2.6");
        
        Assert.assertEquals(actual, expected);
    }
    
    
    @Test
    public void testExponentIntegers() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("2"), 0);
        fb.add(new Exponent(), 0);
        fb.add(new Number("3"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("8");
        
        Assert.assertEquals(actual, expected);
    }
    
    
    @Test
    public void testExponentBaseOne() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("1"), 0);
        fb.add(new Exponent(), 0);
        fb.add(new Number("5"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("1");
        
        Assert.assertEquals(actual, expected);
    }
    
    
    @Test
    public void testExponentBaseZero() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("0"), 0);
        fb.add(new Exponent(), 0);
        fb.add(new Number("5"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("0");
        
        Assert.assertEquals(actual, expected);
    }
    
    
    @Test
    public void testExponentZero() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("4"), 0);
        fb.add(new Exponent(), 0);
        fb.add(new Number("0"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("1");
        
        Assert.assertEquals(actual, expected);
    }
    
    
    @Test
    public void testExponentFloats() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("1.5"), 0);
        fb.add(new Exponent(), 0);
        fb.add(new Number("4.0"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("5.0625");
        
        Assert.assertEquals(actual, expected);
    }
    
    
    @Test
    public void testLog10() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Log("10"), 0);
        fb.add(new Number("1000"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("3");
        
        Assert.assertEquals(actual, expected);
    }
    
    
    @Test
    public void testNegativeAndNumber() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Negative(), 0);
        fb.add(new Number("10"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("-10");
        
        Assert.assertEquals(actual, expected);
    }
    
    
    @Test
    public void testNegativeAfterOperator() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("2"), 0);
        fb.add(new Multiply(), 0);
        fb.add(new Negative(), 0);
        fb.add(new Number("10"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("-20");
        
        Assert.assertEquals(actual, expected);
    }
    
    
    /* Parsing tests */
    
    @Test
    public void testParseInteger() {
        String math = "34";
        FunctionParser fp = new FunctionParser();
        Function actual = fp.parse(math);
        Function expected = new Number("34");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseFloat() {
        String math = "12.345";
        FunctionParser fp = new FunctionParser();
        Function actual = fp.parse(math);
        Function expected = new Number("12.345");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseNegativeNumber() {
        String math = "-156";
        FunctionParser fp = new FunctionParser();
        Function actual = fp.parse(math);
        Function expected = new Number("-156");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseAddition() {
        String math = "34+1.3";
        FunctionParser fp = new FunctionParser();
        Function actual = fp.parse(math);
        Function expected = new Number("35.3");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseSubtraction() {
        String math = "35.6-10.4";
        FunctionParser fp = new FunctionParser();
        Function actual = fp.parse(math);
        Function expected = new Number("25.2");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseMultiply() {
        String math = "50*4";
        FunctionParser fp = new FunctionParser();
        Function actual = fp.parse(math);
        Function expected = new Number("200");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseDivide() {
        String math = "12/3";
        FunctionParser fp = new FunctionParser();
        Function actual = fp.parse(math);
        Function expected = new Number("4");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseExponent() {
        String math = "5^2";
        FunctionParser fp = new FunctionParser();
        Function actual = fp.parse(math);
        Function expected = new Number("25");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseBrackets() {
        String math = "2*(3+4)";
        FunctionParser fp = new FunctionParser();
        Function actual = fp.parse(math);
        Function expected = new Number("14");

        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testParseVariable() {
        String math = "x";
        FunctionParser fp = new FunctionParser();
        Function actual = fp.parse(math);
        Function expected = new Variable("x");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseSin() {
        String math = "sinx";
        FunctionParser fp = new FunctionParser();
        Function actual = fp.parse(math);
        
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Sin(), 0);
        fb.add(new Variable("x"), 0);
        Function expected = fb.getFunction();
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseCos() {
        String math = "cosx";
        FunctionParser fp = new FunctionParser();
        Function actual = fp.parse(math);
        
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Cos(), 0);
        fb.add(new Variable("x"), 0);
        Function expected = fb.getFunction();
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseTan() {
        String math = "tanx";
        FunctionParser fp = new FunctionParser();
        Function actual = fp.parse(math);
        
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Tan(), 0);
        fb.add(new Variable("x"), 0);
        Function expected = fb.getFunction();
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseLn() {
        String math = "lnx";
        FunctionParser fp = new FunctionParser();
        Function actual = fp.parse(math);
        
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Ln(), 0);
        fb.add(new Variable("x"), 0);
        Function expected = fb.getFunction();
        
        Assert.assertEquals(expected, actual);
    }

    
    @Test
    public void testParseLog() {
        String math = "logx";
        FunctionParser fp = new FunctionParser();
        Function actual = fp.parse(math);
        
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Log("10"), 0);
        fb.add(new Variable("x"), 0);
        Function expected = fb.getFunction();
        
        Assert.assertEquals(expected, actual);
    }
    
    /* Command parsing tests */
    
    @Test
    public void testParseCommandEval() {
        String input = "eval 3+4";
        CommandParser cp = new CommandParser();
        String actual = cp.parse(input);
        String expected = "7";
        
        Assert.assertEquals(expected, actual);
    }
}
