package com.adamheins.diff.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.adamheins.diff.builder.FunctionBuilder;
import com.adamheins.diff.function.Cos;
import com.adamheins.diff.function.Function;
import com.adamheins.diff.function.Ln;
import com.adamheins.diff.function.Log;
import com.adamheins.diff.function.Number;
import com.adamheins.diff.function.Sin;
import com.adamheins.diff.function.Tan;
import com.adamheins.diff.function.Variable;
import com.adamheins.diff.parser.FunctionParser;
import com.adamheins.diff.parser.ParsingException;


/**
 * Tests for the FunctionParser.
 * 
 * @author Adam
 */
public class FunctionParserTests {

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
    public void testParseBracketedNumber() throws ParsingException {
        String math = "(-5.6)";
        FunctionParser fp = new FunctionParser(null);
        Function actual = fp.parse(math);
        Function expected = new Number("-5.6");

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
    public void testParseComplexBrackets() throws ParsingException {
        String math = "(2*(6-3))^2";
        FunctionParser fp = new FunctionParser(null);
        Function actual = fp.parse(math);
        Function expected = new Number("36");

        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseComplexBrackets2() throws ParsingException {
        String math = "(2+8)/4-0.5*2";
        FunctionParser fp = new FunctionParser(null);
        Function actual = fp.parse(math);
        Function expected = new Number("1.5");

        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseComplexBrackets3() throws ParsingException {
        String math = "(log(500+500)*2/3)^3";
        FunctionParser fp = new FunctionParser(null);
        Function actual = fp.parse(math);
        Function expected = new Number("8");

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
    
    
    @Test
    public void testParsePi() throws ParsingException {
        String math = "pi";
        FunctionParser fp = new FunctionParser(new ArrayList<String>());
        Function actual = fp.parse(math);
        
        Function expected = new Number("3.14159265358979323846");
        
        Assert.assertEquals(expected, actual);
    }
    
    
    @Test
    public void testParseE() throws ParsingException {
        String math = "e";
        FunctionParser fp = new FunctionParser(new ArrayList<String>());
        Function actual = fp.parse(math);
        
        Function expected = new Number("2.71828182845904523536");
        
        Assert.assertEquals(expected, actual);
    }
}
