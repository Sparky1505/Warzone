package Models;

import Utils.CommonUtil;

import java.io.Serializable;

/**
 * The Blockade class represents a Blockade card in the game, which can be used to execute a defensive blockade on a target country.
 */
public class Blockade implements Card, Serializable {

    /** The player initiating the blockade. */
    Player d_playerInitiator;

    /** name of the target country. */
    String d_targetCountryID;

    /** Log message for the order execution. */
    String d_orderLog;

    /**
     * Constructs a Blockade card with the specified initiator player and target country ID.
     *
     * @param p_playerInitiator the player initiating the blockade
     * @param p_targetCountry  the ID of the target country for the blockade
     */
    public Blockade(Player p_playerInitiator, String p_targetCountry) {
        this.d_playerInitiator = p_playerInitiator;
        this.d_targetCountryID = p_targetCountry;
    }

    /**
     * Executes the blockade card action in the game state.
     *
     * @param p_gameState the current game state
     */
    @Override
    public void execute(GameState p_gameState) {
        if (valid(p_gameState)) {
            Country l_targetCountryID = p_gameState.getD_map().getCountryByName(d_targetCountryID);
            Integer l_noOfArmiesOnTargetCountry = l_targetCountryID.getD_armies() == 0 ? 1
                    : l_targetCountryID.getD_armies();
            l_targetCountryID.setD_armies(l_noOfArmiesOnTargetCountry * 3);

            d_playerInitiator.getD_coutriesOwned().remove(l_targetCountryID);

            Player l_player = p_gameState.getD_players().stream()
                    .filter(l_pl -> l_pl.getPlayerName().equalsIgnoreCase("Neutral")).findFirst().orElse(null);

            if (!CommonUtil.isNull(l_player)) {
                l_player.getD_coutriesOwned().add(l_targetCountryID);
                System.out.println("Neutral territory: " + l_targetCountryID.getD_countryName() + "assigned to the Neutral Player.");
            }

            d_playerInitiator.removeCard("blockade");
            this.setD_orderExecutionLog("\nPlayer : " + this.d_playerInitiator.getPlayerName()
                    + " is executing defensive blockade on Country :  " + l_targetCountryID.getD_countryName()
                    + " with armies :  " + l_targetCountryID.getD_armies(), "default");
            p_gameState.updateLog(orderExecutionLog(), "effect");
        }
    }

    /**
     * Checks if the blockade card action is valid in the current game state.
     *
     * @param p_gameState the current game state
     * @return true if the blockade action is valid, false otherwise
     */
    @Override
    public boolean valid(GameState p_gameState) {

        Country l_country = d_playerInitiator.getD_coutriesOwned().stream()
                .filter(l_pl -> l_pl.getD_countryName().equalsIgnoreCase(this.d_targetCountryID)).findFirst()
                .orElse(null);

        if (CommonUtil.isNull(l_country)) {
            this.setD_orderExecutionLog(this.currentOrder() + " is not executed since Target country : "
                    + this.d_targetCountryID + " given in blockade command does not owned to the player : "
                    + d_playerInitiator.getPlayerName()
                    + " The card will have no affect and you don't get the card back.", "error");
            p_gameState.updateLog(orderExecutionLog(), "effect");
            return false;
        }
        return true;
    }

    /**
     * Prints the order details of the blockade card.
     */
    @Override
    public void printOrder() {
        this.d_orderLog = "----------Player "+this.d_playerInitiator.getPlayerName()+
                " issued Blockade card order----------\n"
                + "Creating a defensive blockade with armies = " + "on country ID: " + this.d_targetCountryID;
        System.out.println(System.lineSeparator() + this.d_orderLog);

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
     * Checks if the blockade card order is valid in the current game state.
     *
     * @param p_gameState the current game state
     * @return true if the order is valid, false otherwise
     */
    @Override
    public Boolean checkValidOrder(GameState p_gameState) {
        Country l_targetCountry = p_gameState.getD_map().getCountryByName(d_targetCountryID);
        if (l_targetCountry == null) {
            this.setD_orderExecutionLog("Invalid Target Country! Doesn't exist on the map!", "error");
            return false;
        }
        return true;
    }

    /**
     * Gets the name of the blockade card order.
     *
     * @return the name of the blockade card order
     */
    @Override
    public String getOrderName() {
        return "blockade";
    }

    /**
     * Generates the current blockade card order.
     *
     * @return the current blockade card order
     */
    private String currentOrder() {
        return "Blockade card order : " + getOrderName() + " " + this.d_targetCountryID;
    }

    /**
     * Gets the order execution log.
     *
     * @return the order execution log
     */
    public String orderExecutionLog() {
        return this.d_orderLog;
    }

}
