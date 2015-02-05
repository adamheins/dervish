package com.adamheins.diff.function;

/**
 * Thrown when mathematical errors are determined are found during evaluation
 * or differentiation. For example, division by zero errors.
 */
public class EvaluationException extends RuntimeException {

    private static final long serialVersionUID = 3153675910408849745L;

    EvaluationException(String msg) {
        super(msg);
    }

}
