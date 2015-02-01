package com.adamheins.function.tree;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/** Parent for all types of math nodes. */
abstract class Function {
    
    // Precision of the results of operations that generate less precise results than the operands.
    // One example is division, which can take infinite-precision integers and produce finite-
    // precision fractional numbers.
    protected final static int PRECISION = 20;
    
    // True if the Apfloat values used internally are formatted to be 'pretty', false otherwise.
    protected final static boolean PRETTY = true;
    
    protected final Object value;
    protected final Precedence precedence;
    protected final Associativity associativity;
    protected final boolean commutative;
    
    // Children.
    protected Function first;
    protected Function second;

    
    /**
     * Constructor.
     * 
     * @param value The string representation of this Function.
     * @param precedence The precedence of this Function.
     * @param associativity The associativity of this Function.
     * @param commutative True if this function is commutative, false otherwise.
     */
    Function(Object value, Precedence precedence, Associativity associativity, 
            boolean commutative) {
        this.value = value;
        this.precedence = precedence;
        this.associativity = associativity;
        this.commutative = commutative;

        first = null;
        second = null;
        
        commutative = false;
    }
    
    
    /**
     * Evaluates the function, substituting values for all variables in the passed map. The function
     * will be reduced to a simpler function. If no variables are present or all variables have
     * a number substituted, it will be reduced to a number.
     * 
     * @param varMap A map of variable names and the Functions that should be substituted into
     *               them.
     * 
     * @return The evaluated Function.
     */
    public abstract Function evaluate(Map<String, Function> varMap);
    
    
    /**
     * Evaluates the Function without performing any variable substitution.
     * 
     * @return The evaluated Function.
     */
    public Function evaluate() {
        return evaluate(null);
    }
    
    
    /**
     * Calculates the derivative of the subtree that has this Node at its root. Used internally
     * within the class.
     * 
     * @param var - The variable to take the derivative with respect to.
     * 
     * @return The derivative function.
     */
    protected abstract Function differentiateInternal(String var);
    
    
    /**
     * Calculates a simplified form of the derivative of the function.
     * 
     * @param var - The variable to take the derivative with respect to.
     * 
     * @return The simplified derivative function.
     */
    public Function differentiate(String var) {
        return differentiateInternal(var).evaluate();
    }
    
    
    /**
     * Determines the variables of which this Function is a function.
     * 
     * @return A list containing the string representation of each variable in this function.
     */
    public List<String> getVariables() {
        
        List<String> varList = new ArrayList<String>();
        Deque<Function> funcStack = new LinkedList<Function>();
        
        funcStack.push(this);
        
        while(!funcStack.isEmpty()) {
            Function current = funcStack.pop();

            if (current instanceof Variable) {
                varList.add(current.getValue().toString());
                continue;
            } else if (current instanceof Number) {
                continue;
            }
            
            funcStack.push(current.getFirstChild());
            if (getSecondChild() != null)
                funcStack.push(current.getSecondChild());
        }
        
        return varList;
    }
    
    
    /**
     * Get the first child Node.
     * 
     * @return The first child Node.
     */
    protected Function getFirstChild() {
        return first;
    }
    
    
    /**
     * Get the second child Function.
     * 
     * @return The second child Function.
     */
    protected Function getSecondChild() {
        return second;
    }
    
    
    /**
     * Get the mathematical precedence of this Function.
     * 
     * @return The mathematical precedence of this Function.
     */
    Precedence getPrecedence() {
        return precedence;
    }
    
    
    /**
     * Get the associativity of this Function. This can be either RIGHT or LEFT.
     * 
     * @return The associativity of this Function.
     */
    Associativity getAssociativity() {
        return associativity;
    }
    
    
    /**
     * Add the first child to this Function.
     * 
     * @param child The Function to add as the first child of this Function.
     */
    void setFirstChild(Function child) {
        first = child;
    }
    
    
    /**
     * Add a second child to this Function.
     * 
     * @param child The Function to add as the second child of this Function.
     */
    void setSecondChild(Function child) {
        second = child;
    }
    
    
    /**
     * Get the value of the Function.
     * 
     * @return The value of the Function.
     */
    Object getValue() {
        return value;
    }
    
    
    @Override
    public String toString() {
        if (first == null && second == null)
            return value.toString();
        
        String str = getFirstChild().toString();
        
        if (first.precedence.compareTo(precedence) <= 0) // whether it is <= 0 probably depends on commutativity
            str = "(" + str + ")";
        
        // Check if this is a unary operator, which only has its first child populated.
        if (second == null)
            return value + "(" + str + ")";
        
        // Add the operator.
        str += value;

        // Add the string representing the second child.
        if (second.precedence.compareTo(precedence) <= 0)
            str += "(" + getSecondChild().toString() + ")";
        else
            str += getSecondChild().toString();
        
        return str;
    }
    
    
    @Override
    public boolean equals(Object other) {
        
        if (other instanceof Function)
            return equalsFunction((Function)other);
        return false;
    }
    
    
    /**
     * Tests for equality between this and another Function.
     * 
     * @param other The other Function with which to determine equality.
     * 
     * @return True if this Function equals the other, false otherwise.
     */
    private boolean equalsFunction(Function other) {
        
        // The two functions are exactly equal.
        boolean equal = getValue().equals(other.getValue())
                && ((getFirstChild() != null && other.getFirstChild() != null && getFirstChild().equals(other.getFirstChild()))
                || (getFirstChild() == null && other.getFirstChild() == null))
                && ((getSecondChild() != null && other.getSecondChild() != null && getSecondChild().equals(other.getSecondChild()))
                || (getSecondChild() == null && other.getSecondChild() == null));
        
        if (commutative) {
            
            // The two functions are not in the exact same form, but are still mathematically equal.
            boolean commutativeEqual = getValue().equals(other.getValue())
                    && ((getFirstChild() != null && other.getSecondChild() != null && getFirstChild().equals(other.getSecondChild()))
                    || (getFirstChild() == null && other.getSecondChild() == null))
                    && ((getSecondChild() != null && other.getFirstChild() != null && getSecondChild().equals(other.getFirstChild()))
                    || (getSecondChild() == null && other.getFirstChild() == null));
            
            return equal || commutativeEqual;
        }
            
        return equal;
    }
    
    
    /**
     * Precedence of different types of Nodes. Values on the left have a lower precedence than
     * those on the right. NUMBER should always come last because for the purposes of this program,
     * numbers have infinite precedence.
     */
    protected enum Precedence {
        ADDITION, MULTIPLICATION, EXPONENTIATION, TRIG, NUMBER; 
    }
    
    
    /**
     * The associativity of a Function.
     */
    protected enum Associativity {
        LEFT, RIGHT;
    }
}
