package org.usfirst.frc.team1089.main;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.util.StringConverter;
import sun.java2d.pipe.SpanShapeRenderer;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;


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

    private ObservableList<TaskConfig> dataLLL = FXCollections.observableArrayList();
    private ObservableList<TaskConfig> dataLRL = FXCollections.observableArrayList();
    private ObservableList<TaskConfig> dataRLR = FXCollections.observableArrayList();
    private ObservableList<TaskConfig> dataRRR = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        radioGroup = new ToggleGroup();
        leftRadioButton.setToggleGroup(radioGroup);
        middleRadioButton.setToggleGroup(radioGroup);
        rightRadioButton.setToggleGroup(radioGroup);

        StringConverter<AutonTask> autonTaskStringConverter= new StringConverter<AutonTask>() {
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

        StringConverter<ScoringSide> scoringSideStringConverter = new StringConverter<ScoringSide>() {
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
        boolean isComplete = ((TaskConfig) tableLLL.getItems().get(tableLLL.getItems().size() - 1)).autonTask.getValue() == AutonTask.DONE &&
                ((TaskConfig) tableLRL.getItems().get(tableLRL.getItems().size() - 1)).autonTask.getValue() == AutonTask.DONE &&
                ((TaskConfig) tableRLR.getItems().get(tableRLR.getItems().size() - 1)).autonTask.getValue() == AutonTask.DONE &&
                ((TaskConfig) tableRRR.getItems().get(tableRRR.getItems().size() - 1)).autonTask.getValue() == AutonTask.DONE;

        if (!isComplete) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("");
            alert.setContentText("Finish each configuration before continuing.");
            alert.show();
        }
        return isComplete;
    }

    @FXML
    private void saveConfiguration() {
        if (!checkIfComplete()); {
            return;
        }


    }

    @FXML
    private void loadConfiguration() {
        if (!checkIfComplete()); {
            return;
        }


    }

    @FXML
    private void publishConfiguration() {
        if (!checkIfComplete()); {
            return;
        }


    }
}
