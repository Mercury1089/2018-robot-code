package org.usfirst.frc.team1089.util;

import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Purpose of this class is to reduce logging without removing it from frequently-called periodic methods. <p>
 * 
 * Usage can be something like:
 * 
 * <pre>
 * // in init save it as a field
 * everySecond = new InfrequentLogger(log, 1_000, TimeUnit.Milliseconds);
 * // TODO: think of better naming convention
 * 
 * // in execute call the run method with a lambda
 * everySecond.run(log -> log.info("I'm doing something!"));
 * </pre>
 * 
 * This will only log every 1 second as opposed to every time execute is called.
 *
 */
public class InfrequentLogger {

    private final Logger delegate;
    private final long minTimeBetweenLogs;
    private final TimeUnit minTimeBetweenLogsUnit;
    private long lastLogTime;

    public InfrequentLogger(Logger log, long timeBetweenLogs, TimeUnit timeBetweenLogsUnit) {
        if (log == null)
            throw new IllegalArgumentException("log must not be null!");
        if (timeBetweenLogs < 0)
            throw new IllegalArgumentException("time between logs must not be negative!");

        delegate = log;
        minTimeBetweenLogs = timeBetweenLogs;
        minTimeBetweenLogsUnit = timeBetweenLogsUnit;
    }

    public void run(Consumer<Logger> it) {
        if (minTimeBetweenLogs == 0) {
            it.accept(delegate);
            return;
        }
        long now = System.currentTimeMillis();
        if (now > lastLogTime + minTimeBetweenLogsUnit.toMillis(minTimeBetweenLogs)) {
            it.accept(delegate);
            lastLogTime = now;
        }
    }

}
