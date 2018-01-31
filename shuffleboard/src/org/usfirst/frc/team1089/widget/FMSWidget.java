package org.usfirst.frc.team1089.widget;

import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.fxml.FXML;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.fxmisc.easybind.EasyBind;
import org.usfirst.frc.team1089.data.FMSData;

@Description(name = "FMS", dataTypes = FMSData.class)
@ParametrizedController("FMSWidget.fxml")
public class FMSWidget extends SimpleAnnotatedWidget<FMSData> {
    private final Color
            RED = new Color(1.0, 0.0, 0.0, 1.0),
            BLUE = new Color(0.0, 0.0, 1.0, 1.0);

    @FXML
    private Pane root;

    @FXML
    private Pane switchA, switchB;

    @FXML
    private void initialize() {
        switchA.backgroundProperty().bind(
            EasyBind.monadic(dataOrDefault).
                    map(this::getSwitchFarL)
        );
    }

    private Color getAllianceColor(FMSData data) {
        return "R".equals(data.getAlliance()) ? RED : BLUE;
    }

    private Color getEnemyColor(FMSData data) {
        return "R".equals(data.getAlliance()) ? BLUE : RED;
    }

    private Background getSwitchFarL(FMSData data) {
        BackgroundFill fill;
        Color alliance = getAllianceColor(data), enemy = getEnemyColor(data);
        char side = data.getData().charAt(0);

        if (side == 'L')
            fill = new BackgroundFill(alliance, null, null);
        else
            fill = new BackgroundFill(enemy, null, null);

        return new Background(fill);

    }

    private Background getSwitchFarR(FMSData data) {
        BackgroundFill fill;
        Color alliance = getAllianceColor(data), enemy = getEnemyColor(data);
        char side = data.getData().charAt(0);

        if (side == 'R')
            fill = new BackgroundFill(alliance, null, null);
        else
            fill = new BackgroundFill(enemy, null, null);

        return new Background(fill);
    }

//    private Background getScaleL(FMSData data) {
//        String alliance = data.getAlliance();
//        String side
//    }
//
//    private Background getScaleR(FMSData data) {
//        String alliance = data.getAlliance();
//        String side
//    }
//
//    private Background getSwitchNearL(FMSData data) {
//        String alliance = data.getAlliance();
//        String side
//    }
//
//    private Background getSwitchNearL(FMSData data) {
//        String alliance = data.getAlliance();
//        String side
//    }

    @Override
    public Pane getView() {
        return root;
    }
}
