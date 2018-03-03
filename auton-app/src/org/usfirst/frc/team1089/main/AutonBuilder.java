package org.usfirst.frc.team1089.main;

import com.opencsv.CSVWriter;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import javafx.collections.ObservableList;
import org.fxmisc.easybind.EasyBind;
import org.usfirst.frc.team1089.main.util.AutonPosition;
import org.usfirst.frc.team1089.main.util.FieldSide;
import org.usfirst.frc.team1089.main.util.TaskConfig;

import java.io.File;
import java.io.FileWriter;
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
        System.out.println("Attempting to save...");
        boolean successful = true;
        List<List<TaskConfig>> configLists = new ArrayList<>();
        int numKeys;

        for (String key : configKeys) {
            configLists.add(configMap.get(key));
        }

        numKeys = configLists.size() * 2;

        if (!csv.toString().endsWith(".csv"))
            return false;

        File dir = csv.getParentFile();

        if (!dir.isDirectory() || (!dir.exists() && !dir.mkdirs()))
            return false;

        try {
            CSVWriter writer = new CSVWriter(new FileWriter(csv));
            String[]
                header = new String[numKeys], valueBuffer = new String[numKeys];

            for (int i = 0; i < configKeys.length; i++) {
                header[2 * i] = TASK_PREFIX + configKeys[i].toUpperCase();
                header[2 * i + 1] = SIDE_PREFIX + configKeys[i].toUpperCase();
            }

            writer.writeNext(header);

            int maxRows = 0;

            // We need the largest table in the set of data
            // We will use that number for the amount of times to iterate
            // through ALL tables.
            // I'm very lazy.
            for (List<TaskConfig> list : configLists)
                maxRows = list.size() > maxRows ? list.size() : maxRows;

            for (int table = 0; table < numKeys - 1; table += 2) {
                List<TaskConfig> curConfigList = configLists.get(table);
                for (int row = 0; row < maxRows; row++) {
                    TaskConfig curConfig = curConfigList.get(row);

                    System.out.println(curConfig);

                    valueBuffer[2 * table] = curConfig.autonTask.getValue().toString();
                    valueBuffer[2 * table + 1] = curConfig.scoringSide.getValue().toString();
                }

                writer.writeNext(valueBuffer);

                // Probably shouldn't reallocate memory in a loop.
                // Who cares, going for MVP bois.
                valueBuffer = new String[numKeys];
            }

            writer.close();
        } catch (Exception e) { // Definitely not the best way to do this
            e.printStackTrace();
            successful = false;
        }

        return successful;
    }

    public void setStartingPosition(AutonPosition newPosition) {
        startingPosition = newPosition;
    }

    public void setFieldSide(String configKey, FieldSide newSide) {
        fieldSideMap.replace(configKey, newSide);
    }
}
