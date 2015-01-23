package com.adamheins.function.tree;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String args[]) {
        String[] arr = {"one", "two", "three"};
        List<String> list = Arrays.asList(arr);
        list.remove(0);
        System.out.println(list);
    }
}
