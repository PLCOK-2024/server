package com.example.demo.common.extension;

public class StringExtension {
    public static String camelCaseToSnakeCase(String str) {
        return str.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
