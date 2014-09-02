package com.adamheins.expression.tree;

import org.junit.Assert;
import org.junit.Test;

public class Tests {
    
    // Small arbitrary error bound in case floating points are acting up.
    private static final double ERROR = 1e-10; 
    
    
    /*
     * 2 + 3 + 4 = 9
     */
    @Test
    public void testAddition() {
        ExpressionTree expression = new ExpressionTree();      
        expression.add(new Number("2", 0));
        expression.add(new Plus(0));
        expression.add(new Number("3", 0));
        expression.add(new Plus(0));
        expression.add(new Number("4", 0));
        Assert.assertEquals(9, expression.evaluate(), ERROR);
    }
    
    
    /*
     * 10 - 7 = 3
     */
    @Test
    public void testSubtraction() {
        ExpressionTree expression = new ExpressionTree();      
        expression.add(new Number("10", 0));
        expression.add(new Minus(0));
        expression.add(new Number("7", 0));
        Assert.assertEquals(3, expression.evaluate(), ERROR);
    }
    
    
    /*
     * Tests that operators with left associativity evaluate left to right when multiple
     * operators of the same precedence occur in a row.
     * 
     * 7 - 3 - 2 = 2
     */
    @Test
    public void testLeftAssociativity() {
        ExpressionTree expression = new ExpressionTree();      
        expression.add(new Number("7", 0));
        expression.add(new Minus(0));
        expression.add(new Number("3", 0));
        expression.add(new Minus(0));
        expression.add(new Number("2", 1));
        Assert.assertEquals(2, expression.evaluate(), ERROR);
    }
    
    
    /*
     * 10 * 10 * 3 = 300
     */
    @Test
    public void testMultiplication() {
        ExpressionTree expression = new ExpressionTree();      
        expression.add(new Number("10", 0));
        expression.add(new Multiply(0));
        expression.add(new Number("10", 0));
        expression.add(new Multiply(0));
        expression.add(new Number("3", 0));
        Assert.assertEquals(300, expression.evaluate(), ERROR);
    }
    
    
    /*
     * 16 / 4
     */
    @Test
    public void testDivision() {
        ExpressionTree expression = new ExpressionTree();      
        expression.add(new Number("16", 0));
        expression.add(new Divide(0));
        expression.add(new Number("4", 0));
        Assert.assertEquals(4, expression.evaluate(), ERROR);
    }
    
    
    /*
     * 9 ^ 2 = 81
     */
    @Test
    public void testPow() {
        ExpressionTree expression = new ExpressionTree();      
        expression.add(new Number("9", 0));
        expression.add(new Exponent(0));
        expression.add(new Number("2", 0));
        Assert.assertEquals(81, expression.evaluate(), ERROR);
    }
    
    
    /*
     * Tests that operators with right associativity evaluate right to left when multiple
     * operators of the same precedence occur in a row.
     * 
     * 2 ^ 3 ^ 2 = 512
     */
    @Test
    public void testRightAssociativity() {
        ExpressionTree expression = new ExpressionTree();      
        expression.add(new Number("2", 0));
        expression.add(new Exponent(0));
        expression.add(new Number("3", 0));
        expression.add(new Exponent(0));
        expression.add(new Number("2", 0));
        Assert.assertEquals(512, expression.evaluate(), ERROR);
    }
    
    
    /*
     * 2 + 3 * 4 - 5 = 9
     */
    @Test
    public void testMultipleBinaryOperators() {
        ExpressionTree expression = new ExpressionTree();      
        expression.add(new Number("2", 0));
        expression.add(new Plus(0));
        expression.add(new Number("3", 0));
        expression.add(new Multiply(0));
        expression.add(new Number("4", 0));
        expression.add(new Minus(0));
        expression.add(new Number("5", 0));
        Assert.assertEquals(9, expression.evaluate(), ERROR);
    }
    
    
    /*
     * sin1.57079632679489 = 1 (approx)
     */
    @Test
    public void testSin() {
        ExpressionTree expression = new ExpressionTree();      
        expression.add(new Sin(0));
        expression.add(new Number("1.57079632679489", 0));
        Assert.assertEquals(1, expression.evaluate(), ERROR);
    }
    
    
    /*
     * cos0 = 1
     */
    @Test
    public void testCos() {
        ExpressionTree expression = new ExpressionTree();      
        expression.add(new Cos(0));
        expression.add(new Number("0", 0));
        Assert.assertEquals(1, expression.evaluate(), ERROR);
    }
    
    
    /*
     * tan0.78539816339744 = 1 (approx)
     */
    @Test
    public void testTan() {
        ExpressionTree expression = new ExpressionTree();      
        expression.add(new Tan(0));
        expression.add(new Number("0.78539816339744", 0));
        Assert.assertEquals(1, expression.evaluate(), ERROR);
    }
    
    
    /*
     * sinsin1 = 0.745624141665557 (approx)
     */
    @Test
    public void testMultipleUnaryOperators() {
        ExpressionTree expression = new ExpressionTree();      
        expression.add(new Sin(0));
        expression.add(new Sin(0));
        expression.add(new Number("1", 0));
        Assert.assertEquals(0.745624141665557, expression.evaluate(), ERROR);
    }
    
    
    /*
     * 2 + sin1.57079632679489 * 3 = 5 (approx)
     */
    @Test
    public void testUnaryAndBinaryOperators() {
        ExpressionTree expression = new ExpressionTree();      
        expression.add(new Number("2", 0));
        expression.add(new Plus(0));
        expression.add(new Sin(0));
        expression.add(new Number("1.57079632679489", 0));
        expression.add(new Multiply(0));
        expression.add(new Number("3", 0));
        Assert.assertEquals(5, expression.evaluate(), ERROR);
    }
    
    
    /*
     * (2 + 3) * (4 + 5) = 45
     */
    @Test
    public void testBrackets() {
        ExpressionTree expression = new ExpressionTree();       
        expression.add(new Number("2", 1));
        expression.add(new Plus(1));
        expression.add(new Number("3", 1));
        expression.add(new Multiply(0));
        expression.add(new Number("4", 1));
        expression.add(new Plus(1));
        expression.add(new Number("5", 1));
        Assert.assertEquals(45, expression.evaluate(), ERROR);
    }
}
