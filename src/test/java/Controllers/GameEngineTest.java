package Controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Models.Continent;
import Models.GameState;
import Models.Map;
import Models.Phase;
import Models.StartUpPhase;

/**
 * The GameEngineTest class provides unit tests for the GameEngine class.
 */
public class GameEngineTest {


    /** The map used for testing. */
    Map d_map;

    /** The current phase of the game. */
    Phase d_currentPhase;

    /** The game engine instance being tested. */
    GameEngine d_gameEngine;


    /**
     * Sets up the test environment before each test case.
     */
    @Before
    public void setup() {
        d_map = new Map();
        d_gameEngine = new GameEngine();
        d_currentPhase = d_gameEngine.getD_CurrentPhase();
    }

    /**
     * Tests the behavior of the performEditMap method when an invalid command is given.
     *
     * @throws IOException      If an I/O error occurs.
     * @throws InvalidCommand  If the command is invalid.
     * @throws InvalidMap      If the map is invalid.
     */
    @Test(expected = InvalidCommand.class)
    public void testPerformEditMapInvalidCommand() throws IOException, InvalidCommand, InvalidMap {
        d_currentPhase.handleCommand("editmap");
    }

    /**
     * Tests the behavior of the performEditContinent method when an invalid command is given.
     * It checks if the log message indicates that continent editing is not possible without editing the map first.
     *
     * @throws InvalidCommand  If the command is invalid.
     * @throws IOException     If an I/O error occurs.
     * @throws InvalidMap      If the map is invalid.
     */
    @Test
    public void testPerformEditContinentInvalidCommand() throws IOException, InvalidCommand, InvalidMap {
        d_currentPhase.handleCommand("editcontinent");
        GameState l_state = d_currentPhase.getD_gameState();
        String actualLog = l_state.getRecentLog().trim();
        assertEquals("Log: Can not Edit Continent, please perform `editmap` first", actualLog);
    }

    /**
     * Tests the behavior of the performEditContinent method when valid commands are given.
     * It verifies if continents can be added and removed successfully, and if the state is updated accordingly.
     *
     * @throws IOException      If an I/O error occurs.
     * @throws InvalidCommand  If the command is invalid.
     * @throws InvalidMap      If the map is invalid.
     */
    @Test
    public void testPerformEditContinentValidCommand() throws IOException, InvalidCommand, InvalidMap {
        d_map.setD_mapFile("testedit");
        GameState l_state = d_currentPhase.getD_gameState();

        l_state.setD_map(d_map);
        d_currentPhase.setD_gameState(l_state);

        d_currentPhase.handleCommand("editcontinent -add Europe 10 -add America 20");

        l_state = d_currentPhase.getD_gameState();

        List<Continent> l_continents = l_state.getD_map().getD_continents();
        assertEquals(3, l_continents.size());
        assertEquals("Asia", l_continents.get(0).getD_continentName());
        assertEquals("5", l_continents.get(0).getD_continentValue().toString());
        assertEquals("Europe", l_continents.get(1).getD_continentName());
        assertEquals("10", l_continents.get(1).getD_continentValue().toString());

        d_currentPhase.handleCommand("editcontinent -remove Europe");

        l_state = d_currentPhase.getD_gameState();
        l_continents = l_state.getD_map().getD_continents();
        assertEquals(2, l_continents.size());
    }


    /**
     * Tests the behavior of the performSaveMap method when an invalid command is given.
     * It checks if the log message indicates that no map was found to save.
     *
     * @throws InvalidCommand  If the command is invalid.
     * @throws InvalidMap      If the map is invalid.
     * @throws IOException     If an I/O error occurs.
     */
    @Test
    public void testPerformSaveMapInvalidCommand() throws InvalidCommand, InvalidMap, IOException {
        d_currentPhase.handleCommand("savemap");
        GameState l_state = d_currentPhase.getD_gameState();

        String actualLog = l_state.getRecentLog().trim();
        assertEquals("Log: No map found to save, Please `editmap` first", actualLog);

    }

    /**
     * Tests savegame command.
     *
     * @throws InvalidCommand Exception
     * @throws InvalidMap     Exception
     * @throws IOException Exception
     */
    @Test
    public void testPerformSaveGameValidCommand() throws InvalidCommand, InvalidMap, IOException {
        d_currentPhase.handleCommand("savegame TestGameLog.txt");
        GameState l_state = d_currentPhase.getD_gameState();
        String actualLog = l_state.getRecentLog().trim();
        assertEquals("Log: Game Saved Successfully to TestGameLog.txt", actualLog);

    }

    /**
     * Tests loadgame command.
     *
     * @throws InvalidCommand Exception
     * @throws InvalidMap     Exception
     * @throws IOException Exception
     */
    @Test(expected = FileNotFoundException.class)
    public void testPerformLoadGameValidCommand() throws InvalidCommand, InvalidMap, IOException {
        d_currentPhase.handleCommand("loadgame NoGame.txt");
    }

    /**
     * Tests the behavior of the assignCountries method when an invalid command is given.
     * It expects an InvalidCommand exception to be thrown.
     *
     * @throws InvalidCommand  If the command is invalid.
     * @throws InvalidMap      If the map is invalid.
     * @throws IOException     If an I/O error occurs.
     */
    @Test(expected = InvalidCommand.class)
    public void testAssignCountriesInvalidCommand() throws IOException, InvalidCommand, InvalidMap {
        d_currentPhase.getD_gameState().setD_loadCommand();
        d_currentPhase.handleCommand("assigncountries add india");
    }


    /**
     * Tests if the current phase of the game is StartUpPhase.
     */
    @Test
    public void testCorrectStartupPhase() {
        assertTrue(d_gameEngine.getD_CurrentPhase() instanceof StartUpPhase);
    }
}
