/*
 * Copyright (c) 2015 Adam Heins
 *
 * This file is part of the Dervish project, which is distributed under the MIT
 * license. For the full terms, see the included LICENSE file.
 */

package com.adamheins.dervish.function;

/**
 * Class defining mathematical constants.
 *
 * @author Adam
 */
public class Constant extends Number {

    public static final Constant E = new Constant("2.71828182845904523536",
            "e");
    public static final Constant PI = new Constant("3.14159265358979323846",
            "pi");

    // The string representing the constant.
    private String strValue;

    private Constant(String value, String strValue) {
        super(value);
        this.strValue = strValue;
    }


    @Override
    public String toString() {
        return strValue;
    }
}
