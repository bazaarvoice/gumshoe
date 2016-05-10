package com.bazaarvoice.gumshoe;

import java.io.PrintStream;
import java.util.Map;

public class PrintStreamPublisher implements Publisher {
    private PrintStream printStream;

    public PrintStreamPublisher() {
        this(System.out);
    }

    public PrintStreamPublisher(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void publish(Map<String, Object> event) {
        this.printStream.println(pp(event));
    }

    private String pp(Map<String, Object> event) {
        StringBuffer buff = new StringBuffer("{\n");
        for (String key : event.keySet()) {
            buff.append(String.format("  %s: %s,\n", key, event.get(key)));
        }
        buff.deleteCharAt(buff.length() - 2);
        buff.append("}");
        return buff.toString();
    }
}
