package Services;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import Constants.ApplicationConstants;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Map;
import Utils.CommonUtil;


/**
 * The MapService class provides services related to maps.
 * This class can be used to perform various CRUD operations and
 * show maps with continents, country and it's neighbours
 */
public class MapService implements Serializable {

    /**
     * Loads a map from a specified file, parsing continent, country, and border data to create a complete
     * map structure.
     *
     * @param p_gameState The game state into which the loaded map will be set.
     * @param p_loadFileName The name of the file from which to load the map.
     * @return The loaded and fully constructed Map object.
     * @throws InvalidMap If any errors occur during the file reading process or if the data is not correctly formatted.
     */
    public Map loadMap(GameState p_gameState, String p_loadFileName) throws InvalidMap
    {
        Map l_map = new Map();
        List<String> l_linesOfFile = loadFile(p_loadFileName);

        if (null != l_linesOfFile && !l_linesOfFile.isEmpty()) {
            if(l_linesOfFile.contains("[Territories]")) {
                MapReaderAdapter l_mapReaderAdapter = new MapReaderAdapter(new ConquestMapFileReader());
                l_mapReaderAdapter.parseMapFile(p_gameState, l_map, l_linesOfFile);
            } else if(l_linesOfFile.contains("[countries]")) {
                new MapFileReader().parseMapFile(p_gameState, l_map, l_linesOfFile);
            }
        }
        return l_map;
    }

    /**
     * Loads the contents of a specified map file into a list of strings.
     *
     * @param p_loadFileName The name of the file to be loaded.
     * @return A list of strings, each containing the contents of one line from the file.
     * @throws InvalidMap If the file cannot be found, is inaccessible, or any other IOException occurs.
     */
    public List<String> loadFile(String p_loadFileName) throws InvalidMap{

        String l_filePath = CommonUtil.getMapFilePath(p_loadFileName);
        List<String> l_lineList = new ArrayList<>();

        BufferedReader l_reader;
        try {
            l_reader = new BufferedReader(new FileReader(l_filePath));
            l_lineList = l_reader.lines().collect(Collectors.toList());
            l_reader.close();
        } catch (IOException l_e1) {
            throw new InvalidMap("The Map File name entered does not exist!");
        }
        return l_lineList;
    }

    /**
     * Prepares a map for editing by attempting to create a new map file if it does not exist or loading
     * an existing map file for editing.
     *
     * @param p_gameState The current game state.
     * @param p_editFilePath The path to the file to be edited.
     * @throws IOException If an I/O error occurs during the file operation.
     * @throws InvalidMap If the existing map file is found to be invalid during the loading process.
     */
    public void editMap(GameState p_gameState, String p_editFilePath) throws IOException, InvalidMap {

        String l_filePath = CommonUtil.getMapFilePath(p_editFilePath);
        File l_fileToBeEdited = new File(l_filePath);

        if (l_fileToBeEdited.createNewFile()) {
            System.out.println("The File has been successfully created");
            Map l_map = new Map();
            l_map.setD_mapFile(p_editFilePath);
            p_gameState.setD_map(l_map);
            p_gameState.updateLog(p_editFilePath+ " The File has been successfully created for the user to edit", "effect");
        } else {
            System.out.println("File already exists.");
            this.loadMap(p_gameState, p_editFilePath);
            if (null == p_gameState.getD_map()) {
                p_gameState.setD_map(new Map());
            }
            p_gameState.getD_map().setD_mapFile(p_editFilePath);
            p_gameState.updateLog(p_editFilePath+ " The File already exists and is loaded for editing", "effect");
        }
    }

