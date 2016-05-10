package com.bazaarvoice.gumshoe;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * Decorator that adds hostname, user, pid and thread to events.
 *
 * @author lance.woodson
 *
 */
public class SimpleDecorator implements Decorator {
    private String hostname;
    private String user;
    private String pid;

    public SimpleDecorator() {
    }

    @Override
    public Map<String, Object> decorate(Map<String, Object> event) {
        event.put("_hostname", getHostname());
        event.put("_user", getUser());
        event.put("_pid", getPid());
        event.put("_thread", getThreadName());

        return event;
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
                pid = new String(commandOutput);
            }
        } catch (IOException e) {
            pid = "UNKNOWN";
        }
        return pid;
    }

    private String getThreadName() {
        return Thread.currentThread().getName();
    }
}
