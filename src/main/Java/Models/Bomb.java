package Models;

import Utils.CommonUtil;

import java.io.Serializable;

/**
 * The Bomb class represents a Bomb card in the game, which can be used to execute a bomb action on a target country.
 */
public class Bomb implements Card, Serializable {

    /** The player initiating the bomb action. */
    Player d_playerInitiator;

    /** The ID of the target country for the bomb action. */
    String d_targetCountryID;

    /** Log message for the order execution. */
    String d_orderLog;

    /**
     * Constructs a Bomb card with the specified initiator player and target country ID.
     *
     * @param p_playerInitiator the player initiating the bomb action
     * @param p_targetCountry   the ID of the target country for the bomb action
     */
    public Bomb(Player p_playerInitiator, String p_targetCountry) {
        this.d_playerInitiator = p_playerInitiator;
        this.d_targetCountryID = p_targetCountry;
    }

    /**
     * Executes the bomb card action in the game state.
     *
     * @param p_gameState the current game state
     */
    @Override
    public void execute(GameState p_gameState) {
        if (valid(p_gameState)) {
            Country l_targetCountry = p_gameState.getD_map().getCountryByName(d_targetCountryID);
            Integer l_noOfArmiesOnTargetCountry = l_targetCountry.getD_armies() == 0 ? 1
                    : l_targetCountry.getD_armies();
            Integer l_newArmies = (int) Math.floor(l_noOfArmiesOnTargetCountry / 2);
            l_targetCountry.setD_armies(l_newArmies);
            d_playerInitiator.removeCard("bomb");
            this.setD_orderExecutionLog(
                    "\nPlayer : " + this.d_playerInitiator.getPlayerName() + " is executing Bomb card on country :  "
                            + l_targetCountry.getD_countryName() + " with armies :  " + l_noOfArmiesOnTargetCountry
                            + ". New armies: " + l_targetCountry.getD_armies(),
                    "default");
            p_gameState.updateLog(orderExecutionLog(), "effect");
        }
    }

    /**
     * Generates the current bomb card order.
     *
     * @return the current bomb card order
     */
    private String currentOrder() {
        return "Bomb card order : " + "bomb" + " " + this.d_targetCountryID;
    }

    /**
     * Checks if the bomb card action is valid in the current game state.
     *
     * @param p_gameState the current game state
     * @return true if the bomb action is valid, false otherwise
     */
    @Override
    public boolean valid(GameState p_gameState) {
        Country l_country = d_playerInitiator.getD_coutriesOwned().stream()
                .filter(l_pl -> l_pl.getD_countryName().equalsIgnoreCase(this.d_targetCountryID)).findFirst()
                .orElse(null);

        if (!CommonUtil.isNull(l_country)) {
            this.setD_orderExecutionLog(this.currentOrder() + " is not executed since Target country : "
                    + this.d_targetCountryID + " given in bomb command is owned by the player : "
                    + d_playerInitiator.getPlayerName() + " VALIDATES:- You cannot bomb your own territory!", "error");
            p_gameState.updateLog(orderExecutionLog(), "effect");
            return false;
        }

        if(!d_playerInitiator.negotiationValidation(this.d_targetCountryID)){
            this.setD_orderExecutionLog(this.currentOrder() + " is not executed as "+ d_playerInitiator.getPlayerName()+ " has negotiation pact with the target country's player!", "error");
            p_gameState.updateLog(orderExecutionLog(), "effect");
            return false;
        }
        return true;
    }

    /**
     * Prints the order details of the bomb card.
     */
    @Override
    public void printOrder() {
        this.d_orderLog = "----------Player "+this.d_playerInitiator.getPlayerName() + " issued Bomb card order----------\n"
                + "Creating a bomb order = " + "on country ID. " + this.d_targetCountryID;
        System.out.println(System.lineSeparator() + this.d_orderLog);

    }

    /**
     * Gets the order execution log.
     *
     * @return the order execution log
     */
    public String orderExecutionLog() {
        return this.d_orderLog;
    }

    /**
     * Sets the order execution log with the provided message and log type.
     *
     * @param p_orderExecutionLog the order execution log message
     * @param p_logType           the type of log (error or default)
     */
    public void setD_orderExecutionLog(String p_orderExecutionLog, String p_logType) {
        this.d_orderLog = p_orderExecutionLog;
        if (p_logType.equals("error")) {
            System.err.println(p_orderExecutionLog);
        } else {
            System.out.println(p_orderExecutionLog);
        }
    }

    /**
     * Checks if the bomb card order is valid in the current game state.
     *
     * @param p_gameState the current game state
     * @return true if the order is valid, false otherwise
     */
    @Override
    public Boolean checkValidOrder(GameState p_gameState) {
        Country l_targetCountry = p_gameState.getD_map().getCountryByName(d_targetCountryID);
        if (l_targetCountry == null) {
            this.setD_orderExecutionLog("Target Country doesn't exist on the map, it is not valid! !", "error");
            return false;
        }
        return true;
    }

    /**
     * Gets the name of the bomb card order.
     *
     * @return the name of the bomb card order
     */
    @Override
    public String getOrderName() {
        return "bomb";
    }
}
