package org.usfirst.frc.team1089.main;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvConstraintViolationException;
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
import java.util.function.Consumer;

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

    static {
        try {
            String
                env = System.getProperty("os.name", "generic").toLowerCase(),
                path = "/";
            boolean is64 = System.getProperty("os.arch", "32").contains("64");

            if (env.contains("win")) { // Windows environment
                path += "windows/";
                path += "x86" + (is64 ? "-64" : "") + "/";
                NativeUtils.loadLibraryFromJar(path + "ntcore.dll");
            } else if (env.contains("mac")) { // macOS environment
                path += "osx/";
                path += "x86" + (is64 ? "-64" : "") + "/";
                NativeUtils.loadLibraryFromJar(path + "libntcore.dylib");
            } else if (env.contains("nux")) { // Unix environment
                path += "linux/";
                path += "x86" + (is64 ? "-64" : "") + "/";
                NativeUtils.loadLibraryFromJar(path + "libntcore.so");
            } else {
                System.out.println("WARNING: ntcore NOT LOADED! On OS " + env);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AutonBuilder() {
        //Left is the default, gets updated later.
        startingPosition = AutonPosition.LEFT;

        // Initialize HashMap
        configMap = new HashMap<>();
        configMap.put("LLL", new ArrayList<>());
        configMap.put("LRL", new ArrayList<>());
        configMap.put("RLR", new ArrayList<>());
        configMap.put("RRR", new ArrayList<>());

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
    /*public void publishValues() {

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
    }*/

    /**
     * Save configuration to a comma separated value file (CSV)
     *
     * @param csv file to save config to; must have csv prefix
     *
     * @return whether or not the action was successful
     */
    public void save(File csv, String... configKeys) throws IOException {
        int numTables = 0;
        int numCols = 0;
        int maxNumRows = 0;

        CSVWriter writer;

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

        writer = new CSVWriter(new FileWriter(csv));

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
    }

    public void load(File csv) throws IOException, CsvConstraintViolationException {
        CSVReader reader = new CSVReader(new FileReader(csv.getPath()));
        String[] csvBuffer = reader.readNext(), headers;

        if (csvBuffer.length % 2 != 0)
            throw new CsvConstraintViolationException("Data is missing a header; assume that the data is broken");

        headers = new String[csvBuffer.length / 2];

        // Find ALL valid headers first
        // We parse the table by-row, so there's no point to parse
        // the entire table here.
        for (int i = 0; i < csvBuffer.length; i += 2) {
            String
                taskTable = csvBuffer[i].replaceFirst(TASK_PREFIX, ""),
                sideTable = csvBuffer[i + 1].replaceFirst(SIDE_PREFIX, ""),
                header = "X";

            if (taskTable.equals(sideTable))
                header = taskTable;

            headers[i / 2] = header;
        }

        // Iterate through remaining rows
        // Assume that the table is square and it hasn't been edited
        // outside of the auton app.
        reader.iterator().forEachRemaining(csvRow -> {
            for (int i = 0; i < headers.length; i++) {
                if (!"X".equals(headers[i])) {
                    List<TaskConfig> curList = configMap.get(headers[i]);
                    TaskConfig.AutonTask auton = TaskConfig.AutonTask.fromString(csvRow[2 * i]);
                    TaskConfig.ScoringSide score = TaskConfig.ScoringSide.fromString(csvRow[2 * i + 1]);

                    if (auton != null && score != null)
                        curList.add(new TaskConfig(auton, score));
                }
            }
        });

    }

    public void publish() {
        NetworkTable
                rootTable = ntInstance.getTable("AutonConfiguration"),
                lllTable = rootTable.getSubTable("LLL"),
                lrlTable = rootTable.getSubTable("LRL"),
                rlrTable = rootTable.getSubTable("RLR"),
                rrrTable = rootTable.getSubTable("RRR");

        String[]
                autonTaskLLL = TaskConfig.AutonTask.arrayToString(configMap.get("LLL").toArray(new TaskConfig[configMap.get("LLL").size()])),
                autonTaskLRL = TaskConfig.AutonTask.arrayToString(configMap.get("LRL").toArray(new TaskConfig[configMap.get("LRL").size()])),
                autonTaskRLR = TaskConfig.AutonTask.arrayToString(configMap.get("RLR").toArray(new TaskConfig[configMap.get("RLR").size()])),
                autonTaskRRR = TaskConfig.AutonTask.arrayToString(configMap.get("RRR").toArray(new TaskConfig[configMap.get("RRR").size()])),

                scoringSideLLL = TaskConfig.ScoringSide.arrayToString(configMap.get("LLL").toArray(new TaskConfig[configMap.get("LLL").size()])),
                scoringSideLRL = TaskConfig.ScoringSide.arrayToString(configMap.get("LRL").toArray(new TaskConfig[configMap.get("LRL").size()])),
                scoringSideRLR = TaskConfig.ScoringSide.arrayToString(configMap.get("RLR").toArray(new TaskConfig[configMap.get("RLR").size()])),
                scoringSideRRR = TaskConfig.ScoringSide.arrayToString(configMap.get("RRR").toArray(new TaskConfig[configMap.get("RRR").size()]));

        rootTable.getEntry("startingPosition").setString(startingPosition.toString());

        lllTable.getEntry("fieldSide").setString(fieldSideMap.get("LLL").toString());
        lllTable.getEntry("tasks").setStringArray(autonTaskLLL);
        lllTable.getEntry("sides").setStringArray(scoringSideLLL);

        lrlTable.getEntry("fieldSide").setString(fieldSideMap.get("LRL").toString());
        lrlTable.getEntry("tasks").setStringArray(autonTaskLRL);
        lrlTable.getEntry("sides").setStringArray(scoringSideLRL);

        rlrTable.getEntry("fieldSide").setString(fieldSideMap.get("RLR").toString());
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

    public List<TaskConfig> getTaskList(String key) {
        return configMap.get(key);
    }
}
