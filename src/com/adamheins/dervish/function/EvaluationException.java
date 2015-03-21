/*
 * Copyright (c) 2015 Adam Heins
 *
 * This file is part of the Dervish project, which is distributed under the MIT
 * license. For the full terms, see the included LICENSE file.
 */

package com.adamheins.dervish.function;

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
