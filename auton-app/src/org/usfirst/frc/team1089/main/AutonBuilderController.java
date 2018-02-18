package org.usfirst.frc.team1089.main;

import edu.wpi.first.networktables.NetworkTable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;


public class AutonBuilderController {
    @FXML
    private Pane root;

    @FXML
    private Button loadButton;
    @FXML
    private Button saveButton;

    private ToggleGroup radioGroup;
    @FXML
    private RadioButton leftRadioButton;
    @FXML
    private RadioButton middleRadioButton;
    @FXML
    private RadioButton rightRadioButton;

    @FXML
    private TableView tableLLL;
    @FXML
    private TableView tableLRL;
    @FXML
    private TableView tableRLR;
    @FXML
    private TableView tableRRR;
    @FXML
    private TableColumn taskColLLL;
    @FXML
    private TableColumn sideColLLL;
    @FXML
    private TableColumn taskColLRL;
    @FXML
    private TableColumn sideColLRL;
    @FXML
    private TableColumn taskColRLR;
    @FXML
    private TableColumn sideColRLR;
    @FXML
    private TableColumn taskColRRR;
    @FXML
    private TableColumn sideColRRR;

    private StringConverter<AutonTask> autonTaskStringConverter;
    private StringConverter<ScoringSide> scoringSideStringConverter;

    private ObservableList<TaskConfig> dataLLL = FXCollections.observableArrayList();
    private ObservableList<TaskConfig> dataLRL = FXCollections.observableArrayList();
    private ObservableList<TaskConfig> dataRLR = FXCollections.observableArrayList();
    private ObservableList<TaskConfig> dataRRR = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        //Setup up the radio buttons to be in a group.
        radioGroup = new ToggleGroup();
        leftRadioButton.setToggleGroup(radioGroup);
        middleRadioButton.setToggleGroup(radioGroup);
        rightRadioButton.setToggleGroup(radioGroup);


        //Set up StringConverters to convert the AutonTask and ScoringSide Enums into Strings for display.
        autonTaskStringConverter = new StringConverter<AutonTask>() {
            @Override
            public String toString(AutonTask object) {
                if (object == null) {
                    return "";
                }
                switch (object) {
                    case GRAB_CUBE: {
                        return "Grab Cube";
                    }
                    case SCORE_SCALE: {
                        return "Score Scale";
                    }
                    case SCORE_SWITCH: {
                        return "Score Switch";
                    }
                    case DO_NOTHING: {
                        return "Do Nothing";
                    }
                    case DELETE: {
                        return "Delete";
                    }
                    case DONE: {
                        return "Done";
                    }
                    default:
                        return object.toString();
                }
            }

            @Override
            public AutonTask fromString(String string) {
                switch (string) {
                    case "Grab Cube": {
                        return AutonTask.GRAB_CUBE;
                    }
                    case "Score Scale": {
                        return AutonTask.SCORE_SCALE;
                    }
                    case "Score Switch": {
                        return AutonTask.SCORE_SWITCH;
                    }
                    case "Done": {
                        return AutonTask.DONE;
                    }
                    case "Delete": {
                        return AutonTask.DELETE;
                    }
                    default:
                        return null;
                }
            }
        };

        scoringSideStringConverter = new StringConverter<ScoringSide>() {
            @Override
            public String toString(ScoringSide object) {
                if (object == null) {
                    return "";
                }
                switch (object) {
                    case Not_Applicable:
                        return "Not Applicable";
                    default:
                        return object.toString();
                }
            }

            @Override
            public ScoringSide fromString(String string) {
                switch (string) {
                    case "Front": {
                        return ScoringSide.Front;
                    }
                    case "Middle": {
                        return ScoringSide.Mid;
                    }
                    case "Back": {
                        return ScoringSide.Back;
                    }
                    default:
                        return null;
                }
            }
        };

