package org.usfirst.frc.team1089.main.gui;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.usfirst.frc.team1089.main.AutonBuilder;
import org.usfirst.frc.team1089.main.util.TaskConfig;
import org.usfirst.frc.team1089.main.util.*;

import java.io.File;

public class AutonBuilderController2 {

    private AutonBuilder backend;

    @FXML
    private VBox root;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu fileMenu, editMenu, helpMenu;

    @FXML
    private MenuItem saveMenuItem, loadMenuItem, moveToMenuItem, aboutMenuItem;

    @FXML
    private FlowPane headerRoot;

    private ToggleGroup radioGroup = new ToggleGroup();

    @FXML
    private RadioButton leftRadioButton, middleLeftRadioButton, middleRightRadioButton, rightRadioButton;

    @FXML
    private Slider sliderLLL, sliderLRL, sliderRLR, sliderRRR;

    @FXML
    private GridPane contentRoot;

    @FXML
    private TableView tableLLL, tableLRL, tableRLR, tableRRR;

    @FXML
    private TableColumn
            taskColLLL, sideColLLL,
            taskColLRL, sideColLRL,
            taskColRLR, sideColRLR,
            taskColRRR, sideColRRR;

    private ObservableList<TaskConfig>
            dataLLL = FXCollections.observableArrayList(),
            dataLRL = FXCollections.observableArrayList(),
            dataRLR = FXCollections.observableArrayList(),
            dataRRR = FXCollections.observableArrayList();

    @FXML
    private Button
            addRowButtonLLL, deleteRowButtonLLL,
            addRowButtonLRL, deleteRowButtonLRL,
            addRowButtonRLR, deleteRowButtonRLR,
            addRowButtonRRR, deleteRowButtonRRR;

