package org.usfirst.frc.team1089.robot.subsystems;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.commands.LogPDPInfo;
import org.usfirst.frc.team1089.util.TalonDrive;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Subsystem that encapsulates PDP.
 */
public class PDP extends Subsystem {
	private static Logger log = LogManager.getLogger(PDP.class);
	public PowerDistributionPanel pdpPanel;
	
	public PDP() {
		pdpPanel = new PowerDistributionPanel();
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new LogPDPInfo());
	}
		
}
	
