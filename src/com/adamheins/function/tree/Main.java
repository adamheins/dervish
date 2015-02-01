package com.adamheins.function.tree;

import org.apfloat.Apfloat;

public class Main {
    
    public static void main(String[] args) {
        Apfloat num1 = new Apfloat("4");
        Apfloat num2 = new Apfloat("8");
        
        System.out.println(num1.add(num2).toString(true));
    }
}
