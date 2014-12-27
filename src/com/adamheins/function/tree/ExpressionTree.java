package com.adamheins.function.tree;

import java.util.Map;

public class ExpressionTree {
    
    // Most recently added node in the tree.
    private Node current;
    
    
    /**
     * Initializes a new ExpressionTree.
     */
    public ExpressionTree() {
        current = null;
    }
    
    
    /**
     * Add a new node to the tree.
     * 
     * @param node Node to add.
     */
    public void add(Node node) {
        if (current != null)
            current.add(node);
        current = node;
    }
    
    
    /**
     * Take the derivative of the function.
     * 
     * @return A string representing the derivative of this function.
     */
    public ExpressionTree differentiate(String var) {
        
        ExpressionTree derivative = new ExpressionTree();
        current.getRoot().differentiate(var, derivative);
        return derivative.evaluate(null);
    }
    
    
    /*
     * This should probably give me another ExpressionTree, which could then be converted to a
     * string with the toString() method.
     */
    public ExpressionTree evaluate(Map<String, Double> varMap) {
        
        ExpressionTree expression = new ExpressionTree();
        expression.current = current.getRoot().evaluate(varMap); // need to really deal with the root structure bs.
        return expression;
    }
    
    
    /**
     * Like evaluate, but prints out straight strings.
     * Possibly add support for removing zeros.
     */
    @Override
    public String toString() {
        return current.getRoot().toString();
    }
}
