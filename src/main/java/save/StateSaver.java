package save;

import log.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class StateSaver {
    /**
     * Path to the config file
     */
    private final static String FILENAME = System.getProperty("user.home") + "/RobotData.txt";
    /**
     * Store saved data locally
     */
    public void save(Map<String, String> stateMap) {
        Logger.debug("Store trigger");
        try {
            Path file = Path.of(FILENAME);
            List<String> data = new ArrayList<>();
            for (String key : stateMap.keySet()) {
                data.add(key + "=" + stateMap.get(key));
            }
            Files.write(file, data);
        } catch (IOException e) {
            Logger.debug("Failed to store data due to IO exception with message: \n" + e.getMessage());
        }
    }

    /**
     * Restore locally saved data
     */
    public Map<String, String> load() {
        Logger.debug("Restore trigger");
        Map<String, String> stateMap = new HashMap<>();
        try {
            List<String> data = Files.readAllLines(Path.of(FILENAME), StandardCharsets.UTF_8);
            String[] parsedLine;
            for (String line : data) {
                parsedLine = line.split("=");
                stateMap.put(parsedLine[0], parsedLine[1]);
            }
        } catch (IOException e) {
            Logger.debug("Failed to restore data due to IO exception with message: \n" + e.getMessage());
        }
        return stateMap;
    }
}
