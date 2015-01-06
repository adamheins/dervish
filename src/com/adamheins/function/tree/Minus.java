package com.adamheins.function.tree;

import java.math.BigDecimal;
import java.util.Map;

public class Minus extends Function {

    public Minus() {
        super("-", Precedence.ADDITION, Associativity.LEFT);
    }


    @Override
    public Function evaluate(Map<String, Function> varMap) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public Function differentiate(String var) {
        // TODO Auto-generated method stub
        return null;
    }
}
