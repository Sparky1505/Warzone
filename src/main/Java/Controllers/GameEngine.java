package Controllers;

import Models.GameState;
import Models.IssueOrderPhase;
import Models.OrderExecutionPhase;
import Models.Phase;
import Models.StartUpPhase;

import java.io.Serializable;

/**
 * The GameEngine class acts as the entry point of the Game and keeps the track of current Game
 * State.
 */
public class GameEngine implements Serializable {
    /**
     * The d_gameState stores the data about the current GamePlay.
     */
    GameState d_gameState = new GameState();

    /**
     * Retrives the current state of the game.
     *
     * @return The current GameState object.
     */
    public GameState getD_gameState() {
        return d_gameState;
    }

    /**
     * Sets the game state to the specified GameState object.
     *
     * @param p_gameState The new GameState object.
     */
    public void setD_gameState(GameState p_gameState) {
        this.d_gameState = p_gameState;
    }

    /**
     * Represents the current phase of the game.
     */
    Phase d_currentPhase = new StartUpPhase(this, d_gameState);

    /**
     * Indicates whether the game is currently in tournament mode.
     */
    static boolean d_isTournamentMode = false;

    /**
     * Checks if the game is in tournament mode.
     *
     * @return true if the game is in tournament mode, false otherwise.
     */
    public boolean isD_isTournamentMode() {
        return d_isTournamentMode;
    }

    /**
     * Sets the tournament mode of the game.
     *
     * @param p_isTournamentMode The tournament mode to set.
     */
    public void setD_isTournamentMode(boolean p_isTournamentMode) {
        GameEngine.d_isTournamentMode = p_isTournamentMode;
    }

    /**
     * Sets the current phase of the game.
     *
     * @param p_phase The phase to set as the current phase.
     */
    public void setD_CurrentPhase(Phase p_phase) {
        d_currentPhase = p_phase;
    }

    /**
     * Loads a specified phase into the game, setting it as the current phase and initializing it.
     *
     * @param p_phase The phase to load into the game.
     */
    public void loadPhase(Phase p_phase){
        d_currentPhase = p_phase;
        d_gameState = p_phase.getD_gameState();
        getD_CurrentPhase().initPhase(d_isTournamentMode);
    }

    /**
     * Sets the game to the Startup Phase and initializes it.
     */
    public void setStartUpPhase(){
        this.setD_gameEngineLog("Start Up Phase", "phase");
        setD_CurrentPhase(new StartUpPhase(this, d_gameState));
        getD_CurrentPhase().initPhase(d_isTournamentMode);
    }

    /**
     * Sets the game to the Issue Order Phase and initializes it.
     *
     * @param p_isTournamentMode Indicates whether the phase is being initialized in tournament mode.
     */
    public void setIssueOrderPhase(boolean p_isTournamentMode) {
        this.setD_gameEngineLog("Issue Order Phase", "phase");
        setD_CurrentPhase(new IssueOrderPhase(this, d_gameState));
        getD_CurrentPhase().initPhase(p_isTournamentMode);
    }

    /**
     * Sets the game to the Order Execution Phase and initializes it.
     */
    public void setOrderExecutionPhase() {
        this.setD_gameEngineLog("Order Execution Phase", "phase");
        setD_CurrentPhase(new OrderExecutionPhase(this, d_gameState));
        getD_CurrentPhase().initPhase(d_isTournamentMode);
    }

    /**
     * Retrieves the current phase of the game.
     *
     * @return The current Phase object.
     */
    public Phase getD_CurrentPhase() {
        return d_currentPhase;
    }

    /**
     * Logs game engine activities and displays them to the console.
     *
     * @param p_gameEngineLog The log message to be displayed.
     * @param p_logType The type of the log message, indicating the context in which it was generated.
     */
    public void setD_gameEngineLog(String p_gameEngineLog, String p_logType) {
        d_currentPhase.getD_gameState().updateLog(p_gameEngineLog, p_logType);
        String l_consoleLogger = p_logType.toLowerCase().equals("phase")
                ? "\n************ " + p_gameEngineLog + " ************\n"
                : p_gameEngineLog;
        System.out.println(l_consoleLogger);
    }

    /**
     * The main method responsible for accepting command from users and redirecting
     * those to corresponding logical flows.
     *
     * @param p_args the program doesn't use default command line arguments
     */
    public static void main(String[] p_args) {
        GameEngine l_game = new GameEngine();

        l_game.getD_CurrentPhase().getD_gameState().updateLog("Initializing the Game ......" + System.lineSeparator(),
                "start");
        l_game.setD_gameEngineLog("Game Startup Phase", "phase");
        l_game.getD_CurrentPhase().initPhase(d_isTournamentMode);
    }
}
