package org.usfirst.frc.team1089.shuffleboard;

import java.util.List;

import org.usfirst.frc.team1089.shuffleboard.controller.FMSViewer;
import org.usfirst.frc.team1089.shuffleboard.datatype.FMSDataType;

import com.google.common.collect.ImmutableList;

import edu.wpi.first.shuffleboard.api.data.DataType;
import edu.wpi.first.shuffleboard.api.plugin.Description;
import edu.wpi.first.shuffleboard.api.plugin.Plugin;
import edu.wpi.first.shuffleboard.api.widget.ComponentType;
import edu.wpi.first.shuffleboard.api.widget.WidgetType;

@Description(group = "org.usfirst.frc.team1089",
			 name = "MercuryPlugin",
			 summary = "Defines widgets used by Team Mercury 1089",
		     version = "1.0.0")
public class MercuryPlugin extends Plugin {

	@Override
	public List<ComponentType> getComponents() {
		return ImmutableList.of(WidgetType.forAnnotatedWidget(FMSViewer.class));
	}
		
	@Override
	public List<DataType> getDataTypes() {
	    return ImmutableList.of(FMSDataType.Instance);
	}

	@Override
	public void onLoad() {
		super.onLoad();
	}
}
