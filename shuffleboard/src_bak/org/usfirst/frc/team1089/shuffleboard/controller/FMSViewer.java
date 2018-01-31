package org.usfirst.frc.team1089.shuffleboard.controller;

import org.usfirst.frc.team1089.shuffleboard.datatype.Alliance;
import org.usfirst.frc.team1089.shuffleboard.datatype.FMSData;

import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
/**
 * 
 * @author Sagar P
 *
 * FMSViewer is a widget that displays the colors of both switches and the scale on both of their respective sides based on the FMS data.
 */
@Description(dataTypes = String.class, name = "FMS Viewer")
@ParametrizedController("FMSViewer.fxml")
public final class FMSViewer extends SimpleAnnotatedWidget<FMSData> {

	@FXML
	private Pane root;

	@FXML
	private Pane
		enemySwitchLeft,
		enemySwitchRight,
		scaleLeft,
		scaleRight,
		allySwitchLeft,
		allySwitchRight;

	private String ourAlliance, enemyAlliance;
	
	@Override
	public Pane getView() {
		return (Pane) root;
	}	
	
	@FXML
	private void initialize() {
		final FMSData data = getData();
		
		//Get the String with the positions from the data and break it down into its parts. 
		final String positions = data.getPositions();
		final char allySwitchData = positions.charAt(0);
		final char scaleData = positions.charAt(1);
		final char enemySwitchData = positions.charAt(2);
		
		//Determine the color of both alliances 
		ourAlliance = data.getAlliance() == Alliance.BLUE ? "blue" : "red";
		enemyAlliance = data.getAlliance() == Alliance.BLUE ? "red" : "blue";
		
		// Set the colors of each side of both switches and the scale.
		enemySwitchLeft.setStyle("-fx-background-color: " + determineColor('L', enemySwitchData) + ";");
		enemySwitchRight.setStyle("-fx-background-color: " + determineColor('R', enemySwitchData) + ";");
		scaleLeft.setStyle("-fx-background-color: " + determineColor('L', scaleData) + ";");
		scaleRight.setStyle("-fx-background-color: " + determineColor('R', scaleData) + ";");
		allySwitchLeft.setStyle("-fx-background-color: " + determineColor('L', allySwitchData) + ";");
		allySwitchRight.setStyle("-fx-background-color: " + determineColor('R', allySwitchData) + ";");
	}
	
	/**
	 * 
	 * @param side The side of the switch/scale whose color needs to be determined
	 * @param datum The specific char that signals which side is our alliance's color.
	 * @return The color of the given side as a String.
	 */
	private String determineColor(char side, char datum) {
		return side == datum ? ourAlliance : enemyAlliance;
	}
}
	