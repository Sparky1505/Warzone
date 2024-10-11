package Models;

import java.io.Serializable;

/**
 * Airlift class represents a card for executing Airlift operation in the game.
 */
public class Airlift implements Card, Serializable {
    Player d_player;
    String d_sourceCountryName;
    String d_targetCountryName;
    Integer d_numberOfArmies;
    String d_orderExecutionLog;

    /**
     * Constructor for Airlift card.
     *
     * @param p_sourceCountryName The name of the source country.
     * @param p_targetCountryName The name of the target country.
     * @param p_noOfArmies        Number of armies to be airlifted.
     * @param p_player            The player owning the card.
     */
    public Airlift(String p_sourceCountryName, String p_targetCountryName, Integer p_noOfArmies, Player p_player) {
        this.d_numberOfArmies = p_noOfArmies;
        this.d_targetCountryName = p_targetCountryName;
        this.d_sourceCountryName = p_sourceCountryName;
        this.d_player = p_player;
    }

    /**
     * Executes the Airlift operation.
     *
     * @param p_gameState The current game state.
     */
    @Override
    public void execute(GameState p_gameState) {
        if (valid(p_gameState)) {
            Country l_sourceCountry = p_gameState.getD_map().getCountryByName(d_sourceCountryName);
            Country l_targetCountry = p_gameState.getD_map().getCountryByName(d_targetCountryName);
            Integer l_updatedTargetArmies = l_targetCountry.getD_armies() + this.d_numberOfArmies;
            Integer l_updatedSourceArmies = l_sourceCountry.getD_armies() - this.d_numberOfArmies;
            l_targetCountry.setD_armies(l_updatedTargetArmies);
            l_sourceCountry.setD_armies(l_updatedSourceArmies);
            d_player.removeCard("airlift");
            this.setD_orderExecutionLog("Airlift Operation from "+ d_sourceCountryName+ " to "+d_targetCountryName+" successful!", "default");
            p_gameState.updateLog(d_orderExecutionLog, "effect");
        } else {
            this.setD_orderExecutionLog("Cannot Complete Execution of given Airlift Command!", "error");
            p_gameState.updateLog(d_orderExecutionLog, "effect");
        }
    }

    /**
     * Checks if the Airlift operation is valid.
     *
     * @param p_gameState The current game state.
     * @return true if the operation is valid, false otherwise.
     */
    @Override
    public boolean valid(GameState p_gameState) {
        Country l_sourceCountry = d_player.getD_coutriesOwned().stream()
                .filter(l_pl -> l_pl.getD_countryName().equalsIgnoreCase(this.d_sourceCountryName.toString()))
                .findFirst().orElse(null);
        if (l_sourceCountry == null) {
            this.setD_orderExecutionLog(
                    this.currentOrder() + " is not executed since Source country : " + this.d_sourceCountryName
                            + " given in card order does not belongs to the player : " + d_player.getPlayerName(),
                    "error");
            p_gameState.updateLog(orderExecutionLog(), "effect");
            return false;
        }
        Country l_targetCountry = d_player.getD_coutriesOwned().stream()
                .filter(l_pl -> l_pl.getD_countryName().equalsIgnoreCase(this.d_targetCountryName.toString()))
                .findFirst().orElse(null);
        if (l_targetCountry == null) {
            this.setD_orderExecutionLog(
                    this.currentOrder() + " is not executed since Target country : " + this.d_sourceCountryName
                            + " given in card order does not belongs to the player : " + d_player.getPlayerName(),
                    "error");
            p_gameState.updateLog(orderExecutionLog(), "effect");
            return false;
        }
        if (this.d_numberOfArmies > l_sourceCountry.getD_armies()) {
            this.setD_orderExecutionLog(this.currentOrder()
                    + " is not executed as armies given in card order exceeds armies of source country : "
                    + this.d_sourceCountryName, "error");
            p_gameState.updateLog(orderExecutionLog(), "effect");
            return false;
        }
        return true;
    }

    /**
     * Prints the Airlift order.
     */
    @Override
    public void printOrder() {
        this.d_orderExecutionLog = "----------Airlift order issued by player " + this.d_player.getPlayerName()
                + "----------" + System.lineSeparator() + "Move " + this.d_numberOfArmies + " armies from "
                + this.d_sourceCountryName + " to " + this.d_targetCountryName;
        System.out.println(System.lineSeparator()+this.d_orderExecutionLog);
    }

    /**
     * Returns the order execution log.
     *
     * @return The order execution log.
     */
    @Override
    public String orderExecutionLog() {
        return this.d_orderExecutionLog;
    }

    /**
     * Sets the order execution log and prints it.
     *
     * @param p_orderExecutionLog The order execution log message.
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
     * Retrieves the current order information.
     *
     * @return The current order information.
     */
    private String currentOrder() {
        return "Airlift Order : " + "airlift" + " " + this.d_sourceCountryName + " " + this.d_targetCountryName + " "
                + this.d_numberOfArmies;
    }

    /**
     * Checks if the Airlift order is valid.
     *
     * @param p_GameState The current game state.
     * @return true if the order is valid, false otherwise.
     */
    @Override
    public Boolean checkValidOrder(GameState p_GameState) {
        Country l_sourceCountry = p_GameState.getD_map().getCountryByName(d_sourceCountryName);
        Country l_targetCountry = p_GameState.getD_map().getCountryByName(d_targetCountryName);
        if (l_sourceCountry == null) {
            this.setD_orderExecutionLog("Invalid Source Country! Doesn't exist on the map!", "error");
            p_GameState.updateLog(orderExecutionLog(), "effect");
            return false;
        }
        if (l_targetCountry == null) {
            this.setD_orderExecutionLog("Invalid Target Country! Doesn't exist on the map!", "error");
            p_GameState.updateLog(orderExecutionLog(), "effect");
            return false;
        }
        return true;
    }

    /**
     * Retrieves the name of the order.
     *
     * @return The name of the order.
     */
    @Override
    public String getOrderName() {
        return "airlift";
    }
}
