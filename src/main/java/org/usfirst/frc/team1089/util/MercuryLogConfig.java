package org.usfirst.frc.team1089.util;

import java.net.URI;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

/**
 * Programmed configuration for logger. This is used during program init
 * to set the configuration of the logger since we can't load a config file
 * from the classpath.
 */
public class MercuryLogConfig extends ConfigurationFactory {

	static Configuration createConfiguration(final String name, ConfigurationBuilder<BuiltConfiguration> builder) {
        builder.setConfigurationName(name);
        builder.setStatusLevel(Level.WARN);
       
        // Console Appender
        AppenderComponentBuilder consoleAppender = builder.newAppender("Console", "Console")
    		.addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
        
        consoleAppender.add(builder.newLayout("PatternLayout")
    		.addAttribute("pattern", "%highlight{%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n}"));
        
        AppenderComponentBuilder rollingFileAppender = builder.newAppender("RollingFile", "RollingFile")
    		.addAttribute("fileName", "/home/lvuser/logs/robot.log")
    		.addAttribute("filePattern", "/home/lvuser/logs/robot-%i.log");
        
        rollingFileAppender.add(builder.newLayout("PatternLayout")
    		.addAttribute("pattern", "%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"));
        
        rollingFileAppender.addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "10 MB"));
        
        builder.add(consoleAppender);
        
        RootLoggerComponentBuilder root = builder.newRootLogger(Level.INFO);
        root.add(builder.newAppenderRef("Console"));
        root.add(builder.newAppenderRef("RollingFile"));
        
        
        builder.add(builder.newRootLogger(Level.INFO).add(builder.newAppenderRef("Console")));
        
        return builder.build();
    }
	
	@Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
        return getConfiguration(loggerContext, source.toString(), null);
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final String name, final URI configLocation) {
        ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();
        return createConfiguration(name, builder);
    }

	@Override
    protected String[] getSupportedTypes() {
        return new String[] {"*"};
    }
}
