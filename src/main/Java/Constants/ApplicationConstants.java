package Constants;

import java.util.Arrays;
import java.util.List;

/**
 * This class initializes all the constants that are going to be used throughout the application.
 */
public final class ApplicationConstants {

    /** Error message for invalid command format in savegame. */
    public static final String INVALID_COMMAND_ERROR_SAVEGAME = "Invalid command. Kindly provide command in Format of : savegame filename";

    /** Error message for invalid command format in loadgame. */
    public static final String INVALID_COMMAND_ERROR_LOADGAME = "Invalid command. Kindly provide command in Format of : loadgame filename";

    /** Error message for invalid command format in editing map. */
    public static final String INVALID_COMMAND_ERROR_EDITMAP = "Invalid command. Kindly provide command in Format of : editmap filename";

    /** Error message for invalid command format in editing continent. */
    public static final String INVALID_COMMAND_ERROR_EDITCONTINENT = "Invalid command. Kindly provide command in Format of : editcontinent -add continentID continentvalue -remove continentID";

    /** Error message for invalid command format in editing country. */
    public static final String INVALID_COMMAND_ERROR_EDITCOUNTRY = "Invalid command. Kindly provide command in Format of : editcountry -add countrytID continentID -remove countryID";

    /** Error message for invalid command format in editing neighbor. */
    public static final String INVALID_COMMAND_ERROR_EDITNEIGHBOUR = "Invalid command. Kindly provide command in Format of : editneighbor -add countryID neighborcountryID -remove countryID neighborcountryID";

    /** Error message for invalid command format in saving map. */
    public static final String INVALID_COMMAND_ERROR_SAVEMAP = "Invalid command. Kindly provide command in Format of : savemap filename";

    /** Error message for invalid command format in tournament mode. */
    public static final String INVALID_COMMAND_TOURNAMENT_MODE = "Invalid Command. Kindly provide command in format of : tournament -M listofmapfiles -P listofplayerstrategies -G numberofgames -D maxnumberofturns";

    /** Error message for empty map. */
    public static final String INVALID_MAP_ERROR_EMPTY = "No Map found! Please load a valid map to check!";

    /** Error message for invalid command format in loading map. */
    public static final String INVALID_COMMAND_ERROR_LOADMAP = "Invalid command. Kindly provide command in Format of : loadmap filename";

    /** Error message for invalid command format in validating map. */
    public static final String INVALID_COMMAND_ERROR_VALIDATEMAP = "Invalid command! validatemap is not supposed to have any arguments";

    /** Error message for invalid command format in managing game players. */
    public static final String INVALID_COMMAND_ERROR_GAMEPLAYER = "Invalid command. Kindly provide command in Format of : gameplayer -add playername -remove playername";

    /** Error message for invalid map loading. */
    public static final String INVALID_MAP_LOADED = "Map cannot be loaded, as it is invalid. Kindly provide valid map";

    /** Error message for invalid command format in assigning countries. */
    public static final String INVALID_COMMAND_ERROR_ASSIGNCOUNTRIES = "Invalid command. Kindly provide command in Format of : assigncountries";

    /** Error message for invalid command format in deploying orders. */
    public static final String INVALID_COMMAND_ERROR_DEPLOY_ORDER = "Invalid command. Kindly provide command in Format of : deploy countryID <CountryName> <num> (until all reinforcements have been placed)";

    /** Message indicating a valid map. */
    public static final String VALID_MAP = "The loaded map is valid!";

    /** Key for command arguments. */
    public static final String ARGUMENTS = "arguments";

    /** Key for command operation. */
    public static final String OPERATION = "operation";

    /** File extension for map files. */
    public static final String MAPFILEEXTENSION = ".map";

    /** ANSI color code for red. */
    public static final String RED = "\033[0;31m";

    /** ANSI color code for green. */
    public static final String GREEN = "\033[0;32m";

    /** ANSI color code for yellow. */
    public static final String YELLOW = "\033[0;33m";

    /** ANSI color code for blue. */
    public static final String BLUE = "\033[0;34m";

    /** ANSI color code for purple. */
    public static final String PURPLE = "\033[0;35m";

    /** ANSI color code for cyan. */
    public static final String CYAN = "\033[0;36m";

    /** ANSI color code for white background. */
    public static final String WHITE = "\u001B[47m";

    /** Section header for continents. */
    public static final String CONTINENTS = "[continents]";

    /** Section header for countries. */
    public static final String COUNTRIES = "[countries]";

    /** Section header for borders. */
    public static final String BORDERS = "[borders]";

    /** Label for army counts. */
    public static final String ARMIES = "Armies";

    /** Label for control values. */
    public static final String CONTROL_VALUE = "Control Value";

    /** Label for connectivity. */
    public static final String CONNECTIVITY = "Connections";

    /** Default directory for map files. */
    public static final String SRC_MAIN_RESOURCES = "src/main/resources";

    /** Default console width. */
    public static final int CONSOLE_WIDTH = 80;

    /** List of ANSI color codes. */
    public static final List<String> COLORS = Arrays.asList(YELLOW, PURPLE, RED, BLUE, GREEN, CYAN);

    public static final String CONQUEST_CONTINENTS = "[Continents]";
    public static final String CONQUEST_TERRITORIES = "[Territories]";

    public static final List<String> PLAYER_BEHAVIORS = Arrays.asList("Human", "Aggressive", "Random", "Benevolent", "Cheater");
    public static final List<String> TOURNAMENT_PLAYER_BEHAVIORS = Arrays.asList("Aggressive", "Random", "Benevolent", "Cheater");

    /** List of valid commands for blockade validation. */
    public static final List<String> BLOCKADEVALIDATION = Arrays.asList("bomb", "advance", "airlift", "negotiate");

    /** List of valid cards. */
    public static final List<String> CARDS = Arrays.asList("bomb", "blockade", "airlift", "negotiate");

    /** Size of the cards list. */
    public static final int SIZE = CARDS.size();
}
