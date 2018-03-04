package org.usfirst.frc.team1089.main;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceDialog;
import org.fxmisc.easybind.EasyBind;
import org.usfirst.frc.team1089.main.util.AutonPosition;
import org.usfirst.frc.team1089.main.util.FieldSide;
import org.usfirst.frc.team1089.main.util.TaskConfig;

import java.io.*;
import java.util.*;

/**
 * Backend for auton builder app
 */
public class AutonBuilder {
    private final String
        TABLE_NAME = "AutonBuilder",
        TASK_PREFIX = "task",
        SIDE_PREFIX = "side";

    private AutonPosition startingPosition;
    private HashMap<String, List<TaskConfig>> configMap;
    private HashMap<String, FieldSide> fieldSideMap;

    private NetworkTableInstance ntInstance;


//    static {
//        // Load native libraries and whatnot
//        System.loadLibrary("ntcore");
//    }

    public AutonBuilder(ObservableList<TaskConfig> dataLLL, ObservableList<TaskConfig> dataLRL, ObservableList<TaskConfig> dataRLR, ObservableList<TaskConfig> dataRRR) {
        // Initialize HashMap
        configMap = new HashMap<>();
        configMap.put("LLL", new ArrayList<>());
        configMap.put("LRL", new ArrayList<>());
        configMap.put("RLR", new ArrayList<>());
        configMap.put("RRR", new ArrayList<>());

        EasyBind.listBind(configMap.get("LLL"), dataLLL);
        EasyBind.listBind(configMap.get("LRL"), dataLRL);
        EasyBind.listBind(configMap.get("RLR"), dataRLR);
        EasyBind.listBind(configMap.get("RRR"), dataRRR);

        fieldSideMap = new HashMap<>();
        fieldSideMap.put("LLL", FieldSide.LEFT_SIDE);
        fieldSideMap.put("LRL", FieldSide.LEFT_SIDE);
        fieldSideMap.put("RLR", FieldSide.RIGHT_SIDE);
        fieldSideMap.put("RRR", FieldSide.RIGHT_SIDE);

        ntInstance = NetworkTableInstance.getDefault();
        ntInstance.setServerTeam(1089);
        ntInstance.startClient();
    }

    /**
     * Publish current task configs to the network table
     */
    public void publishValues() {
        NetworkTable builderTable = ntInstance.getTable(TABLE_NAME);
        for (Map.Entry<String, List<TaskConfig>> entry : configMap.entrySet()) {
            String key = entry.getKey();
            List<TaskConfig> configList = entry.getValue();
            int size = configList.size();
            String[]
                tasks = new String[size],
                sides = new String[size];

            for (int i = 0; i < size; i++) {
                tasks[i] = configList.get(i).autonTask.toString();
                sides[i] = configList.get(i).scoringSide.toString();
            }

            builderTable.getEntry(TASK_PREFIX + key).setStringArray(tasks);
            builderTable.getEntry(SIDE_PREFIX + key).setStringArray(sides);
        }
    }

    /**
     * Save configuration to a comma separated value file (CSV)
     *
     * @param csv file to save config to; must have csv prefix
     *
     * @return whether or not the action was successful
     */
    public boolean save(File csv, String... configKeys) {
        boolean successful = true;
        String[] filteredKeys = new String[0];
        List<List<TaskConfig>> configTableLists = new ArrayList<>();
        List<String> filteredKeysList = new ArrayList<>();
        File dir = csv.getParentFile();

        // Check if file directory exists first.
        if (!dir.exists())
            dir.mkdirs();

        // Filter bad keys
        for (String s : configKeys) {
            if (s != null) {
                List<TaskConfig> taskList = configMap.get(s);

                if (taskList != null) {
                    filteredKeysList.add(s);
                    configTableLists.add(taskList);
                }

            }
        }

        filteredKeys = filteredKeysList.toArray(filteredKeys);

        try {
            int numTables = 0;
            int numCols = 0;
            int maxNumRows = 0;
            CSVWriter writer = new CSVWriter(new FileWriter(csv));

            // Get all tables from keys
            // Also get numTables, numCols, and maxNumRows
            numTables = configTableLists.size();
            numCols = numTables * 2;

            for (List<TaskConfig> l: configTableLists) {
                maxNumRows = maxNumRows < l.size() ? l.size() : maxNumRows;
            }

            // Start by applying header
            String[] rowBuffer = new String[numCols];

            for (int i = 0; i < numTables; i++) {
                rowBuffer[2 * i] = "task" + filteredKeys[i];
                rowBuffer[2 * i + 1] = "side" + filteredKeys[i];
            }

            writer.writeNext(rowBuffer, false);

            // Start adding all values here
            for (int row = 0; row < maxNumRows; row++) {
                // Clear string array
                for (int i = 0; i < numCols; i++)
                    rowBuffer[i] = "";

                // NOW start adding all values
                for (int table = 0; table < numTables; table++) {
                    String task = "", side = "";
                    List<TaskConfig> taskList = configTableLists.get(table);

                    if (row < taskList.size()) {
                        TaskConfig t = taskList.get(row);

                        task = t.autonTask.get().toString();
                        side = t.scoringSide.get().toString();

                    }

                    rowBuffer[2 * table] = task;
                    rowBuffer[2 * table + 1] = side;
                }

                writer.writeNext(rowBuffer, false);
            }


            writer.close();
        } catch (Exception e) { // Definitely not the best way to do this
            e.printStackTrace();
            successful = false;
        }

        return successful;
    }

