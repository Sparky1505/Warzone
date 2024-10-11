package Services;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import Models.GameState;
import Models.Map;

/**
 * The MapWriterAdapter class is used for writing to conquest map file
 */
public class MapWriterAdapter extends MapFileWriter{
    /**
     * An instance of ConquestMapFileWriter that this adapter encapsulates.
     */
    private ConquestMapFileWriter l_conquestMapFileWriter;

    /**
     * Constructs a MapWriterAdapter with a specified ConquestMapFileWriter.
     *
     * @param p_conquestMapFileWriter The ConquestMapFileWriter to be adapted.
     */
    public MapWriterAdapter(ConquestMapFileWriter p_conquestMapFileWriter) {
        this.l_conquestMapFileWriter = p_conquestMapFileWriter;
    }

    /**
     * Writes the map data to a file using the adapted ConquestMapFileWriter. This method
     * delegates the writing process to the parseMapToFile method of the ConquestMapFileWriter,
     * enabling the adaptation of different map formats.
     *
     * @param p_gameState The GameState object containing the map data to be written.
     * @param l_writer The FileWriter object used to write to the file.
     * @param l_mapFormat The format of the map to be written.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public void parseMapToFile(GameState p_gameState, FileWriter l_writer, String l_mapFormat) throws IOException {
        l_conquestMapFileWriter.parseMapToFile(p_gameState, l_writer, l_mapFormat);
    }
}
