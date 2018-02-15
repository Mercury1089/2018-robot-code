package org.usfirst.frc.team1089.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.util.StringConverter;


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

        Callback<TableColumn<TaskConfig, AutonTask>, TableCell> autonTaskCellFactory = param -> {
            ComboBoxTableCell<TaskConfig, AutonTask> comboBoxTableCell = new ComboBoxTableCell<>(FXCollections.observableArrayList(AutonTask.values()));
            comboBoxTableCell.setEditable(true);
            comboBoxTableCell.setComboBoxEditable(true);
            return comboBoxTableCell;
        };

        Callback<TableColumn<TaskConfig, ScoringSide>, TableCell> scoringSideCellFactory = param -> {
            ComboBoxTableCell<TaskConfig, ScoringSide> comboBoxTableCell = new ComboBoxTableCell<>(FXCollections.observableArrayList(ScoringSide.values()));
            comboBoxTableCell.setEditable(true);
            comboBoxTableCell.setComboBoxEditable(true);
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


        taskColLLL.setCellValueFactory(new PropertyValueFactory<TaskConfig, AutonTask>("autonTask"));
        taskColLRL.setCellValueFactory(new PropertyValueFactory<TaskConfig, AutonTask>("autonTask"));
        taskColRLR.setCellValueFactory(new PropertyValueFactory<TaskConfig, AutonTask>("autonTask"));
        taskColRRR.setCellValueFactory(new PropertyValueFactory<TaskConfig, AutonTask>("autonTask"));

        sideColLLL.setCellValueFactory(new PropertyValueFactory<TaskConfig, ScoringSide>("scoringSide"));
        sideColLRL.setCellValueFactory(new PropertyValueFactory<TaskConfig, ScoringSide>("scoringSide"));
        sideColRLR.setCellValueFactory(new PropertyValueFactory<TaskConfig, ScoringSide>("scoringSide"));
        sideColRRR.setCellValueFactory(new PropertyValueFactory<TaskConfig, ScoringSide>("scoringSide"));

        dataLLL.add(new TaskConfig(null, null));
        dataLRL.add(new TaskConfig(null, null));
        dataRLR.add(new TaskConfig(null, null));
        dataRRR.add(new TaskConfig(null, null));

        tableLLL.setItems(dataLLL);
        tableLRL.setItems(dataLRL);
        tableRLR.setItems(dataRLR);
        tableRRR.setItems(dataRRR);

    }
}
