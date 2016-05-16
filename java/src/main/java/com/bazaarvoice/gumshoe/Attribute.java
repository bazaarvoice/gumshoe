package com.bazaarvoice.gumshoe;

public class Attribute {
    public static String named(String name) {
        return String.format("@%s", name);
    }
}
