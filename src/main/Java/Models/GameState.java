package Models;

import java.util.List;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents the state of a game including the map, players, and unexecuted orders.
 */
public class GameState implements Serializable{

    /** The map of the game. */
    Map d_map;

    /** Any error message associated with the game state. */
    String d_error;

    /** The list of players in the game. */
    List<Player> d_players;

    /** The list of unexecuted orders in the game. */
    List<Order> d_unexecutedOrders;

    /** The buffer for logging game events. */
    LogEntryBuffer d_logEntryBuffer = new LogEntryBuffer();

    /** A flag indicating whether the load command has been executed. */
    Boolean d_loadCmd = false;

    /**
     * Number of turns in tournament.
     */
    int d_maxnumberofturns = 0;

    /**
     * Number of remaining turns in tournament.
     */
    int d_numberOfTurnsLeft = 0;

    /**
     * Maintains list of players lost in the game.
     */
    List<Player> d_playersFailed = new ArrayList<Player>();

    /**
     * Winner Player.
     */
    Player d_winner;

    /**
     * Retrieves the map of the game.
     * @return The map of the game.
     */
    public Map getD_map() {
        return d_map;
    }

    /**
     * Sets the map of the game.
     * @param p_map The map of the game.
     */
    public void setD_map(Map p_map) {
        this.d_map = p_map;
    }

    /**
     * Retrieves the list of players in the game.
     * @return The list of players in the game.
     */
    public List<Player> getD_players() {
        return d_players;
    }

    /**
     * Sets the list of players in the game.
     * @param p_players The list of players in the game.
     */
    public void setD_players(List<Player> p_players) {
        this.d_players = p_players;
    }

    /**
     * Retrieves the list of unexecuted orders in the game.
     * @return The list of unexecuted orders in the game.
     */
    public List<Order> getD_unexecutedOrders() {
        return d_unexecutedOrders;
    }

    /**
     * Sets the list of unexecuted orders in the game.
     * @param p_unexecutedOrders The list of unexecuted orders in the game.
     */
    public void setD_unexecutedOrders(List<Order> p_unexecutedOrders) {
        this.d_unexecutedOrders = p_unexecutedOrders;
    }

    /**
     * Retrieves the error message associated with the game state.
     * @return The error message associated with the game state.
     */
    public String getError() {
        return d_error;
    }

    /**
     * Sets the error message associated with the game state.
     * @param p_error The error message associated with the game state.
     */
    public void setError(String p_error) {
        this.d_error = p_error;
    }

    /**
     * Updates the log with a new message and its type.
     * @param p_logMessage The log message to be added.
     * @param p_logType    The type of the log message.
     */
    public void updateLog(String p_logMessage, String p_logType) {
        d_logEntryBuffer.logEvent(p_logMessage, p_logType);
    }

    /**
     * Retrieves the most recent log message.
     * @return The most recent log message.
     */
    public String getRecentLog(){
        return d_logEntryBuffer.getD_logMessage();
    }

    /**
     * Sets the load command flag to indicate that the load command has been executed.
     */
    public void setD_loadCommand() {
        this.d_loadCmd = true;
    }

    /**
     * Retrieves the value of the load command flag.
     * @return true if the load command has been executed, false otherwise.
     */
    public boolean getD_loadCommand(){
        return this.d_loadCmd;
    }

    /**
     * Returns max number of turns allowed in tournament.
     * @return int number of turns
     */
    public int getD_maxnumberofturns() {
        return d_maxnumberofturns;
    }

    /**
     * Sets max number of turns allowed in tournament.
     * @param d_maxnumberofturns number of turns
     */
    public void setD_maxnumberofturns(int d_maxnumberofturns) {
        this.d_maxnumberofturns = d_maxnumberofturns;
    }

    /**
     * Gets number of turns left at any stage of tournament.
     * @return int number of remaining turns
     */
    public int getD_numberOfTurnsLeft() {
        return d_numberOfTurnsLeft;
    }

    /**
     * Sets number of turns left at any stage of tournament.
     * @param d_numberOfTurnsLeft number of remaining turns
     */
    public void setD_numberOfTurnsLeft(int d_numberOfTurnsLeft) {
        this.d_numberOfTurnsLeft = d_numberOfTurnsLeft;
    }

    /**
     * Adds the Failed Player in GameState.
     * @param p_player player instance to remove
     */
    public void removePlayer(Player p_player){
        d_playersFailed.add(p_player);
    }

    /**
     * Retrieves the list of failed players.
     * @return List of Players that lost game.
     */
    public List<Player> getD_playersFailed() {
        return d_playersFailed;
    }

    /**
     * Sets the winner player object.
     * @param p_player winner player object
     */
    public void setD_winner(Player p_player){
        d_winner = p_player;
    }

    /**
     * Returns the winner player object.
     * @return returns winning player
     */
    public Player getD_winner(){
        return d_winner;
    }

}
