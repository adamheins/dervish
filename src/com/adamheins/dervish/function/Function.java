/*
 * Copyright (c) 2015 Adam Heins
 *
 * This file is part of the Dervish project, which is distributed under the MIT
 * license. For the full terms, see the included LICENSE file.
 */

package com.adamheins.dervish.function;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

/**
 * Base class for all function types.
 *
 * @author Adam
 */
public abstract class Function {

    // Precision of the results of operations that generate less precise results
    // than the operands. One example is division, which can take
    // infinite-precision integers and produce finite-precision fractional
    // numbers. This is the precision that values retain internally.
    protected static final int INTERNAL_PRECISION = 21;

    // Outward-facing precision. This is the lowest possible precision to
    // which the external user may be exposed.
    protected static final int EXTERNAL_PRECISION = 20;

    // The rounding mode for numbers that need rounding.
    protected static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    // True if the Apfloat values used internally are formatted to be 'pretty',
    // false otherwise.
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
    }


    /**
     * Evaluates the function, substituting values for all variables in the
     * passed map. The function will be reduced to a simpler function. If no
     * variables are present or all variables have a number substituted, it will
     * be reduced to a number.
     *
     * @param varMap A map of variable names and the Functions that should be
     *         substituted into them.
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
     * Calculates the derivative of the subtree that has this Node at its root.
     * Used internally within the class.
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
     * @return A list containing the string representation of each variable in
     *         this function.
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
    public Function getFirstChild() {
        return first;
    }


    /**
     * Get the second child Function.
     *
     * @return The second child Function.
     */
    public Function getSecondChild() {
        return second;
    }


    /**
     * Get the mathematical precedence of this Function.
     *
     * @return The mathematical precedence of this Function.
     */
    public Precedence getPrecedence() {
        return precedence;
    }


    /**
     * Get the associativity of this Function. This can be either RIGHT or LEFT.
     *
     * @return The associativity of this Function.
     */
    public Associativity getAssociativity() {
        return associativity;
    }


    /**
     * Add the first child to this Function.
     *
     * @param child The Function to add as the first child of this Function.
     */
    public void setFirstChild(Function child) {
        first = child;
    }


    /**
     * Add a second child to this Function.
     *
     * @param child The Function to add as the second child of this Function.
     */
    public void setSecondChild(Function child) {
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

        String str = getFirstChild().toString();

        if (first.precedence.compareTo(precedence) <= 0)
            str = "(" + str + ")";

        // Check if this is a unary operator, which only has its first child
        // populated.
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
     * Check if these two children are equal, with each having the possibility
     * to be null.
     *
     * @param thisChild One of the children to compare.
     * @param otherChild The other child to compare.
     *
     * @return True if the children are equal, false otherwise.
     */
    private boolean childEqual(Function thisChild, Function otherChild) {
        boolean notNullEqual = thisChild != null && otherChild != null
                && thisChild.equals(otherChild);
        boolean bothNull = thisChild == null && otherChild == null;
        return notNullEqual || bothNull;
    }


    /**
     * Check if this function and the other function have equal children
     * in the same order.
     *
     * @param other The other function.
     *
     * @return True if the children have equal values in the same order, false
     *         otherwise.
     */
    private boolean childrenExactlyEqual(Function other) {
        boolean firstChildrenEqual = childEqual(getFirstChild(),
                other.getFirstChild());
        boolean secondChildrenEqual = childEqual(getSecondChild(),
                other.getSecondChild());
        return firstChildrenEqual && secondChildrenEqual;
    }


    /**
     * Check if this function and the other function have equal children
     * in the opposite order.
     *
     * @param other The other function.
     *
     * @return True if the children have equal values in the opposite order,
     *         false otherwise.
     */
    private boolean childrenCommutativelyEqual(Function other) {
        boolean firstChildrenEqual = childEqual(getFirstChild(),
                other.getSecondChild());
        boolean secondChildrenEqual = childEqual(getSecondChild(),
                other.getFirstChild());
        return firstChildrenEqual && secondChildrenEqual;
    }


    /**
     * Tests for equality between this and another Function.
     *
     * @param other The other Function with which to determine equality.
     *
     * @return True if this Function equals the other, false otherwise.
     */
    private boolean equalsFunction(Function other) {

        // Values are not equal, functions are not equal.
        if (!getValue().equals(other.getValue()))
            return false;

        // The two functions are exactly equal.
        boolean childrenExactlyEqual = childrenExactlyEqual(other);

        // The two functions may not be in the exact same form, but could still
        // be mathematically equal.
        if (commutative && !childrenExactlyEqual) {
            boolean childrenCommutativelyEqual
                    = childrenCommutativelyEqual(other);
            return childrenExactlyEqual || childrenCommutativelyEqual;
        }

        return childrenExactlyEqual;
    }


    /**
     * Rounds the value to eliminate floating point imprecisions. Then set the
     * precision back to the original.
     *
     * @param result The value to round.
     *
     * @return The rounded value.
     */
    protected Apfloat precisionRound(Apfloat result) {
        return ApfloatMath.round(result, EXTERNAL_PRECISION, ROUNDING_MODE)
                .precision(INTERNAL_PRECISION);
    }


    /**
     * Precedence of different types of Nodes. Values on the left have a lower
     * precedence than those on the right. NUMBER should always come last
     * because for the purposes of this program, numbers have infinite
     * precedence.
     */
    public enum Precedence {
        ADDITION, MULTIPLICATION, EXPONENTIATION, TRIG, NUMBER;
    }


    /**
     * The associativity of a Function.
     */
    public enum Associativity {
        LEFT, RIGHT;
    }
}
