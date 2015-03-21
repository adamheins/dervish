/*
 * Copyright (c) 2015 Adam Heins
 *
 * This file is part of the Dervish project, which is distributed under the MIT
 * license. For the full terms, see the included LICENSE file.
 */

package com.adamheins.dervish.builder;

import com.adamheins.dervish.function.Function;


/**
 * The FunctionBuilder class allows trees of Functions to be easily built from a
 * serial stream of constituting functions.
 *
 * @author Adam
 */
public class FunctionBuilder {

    // Current node in the tree, which is the one that was most recently added.
    private FunctionNode current;


    /**
     * Constructor.
     */
    public FunctionBuilder() {
        current = null;
    }


    /**
     * Add a new function into the tree.
     *
     * @param function The function to be added to the tree.
     * @param bracketDepth The number of brackets in which the function is
     *         nested.
     */
    public void add(Function function, int bracketDepth) {
        FunctionNode node = new FunctionNode(function, bracketDepth);

        if (current!= null)
            current.add(node);

        current = node;
    }


    /**
     * Get the root function in the function-tree.
     *
     * @return The root function.
     */
    private Function getRoot() {

        FunctionNode rootNode = current;
        while (rootNode.getParent() != null)
            rootNode = rootNode.getParent();
        return rootNode.getFunction();
    }


    /**
     * Gets the root function after it has been built. The function is
     * automatically evaluated first, to put it into its simplest form.
     *
     * It should be fine if additional nodes are added after this function is
     * called.
     *
     * @return The root function.
     */
    public Function getFunction() {
        return getRoot().evaluate();
    }
}
