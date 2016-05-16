package com.bazaarvoice.gumshoe;

public class Attribute {
    public static String named(String name) {
        return String.format("gs$%s", name);
    }
    
    public static String asPath(String...parts) {
        return String.join(" > ", parts);
    }
}
