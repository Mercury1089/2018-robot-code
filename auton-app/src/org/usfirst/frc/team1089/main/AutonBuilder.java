package org.usfirst.frc.team1089.main;

import com.opencsv.CSVWriter;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

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

    private HashMap<String, List<TaskConfig>> configMap;
    private NetworkTableInstance ntInstance;


//    static {
//        // Load native libraries and whatnot
//        System.loadLibrary("ntcore");
//    }

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
    public boolean save(File csv, String... configKeys) {
        boolean successful = true;
        List<List<TaskConfig>> configLists = new ArrayList<>();
        int numKeys;

        for (String key : configKeys) {
            if (!allDone(key))
                return false;

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

            int maxRows = Integer.MIN_VALUE;

            for (List<TaskConfig> list : configLists)
                maxRows = list.size() > maxRows ? list.size() : maxRows;

            for (int table = 0; table < numKeys; table++) {
                List<TaskConfig> curConfigList = configLists.get(table);
                for (int row = 0; row < maxRows; row++) {
                    TaskConfig curConfig = curConfigList.get(row);

                    valueBuffer[2 * table] = curConfig.autonTask.toString();
                    valueBuffer[2 * table + 1] = curConfig.scoringSide.toString();
                }

                writer.writeNext(valueBuffer);

                // Probably shouldn't reallocate memory in a loop.
                // Who cares, going for MVP bois.
                valueBuffer = new String[numKeys];
            }
        } catch (Exception e) { // Definitely not the best way to do this
            e.printStackTrace();
            successful = false;
        }

        return successful;
    }

    /**
     * Check whether or not the specified task config lists end with "Done"
     *
     * @param config the config to check
     *
     * @return {@code true} if task config exists and list ends with {@code AutonTask.DONE}
     */
    public boolean allDone(String config) {
        List<TaskConfig> configList = configMap.get(config.toUpperCase());

        return
            configList != null && !configList.isEmpty() &&
            configList.get(configList.size() - 1).autonTask.get() != AutonTask.DONE;
    }
}
