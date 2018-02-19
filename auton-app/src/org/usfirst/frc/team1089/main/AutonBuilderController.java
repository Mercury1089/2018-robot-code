package org.usfirst.frc.team1089.main;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
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

import java.io.*;
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
    private RadioButton leftRadioButton, middleRadioButton, rightRadioButton;

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
        middleRadioButton.setToggleGroup(radioGroup);
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
                    else if (item == AutonTask.DONE) {
                        ((TaskConfig) this.getTableRow().getItem()).scoringSide.setValue(ScoringSide.NOT_APPLICABLE);
                    }
                    if (item == AutonTask.DELETE) {
                        this.getTableView().getItems().remove(this.getItem());
                    }
                }
            };
            comboBoxTableCell.setEditable(true);
            comboBoxTableCell.setComboBoxEditable(false);
            comboBoxTableCell.setConverter(AutonTask.STRING_CONVERTER);
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
            comboBoxTableCell.setConverter(ScoringSide.STRING_CONVERTER);
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

        taskColLLL.setCellValueFactory((Callback<TableColumn.CellDataFeatures<TaskConfig, AutonTask>, ObservableValue>) param -> param.getValue().autonTask);
        taskColLRL.setCellValueFactory((Callback<TableColumn.CellDataFeatures<TaskConfig, AutonTask>, ObservableValue>) param -> param.getValue().autonTask);
        taskColRLR.setCellValueFactory((Callback<TableColumn.CellDataFeatures<TaskConfig, AutonTask>, ObservableValue>) param -> param.getValue().autonTask);
        taskColRRR.setCellValueFactory((Callback<TableColumn.CellDataFeatures<TaskConfig, AutonTask>, ObservableValue>) param -> param.getValue().autonTask);

        sideColLLL.setCellValueFactory((Callback<TableColumn.CellDataFeatures<TaskConfig, ScoringSide>, ObservableValue>) param -> param.getValue().scoringSide);
        sideColLRL.setCellValueFactory((Callback<TableColumn.CellDataFeatures<TaskConfig, ScoringSide>, ObservableValue>) param -> param.getValue().scoringSide);
        sideColRLR.setCellValueFactory((Callback<TableColumn.CellDataFeatures<TaskConfig, ScoringSide>, ObservableValue>) param -> param.getValue().scoringSide);
        sideColRRR.setCellValueFactory((Callback<TableColumn.CellDataFeatures<TaskConfig, ScoringSide>, ObservableValue>) param -> param.getValue().scoringSide);

        //Make sure each table has at least one blank row to start with. Additional rows will be added when tasks are selected in blank rows.
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

    @FXML
    /**
     * Saves a chosen configuration table to a CSV file at a given directory.
     */
    private void saveConfiguration() throws IOException {
        //Prompt the user for the table they want to save to a CSV.
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("Table LLL", "Table LLL", "Table LRL", "Table RLR", "Table RRR");
        choiceDialog.setTitle("Choosing Save Method...");
        choiceDialog.setHeaderText("");
        choiceDialog.setContentText("Choose which table(s) to save:");
        Optional<String> tableResult = choiceDialog.showAndWait();

        //Figure out which table object the string corresponds to.
        ObservableList<TaskConfig> relevantData = determineData(tableResult.get());


        //Make sure that the table they chose has been finished by checking the last row for AutonTask.Done.
        if (relevantData != null && relevantData.get(relevantData.size() - 1).autonTask.getValue() == AutonTask.DONE) {
            //Open the File Explorer for the user to chose a directory to save to. This should always be the Configurations folder.
            //TODO Make the initial directory be the Configurations folder.
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            fileChooser.setInitialFileName(".csv");
            File directory = fileChooser.showSaveDialog(root.getScene().getWindow());

            //Make sure the user didn't close the tab and not choose a location..
            if (directory != null) {
                //Write to the CSV file using OpenCSV.
                CSVWriter csvWriter = new CSVWriter(new FileWriter(directory));
                for (TaskConfig tc : relevantData) {
                    String task = AutonTask.STRING_CONVERTER.toString(tc.autonTask.getValue());
                    String side = ScoringSide.STRING_CONVERTER.toString(tc.scoringSide.getValue());
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
        choiceDialog.setContentText("Choose which table to load into:");
        Optional<String> tableResult = choiceDialog.showAndWait();

        //Figure out which table object the string corresponds to.
        ObservableList<TaskConfig> relevantData = determineData(tableResult.get());

        //Clear the data to avoid weird interactions with tables that already have data in them.
        relevantData.clear();

        //Open File Explorer for the user to choose the file to load, then grab its directory. All the CSVs should be saved into the Configurations folder.
        //TODO Make the initial directory be the Configurations folder.
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Configuration CSV");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv" ));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File directory = fileChooser.showOpenDialog(root.getScene().getWindow());

        //Parse the loaded file using OpenCSV to put into the table the user chose.
        CSVReader csvReader = new CSVReader(new FileReader(directory.getPath()));
        String[] nextLine = null;
        while((nextLine = csvReader.readNext()) != null) {
            AutonTask task = AutonTask.STRING_CONVERTER.fromString(nextLine[0]);
            ScoringSide side = ScoringSide.STRING_CONVERTER.fromString(nextLine[1]);
            relevantData.add(new TaskConfig(task, side));
        }

        //Alert the user that the load was successful because reasons.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Loaded successfully!");
        alert.setTitle("Success");
        alert.setHeaderText("");
        alert.show();
    }

    @FXML
    /**
     * Pushes the auton configuration to the NetworkTable in the form of String arrays if the auton configuration is completely finished.
     */
    private void publishConfiguration() {
        int startingPos;

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

        if (isCompletelyFinished()) {
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

    //HELPER METHODS

    /**
     * Adds a blank item to the given data. This will cause the table to display a blank, editable row.
     * @param data The table data to add a blank row to.
     */
    private void addBlankRow(ObservableList<TaskConfig> data) {
        data.add(new TaskConfig(null, null));
    }

    /**
     * Checks to see if all the tables are complete AND if a starting auton position has been selected. The method will throw an error depending on what is not finished.
     * @return All the tables are complete AND if a starting auton position has been selected.
     */
    private boolean isCompletelyFinished() {
        boolean tableIsComplete =
                ((TaskConfig) tableLLL.getItems().get(tableLLL.getItems().size() - 1)).autonTask.getValue() == AutonTask.DONE &&
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
