package Models;

import Services.PlayerService;
import java.io.Serializable;

/**
 * Represents a diplomacy card in the game.
 */
public class Diplomacy implements Card , Serializable{

    Player d_IssuingPlayer;
    String d_targetPlayer;
    String d_orderExecutionLog;

    /**
     * Initializes a new instance of the Diplomacy class.
     *
     * @param p_targetPlayer   The target player for the diplomacy.
     * @param p_IssuingPlayer The player issuing the diplomacy.
     */
    public Diplomacy(String p_targetPlayer, Player p_IssuingPlayer){
        this.d_targetPlayer = p_targetPlayer;
        this.d_IssuingPlayer = p_IssuingPlayer;
    }

    /**
     * Executes the diplomacy card.
     *
     * @param p_gameState The current game state.
     */
    @Override
    public void execute(GameState p_gameState) {
        PlayerService l_playerService = new PlayerService();
        Player l_targetPlayer = l_playerService.findPlayerByName(d_targetPlayer, p_gameState);
        l_targetPlayer.addPlayerNegotiation(d_IssuingPlayer);
        d_IssuingPlayer.addPlayerNegotiation(l_targetPlayer);
        d_IssuingPlayer.removeCard("negotiate");
        this.setD_orderExecutionLog("Negotiation with "+ d_targetPlayer+ " approached by "+d_IssuingPlayer.getPlayerName()+" successful!", "default");
        p_gameState.updateLog(d_orderExecutionLog, "effect");
    }

    /**
     * Checks if the diplomacy card is valid.
     *
     * @param p_gameState The current game state.
     * @return True if the diplomacy card is valid, false otherwise.
     */
    @Override
    public boolean valid(GameState p_gameState) {
        return true;
    }

    /**
     * Prints the details of the diplomacy card.
     */
    public void printOrder() {
        this.d_orderExecutionLog = "----------Diplomacy order issued by player " + this.d_IssuingPlayer.getPlayerName()
                + "----------" + System.lineSeparator() + "Request to " + " negotiate attacks from "
                + this.d_targetPlayer;
        System.out.println(System.lineSeparator()+this.d_orderExecutionLog);
    }

    /**
     * Retrieves the order execution log.
     *
     * @return The order execution log.
     */
    @Override
    public String orderExecutionLog() {
        return this.d_orderExecutionLog;
    }

    /**
     * Checks if the diplomacy card is a valid order.
     *
     * @param p_gameState The current game state.
     * @return True if the diplomacy card is valid, false otherwise.
     */
    @Override
    public Boolean checkValidOrder(GameState p_gameState) {
        PlayerService l_playerService = new PlayerService();
        Player l_targetPlayer = l_playerService.findPlayerByName(d_targetPlayer, p_gameState);
        if(!p_gameState.getD_players().contains(l_targetPlayer)){
            this.setD_orderExecutionLog("Player to negotiate doesn't exist!", "error");
            p_gameState.updateLog(orderExecutionLog(), "effect");
            return false;
        }
        return true;
    }

    /**
     * Sets the order execution log.
     *
     * @param p_orderExecutionLog The order execution log to set.
     * @param p_logType           The type of log.
     */
    public void setD_orderExecutionLog(String p_orderExecutionLog, String p_logType) {
        this.d_orderExecutionLog = p_orderExecutionLog;
        if (p_logType.equals("error")) {
            System.err.println(p_orderExecutionLog);
        } else {
            System.out.println(p_orderExecutionLog);
        }
    }

    /**
     * Generates the current order string.
     *
     * @return The current order string.
     */
    private String currentOrder() {
        return "Diplomacy Order : " + "negotiate" + " " + this.d_targetPlayer;
    }

    /**
     * Retrieves the name of the order.
     *
     * @return The name of the order.
     */
    @Override
    public String getOrderName() {
        return "diplomacy";
    }
}
