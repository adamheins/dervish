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
    protected final int PRECISION = 20;
    
    protected final String value;
    protected final Precedence precedence;
    protected final Associativity associativity;
    
    // Children.
    protected Function first;
    protected Function second;

    
    Function(String value, Precedence precedence, Associativity associativity) {
        this.value = value;
        this.precedence = precedence;
        this.associativity = associativity;

        first = null;
        second = null;
    }
    
    
    
    public abstract Function evaluate(Map<String, Function> varMap);
    
    
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
                varList.add(current.getValue());
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
     * Get the second child Node.
     * 
     * @return The second child Node.
     */
    protected Function getSecondChild() {
        return second;
    }
    
    
    Precedence getPrecedence() {
        return precedence;
    }
    
    
    Associativity getAssociativity() {
        return associativity;
    }
    
    
    void setFirstChild(Function child) {
        first = child;
    }
    
    
    void setSecondChild(Function child) {
        second = child;
    }
    
    
    String getValue() {
        return value;
    }
    
    
    @Override
    public String toString() {
        if (first == null && second == null)
            return value;
        
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
    

    public boolean equals(Function other) {
        return getValue().equals(other.getValue()) 
                && getFirstChild().equals(other.getFirstChild()) 
                && getSecondChild().equals(other.getSecondChild());
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
     * The associativity of the Node.
     */
    protected enum Associativity {
        LEFT, RIGHT;
    }
}
