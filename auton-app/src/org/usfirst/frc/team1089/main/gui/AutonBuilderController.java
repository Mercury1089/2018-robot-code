package org.usfirst.frc.team1089.main.gui;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import edu.wpi.first.networktables.NetworkTable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.usfirst.frc.team1089.main.Client;
import org.usfirst.frc.team1089.main.util.TaskConfig;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class AutonBuilderController {
    @FXML
    private Pane root;

    @FXML
    private Button loadButton;
    @FXML
    private Button saveButton;

    private ToggleGroup radioGroup = new ToggleGroup();

    @FXML
    private RadioButton leftRadioButton, middleLeftRadioButton, middleRightRadioButton, rightRadioButton;

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
    public void initialize() {
        //Setup up the radio buttons to be in a group.
        leftRadioButton.setToggleGroup(radioGroup);
        middleLeftRadioButton.setToggleGroup(radioGroup);
        middleRightRadioButton.setToggleGroup(radioGroup);
        rightRadioButton.setToggleGroup(radioGroup);

        //Format columns.
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

        //Not even going to try to explain cell factories. Nope.
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


        // Make sure each table has at least one blank row to start with. Additional rows will be added when tasks are selected in blank rows.
        addBlankRow(dataLLL);
        addBlankRow(dataLRL);
        addBlankRow(dataRLR);
        addBlankRow(dataRRR);

        //Set the tables to use their respective data objects.
        tableLLL.setItems(dataLLL);
        tableLRL.setItems(dataLRL);
        tableRLR.setItems(dataRLR);
        tableRRR.setItems(dataRRR);
    }

    /**
     * Saves a chosen configuration table to a CSV file at a given directory.
     */
    @FXML
    private void saveConfiguration() throws IOException {
        //Prompt the user for the table they want to save to a CSV.
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("Table LLL", "Table LLL", "Table LRL", "Table RLR", "Table RRR");
        choiceDialog.setTitle("Choosing Save Method..");
        choiceDialog.setHeaderText("");
        choiceDialog.setContentText("Choose which table(s) to save:");
        Optional<String> tableResult = choiceDialog.showAndWait();

        //Figure out which table object the string corresponds to.
        ObservableList<TaskConfig> relevantData = determineData(tableResult.get());

        //Make sure that the table they chose has been finished by checking the last row for AutonTask.Done.
        if (relevantData != null) {
            //Open the File Explorer for the user to chose a directory to save to. This should always be the Configurations folder.
            //TODO Make the initial directory be the Configurations folder.
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            fileChooser.setInitialFileName(".csv");
            File directory = fileChooser.showSaveDialog(root.getScene().getWindow());

            //Make sure the user didn't close the tab and not choose a location.
            if (directory != null) {
                //Write to the CSV file using OpenCSV.
                CSVWriter csvWriter = new CSVWriter(new FileWriter(directory));
                for (TaskConfig tc : relevantData) {
                    String task = tc.autonTask.getValue().toString();
                    String side = tc.scoringSide.getValue().toString();
                    String[] entry = {task, side};
                    csvWriter.writeNext(entry, false);
                }
                csvWriter.flush();
                csvWriter.close();

                //Alert the user that the save was successful.
                Alert successfulAlert = new Alert(Alert.AlertType.CONFIRMATION);
                successfulAlert.setContentText("File has been saved successfully!");
                successfulAlert.show();
            }
        } else {
            //Alert the user that they still need to complete the table.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("The selected table has not been completed!");
            alert.setHeaderText("Unfinished Error");
            alert.setTitle("Error");
            alert.show();
        }
    }



    @FXML
    /**
     * Loads a user selected CSV file into a table, chosen by the user in the form of a dialog prompt.
     */
    private void loadConfiguration() throws IOException {
        //Prompt the user for the table they wish to load into.
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("Table LLL", "Table LLL", "Table LRL", "Table RLR", "Table RRR");
        choiceDialog.setTitle("Choosing Load Method...");
        choiceDialog.setHeaderText("Warning: Loading data into a table will clear the current contents!");
        Optional<String> tableResult = choiceDialog.showAndWait();

        // Figure out which table object the string corresponds to.
        ObservableList<TaskConfig> relevantData = determineData(tableResult.get());

        // Open File Explorer for the user to choose the file to load, then grab its directory. All the CSVs should be saved into the Configurations folder.
        // TODO Make the initial directory be the Configurations folder.
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Configuration CSV");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Comma Separated Values", "*.csv" )
        );
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File directory = fileChooser.showOpenDialog(root.getScene().getWindow());

        // If a file has been chosen and is ready to be loaded
        if (directory != null) {
            //Parse the loaded file using OpenCSV to put into the table the user chose.
            CSVReader csvReader = new CSVReader(new FileReader(directory.getPath()));
            List<TaskConfig> taskConfigs = new ArrayList<>();
            List<String[]> csv = csvReader.readAll();

            //Clear the data to avoid weird interactions with tables that already have data in them.
            relevantData.clear();

            for (int i = 0; i < csv.size(); i++) {
                relevantData.add(new TaskConfig(TaskConfig.AutonTask.fromString(csv.get(i)[0]), TaskConfig.ScoringSide.fromString(csv.get(i)[1])));
            }

            //Alert the user that the load was successful because reasons.
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Loaded successfully!");
            alert.setTitle("Success");
            alert.setHeaderText("");
            alert.show();
        }
    }

    @FXML
    /**
     * Pushes the auton configuration to the NetworkTable in the form of String arrays if the auton configuration is completely finished.
     */
    private void publishConfiguration() {
        int startingPos;

        NetworkTable
            rootTable = Client.getNT().getTable("AutonConfiguration"),
            lllTable = rootTable.getSubTable("LLL"),
            lrlTable = rootTable.getSubTable("LRL"),
            rlrTable = rootTable.getSubTable("RLR"),
            rrrTable = rootTable.getSubTable("RRR");

        // First off get the starting position
        if (radioGroup.getSelectedToggle() == rightRadioButton)
            startingPos = 3;
        else if (radioGroup.getSelectedToggle() == middleLeftRadioButton)
            startingPos = 2;
        else if (radioGroup.getSelectedToggle() == middleRightRadioButton)
            startingPos = 1;
        else
            startingPos = 0;

        // Get all auton tasks and scoring side
        String[]
                autonTaskLLL = TaskConfig.AutonTask.arrayToString(((TaskConfig[]) dataLLL.toArray())),
                autonTaskLRL = TaskConfig.AutonTask.arrayToString(((TaskConfig[]) dataLRL.toArray())),
                autonTaskRLR = TaskConfig.AutonTask.arrayToString(((TaskConfig[]) dataRLR.toArray())),
                autonTaskRRR = TaskConfig.AutonTask.arrayToString(((TaskConfig[]) dataRRR.toArray())),

                scoringSideLLL = TaskConfig.ScoringSide.arrayToString(((TaskConfig[]) dataLLL.toArray())),
                scoringSideLRL = TaskConfig.ScoringSide.arrayToString(((TaskConfig[]) dataLRL.toArray())),
                scoringSideRLR = TaskConfig.ScoringSide.arrayToString(((TaskConfig[]) dataRLR.toArray())),
                scoringSideRRR = TaskConfig.ScoringSide.arrayToString(((TaskConfig[]) dataRRR.toArray()));

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

    //HELPER METHODS

    /**
     * Adds a blank item to the given data. This will cause the table to display a blank, editable row.
     *
     * @param data The table data to add a blank row to.
     */
    private void addBlankRow(ObservableList<TaskConfig> data) {
        data.add(new TaskConfig(null, null));
    }

    /**
     * Trims off ending, blank rows in the table.
     * @param data The data to have its rows trimmed.
     */
    private void trimRows(ObservableList data) {
        while (((TaskConfig) data.get(data.size() - 1)).autonTask.getValue() == null) {
            removeLastRow(data);
        }
    }
    private TaskConfig getLastItem(ObservableList<TaskConfig> data) {
        return data.get(data.size() - 1);
    }
    /**
     * Removes the last row from a given set of data.
     * @param data The table data that should have its lsat row removed.
     */
    private void removeLastRow(ObservableList<TaskConfig> data) {
        data.remove(data.size() - 1);
    }

    /**
     * Checks to see if all the tables are complete AND if a starting auton position has been selected. The method will throw an error depending on what is not finished.
     * @return All the tables are complete AND if a starting auton position has been selected.
     */


    /**
     * Determines the data that the input String is referring to.
     * @param input The string selected by the user for the table they want.
     * @return The data object relevant to the input String.
     */
    private ObservableList<TaskConfig> determineData(String input) {
        switch (input) {
            case "Table LLL":
                return dataLLL;
            case "Table LRL":
                return dataLRL;
            case "Table RLR":
                return dataRLR;
            case "Table RRR": 
                return dataRRR;
            default:
                return null;
        }
    }

}
