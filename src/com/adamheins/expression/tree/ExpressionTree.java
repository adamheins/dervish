package com.adamheins.expression.tree;

public class ExpressionTree {
    
    // Most recently added node in the tree.
    private Node current;
    
    // Number of nodes in the tree.
    private int size;
    
    
    /**
     * Initializes a new ExpressionTree.
     */
    public ExpressionTree() {
        size = 0;
        current = null;
    }
    
    
    /**
     * Get the number of nodes in the tree.
     * 
     * @return The number of nodes in the tree.
     */
    public int getSize() {
        return size;
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
        size++;
    }
    
    
    /**
     * Evaluate the expression tree.
     * 
     * @return The value of the expression represented by this tree.
     */
    public double evaluate() {
        return current.getRoot().evaluate();
    }
}
