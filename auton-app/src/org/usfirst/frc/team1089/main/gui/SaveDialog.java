package org.usfirst.frc.team1089.main.gui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SaveDialog {

    private List<String> selectedTablesKey = new ArrayList<>();

    private File chosenFile;
    private ChoiceDialog dialogBase = new ChoiceDialog();
    private GridPane contentPane = new GridPane();

    public SaveDialog() {
        //Set up the dialog base.
        dialogBase.setTitle("Saving...");
        dialogBase.setHeaderText("Select the table(s) to save:");

        contentPane.setPadding(new Insets(10,10,10,10));

        CheckBox checkBoxLLL = new CheckBox("Table LLL");
        CheckBox checkBoxLRL = new CheckBox("Table LRL");
        CheckBox checkBoxRLR = new CheckBox("Table RLR");
        CheckBox checkBoxRRR = new CheckBox("Table RRR");

        Label identifier = new Label("File Path:");
        TextField pathField = new TextField();
        Button browseButton = new Button("Browse");

        contentPane.add(checkBoxLLL, 0,0);
        contentPane.add(checkBoxLRL, 0, 1);
        contentPane.add(checkBoxRLR, 0, 2);
        contentPane.add(checkBoxRRR, 0, 3);

        contentPane.add(identifier, 0,4);
        contentPane.add(pathField, 1, 4);
        contentPane.add(browseButton,2,4);

        identifier.setPadding(new Insets(10, 0, 10, 3));

        browseButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            fileChooser.setInitialFileName(".csv");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Comma Separated Values", "*.csv"));
            chosenFile = fileChooser.showSaveDialog(dialogBase.getDialogPane().getScene().getWindow());
            pathField.setText(chosenFile.getAbsolutePath());
            pathField.setMaxHeight(Double.POSITIVE_INFINITY);
        });

        dialogBase.getDialogPane().setContent(contentPane);


    }

    public void show() {
        dialogBase.showAndWait();

        for (Node n : contentPane.getChildren()) {
            if (n instanceof CheckBox) {
                CheckBox cb = (CheckBox) n;
                String text = cb.getText();
                if (cb.isSelected()) {
                    selectedTablesKey.add(text.substring(text.indexOf(" ") + 1, text.length()));
                }
            } else if (n instanceof TextField) {
                TextField tf = (TextField) n;
                chosenFile = new File(tf.getText());
            }
        }
    }

    public String[] getCheckBoxResult() {
        return selectedTablesKey.toArray(new String[selectedTablesKey.size()]);
    }

    public File getFileResult() {
        return chosenFile;
    }
}
