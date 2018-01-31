package org.usfirst.frc.team1089.data;

import edu.wpi.first.shuffleboard.api.data.ComplexData;
import edu.wpi.first.shuffleboard.api.util.Maps;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class FMSData extends ComplexData<FMSData> {
    private final String DATA, ALLIANCE;

    public FMSData(@NotNull String data, @NotNull String alliance) {
        if (data.length() != 3) {
            // Something went wrong, define default.
            data = "RRR";
        }

        if (!"R".equals(alliance) || !"B".equals(alliance)) {
            // Something went wrong, define default.
            alliance = "B";
        }

        DATA = data;
        ALLIANCE = alliance;
    }

    public String getData() {
        return DATA;
    }

    public String getAlliance() {
        return ALLIANCE;
    }

    public String test(int i) {
        return "";
    }

    @Override
    public Map<String, Object> asMap() {
        return Maps.<String, Object>builder()
            .put("data", DATA)
            .put("alliance", ALLIANCE)
            .build();
    }
}
