package org.usfirst.frc.team1089.main;

import edu.wpi.first.networktables.NetworkTableInstance;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Client extends Application {
    private static NetworkTableInstance ntInstance;

    static {
        // Load native libraries and whatnot
        System.loadLibrary("ntcore");
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = FXMLLoader.load(getClass().getResource("gui/AutonBuilder.fxml"));
        primaryStage.setTitle("Mercury Auton Builder");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static NetworkTableInstance getNT() {
        if (ntInstance == null) {
            ntInstance = NetworkTableInstance.getDefault();
            ntInstance.setServerTeam(1089);
            ntInstance.startClient();
        }

        return ntInstance;
    }
}
