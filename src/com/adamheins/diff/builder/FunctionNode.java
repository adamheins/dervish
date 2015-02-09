package com.adamheins.diff.builder;

import com.adamheins.diff.function.Function;
import com.adamheins.diff.function.Function.Associativity;

/**
 * A node that wraps a Function object so that it can be used placed into
 * a tree structure using a FunctionBuilder object.
 *
 * @author Adam
 */
class FunctionNode {

    // The actual function itself.
    private Function function;

    // Parent node.
    private FunctionNode parent;

    // Depth within sets of brackets in the function.
    private int bracketDepth;


    /**
     * Constructor.
     *
     * @param function The Function that this node wraps.
     * @param bracketDepth The number of sets of brackets in which this node is
     *         nested.
     */
    FunctionNode(Function function, int bracketDepth) {
        this.function = function;
        this.bracketDepth = bracketDepth;
        this.parent = null;
    }


    /**
     * Adds a new child node to this one.
     *
     * @param child The node to be added as a child.
     */
    private void addChild(FunctionNode child) {
        child.setParent(this);
        if (getFunction().getFirstChild() == null)
            getFunction().setFirstChild(child.getFunction());
        else if (getFunction().getSecondChild() == null)
            getFunction().setSecondChild(child.getFunction());
    }


    /**
     * Removes a child node from this one.
     *
     * @param child The node to be removed.
     */
    private void removeChild(FunctionNode child) {
        if (getFunction().getFirstChild() == child.getFunction())
            getFunction().setFirstChild(null);
        else if (getFunction().getSecondChild() == child.getFunction())
            getFunction().setSecondChild(null);
        else
            throw new RuntimeException("Parent does not have reference to"
                    + " child.");
    }


    /**
     * Inserts a new parent for this node. If this node has an existing parent,
     * the new parent becomes that node's child.
     *
     * @param newParent The new parent node for this node.
     */
    private void insertParent(FunctionNode newParent) {
        FunctionNode oldParent = getParent();
        if (oldParent != null) {
            oldParent.removeChild(this);
            oldParent.addChild(newParent);
        }
        newParent.addChild(this);
    }


    /**
     * Compares the precedence of this Node and another. This takes into account
     * the innate precedence of the type of Node, as well as the bracket depth
     * of the nodes.
     *
     * @param other The FunctionNode to which to compare this one.
     *
     * @return 0 if the nodes have equal precedence, a positive integer if this
     *         node has a higher precedence, or a negative integer is this node
     *         has lower precedence.
     */
    private int comparePrecedence(FunctionNode other) {
        if (getBracketDepth() > other.getBracketDepth())
            return 1;
        if (getBracketDepth() < other.getBracketDepth())
            return -1;
        return (getFunction().getPrecedence().compareTo(other.getFunction()
                .getPrecedence()));
    }


    /**
     * Add a new node to the tree.
     *
     * @param newNode The new node to add.
     */
    void add(FunctionNode newNode) {

        // Calculate the precedence of the new node relative to this one.
        int prec = newNode.comparePrecedence(this);

        // Child with higher precedence - all good.
        if (prec > 0) {
            addChild(newNode);

        // Child has equal precedence.
        } else if (prec == 0) {
            if (newNode.getFunction().getAssociativity() == Associativity.RIGHT)
                addChild(newNode);
            else
                insertParent(newNode);

        // Child has lower precedence.
        } else {

            FunctionNode greatParent = this;

            // Nodes with left associativity evaluate left to right. Therefore,
            // if the parent has the same precedence as the child, the child
            // swaps with it. The parent ends up lower in the tree, and is thus
            // evaluated first.
            if (newNode.getFunction().getAssociativity()
                    == Associativity.LEFT) {
                while (greatParent.getParent() != null
                        && greatParent.getParent().comparePrecedence(newNode)
                        >= 0)
                    greatParent = greatParent.getParent();
            } else {
                while (greatParent.getParent() != null
                        && greatParent.getParent().comparePrecedence(newNode)
                        > 0)
                    greatParent = greatParent.getParent();
            }
            greatParent.insertParent(newNode);
        }
    }


    /**
     * Set the parent of this node.
     *
     * @param parent The new parent.
     */
    void setParent(FunctionNode parent) {
        this.parent = parent;
    }


    /**
     * Get the parent of this node.
     *
     * @return The parent of this node.
     */
    FunctionNode getParent() {
        return parent;
    }


    /**
     * Get the function wrapped by this node.
     *
     * @return The function wrapped by this node.
     */
    Function getFunction() {
        return function;
    }


    /**
     * Get the bracket depth of this node.
     *
     * @return The bracket depth of this node.
     */
    int getBracketDepth() {
        return bracketDepth;
    }
}