    /**
     * Edits map functions based on user inputs.
     *
     * @param p_gameState The current game state.
     * @param p_argument The argument provided by the user.
     * @param p_operation The operation to be performed.
     * @param p_switchParameter The switch parameter for choosing the operation.
     * @throws IOException If an I/O error occurs.
     * @throws InvalidMap If the map is invalid.
     * @throws InvalidCommand If an invalid command is passed.
     */
    public void editFunctions(GameState p_gameState, String p_argument, String p_operation, Integer p_switchParameter) throws IOException, InvalidMap, InvalidCommand{
        Map l_updatedMap;
        String l_mapFileName = p_gameState.getD_map().getD_mapFile();
        Map l_mapToBeUpdated = (CommonUtil.isNull(p_gameState.getD_map().getD_continents()) && CommonUtil.isNull(p_gameState.getD_map().getD_countries())) ? this.loadMap(p_gameState, l_mapFileName) : p_gameState.getD_map();

        if(!CommonUtil.isNull(l_mapToBeUpdated)){
            switch(p_switchParameter){
                case 1:
                    l_updatedMap = addRemoveContinents(p_gameState, l_mapToBeUpdated, p_operation, p_argument);
                    break;
                case 2:
                    l_updatedMap = addRemoveCountry(p_gameState, l_mapToBeUpdated, p_operation, p_argument);
                    break;
                case 3:
                    l_updatedMap = addRemoveNeighbour(p_gameState, l_mapToBeUpdated, p_operation, p_argument);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + p_switchParameter);
            }
            p_gameState.setD_map(l_updatedMap);
            p_gameState.getD_map().setD_mapFile(l_mapFileName);
        }
    }

    /**
     * Adds or removes continents from the map.
     *
     * @param p_gameState The current game state.
     * @param p_mapToBeUpdated The map to be updated.
     * @param p_operation The operation to be performed (add/remove).
     * @param p_argument The continent details to be added or removed.
     * @return The updated Map object.
     * @throws InvalidMap If the operation fails due to invalid data or command.
     */
    public Map addRemoveContinents(GameState p_gameState, Map p_mapToBeUpdated, String p_operation, String p_argument) throws InvalidMap {

        try {
            if (p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length==2) {
                p_mapToBeUpdated.createContinent(p_argument.split(" ")[0], Integer.parseInt(p_argument.split(" ")[1]));
                this.setD_MapServiceLog("Continent "+ p_argument.split(" ")[0]+ " added successfully!", p_gameState);
            } else if (p_operation.equalsIgnoreCase("remove") && p_argument.split(" ").length==1) {
                p_mapToBeUpdated.deleteContinent(p_argument.split(" ")[0]);
                this.setD_MapServiceLog("Continent "+ p_argument.split(" ")[0]+ " removed successfully!", p_gameState);
            } else {
                throw new InvalidMap("Continent "+p_argument.split(" ")[0]+" couldn't be added/removed. Changes are not made due to Invalid Command Passed.");
            }
        } catch (InvalidMap | NumberFormatException l_e) {
            this.setD_MapServiceLog(l_e.getMessage(), p_gameState);
        }
        return p_mapToBeUpdated;
    }

    /**
     * Adds or removes countries from the map.
     *
     * @param p_gameState The current game state.
     * @param p_mapToBeUpdated The map to be updated.
     * @param p_argument The country details to be added or removed.
     * @param p_operation The operation to be performed (add/remove).
     * @return The updated Map object.
     * @throws InvalidMap If the operation fails due to invalid data or command.
     */
    public Map addRemoveCountry(GameState p_gameState, Map p_mapToBeUpdated, String p_argument, String p_operation) throws InvalidMap{

        try {
            if (p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length==2){
                p_mapToBeUpdated.createCountry(p_argument.split(" ")[0], p_argument.split(" ")[1]);
                this.setD_MapServiceLog("Country "+ p_argument.split(" ")[0]+ " added successfully!", p_gameState);
            }else if(p_operation.equalsIgnoreCase("remove")&& p_argument.split(" ").length==1){
                p_mapToBeUpdated.deleteCountry(p_argument.split(" ")[0]);
                this.setD_MapServiceLog("Country "+ p_argument.split(" ")[0]+ " removed successfully!", p_gameState);
            }else{
                throw new InvalidMap("Country "+p_argument.split(" ")[0]+" could not be "+ p_operation +"ed!");
            }
        } catch (InvalidMap l_e) {
            this.setD_MapServiceLog(l_e.getMessage(), p_gameState);
        }
        return p_mapToBeUpdated;
    }

    /**
     * Adds or removes country neighbours from the map.
     *
     * @param p_gameState The current game state.
     * @param p_mapToBeUpdated The map to be updated.
     * @param p_argument The neighbour details to be added or removed.
     * @param p_operation The operation to be performed (add/remove).
     * @return The updated Map object.
     * @throws InvalidMap If the operation fails due to invalid data or command.
     */
    public Map addRemoveNeighbour(GameState p_gameState, Map p_mapToBeUpdated, String p_argument, String p_operation) throws InvalidMap{

        try {
            if (p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length==2){
                p_mapToBeUpdated.addCountryNeighbour(p_argument.split(" ")[0], p_argument.split(" ")[1]);
                this.setD_MapServiceLog("Neighbour Pair "+p_argument.split(" ")[0]+" "+p_argument.split(" ")[1]+" added successfully!", p_gameState);
            }else if(p_operation.equalsIgnoreCase("remove") && p_argument.split(" ").length==2){
                p_mapToBeUpdated.deleteCountryNeighbour(p_argument.split(" ")[0], p_argument.split(" ")[1]);
                this.setD_MapServiceLog("Neighbour Pair "+p_argument.split(" ")[0]+" "+p_argument.split(" ")[1]+" removed successfully!", p_gameState);
            }else{
                throw new InvalidMap("Neighbour could not be "+ p_operation +"ed!");
            }
        } catch (InvalidMap l_e) {
            this.setD_MapServiceLog(l_e.getMessage(), p_gameState);
        }
        return p_mapToBeUpdated;
    }


    /**
     * Attempts to save the current map to a file.
     *
     * @param p_gameState The current game state.
     * @param p_fileName The name of the file to save the map to.
     * @return True if the map is successfully saved, false otherwise.
     * @throws InvalidMap If the map is found to be invalid during validation.
     */
    public boolean saveMap(GameState p_gameState, String p_fileName) throws InvalidMap {
        boolean l_flagValidate = false;
        try {

            String l_mapFormat = null;
            // Verifies if the file linked to savemap and edited by user are same
            if (!p_fileName.equalsIgnoreCase(p_gameState.getD_map().getD_mapFile())) {
                p_gameState.setError("Please use the same File name for saving that you used for edit");
                return false;
            } else {
                if (null != p_gameState.getD_map()) {
                    Models.Map l_currentMap = p_gameState.getD_map();

                    // Proceeds to save the map if it passes the validation check
                    this.setD_MapServiceLog("Authenticating Map......", p_gameState);
                    if (l_currentMap.Validate()) {
                        l_mapFormat = this.getFormatToSave();
                        Files.deleteIfExists(Paths.get(CommonUtil.getMapFilePath(p_fileName)));
                        FileWriter l_writer = new FileWriter(CommonUtil.getMapFilePath(p_fileName));

                        parseMapToFile(p_gameState, l_writer, l_mapFormat);
                        p_gameState.updateLog("Map Saved Successfully", "effect");

                        p_gameState.updateLog("Map File Saved Successfully", "effect");
                        l_writer.close();
                    }
                } else {
                    p_gameState.updateLog("Failed to Save the Map File due to Unsuccessful Authentication!", "effect");
                    p_gameState.setError("Authentication Failed!");
                    return false;
                }
            }
            return true;
        } catch (IOException | InvalidMap l_e) {
            this.setD_MapServiceLog(l_e.getMessage(), p_gameState);
            p_gameState.updateLog("Couldn't save the changes in map file", "effect");
            p_gameState.setError("There was an issue while saving the Map File");
            return false;
        }
    }

    /**
     * Parses the game state to a file based on the specified map format.
     * If the map format is "ConquestMap", it uses a MapWriterAdapter with a ConquestMapFileWriter.
     * Otherwise, it uses a default MapFileWriter.
     *
     * @param p_gameState The game state containing the map data to be parsed.
     * @param l_writer The FileWriter object to write the parsed map data.
     * @param l_mapFormat The format of the map ("ConquestMap" or other).
     * @throws IOException If an I/O error occurs during the file writing process.
     */
    private void parseMapToFile(GameState p_gameState, FileWriter l_writer, String l_mapFormat) throws IOException {
        if(l_mapFormat.equalsIgnoreCase("ConquestMap")) {
            MapWriterAdapter l_mapWriterAdapter = new MapWriterAdapter(new ConquestMapFileWriter());
            l_mapWriterAdapter.parseMapToFile(p_gameState, l_writer, l_mapFormat);
        } else {
            new MapFileWriter().parseMapToFile(p_gameState, l_writer, l_mapFormat);
        }
    }

    /**
     * Resets the map to its initial state.
     *
     * @param p_gameState The current game state.
     * @param p_fileToLoad The map file to be loaded.
     */
    public void resetMap(GameState p_gameState, String p_fileToLoad) {
        System.err.println("Map cannot be loaded, as it is invalid. Kindly provide valid map");
        p_gameState.updateLog(p_fileToLoad+" map could not be loaded as it is invalid!", "effect");
        p_gameState.setD_map(new Models.Map());
    }

    /**
     * Sets the log message for map service.
     *
     * @param p_MapServiceLog The log message to be set.
     * @param p_gameState The current game state.
     */
    public void setD_MapServiceLog(String p_MapServiceLog, GameState p_gameState){
        System.out.println(p_MapServiceLog);
        p_gameState.updateLog(p_MapServiceLog, "effect");
    }

    /**
     * Prompts the user to select the format to save the map.
     *
     * @return The selected format for saving the map.
     * @throws IOException If an I/O error occurs.
     */
    public String getFormatToSave() throws IOException {
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Kindly press 1 to save the map as conquest map format or else press 2");
        String l_nextOrderCheck = l_reader.readLine();
        if (l_nextOrderCheck.equalsIgnoreCase("1")) {
            return "ConquestMap";
        } else if (l_nextOrderCheck.equalsIgnoreCase("2")) {
            return "NormalMap";
        } else {
            System.err.println("Invalid Input Passed.");
            return this.getFormatToSave();
        }
    }
}


