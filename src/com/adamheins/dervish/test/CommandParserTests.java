/*
 * Copyright (c) 2015 Adam Heins
 *
 * This file is part of the Dervish project, which is distributed under the MIT
 * license. For the full terms, see the included LICENSE file.
 */

package com.adamheins.dervish.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.adamheins.dervish.builder.FunctionBuilder;
import com.adamheins.dervish.function.Function;
import com.adamheins.dervish.function.Multiply;
import com.adamheins.dervish.function.Number;
import com.adamheins.dervish.function.Plus;
import com.adamheins.dervish.function.Variable;
import com.adamheins.dervish.parser.CommandParser;
import com.adamheins.dervish.parser.FunctionParser;


/**
 * Tests for the CommandParser.
 *
 * @author Adam
 */
public class CommandParserTests {

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

        String vars = cp.parse("show all");
        Assert.assertTrue(vars.contains("x"));
        Assert.assertTrue(vars.contains("y"));
        Assert.assertTrue(vars.contains("z"));

        Assert.assertEquals("", response);
    }


    @Test
    public void testParseCommandUseMultiCharVars() throws Exception {
        String input = "use x var variable";
        CommandParser cp = new CommandParser();
        String response = cp.parse(input);

        String vars = cp.parse("show all");
        Assert.assertTrue(vars.contains("x"));
        Assert.assertTrue(vars.contains("var"));
        Assert.assertTrue(vars.contains("variable"));

        Assert.assertEquals("", response);
    }


    @Test
    public void testParseCommandForget() throws Exception {
        CommandParser cp = new CommandParser();
        cp.parse("use x y z");
        cp.parse("forget x y");

        String vars = cp.parse("show all");
        Assert.assertFalse(vars.contains("x"));
        Assert.assertFalse(vars.contains("y"));
        Assert.assertTrue(vars.contains("z"));
    }


    @Test
    public void testParseCommandForgetAll() throws Exception {
        CommandParser cp = new CommandParser();
        cp.parse("use x y z");
        cp.parse("forget all");

        String vars = cp.parse("show all");
        Assert.assertFalse(vars.contains("x"));
        Assert.assertFalse(vars.contains("y"));
        Assert.assertFalse(vars.contains("z"));
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
        Function expected = fb.getFunction();

        List<String> varList = new ArrayList<String>();
        varList.add("x");
        varList.add("y");

        FunctionParser fp = new FunctionParser(varList);
        Function actual = fp.parse(cp.parse("show x").split(" ")[2]);

        Assert.assertEquals(expected, actual);
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
        Function expected = fb.getFunction();

        List<String> varList = new ArrayList<String>();
        varList.add("x");
        varList.add("y");

        FunctionParser fp = new FunctionParser(varList);
        Function actual = fp.parse(cp.parse("show x").split(" ")[2]);

        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testParseCommandClear() throws Exception {
        CommandParser cp = new CommandParser();
        cp.parse("use x y");
        cp.parse("set x y+9.73");
        cp.parse("set y 4");
        cp.parse("clear x");
        String actual = cp.parse("show y");

        String expected = "y = 4";

        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testParseCommandClearAll() throws Exception {
        CommandParser cp = new CommandParser();
        cp.parse("use x y");
        cp.parse("set x y+9.73");
        cp.parse("set y 4");
        cp.parse("clear all");

        String response = cp.parse("show all");

        Assert.assertFalse(response.contains("="));
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
        String vars = cp.parse("show all");
        Assert.assertTrue(vars.contains("$"));
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