        //Not even going to try to explain cell factories. Nope.
        Callback<TableColumn<TaskConfig, AutonTask>, TableCell> autonTaskCellFactory = param -> {
            ComboBoxTableCell<TaskConfig, AutonTask> comboBoxTableCell = new ComboBoxTableCell(FXCollections.observableArrayList(AutonTask.values())) {
                private boolean hasCreatedNewRow = false;
                @Override
                public void updateItem(Object item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!hasCreatedNewRow && item != null && item != AutonTask.DONE) {
                        addBlankRow(this.getTableView().getItems());
                        hasCreatedNewRow = true;
                    }
                    else if (item == AutonTask.DONE || item == AutonTask.GRAB_CUBE) {
                        ((TaskConfig) this.getTableRow().getItem()).scoringSide.setValue(ScoringSide.Not_Applicable);
                    }
                    if (item == AutonTask.DELETE) {
                        this.getTableView().getItems().remove(this.getItem());
                    }
                }
            };
            comboBoxTableCell.setEditable(true);
            comboBoxTableCell.setComboBoxEditable(false);
            comboBoxTableCell.setConverter(autonTaskStringConverter);
            comboBoxTableCell.itemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == AutonTask.DELETE) {
                    comboBoxTableCell.getTableView().getItems().remove(comboBoxTableCell.getTableRow().getItem());
                    addBlankRow(comboBoxTableCell.getTableView().getItems());
                }
            });
            return comboBoxTableCell;
        };

        Callback<TableColumn<TaskConfig, ScoringSide>, TableCell> scoringSideCellFactory = param -> {
            ComboBoxTableCell<TaskConfig, ScoringSide> comboBoxTableCell = new ComboBoxTableCell<>(FXCollections.observableArrayList(ScoringSide.values()));
            comboBoxTableCell.setEditable(true);
            comboBoxTableCell.setComboBoxEditable(false);
            comboBoxTableCell.setConverter(scoringSideStringConverter);
            return comboBoxTableCell;
        };

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

        taskColLLL.setCellFactory(autonTaskCellFactory);
        taskColLRL.setCellFactory(autonTaskCellFactory);
        taskColRLR.setCellFactory(autonTaskCellFactory);
        taskColRRR.setCellFactory(autonTaskCellFactory);

        sideColLLL.setCellFactory(scoringSideCellFactory);
        sideColLRL.setCellFactory(scoringSideCellFactory);
        sideColRLR.setCellFactory(scoringSideCellFactory);
        sideColRRR.setCellFactory(scoringSideCellFactory);

        taskColLLL.setCellValueFactory((Callback<TableColumn.CellDataFeatures<TaskConfig, AutonTask>, ObservableValue>) param -> param.getValue().autonTask);
        taskColLRL.setCellValueFactory((Callback<TableColumn.CellDataFeatures<TaskConfig, AutonTask>, ObservableValue>) param -> param.getValue().autonTask);
        taskColRLR.setCellValueFactory((Callback<TableColumn.CellDataFeatures<TaskConfig, AutonTask>, ObservableValue>) param -> param.getValue().autonTask);
        taskColRRR.setCellValueFactory((Callback<TableColumn.CellDataFeatures<TaskConfig, AutonTask>, ObservableValue>) param -> param.getValue().autonTask);


        sideColLLL.setCellValueFactory((Callback<TableColumn.CellDataFeatures<TaskConfig, ScoringSide>, ObservableValue>) param -> param.getValue().scoringSide);
        sideColLRL.setCellValueFactory((Callback<TableColumn.CellDataFeatures<TaskConfig, ScoringSide>, ObservableValue>) param -> param.getValue().scoringSide);
        sideColRLR.setCellValueFactory((Callback<TableColumn.CellDataFeatures<TaskConfig, ScoringSide>, ObservableValue>) param -> param.getValue().scoringSide);
        sideColRRR.setCellValueFactory((Callback<TableColumn.CellDataFeatures<TaskConfig, ScoringSide>, ObservableValue>) param -> param.getValue().scoringSide);

        addBlankRow(dataLLL);
        addBlankRow(dataLRL);
        addBlankRow(dataRLR);
        addBlankRow(dataRRR);

        tableLLL.setItems(dataLLL);
        tableLRL.setItems(dataLRL);
        tableRLR.setItems(dataRLR);
        tableRRR.setItems(dataRRR);
    }

    private void addBlankRow(ObservableList<TaskConfig> data) {
        data.add(new TaskConfig(null, null));
    }

    private boolean checkIfComplete() {
        boolean tableIsComplete = ((TaskConfig) tableLLL.getItems().get(tableLLL.getItems().size() - 1)).autonTask.getValue() == AutonTask.DONE &&
                ((TaskConfig) tableLRL.getItems().get(tableLRL.getItems().size() - 1)).autonTask.getValue() == AutonTask.DONE &&
                ((TaskConfig) tableRLR.getItems().get(tableRLR.getItems().size() - 1)).autonTask.getValue() == AutonTask.DONE &&
                ((TaskConfig) tableRRR.getItems().get(tableRRR.getItems().size() - 1)).autonTask.getValue() == AutonTask.DONE;
        boolean radioSelected = radioGroup.getSelectedToggle() != null;

        if (!tableIsComplete || !radioSelected) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("");
            if (!tableIsComplete && !radioSelected) {
                alert.setContentText("Finish each configuration and select a starting auton position before continuing.");
            }
            else if (!tableIsComplete && radioSelected) {
                alert.setContentText("Finish each configuration before continuing.");
            }
            else if (tableIsComplete && !radioSelected) {
                alert.setContentText("Select a starting auton position before continuing.");
            }
            alert.show();
        }
        return tableIsComplete && radioSelected;
    }

    @FXML
    private void saveConfiguration() {
        if (checkIfComplete()) {
            TextInputDialog fileNameDialog = new TextInputDialog("FileName");
            fileNameDialog.setTitle("Saving presets");
            fileNameDialog.setHeaderText("");
            fileNameDialog.setContentText("Enter a name for the new preset:");
            Optional<String> fileNameResult = fileNameDialog.showAndWait();

            ChoiceDialog<String> choiceDialog = new ChoiceDialog<>(null, "Table LLL", "Table LRL", "Table RLR", "Table RRR");
            choiceDialog.setTitle("Choosing Save Method...");
            choiceDialog.setHeaderText("");
            choiceDialog.setContentText("Choose which table(s) to save:");
            Optional<String> tableResult = choiceDialog.showAndWait();

            fileNameResult.ifPresent(fileName -> {
                //TODO Save the current tables as a CSV with the given file name in /auton-app/Configurations
                FileWriter fileWriter = null;
                try {
                    fileWriter = new FileWriter(fileName + ".csv");
                    PrintWriter printWriter = new PrintWriter(fileWriter);
                    ObservableList<TaskConfig> relevantData;
                    switch (tableResult.get()) {
                        case "Table LLL": {
                            relevantData = dataLLL;
                        }
                        case "Table LRL": {
                            relevantData = dataLRL;
                        }
                        case "Table RLR": {
                            relevantData = dataRLR;
                        }
                        case "Table RRR": {
                            relevantData = dataRRR;
                        }
                        default: {
                            relevantData = null;
                        }
                    }

                    for (TaskConfig tc : relevantData) {
                        String autonTask = autonTaskStringConverter.toString(tc.autonTask.get());
                        String scoringSide = scoringSideStringConverter.toString(tc.scoringSide.get());
                        printWriter.println(autonTask + "," + scoringSide);
                    }
                    printWriter.flush();
                    printWriter.close();
                    fileWriter.close();
                }  catch (Exception e) {
                    System.out.println("AutonBuilderController.saveConfiguration threw an exception: " + e.toString());
                }
            });
        }
    }
