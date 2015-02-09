package com.adamheins.diff.parser;

/**
 * Base class for Diff parsing exceptions.
 */
public class ParsingException extends Exception {

    private static final long serialVersionUID = 1L;

    public ParsingException(String msg) {
        super(msg);
    }

}
