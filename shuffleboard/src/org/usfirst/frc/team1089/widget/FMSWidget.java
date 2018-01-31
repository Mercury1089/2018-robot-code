package org.usfirst.frc.team1089.widget;

import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.fxmisc.easybind.EasyBind;
import org.usfirst.frc.team1089.data.FMSData;

@Description(name = "FMS", dataTypes = FMSData.class)
@ParametrizedController("FMSWidget.fxml")
public class FMSWidget extends SimpleAnnotatedWidget<FMSData> {

    @FXML
    private Pane root;

    @FXML
    private Label lblData, lblAlliance;

    @FXML
    private void initialize() {
        lblData.textProperty().bind(
            EasyBind.monadic(dataOrDefault)
                .map(FMSData::getData)
                .orElse("RRR")
        );

        lblAlliance.textProperty().bind(
            EasyBind.monadic(dataOrDefault)
                .map(FMSData::getAlliance)
                .orElse("B")
        );
    }

    @Override
    public Pane getView() {
        return root;
    }
}