r
    private void loadConfiguration() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Configuration CSV");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv" ));
        fileChooser.setInitialDirectory(new File("")); //TODO Put file path to Configurations folder.
        fileChooser.showOpenDialog(root.getScene().getWindow());
    }

    @FXML
    private void publishConfiguration() {
        int startingPos = 0;

        String[]
            autonTaskLLL = new String[dataLLL.size()],
            autonTaskLRL = new String[dataLRL.size()],
            autonTaskRLR = new String[dataRLR.size()],
            autonTaskRRR = new String[dataRRR.size()],
            scoringSideLLL = new String[dataLLL.size()],
            scoringSideLRL = new String[dataLRL.size()],
            scoringSideRLR = new String[dataRLR.size()],
            scoringSideRRR = new String[dataRRR.size()];

        NetworkTable
            rootTable = Client.getNT().getTable("AutonConfiguration"),
            lllTable = rootTable.getSubTable("LLL"),
            lrlTable = rootTable.getSubTable("LRL"),
            rlrTable = rootTable.getSubTable("RLR"),
            rrrTable = rootTable.getSubTable("RRR");

        if (checkIfComplete()) {
            // First off get the starting position
            if (radioGroup.getSelectedToggle() == rightRadioButton)
                startingPos = 2;
            else if (radioGroup.getSelectedToggle() == middleRadioButton)
                startingPos = 1;
            else
                startingPos = 0;

            // Get all auton tasks and scoring side
            for (int i = 0; i < dataLLL.size(); i++) {
                autonTaskLLL[i] = dataLLL.get(i).autonTask.get().toString();
                scoringSideLLL[i] = dataLLL.get(i).scoringSide.get().toString();
            }

            for (int i = 0; i < dataLRL.size(); i++) {
                autonTaskLRL[i] = dataLRL.get(i).autonTask.get().toString();
                scoringSideLRL[i] = dataLRL.get(i).scoringSide.get().toString();
            }

            for (int i = 0; i < dataRLR.size(); i++) {
                autonTaskRLR[i] = dataRLR.get(i).autonTask.get().toString();
                scoringSideRLR[i] = dataRLR.get(i).scoringSide.get().toString();
            }

            for (int i = 0; i < dataRRR.size(); i++) {
                autonTaskRRR[i] = dataRRR.get(i).autonTask.get().toString();
                scoringSideRRR[i] = dataRRR.get(i).scoringSide.get().toString();
            }

            // Place values in table
            rootTable.getEntry("startingPos").setNumber(startingPos);

            lllTable.getEntry("tasks").setStringArray(autonTaskLLL);
            lllTable.getEntry("sides").setStringArray(scoringSideLLL);

            lrlTable.getEntry("tasks").setStringArray(autonTaskLRL);
            lrlTable.getEntry("sides").setStringArray(scoringSideLRL);

            rlrTable.getEntry("tasks").setStringArray(autonTaskRLR);
            rlrTable.getEntry("sides").setStringArray(scoringSideRLR);

            rrrTable.getEntry("tasks").setStringArray(autonTaskRRR);
            rrrTable.getEntry("sides").setStringArray(scoringSideRRR);
        }
    }
}
