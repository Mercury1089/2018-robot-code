package org.usfirst.frc.team1089.main;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Backend for auton builder app
 */
public class AutonBuilder {
    private final String
        TABLE_NAME = "AutonBuilder",
        TASK_PREFIX = "task",
        SIDE_PREFIX = "side";

    private HashMap<String, List<TaskConfig>> configMap;
    private NetworkTableInstance ntInstance;


    static {
        // Load native libraries and whatnot
        System.loadLibrary("ntcore");
    }

    public AutonBuilder() {
        // Initialize HashMap
        configMap = new HashMap<>();
        configMap.put("LLL", new ArrayList<>());
        configMap.put("LRL", new ArrayList<>());
        configMap.put("RLR", new ArrayList<>());
        configMap.put("RRR", new ArrayList<>());

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
    public boolean save(File csv) {
        boolean successful = true;

        if (!csv.toString().endsWith(".csv"))
            return false;

        File dir = csv.getParentFile();

        if (!dir.isDirectory() || (!dir.exists() && !dir.mkdirs()))
            return false;

        try {
            CSVWriter writer = new CSVWriter(new FileWriter(csv));
        } catch (Exception e) { // Definitely not the best way to do this
            e.printStackTrace();
            successful = false;
        }

        return successful;
    }

}
