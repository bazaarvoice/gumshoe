package com.bazaarvoice.gumshoe;

public class Attribute {
    public static String named(String name) {
        return String.format("gs$%s", name);
    }

    public static String asPath(String...parts) {
        StringBuffer buff = new StringBuffer();
        for (String part : parts) {
            buff.append(part);
            buff.append(" > ");
        }
        buff.delete(buff.length() - 3, buff.length());
        return buff.toString();
    }
}
