package com.bazaarvoice.gumshoe;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TimeZone;

/**
 * Decorator that adds hostname, user, pid and thread to events.
 *
 * @author lance.woodson
 *
 */
public class SimpleDecorator implements Decorator {
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss,SSS");
    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
    private String applicationName;
    private String hostname;
    private String user;
    private String pid;

    public SimpleDecorator(String applicationName) {
        this.applicationName = applicationName;
    }

    @Override
    public Map<String, Object> decorate(Map<String, Object> event) {
        event.put(Attribute.named("application"), getApplicationName());
        event.put(Attribute.named("hostname"), getHostname());
        event.put(Attribute.named("user"), getUser());
        event.put(Attribute.named("pid"), getPid());
        event.put(Attribute.named("thread"), getThreadName());
        event.put(Attribute.named("emitted_at"), getCurrentTime());

        return event;
    }

    private String getApplicationName() {
        if (applicationName == null) {
            return "UNKNOWN";
        } else {
            return applicationName;
        }
    }

    private String getHostname() {
        try {
            if (hostname == null) {
                hostname = InetAddress.getLocalHost().getHostName();
            }
            return hostname;
        } catch (UnknownHostException e) {
            return "UNKNOWN";
        }
    }

    private String getUser() {
        if (user == null) {
            return System.getenv("USER");
        }
        return user;
    }

    private String getPid() {
        try {
            if (pid == null) {
                String[] command = {"bash", "-c", "echo $PPID"};
                byte[] commandOutput = new byte[100];
                Process proc = Runtime.getRuntime().exec(command);
                proc.getInputStream().read(commandOutput);
                pid = new String(commandOutput).split("\n")[0];
            }
        } catch (IOException e) {
            pid = "UNKNOWN";
        }
        return pid;
    }

    private String getThreadName() {
        return Thread.currentThread().getName();
    }

    private String getCurrentTime() {
        return DATE_FORMAT.format(System.currentTimeMillis());
    }
}
