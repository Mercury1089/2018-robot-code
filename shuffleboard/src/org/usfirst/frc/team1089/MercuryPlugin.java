package org.usfirst.frc.team1089;

import com.google.common.collect.ImmutableList;
import edu.wpi.first.shuffleboard.api.data.DataType;
import edu.wpi.first.shuffleboard.api.plugin.Description;
import edu.wpi.first.shuffleboard.api.plugin.Plugin;
import edu.wpi.first.shuffleboard.api.widget.ComponentType;
import edu.wpi.first.shuffleboard.api.widget.WidgetType;
import org.usfirst.frc.team1089.data.FMSDataType;
import org.usfirst.frc.team1089.widget.FMSWidget;

import java.util.List;

@Description(
        group = "org.usfirst.frc.team1089",
        name = "MercuryPlugin",
        version = "0.1.0",
        summary = "Defines collection of custom widgets for use in 2018."
)
public class MercuryPlugin extends Plugin {
    @Override
    public List<DataType> getDataTypes() {
        return ImmutableList.of(
            FMSDataType.inst
        );
    }

    @Override
    public List<ComponentType> getComponents() {
        return ImmutableList.of(
            WidgetType.forAnnotatedWidget(FMSWidget.class)
        );
    }

    @Override
    public void onLoad() {
        super.onLoad();
        System.out.println("wow");
    }
}
