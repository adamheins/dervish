package com.adamheins.expression.tree;

/** Parent for all types of math nodes. */
abstract class Node {
    
    protected final String value;
    protected final Precedence precedence;
    protected final Associativity associativity;
    
    // Number of sets of brackets in which this Node is nested.
    protected final int bracketDepth;
    
    // Relatives.
    protected Node parent;
    protected Node first;
    protected Node second;

    
    Node(String value, Precedence precedence, Associativity associativity, int bracketDepth) {
        this.value = value;
        this.precedence = precedence;
        this.associativity = associativity;
        this.bracketDepth = bracketDepth;
        
        parent = null;
        first = null;
        second = null;
    }
    
    
    /**
     * Calculates the value of the subtree that has this Node as it's root.
     * 
     * @return The value of the subtree of this Node.
     */
    public abstract double evaluate();
    
    
    /**
     * Get the root of the entire tree.
     * 
     * @return The root of the entire tree.
     */
    public Node getRoot() {
        Node root = this;
        while (root.getParent() != null)
            root = root.getParent();
        return root;
    }
    
    
    /**
     * Get the parent of this Node.
     * 
     * @return The parent Node of this Node.
     */
    protected Node getParent() {
        return parent;
    }
    
    
    /**
     * Get the first child Node.
     * 
     * @return The first child Node.
     */
    protected Node getFirstChild() {
        return first;
    }
    
    
    /**
     * Get the second child Node.
     * 
     * @return The second child Node.
     */
    protected Node getSecondChild() {
        return second;
    }
    
    
    /**
     * Add a new child to this node.
     * 
     * @param child The child Node being added.
     */
    protected void addChild(Node child) {
        child.parent = this;
        if (first == null)
            first = child;
        else if (second == null)
            second = child;
        else
            throw new RuntimeException("Maximum children already allocated.");
    }
    
    
    /**
     * Remove a child from this Node.
     * 
     * @param child The child Node to remove.
     */
    protected void removeChild(Node child) {
        if (first == child)
            first = null;
        else if (second == child)
            second = null;
        else
            throw new RuntimeException("Parent does not have reference to child.");
    }
    
    
    /**
     * Compares the precedence of this Node and another. This takes into account the innate 
     * precedence of the type of Node, as well as the bracket depth of the nodes. 
     * 
     * @param other The Node to compare this one to.
     * 
     * @return 0 if the nodes have equal precedence, a positive integer if this node has a higher 
     *     precedence, or a negative integer is this node has lower precedence.
     */
    protected int comparePrecedence(Node other) {       
        if (bracketDepth > other.bracketDepth)
            return 1;
        if (bracketDepth < other.bracketDepth)
            return -1;       
        return (precedence.compareTo(other.precedence));
    }
    
    
    /**
     * Give this Node a new parent. If this Node has an existing parent, the new parent become's
     * the old parent's child.
     * 
     * @param newParent The new parent of this Node.
     */
    protected void insertParent(Node newParent) {
        Node oldParent = getParent();
        if (oldParent != null) {
            oldParent.removeChild(this);
            oldParent.addChild(newParent);
        }
        newParent.addChild(this);
    }
    
    
    /**
     * Add a new Node to the expression tree.
     * 
     * @param newNode The Node to add to the tree.
     */
    public void add(Node newNode) {
        
        // Child with higher precedence - all good.
        if (newNode.comparePrecedence(this) > 0) {
            addChild(newNode);

        // Child has equal precedence.
        } else if (newNode.comparePrecedence(this) == 0) {
            if (newNode.associativity == Associativity.RIGHT)
                addChild(newNode);
            else
                insertParent(newNode);
        
        // Child has lower precedence.
        } else {
                        
            Node greatParent = this;
            
            // Nodes with left associativity evaluate left to right. Therefore, if the parent has 
            // the same precedence as the child, the child swaps with it. The parent ends up lower
            // in the tree, and is thus evaluated first.
            if (newNode.associativity == Associativity.LEFT) {
                while (greatParent.getParent() != null 
                        && greatParent.getParent().comparePrecedence(newNode) >= 0)
                    greatParent = greatParent.getParent();
            } else {                
                while (greatParent.getParent() != null 
                        && greatParent.getParent().comparePrecedence(newNode) > 0)
                    greatParent = greatParent.getParent();
            }            
            greatParent.insertParent(newNode);
        }
    }
    
    
    @Override
    public String toString() {
        return value;
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
