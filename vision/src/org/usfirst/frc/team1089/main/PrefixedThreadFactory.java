package org.usfirst.frc.team1089.main;

import java.util.concurrent.ThreadFactory;

/**
 * ThreadFactory that creates a better naming scheme
 * for the threads.
 */
public class PrefixedThreadFactory implements ThreadFactory {
    private int counter;
    private String prefix;

    public PrefixedThreadFactory() {
        this("Worker");
    }

    public PrefixedThreadFactory(String pref) {
        counter = 0;
        setPrefix(pref);
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, prefix + "-" + counter++);
    }

    public void setPrefix(String p) {
        prefix = p;
    }
}
