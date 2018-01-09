package org.usfirst.frc.team1089.robot.subsystems;

import org.usfirst.frc.team1089.robot.commands.PDPLoggingCommand;
import org.usfirst.frc.team1089.util.TalonDrive;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Subsystem that encapsulates the drive train.
 * This contains the {@link TalonDrive} needed to drive manually
 * using the Talons.
 */
public class PDPSubsystem extends Subsystem {
	public PowerDistributionPanel pdpPanel;
	
	public PDPSubsystem() {
		pdpPanel = new PowerDistributionPanel();
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new PDPLoggingCommand());
	}
		
}
	
