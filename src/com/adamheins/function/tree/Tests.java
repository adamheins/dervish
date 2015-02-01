package com.adamheins.function.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class Tests {
    
    // TODO need to test error conditions

    
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
    
    
    /* Parsing tests */
    
    @Test
    public void testParseInteger() throws ParsingException {
        String math = "34";
        FunctionParser fp = new FunctionParser(null);
        Function actual = fp.parse(math);
        Function expected = new Number("34");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseFloat() throws ParsingException {
        String math = "12.345";
        FunctionParser fp = new FunctionParser(null);
        Function actual = fp.parse(math);
        Function expected = new Number("12.345");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseNegativeNumber() throws ParsingException {
        String math = "-156";
        FunctionParser fp = new FunctionParser(null);
        Function actual = fp.parse(math);
        Function expected = new Number("-156");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseAddition() throws ParsingException {
        String math = "34+1.3";
        FunctionParser fp = new FunctionParser(null);
        Function actual = fp.parse(math);
        Function expected = new Number("35.3");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseSubtraction() throws ParsingException {
        String math = "35.6-10.4";
        FunctionParser fp = new FunctionParser(null);
        Function actual = fp.parse(math);
        Function expected = new Number("25.2");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseMultiply() throws ParsingException {
        String math = "50*4";
        FunctionParser fp = new FunctionParser(null);
        Function actual = fp.parse(math);
        Function expected = new Number("200");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseDivide() throws ParsingException {
        String math = "12/3";
        FunctionParser fp = new FunctionParser(null);
        Function actual = fp.parse(math);
        Function expected = new Number("4");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseExponent() throws ParsingException {
        String math = "5^2";
        FunctionParser fp = new FunctionParser(null);
        Function actual = fp.parse(math);
        Function expected = new Number("25");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseBrackets() throws ParsingException {
        String math = "2*(3+4)";
        FunctionParser fp = new FunctionParser(null);
        Function actual = fp.parse(math);
        Function expected = new Number("14");

        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testParseVariable() throws ParsingException {
        String math = "x";
        List<String> varList = new ArrayList<String>();
        varList.add("x");
        FunctionParser fp = new FunctionParser(varList);
        Function actual = fp.parse(math);
        Function expected = new Variable("x");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseSin() throws ParsingException {
        String math = "sinx";
        List<String> varList = new ArrayList<String>();
        varList.add("x");
        FunctionParser fp = new FunctionParser(varList);
        Function actual = fp.parse(math);
        
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Sin(), 0);
        fb.add(new Variable("x"), 0);
        Function expected = fb.getFunction();
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseCos() throws ParsingException {
        String math = "cosx";
        List<String> varList = new ArrayList<String>();
        varList.add("x");
        FunctionParser fp = new FunctionParser(varList);
        Function actual = fp.parse(math);
        
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Cos(), 0);
        fb.add(new Variable("x"), 0);
        Function expected = fb.getFunction();
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseTan() throws ParsingException {
        String math = "tanx";
        List<String> varList = new ArrayList<String>();
        varList.add("x");
        FunctionParser fp = new FunctionParser(varList);
        Function actual = fp.parse(math);
        
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Tan(), 0);
        fb.add(new Variable("x"), 0);
        Function expected = fb.getFunction();
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseLn() throws ParsingException {
        String math = "lnx";
        List<String> varList = new ArrayList<String>();
        varList.add("x");
        FunctionParser fp = new FunctionParser(varList);
        Function actual = fp.parse(math);
        
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Ln(), 0);
        fb.add(new Variable("x"), 0);
        Function expected = fb.getFunction();
        
        Assert.assertEquals(expected, actual);
    }

    
    @Test
    public void testParseLog() throws ParsingException {
        String math = "logx";
        List<String> varList = new ArrayList<String>();
        varList.add("x");
        FunctionParser fp = new FunctionParser(varList);
        Function actual = fp.parse(math);
        
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Log("10"), 0);
        fb.add(new Variable("x"), 0);
        Function expected = fb.getFunction();
        
        Assert.assertEquals(expected, actual);
    }
    
    /* Command parsing tests */
    
    @Test
    public void testParseCommandEvalNumber() throws Exception {
        String input = "eval 9.73";
        CommandParser cp = new CommandParser();
        String actual = cp.parse(input);
        String expected = "9.73";
        
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testParseCommandEvalLargeNumber() throws Exception {
        String input = "eval 1009.873";
        CommandParser cp = new CommandParser();
        String actual = cp.parse(input);
        String expected = "1009.873";
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseCommandEvalNumbers() throws Exception {
        String input = "eval 3+4";
        CommandParser cp = new CommandParser();
        String actual = cp.parse(input);
        String expected = "7";
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseCommandEvalWithVar() throws Exception {
        CommandParser cp = new CommandParser();
        cp.parse("use x");
        String actual = cp.parse("eval 4+x");
        String expected = "4+x";
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseCommandUseSingleCharVars() throws Exception {
        String input = "use x y z";
        CommandParser cp = new CommandParser();
        String response = cp.parse(input);
        
        List<String> varList = cp.varList;
        Assert.assertTrue(varList.contains("x"));
        Assert.assertTrue(varList.contains("y"));
        Assert.assertTrue(varList.contains("z"));
        
        Assert.assertEquals("", response);
    }
    
    
    @Test
    public void testParseCommandUseMultiCharVars() throws Exception {
        String input = "use x var variable";
        CommandParser cp = new CommandParser();
        String response = cp.parse(input);
        
        List<String> varList = cp.varList;
        Assert.assertTrue(varList.contains("x"));
        Assert.assertTrue(varList.contains("var"));
        Assert.assertTrue(varList.contains("variable"));
        
        Assert.assertEquals("", response);
    }
    
    
    @Test
    public void testParseCommandForget() throws Exception {
        CommandParser cp = new CommandParser();
        cp.parse("use x y z");
        cp.parse("forget x y");
        
        List<String> varList = cp.varList;
        Assert.assertFalse(varList.contains("x"));
        Assert.assertFalse(varList.contains("y"));
        Assert.assertTrue(varList.contains("z"));
    }


    @Test
    public void testParseCommandForgetAll() throws Exception {
        CommandParser cp = new CommandParser();
        cp.parse("use x y z");
        cp.parse("forget all");

        List<String> varList = cp.varList;
        Assert.assertFalse(varList.contains("x"));
        Assert.assertFalse(varList.contains("y"));
        Assert.assertFalse(varList.contains("z"));
    }

    
    @Test
    public void testParseCommandSet() throws Exception {
        CommandParser cp = new CommandParser();
        cp.parse("use x y");
        cp.parse("set x y+9.73");
        
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Variable("y"), 0);
        fb.add(new Plus(), 0);
        fb.add(new Number("9.73"), 0);
        Function actual = fb.getFunction();
        
        Map<String, Function> varMap = cp.varMap;
        
        Assert.assertEquals(varMap.get("x"), actual);
    }


    @Test
    public void testParseCommandSetToDerivative() throws Exception {
        CommandParser cp = new CommandParser();
        cp.parse("use x y");
        cp.parse("set x diff y^2 y");
        
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(new Variable("y"), 0);
        fb.add(new Multiply(), 0);
        fb.add(new Number("2"), 0);
        Function actual = fb.getFunction();
        
        Map<String, Function> varMap = cp.varMap;
        
        Assert.assertEquals(varMap.get("x"), actual);
    }
    
    
    @Test
    public void testParseCommandClear() throws Exception {
        CommandParser cp = new CommandParser();
        cp.parse("use x y");
        cp.parse("set x y+9.73");
        cp.parse("set y 4");
        cp.parse("clear x");
        
        Function actual = new Number("4");
        
        Map<String, Function> varMap = cp.varMap;
        
        Assert.assertEquals(varMap.get("y"), actual);
    }


    @Test
    public void testParseCommandClearAll() throws Exception {
        CommandParser cp = new CommandParser();
        cp.parse("use x y");
        cp.parse("set x y+9.73");
        cp.parse("set y 4");
        cp.parse("clear all");
        
        Map<String, Function> varMap = cp.varMap;
        
        Assert.assertTrue(varMap.isEmpty());
    }
    
    
    @Test
    public void testParseCommandSub() throws Exception {
        CommandParser cp = new CommandParser();
        cp.parse("use x");
        cp.parse("set x 5.5");
        String actual = cp.parse("sub x+4 x");
        String expected = "9.5";
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseCommandSubNoVarArgs() throws Exception {
        CommandParser cp = new CommandParser();
        cp.parse("use x");
        cp.parse("set x 5.5");

        String actual = cp.parse("sub x+4");
        String expected = "9.5";
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseCommandSubAllMultiVars() throws Exception {
        CommandParser cp = new CommandParser();
        cp.parse("use x y");
        cp.parse("set x 5.5");
        cp.parse("set y 6.5");

        String actual = cp.parse("sub x+y all");
        String expected = "12";
        
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testParseCommandSubMultiVarsOneUndefined() throws Exception {
        CommandParser cp = new CommandParser();
        cp.parse("use x y");
        cp.parse("set x -3");

        String actual = cp.parse("sub x+y all");
        String expected = "-3+y";
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseCommandDiff() throws Exception {
        CommandParser cp = new CommandParser();
        cp.parse("use x");
        String actual = cp.parse("diff 45*x+87 x");
        String expected = "45";
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testLASTVarUndefined() throws Exception {
        CommandParser cp = new CommandParser();
        List<String> varList = cp.varList;
        Assert.assertTrue(varList.contains("$"));
    }
    
    
    @Test
    public void testLASTVarDefined() throws Exception {
        CommandParser cp = new CommandParser();
        cp.parse("eval 5+6.7");
        String actual = cp.parse("eval $");
        String expected = "11.7";
        
        Assert.assertEquals(expected, actual);
    }
}
