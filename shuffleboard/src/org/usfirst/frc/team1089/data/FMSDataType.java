package org.usfirst.frc.team1089.data;

import edu.wpi.first.shuffleboard.api.data.ComplexDataType;
import edu.wpi.first.shuffleboard.api.util.Maps;

import java.util.Map;
import java.util.function.Function;

public class FMSDataType extends ComplexDataType<FMSData> {
    public static final FMSDataType inst = new FMSDataType();

    private FMSDataType() {
        super("FMSData", FMSData.class);
    }

    @Override
    public Function<Map<String, Object>, FMSData> fromMap() {
        return map -> new FMSData(
            Maps.get(map, "data"),
            Maps.get(map, "alliance")
        );
    }

    @Override
    public FMSData getDefaultValue() {
        return new FMSData("RRR", "B");
    }
}
