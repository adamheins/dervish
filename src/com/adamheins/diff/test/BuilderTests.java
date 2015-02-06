package com.adamheins.diff.test;

import org.junit.Assert;
import org.junit.Test;

import com.adamheins.diff.builder.FunctionBuilder;
import com.adamheins.diff.function.Cos;
import com.adamheins.diff.function.Divide;
import com.adamheins.diff.function.Exponent;
import com.adamheins.diff.function.Function;
import com.adamheins.diff.function.Log;
import com.adamheins.diff.function.Minus;
import com.adamheins.diff.function.Multiply;
import com.adamheins.diff.function.Negative;
import com.adamheins.diff.function.Number;
import com.adamheins.diff.function.Plus;
import com.adamheins.diff.function.Sin;
import com.adamheins.diff.function.Tan;
import com.adamheins.diff.function.Variable;
import com.adamheins.diff.parser.ParsingException;

public class BuilderTests {

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
        
        // Using strings to compare results in this test since the function is
        // not actual being simplified by getting evaluated. It would not make
        // sense to compare the function to itself.
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
        
        Assert.assertEquals(new Variable("x"), result);
    }
    
    
    @Test
    public void testAdditionTwoZeros() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("0"), 0);
        fb.add(new Plus(), 0);
        fb.add(new Number("0"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("0");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testMinusIntegers() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("2"), 0);
        fb.add(new Minus(), 0);
        fb.add(new Number("3"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("-1");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testMinusFloats() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("4.2"), 0);
        fb.add(new Minus(), 0);
        fb.add(new Number("1.5"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("2.7");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testMultiplyIntegers() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("2"), 0);
        fb.add(new Multiply(), 0);
        fb.add(new Number("3"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("6");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testMultiplyZero() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("2"), 0);
        fb.add(new Multiply(), 0);
        fb.add(new Number("0"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("0");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testMultiplyFloats() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("2.5"), 0);
        fb.add(new Multiply(), 0);
        fb.add(new Number("3.1"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("7.75");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testDivideIntegers() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("6"), 0);
        fb.add(new Divide(), 0);
        fb.add(new Number("3"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("2");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testDivideIntegersNonMultiples() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("15"), 0);
        fb.add(new Divide(), 0);
        fb.add(new Number("10"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("1.5");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testDivideFloats() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("6.5"), 0);
        fb.add(new Divide(), 0);
        fb.add(new Number("2.5"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("2.6");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testExponentIntegers() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("2"), 0);
        fb.add(new Exponent(), 0);
        fb.add(new Number("3"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("8");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testExponentBaseOne() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("1"), 0);
        fb.add(new Exponent(), 0);
        fb.add(new Number("5"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("1");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testExponentBaseZero() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("0"), 0);
        fb.add(new Exponent(), 0);
        fb.add(new Number("5"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("0");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testExponentZero() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("4"), 0);
        fb.add(new Exponent(), 0);
        fb.add(new Number("0"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("1");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testExponentFloats() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Number("1.5"), 0);
        fb.add(new Exponent(), 0);
        fb.add(new Number("4.0"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("5.0625");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testLog10() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Log("10"), 0);
        fb.add(new Number("1000"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("3");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testNegativeAndNumber() {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Negative(), 0);
        fb.add(new Number("10"), 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("-10");
        
        Assert.assertEquals(expected, actual);
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
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testExpDiffIsSelf() {
        
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(Number.E, 0);
        fb.add(new Exponent(), 0);
        fb.add(new Variable("x"), 0);
        
        Function expected = fb.getFunction();
        Function actual = expected.differentiate("x");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testSinPi() throws ParsingException {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Sin(), 0);
        fb.add(Number.PI, 0);
        
        Function actual = fb.getFunction();
        Function expected = Number.ZERO;
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testSinPiOverTwo() throws ParsingException {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Sin(), 0);
        fb.add(Number.PI, 1);
        fb.add(new Divide(), 1);
        fb.add(new Number("2"), 1);
        
        Function actual = fb.getFunction();
        Function expected = Number.ONE;
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testCosPi() throws ParsingException {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Cos(), 0);
        fb.add(Number.PI, 0);
        
        Function actual = fb.getFunction();
        Function expected = new Number("-1");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testTanPi() throws ParsingException {
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Tan(), 0);
        fb.add(Number.PI, 0);
        
        Function actual = fb.getFunction();
        Function expected = Number.ZERO;
        
        Assert.assertEquals(expected, actual);
    }
}
