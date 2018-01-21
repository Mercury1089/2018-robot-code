package org.usfirst.frc.team1089.shuffleboard.datatype;

import java.util.Map;
import java.util.function.Function;

import edu.wpi.first.shuffleboard.api.data.ComplexDataType;

public class FMSDataType extends ComplexDataType<FMSData> {

	private static final String NAME = "FMSData";
	public static final FMSDataType Instance = new FMSDataType();
	
	public FMSDataType() {
		super(NAME, FMSData.class);
	}

	@Override
	public Function<Map<String, Object>, FMSData> fromMap() {
			return map -> {
				return new FMSData(map.getOrDefault("positions", "RRR").toString(), (Alliance) map.getOrDefault("alliance", Alliance.BLUE));
			};
	}

	@Override
	public FMSData getDefaultValue() {
		return new FMSData("RRR", Alliance.BLUE);
	}
	
}
