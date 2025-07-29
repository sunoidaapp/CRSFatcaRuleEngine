package com.vision.config;

import java.io.File;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;

@Configuration
public class LogConfig {

	@Value("${regtech.CrsFatcaRule.LogPath}")
	private String logDir;

	@Value("${logging.level.com.vision:INFO}")
	private String logLevel;

	@Value("${regtech.CrsFatcaRule.LogFileName:CrsFatcaLog}")
	private String logFileName;

	private static final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

	@Bean
	Logger configureLogging() {
		return setupLogger();
	}

	private Logger setupLogger() {

		ensureLogDirectoryExists(logDir);

		loggerContext.reset(); // Clears previous log settings

		RollingFileAppender<ILoggingEvent> fileAppender = new RollingFileAppender<>();
		fileAppender.setContext(loggerContext);

		// Dynamically determine log file format based on environment setting
		String filePattern = logDir + logFileName + "_%d{yyyy-MM-dd}.log";

		TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy<>();
		rollingPolicy.setContext(loggerContext);
		rollingPolicy.setFileNamePattern(filePattern);
//		rollingPolicy.setMaxHistory(30); // Keep logs for 30 days
		rollingPolicy.setParent(fileAppender);
		rollingPolicy.start();

		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setContext(loggerContext);
//        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n");
		encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level - %msg%n");
		encoder.start();

		fileAppender.setEncoder(encoder);
		fileAppender.setRollingPolicy(rollingPolicy);
		fileAppender.start();

		Logger visionLogger = (Logger) LoggerFactory.getLogger("com.vision");
		visionLogger.detachAndStopAllAppenders();
		visionLogger.addAppender(fileAppender);
		visionLogger.setLevel(Level.toLevel(logLevel));

		return visionLogger;
	}

	private void ensureLogDirectoryExists(String dirPath) {
		File dir = new File(dirPath);
		if (!dir.exists()) {
			boolean created = dir.mkdirs();
			if (!created) {
				System.err.println("Failed to create log directory: " + dirPath);
			}
		}
	}
}
