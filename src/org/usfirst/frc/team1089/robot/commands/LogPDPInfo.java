package org.usfirst.frc.team1089.robot.commands;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.subsystems.PDP;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogPDPInfo extends Command {
	private static final DateTimeFormatter fileTimestampFormat = DateTimeFormatter.ofPattern("MMdd_HHmmss");
	private static final DateTimeFormatter statusTimestampFormat = DateTimeFormatter.ofPattern("HH:mm:ss.SSS a");
	private static final double MILLIS_BETWEEN_LOGS = 1_000;
	private static final String CSV_HEADER;

	static {
		StringBuilder sb = new StringBuilder("Time,TimeMillis,TotalCurrent,Temperature,TotalEnergy,TotalPower,Voltage");
		for(int i = 0; i < 16; i++) {
			sb.append(",Channel").append(i);
		}
		CSV_HEADER = sb.toString();
	}

	private static String getCSVStatusLine(PowerDistributionPanel panel) {
		LocalDateTime now = LocalDateTime.now();
		StringBuilder sb = new StringBuilder(statusTimestampFormat.format(now));
		sb.append(',').append(System.currentTimeMillis()).append(',');
		sb.append(',').append(panel.getTotalCurrent());
		sb.append(',').append(panel.getTemperature());
		sb.append(',').append(panel.getTotalEnergy());
		sb.append(',').append(panel.getTotalPower());
		sb.append(',').append(panel.getVoltage());
		for (int channel = 0; channel < 16; channel++) {
			sb.append(',').append(panel.getCurrent(channel));
		}
		return sb.toString();
	}

	private PDP pdpSubsystem;
	private PrintWriter writer;
	private long lastLogTime;
	private File outputDir;
	private Logger log = LogManager.getLogger(LogPDPInfo.class);

	public LogPDPInfo() {
		this(new File("/home/lvuser/logs"));
	}

	public LogPDPInfo(File outputDir) {
		this.outputDir = outputDir;
		requires(Robot.pdp);
		log.debug("LogPDPInfo command created");
	}
	
	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		pdpSubsystem = Robot.pdp;
		initializeLogFile();
	}

	//Initialize the logging file, using date
	private void initializeLogFile() {
		if (writer == null) {
			String fileName = "pdp-" + fileTimestampFormat.format(LocalDateTime.now()) + ".csv";
			File logFile = new File(outputDir, fileName);
			log.info("Creating new pdp log file " + fileName);
			try {
				writer = new PrintWriter(new BufferedWriter(new FileWriter(logFile)));
				writer.println(CSV_HEADER);
			} catch (IOException e) {
				log.error("Could not create pdp log file", e);
			}
		}
	}
	
	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		if (writer == null) return; //although never expected, this will prevent exceptions
		long currentMillis = System.currentTimeMillis();

		//Checks if MILLIS_BETWEEN_LOGS milliseconds has passed since last line
		if (currentMillis >= lastLogTime + MILLIS_BETWEEN_LOGS) {
			writer.println(getCSVStatusLine(pdpSubsystem.pdpPanel));
			lastLogTime = currentMillis;
}
	}

	private void closeLogFile() {
		if (writer != null) {
			writer.flush();
			writer.close();
			writer = null;
			lastLogTime = 0;
			log.info("PDP log file closed");
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return writer == null; //prevent execute being called when we don't have a file to write to
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		closeLogFile();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
		end();
	}
}