    @FXML
    public void initialize() {
        backend = new AutonBuilder(dataLLL, dataLRL, dataRLR, dataRRR);

        //Add each button to the group so that only one can be selected.
        leftRadioButton.setToggleGroup(radioGroup);
        middleLeftRadioButton.setToggleGroup(radioGroup);
        middleRightRadioButton.setToggleGroup(radioGroup);
        rightRadioButton.setToggleGroup(radioGroup);

        //Add a listener so that selecting a new starting position will automatically update the backend.
        radioGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            RadioButton selectedButton = ((RadioButton) newValue);
            backend.setStartingPosition(AutonPosition.fromString(selectedButton.getText()));
        });

        sliderLLL.setDisable(true);
        sliderRRR.setDisable(true);

        sliderLRL.setOnMouseClicked(this::handleSliderClick);
        sliderRRR.setOnMouseClicked(this::handleSliderClick);

        //Formatting
        taskColLLL.setSortable(false);
        sideColLLL.setSortable(false);
        taskColLRL.setSortable(false);
        sideColLRL.setSortable(false);
        taskColRLR.setSortable(false);
        sideColRLR.setSortable(false);
        taskColRRR.setSortable(false);
        sideColRRR.setSortable(false);

        taskColLLL.setResizable(false);
        sideColLLL.setResizable(false);
        taskColLRL.setResizable(false);
        sideColLRL.setResizable(false);
        taskColRLR.setResizable(false);
        sideColRLR.setResizable(false);
        taskColRRR.setResizable(false);
        sideColRRR.setResizable(false);

        //Set up cell factories to create the cells we need for the TableViews.
        Callback<TableColumn<TaskConfig, TaskConfig.AutonTask>, TableCell> autonTaskCellFactory = param -> {
            final ComboBoxTableCell<TaskConfig, TaskConfig.AutonTask> comboBoxTableCell = new ComboBoxTableCell<>(FXCollections.observableArrayList(TaskConfig.AutonTask.values()));
            comboBoxTableCell.setEditable(true);
            comboBoxTableCell.setComboBoxEditable(false);
            return comboBoxTableCell;
        };

        Callback<TableColumn<TaskConfig, TaskConfig.ScoringSide>, TableCell> scoringSideCellFactory = param -> {
            ComboBoxTableCell<TaskConfig, TaskConfig.ScoringSide> comboBoxTableCell = new ComboBoxTableCell<>(FXCollections.observableArrayList(TaskConfig.ScoringSide.values()));
            comboBoxTableCell.setEditable(true);
            comboBoxTableCell.setComboBoxEditable(false);
            return comboBoxTableCell;
        };

        taskColLLL.setCellFactory(autonTaskCellFactory);
        taskColLRL.setCellFactory(autonTaskCellFactory);
        taskColRLR.setCellFactory(autonTaskCellFactory);
        taskColRRR.setCellFactory(autonTaskCellFactory);

        sideColLLL.setCellFactory(scoringSideCellFactory);
        sideColLRL.setCellFactory(scoringSideCellFactory);
        sideColRLR.setCellFactory(scoringSideCellFactory);
        sideColRRR.setCellFactory(scoringSideCellFactory);

        taskColLLL.setCellValueFactory((Callback<TableColumn.CellDataFeatures<TaskConfig, TaskConfig.AutonTask>, ObservableValue>) param -> param.getValue().autonTask);
        taskColLRL.setCellValueFactory((Callback<TableColumn.CellDataFeatures<TaskConfig, TaskConfig.AutonTask>, ObservableValue>) param -> param.getValue().autonTask);
        taskColRLR.setCellValueFactory((Callback<TableColumn.CellDataFeatures<TaskConfig, TaskConfig.AutonTask>, ObservableValue>) param -> param.getValue().autonTask);
        taskColRRR.setCellValueFactory((Callback<TableColumn.CellDataFeatures<TaskConfig, TaskConfig.AutonTask>, ObservableValue>) param -> param.getValue().autonTask);

        sideColLLL.setCellValueFactory((Callback<TableColumn.CellDataFeatures<TaskConfig, TaskConfig.ScoringSide>, ObservableValue>) param -> param.getValue().scoringSide);
        sideColLRL.setCellValueFactory((Callback<TableColumn.CellDataFeatures<TaskConfig, TaskConfig.ScoringSide>, ObservableValue>) param -> param.getValue().scoringSide);
        sideColRLR.setCellValueFactory((Callback<TableColumn.CellDataFeatures<TaskConfig, TaskConfig.ScoringSide>, ObservableValue>) param -> param.getValue().scoringSide);
        sideColRRR.setCellValueFactory((Callback<TableColumn.CellDataFeatures<TaskConfig, TaskConfig.ScoringSide>, ObservableValue>) param -> param.getValue().scoringSide);


        //Set up events

        addRowButtonLLL.setOnAction(event -> addNewRow(tableLLL));
        addRowButtonLRL.setOnAction(event -> addNewRow(tableLRL));
        addRowButtonRLR.setOnAction(event -> addNewRow(tableRLR));
        addRowButtonRRR.setOnAction(event -> addNewRow(tableRRR));

        deleteRowButtonLLL.setOnAction(event -> deleteSelectedRow(tableLLL));
        deleteRowButtonLRL.setOnAction(event -> deleteSelectedRow(tableLRL));
        deleteRowButtonRLR.setOnAction(event -> deleteSelectedRow(tableRLR));
        deleteRowButtonRRR.setOnAction(event -> deleteSelectedRow(tableRRR));

        saveMenuItem.setOnAction(event -> openSaveDialog());
        loadMenuItem.setOnAction(event -> openLoadDialog());
        moveToMenuItem.setOnAction(event -> openMoveToDialog());
        aboutMenuItem.setOnAction(event -> openAbout());

        leftRadioButton.setOnAction(event -> backend.setStartingPosition(AutonPosition.LEFT));
        middleLeftRadioButton.setOnAction(event -> backend.setStartingPosition(AutonPosition.LEFT_MID));
        middleLeftRadioButton.setOnAction(event -> backend.setStartingPosition(AutonPosition.RIGHT_MID));
        rightRadioButton.setOnAction(event -> backend.setStartingPosition(AutonPosition.RIGHT));

        tableLLL.setOnKeyPressed(this::handleKeyPressed);
        tableLRL.setOnKeyPressed(this::handleKeyPressed);
        tableRLR.setOnKeyPressed(this::handleKeyPressed);
        tableRRR.setOnKeyPressed(this::handleKeyPressed);

        //Set the tables to use their respective lists.
        tableLLL.setItems(dataLLL);
        tableLRL.setItems(dataLRL);
        tableRLR.setItems(dataRLR);
        tableRRR.setItems(dataRRR);

        //Each table should start with a row for convenience.
        addNewRow(tableLLL);
        addNewRow(tableLRL);
        addNewRow(tableRLR);
        addNewRow(tableRRR);
    }

    private void handleSliderClick(MouseEvent mouseEvent) {
        Slider source = (Slider) mouseEvent.getSource();
        String sourceConfigKey = source.getId().substring(source.getId().length() -3, source.getId().length()); //Every table specific node's id ends with the fms data key. Get the last 3 characters to get it.
        backend.setFieldSide(sourceConfigKey, FieldSide.values()[((int) source.getValue())]);
    }

    private void handleKeyPressed(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();
        TableView source = ((TableView) keyEvent.getSource());

        if (code == KeyCode.INSERT) {
            addNewRow(source);
        } else if (code == KeyCode.DELETE) {
            deleteSelectedRow(source);
        }
    }

    private void deleteSelectedRow(TableView table) {
        int selectedIndex = table.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            table.getItems().remove(selectedIndex);
        }
    }

    private void addNewRow(TableView table) {
        table.getItems().add(new TaskConfig(TaskConfig.AutonTask.SCORE_SCALE, TaskConfig.ScoringSide.FRONT));
    }



    private void openSaveDialog() {
        SaveDialog saveDialog = new SaveDialog();
        saveDialog.show();
        String[] tableKeyResult = saveDialog.getCheckBoxResult();
        File chosenFile = saveDialog.getFileResult();

        if (chosenFile.getParentFile().exists() && tableKeyResult[0] != null) {
            System.out.println(backend.save(chosenFile, tableKeyResult));
        }
    }


    private void openLoadDialog() {

    }

    private void openMoveToDialog() {

    }

    private void openAbout() {

    }
}
