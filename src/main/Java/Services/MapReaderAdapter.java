package Services;
import java.util.List;
import Models.GameState;
import Models.Map;

/**
 * The MapReaderAdapter class is used for reading conquest map file.
 */
public class MapReaderAdapter extends MapFileReader{
    /**
     * An instance of ConquestMapFileReader that this adapter wraps.
     */
    private ConquestMapFileReader l_conquestMapFileReader;

    /**
     * Constructs a MapReaderAdapter with a specific ConquestMapFileReader.
     *
     * @param p_conquestMapFileReader The ConquestMapFileReader to be adapted.
     */
    public MapReaderAdapter(ConquestMapFileReader p_conquestMapFileReader) {
        this.l_conquestMapFileReader = p_conquestMapFileReader;
    }

    /**
     * Parses a map file using the adapted ConquestMapFileReader. It delegates the call to
     * the readConquestFile method of ConquestMapFileReader.
     *
     * @param p_gameState The GameState object associated with the map being parsed.
     * @param p_map The Map object to which the parsed data will be applied.
     * @param p_linesOfFile The lines of the map file to be parsed.
     */
    public void parseMapFile(GameState p_gameState, Map p_map, List<String> p_linesOfFile) {
        l_conquestMapFileReader.readConquestFile(p_gameState, p_map, p_linesOfFile);
    }
}
