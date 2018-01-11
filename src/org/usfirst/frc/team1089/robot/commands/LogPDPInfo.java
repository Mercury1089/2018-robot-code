package org.usfirst.frc.team1089.robot.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.subsystems.PDP;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Command;

public class LogPDPInfo extends Command {
	private static final DateTimeFormatter fileTimestampFormat = DateTimeFormatter.ofPattern("MMdd_HHmmss");
	private static final DateTimeFormatter statusTimestampFormat = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	private static final double MILLIS_BETWEEN_LOGS = 1_000;
	
	private PDP pdpSubsystem;
	private PrintWriter writer;
	private long lastLogTime;
	
	private static final File OUTPUT_DIR = new File("/home/lvuser/logs");
	private static final String CSV_HEADER;
	
	static {
		StringBuilder sb = new StringBuilder("Time,TotalCurrent,Temperature,TotalEnergy,TotalPower,Voltage");
		for(int i = 0; i < 16; i++) {
			sb.append(",Channel ").append(i);
		}
		CSV_HEADER = sb.toString();
		
		if (!OUTPUT_DIR.exists())
			OUTPUT_DIR.mkdirs();
	}
	
	public LogPDPInfo() {
		requires(Robot.pdp);
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
			File logFile = new File(OUTPUT_DIR, fileName);
				
			try {
				writer = new PrintWriter(new BufferedWriter(new FileWriter(logFile)));
				writer.println(CSV_HEADER);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		boolean canWrite = writer != null;
		
		if (canWrite) {
			long currentMillis = System.currentTimeMillis();
			//Checks if MILLIS_BETWEEN_LOGS milliseconds has passed since last line
			if (currentMillis >= lastLogTime + MILLIS_BETWEEN_LOGS) {
				// initializeLogFile();
				writer.println(getCSVStatusLine());
				lastLogTime = currentMillis;
			}
		}
	}
	
	//Gets PDP status in CSV format.
	private String getCSVStatusLine() {
		StringBuilder sb = new StringBuilder();
		PowerDistributionPanel panel = pdpSubsystem.pdpPanel;
		sb.append(String.format("%s,%.2f,%.2f,%.2f,%.2f,%.2f", statusTimestampFormat.format(LocalDateTime.now()),
				panel.getTotalCurrent(),
				panel.getTemperature(),
				panel.getTotalEnergy(),
				panel.getTotalPower(),
				panel.getVoltage()));
		for(int i = 0; i < 16; i++) {
			sb.append(String.format(",%.2f", panel.getCurrent(i)));
		}
		return sb.toString();
	}
	

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		closeLogFile();
	}
	
	private void closeLogFile() {
		if (writer != null) {
			writer.flush();
			writer.close();
			writer = null;
			lastLogTime = 0;
		}
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
		end();
	}
}
