/*
 * Copyright (c) 2015 Adam Heins
 *
 * This file is part of the Dervish project, which is distributed under the MIT
 * license. For the full terms, see the included LICENSE file.
 */

package com.adamheins.dervish.parser;

/**
 * Base class for Dervish parsing exceptions.
 */
public class ParsingException extends Exception {

    private static final long serialVersionUID = 1L;

    public ParsingException(String msg) {
        super(msg);
    }
}
