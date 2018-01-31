package org.usfirst.frc.team1089.shuffleboard.datatype;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.shuffleboard.api.data.ComplexData;

/**
 * FMSData is a wrapper class used to bundle the String given by the FMS and the team's current alliance color for the
 * use of the FMSViewer widget.
 */
public final class FMSData extends ComplexData<FMSData>{
	private String positions;
	private Alliance alliance; 
	
	public FMSData(String positions, Alliance alliance) {
		this.positions = positions;
		this.alliance = alliance;
	}

	public Map<String, Object> asMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("positions", this.positions);
		map.put("alliance", this.alliance);
		return map;
	}
	
	public String getPositions() {
		return positions;
	}
	
	public Alliance getAlliance() {
		return alliance;
	}
}