    public void load(File csv) {
        try {
            CSVReader reader = new CSVReader(new FileReader(csv.getPath()));
            String[] csvBuffer = reader.readNext();

        } catch (IOException e) {
            System.out.println("AutonBuilder.load has thrown an IOException!");
        }
    }

    public void publish() {
        NetworkTable
                rootTable = Client.getNT().getTable("AutonConfiguration"),
                lllTable = rootTable.getSubTable("LLL"),
                lrlTable = rootTable.getSubTable("LRL"),
                rlrTable = rootTable.getSubTable("RLR"),
                rrrTable = rootTable.getSubTable("RRR");

        String[]
                autonTaskLLL = TaskConfig.AutonTask.arrayToString(((TaskConfig[]) configMap.get("LLL").toArray())),
                autonTaskLRL = TaskConfig.AutonTask.arrayToString(((TaskConfig[]) configMap.get("LRL").toArray())),
                autonTaskRLR = TaskConfig.AutonTask.arrayToString(((TaskConfig[]) configMap.get("RLR").toArray())),
                autonTaskRRR = TaskConfig.AutonTask.arrayToString(((TaskConfig[]) configMap.get("RRR").toArray())),

                scoringSideLLL = TaskConfig.ScoringSide.arrayToString(((TaskConfig[]) configMap.get("LLL").toArray())),
                scoringSideLRL = TaskConfig.ScoringSide.arrayToString(((TaskConfig[]) configMap.get("LRL").toArray())),
                scoringSideRLR = TaskConfig.ScoringSide.arrayToString(((TaskConfig[]) configMap.get("RLR").toArray())),
                scoringSideRRR = TaskConfig.ScoringSide.arrayToString(((TaskConfig[]) configMap.get("RRR").toArray()));

        rootTable.getEntry("startingPosition").setString(startingPosition.toString());

        lllTable.getEntry("fieldSide").setString(fieldSideMap.get("LLL").toString());
        lllTable.getEntry("tasks").setStringArray(autonTaskLLL);
        lllTable.getEntry("sides").setStringArray(scoringSideLLL);

        lrlTable.getEntry("fieldSide").setString(fieldSideMap.get("LRL").toString());
        lrlTable.getEntry("tasks").setStringArray(autonTaskLRL);
        lrlTable.getEntry("sides").setStringArray(scoringSideLRL);

        lrlTable.getEntry("fieldSide").setString(fieldSideMap.get("RLR").toString());
        rlrTable.getEntry("tasks").setStringArray(autonTaskRLR);
        rlrTable.getEntry("sides").setStringArray(scoringSideRLR);

        rrrTable.getEntry("fieldSide").setString(fieldSideMap.get("RRR").toString());
        rrrTable.getEntry("tasks").setStringArray(autonTaskRRR);
        rrrTable.getEntry("sides").setStringArray(scoringSideRRR);
    }


    public void setStartingPosition(AutonPosition newPosition) {
        startingPosition = newPosition;
    }

    public void setFieldSide(String configKey, FieldSide newSide) {
        fieldSideMap.replace(configKey, newSide);
    }
}
