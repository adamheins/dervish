package com.adamheins.function.tree;

import java.util.Map;

/** Parent for all types of math nodes. */
abstract class Function {
    
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
    
    
    /**
     * Calculates the  derivative of the subtree that has this Node at its root.
     * 
     * @param var - The variable to take the derivative with respect to.
     * 
     * @return The derivative function.
     */
    public abstract Function differentiate(String var);
    
    
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
    

    
    
    /*@Override
    public String toString() {
        
        if (first == null && second == null)
            return value;
        
        // If parent doesn't exist, or has a lower precedence, need not bother with brackets.
        if (parent == null || parent.precedence.compareTo(precedence) < 0)
            return getFirstChild().toString() + value + getSecondChild().toString();
        return "(" + getFirstChild().toString() + value + getSecondChild().toString() + ")";
    }*/
    
    
    @Override
    public String toString() {
        if (first == null && second == null)
            return value;
        
        String str = getFirstChild().toString();
        
        if (first.precedence.compareTo(precedence) <= 0) // whether it is <= 0 probably depends on commutativity
            str = "(" + str + ")";
        
        // Check if this is a unary operator, which only has its first child populated.
        if (second == null)
            return value + str;
        
        // Add the operator.
        str += value;

        // Add the string representing the second child.
        if (second.precedence.compareTo(precedence) <= 0)
            str += "(" + getSecondChild().toString() + ")";
        else
            str += getSecondChild().toString();
        
        return str;
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
