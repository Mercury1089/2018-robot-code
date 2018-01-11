package org.usfirst.frc.team1089.util;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShuffleDash {
	
	public ShuffleDash() {
		
	}
	public void updateDash() {
		SmartDashboard.putString("FMS Data", DriverStation.getInstance().getGameSpecificMessage());
		SmartDashboard.putString("Alliance Color", DriverStation.getInstance().getAlliance().toString());
	}
}
