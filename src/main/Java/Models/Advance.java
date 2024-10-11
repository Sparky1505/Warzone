package Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Services.PlayerService;
import Utils.CommonUtil;

/**
 * Represents an Advance order in the game.
 */
public class Advance implements Order, Serializable {
    /**
     * Represents the target country.
     */
    String d_targetCountryName;

    /**
     * Represents the source country.
     */
    String d_sourceCountryName;

    /**
     * Represents the number of armies.
     */
    Integer d_numberOfArmiesToPlace;

    /**
     * Represents the player.
     */
    Player d_playerInitiator;

    /**
     * Assigns the log that records details about orders
     */
    String d_orderExecutionLog;

    /**
     * Initializes a new instance of the Advance class.
     *
     * @param p_playerInitiator      The player initiating the advance order.
     * @param p_sourceCountryName   The name of the source country.
     * @param p_targetCountryName   The name of the target country.
     * @param p_numberOfArmiesToPlace The number of armies to place.
     */
    public Advance(Player p_playerInitiator, String p_sourceCountryName, String p_targetCountryName,
                   Integer p_numberOfArmiesToPlace) {
        this.d_targetCountryName = p_targetCountryName;
        this.d_sourceCountryName = p_sourceCountryName;
        this.d_playerInitiator = p_playerInitiator;
        this.d_numberOfArmiesToPlace = p_numberOfArmiesToPlace;
    }

    /**
     * Executes the order object and makes requisite changes in game state.
     *
     * @param p_gameState The current game state.
     */
    @Override
    public void execute(GameState p_gameState) {
        if (valid(p_gameState)) {
            Player l_playerOfTargetCountry = getPlayerOfTargetCountry(p_gameState);
            Country l_targetCountry = p_gameState.getD_map().getCountryByName(d_targetCountryName);
            Country l_sourceCountry = p_gameState.getD_map().getCountryByName(d_sourceCountryName);
            Integer l_sourceArmiesToUpdate = l_sourceCountry.getD_armies() - this.d_numberOfArmiesToPlace;
            l_sourceCountry.setD_armies(l_sourceArmiesToUpdate);

            if (l_playerOfTargetCountry.getPlayerName().equalsIgnoreCase(this.d_playerInitiator.getPlayerName())) {
                deployArmiesToTarget(l_targetCountry);
            } else if (l_targetCountry.getD_armies() == 0) {
                conquerTargetCountry(p_gameState, l_playerOfTargetCountry, l_targetCountry);
                this.d_playerInitiator.setD_oneCardPerTurn(true);
            } else {
                Random l_random = new Random();
                if(l_random.nextBoolean()) {
                    produceOrderResult(p_gameState, l_playerOfTargetCountry, l_targetCountry, l_sourceCountry);
                } else {
                    produceAdvanceResult(p_gameState, l_playerOfTargetCountry, l_targetCountry, l_sourceCountry);
                }
            }
        } else {
            p_gameState.updateLog(orderExecutionLog(), "effect");
        }
    }

    /**
     * Produces the result of the advance order.
     *
     * @param p_gameState            The current game state.
     * @param p_playerOfTargetCountry The player of the target country.
     * @param p_targetCountry        The target country.
     * @param p_sourceCountry        The source country.
     */
    private void produceOrderResult(GameState p_gameState, Player p_playerOfTargetCountry, Country p_targetCountry,
                                    Country p_sourceCountry) {
        Integer l_armiesInAttack = this.d_numberOfArmiesToPlace < p_targetCountry.getD_armies()
                ? this.d_numberOfArmiesToPlace
                : p_targetCountry.getD_armies();

        List<Integer> l_attackerArmies = generateRandomArmyUnits(l_armiesInAttack, "attacker");
        List<Integer> l_defenderArmies = generateRandomArmyUnits(l_armiesInAttack, "defender");
        this.produceBattleResult(p_sourceCountry, p_targetCountry, l_attackerArmies, l_defenderArmies,
                p_playerOfTargetCountry);

        p_gameState.updateLog(orderExecutionLog(), "effect");
        this.updateContinents(this.d_playerInitiator, p_playerOfTargetCountry, p_gameState);
    }

    /**
     * Conquers the target country.
     *
     * @param p_gameState            The current game state.
     * @param p_playerOfTargetCountry The player of the target country.
     * @param p_targetCountry        The target country.
     */
    private void conquerTargetCountry(GameState p_gameState, Player p_playerOfTargetCountry, Country p_targetCountry) {
        p_targetCountry.setD_armies(d_numberOfArmiesToPlace);
        p_playerOfTargetCountry.getD_coutriesOwned().remove(p_targetCountry);
        this.d_playerInitiator.getD_coutriesOwned().add(p_targetCountry);
        this.setD_orderExecutionLog(
                "Player : " + this.d_playerInitiator.getPlayerName() + " is assigned with Country : "
                        + p_targetCountry.getD_countryName() + " and armies : " + p_targetCountry.getD_armies(),
                "default");
        p_gameState.updateLog(orderExecutionLog(), "effect");
        this.updateContinents(this.d_playerInitiator, p_playerOfTargetCountry, p_gameState);
    }

    /**
     * Retrieves the player of the target country.
     *
     * @param p_gameState The current game state.
     * @return The player of the target country.
     */
    private Player getPlayerOfTargetCountry(GameState p_gameState) {
        Player l_playerOfTargetCountry = null;
        for (Player l_player : p_gameState.getD_players()) {
            String l_cont = l_player.getCountryNames().stream()
                    .filter(l_country -> l_country.equalsIgnoreCase(this.d_targetCountryName)).findFirst().orElse(null);
            if (!CommonUtil.isEmpty(l_cont)) {
                l_playerOfTargetCountry = l_player;
            }
        }
        return l_playerOfTargetCountry;
    }

    /**
     * Deploys armies to the target country.
     *
     * @param p_targetCountry The target country.
     */
    public void deployArmiesToTarget(Country p_targetCountry) {
        Integer l_updatedTargetContArmies = p_targetCountry.getD_armies() + this.d_numberOfArmiesToPlace;
        p_targetCountry.setD_armies(l_updatedTargetContArmies);
    }

    /**
     * Produces the battle result.
     *
     * @param p_sourceCountry        The source country.
     * @param p_targetCountry        The target country.
     * @param p_attackerArmies       The list of attacker armies.
     * @param p_defenderArmies       The list of defender armies.
     * @param p_playerOfTargetCountry The player of the target country.
     */
    private void produceBattleResult(Country p_sourceCountry, Country p_targetCountry, List<Integer> p_attackerArmies,
                                     List<Integer> p_defenderArmies, Player p_playerOfTargetCountry) {
        Integer l_attackerArmiesLeft = this.d_numberOfArmiesToPlace > p_targetCountry.getD_armies()
                ? this.d_numberOfArmiesToPlace - p_targetCountry.getD_armies()
                : 0;
        Integer l_defenderArmiesLeft = this.d_numberOfArmiesToPlace < p_targetCountry.getD_armies()
                ? p_targetCountry.getD_armies() - this.d_numberOfArmiesToPlace
                : 0;
        for (int l_i = 0; l_i < p_attackerArmies.size(); l_i++) {
            if (p_attackerArmies.get(l_i) > p_defenderArmies.get(l_i)) {
                l_attackerArmiesLeft++;
            } else {
                l_defenderArmiesLeft++;
            }
        }
        this.handleSurvivingArmies(l_attackerArmiesLeft, l_defenderArmiesLeft, p_sourceCountry, p_targetCountry,
                p_playerOfTargetCountry);
    }

    /**
     * Handles surviving armies after battle.
     *
     * @param p_attackerArmiesLeft   The remaining attacker armies.
     * @param p_defenderArmiesLeft   The remaining defender armies.
     * @param p_sourceCountry        The source country.
     * @param p_targetCountry        The target country.
     * @param p_playerOfTargetCountry The player of the target country.
     */
    public void handleSurvivingArmies(Integer p_attackerArmiesLeft, Integer p_defenderArmiesLeft,
                                      Country p_sourceCountry, Country p_targetCountry, Player p_playerOfTargetCountry) {
        if (p_defenderArmiesLeft == 0) {
            p_playerOfTargetCountry.getD_coutriesOwned().remove(p_targetCountry);
            p_targetCountry.setD_armies(p_attackerArmiesLeft);
            this.d_playerInitiator.getD_coutriesOwned().add(p_targetCountry);
            this.setD_orderExecutionLog(
                    "Player : " + this.d_playerInitiator.getPlayerName() + " is assigned with Country : "
                            + p_targetCountry.getD_countryName() + " and armies : " + p_targetCountry.getD_armies(),
                    "default");

            this.d_playerInitiator.assignCard();
        } else {
            p_targetCountry.setD_armies(p_defenderArmiesLeft);

            Integer l_sourceArmiesToUpdate = p_sourceCountry.getD_armies() + p_attackerArmiesLeft;
            p_sourceCountry.setD_armies(l_sourceArmiesToUpdate);
            String l_country1 = "Country : " + p_targetCountry.getD_countryName() + " is left with "

                    + p_targetCountry.getD_armies() + " armies and is still owned by player : "
                    + p_playerOfTargetCountry.getPlayerName();
            String l_country2 = "Country : " + p_sourceCountry.getD_countryName() + " is left with "
                    + p_sourceCountry.getD_armies() + " armies and is still owned by player : "
                    + this.d_playerInitiator.getPlayerName();
            this.setD_orderExecutionLog(l_country1 + System.lineSeparator() + l_country2, "default");
        }
    }

    /**
     * Checks if the advance order is valid.
     *
     * @param p_gameState The current game state.
     * @return True if the advance order is valid, false otherwise.
     */
    @Override
    public boolean valid(GameState p_gameState) {
        Country l_country = d_playerInitiator.getD_coutriesOwned().stream()
                .filter(l_pl -> l_pl.getD_countryName().equalsIgnoreCase(this.d_sourceCountryName.toString()))
                .findFirst().orElse(null);
        if (l_country == null) {
            this.setD_orderExecutionLog(this.currentOrder() + " is not executed since Source country : "
                    + this.d_sourceCountryName + " given in advance command does not belongs to the player : "
                    + d_playerInitiator.getPlayerName(), "error");
            p_gameState.updateLog(orderExecutionLog(), "effect");
            return false;
        }
        if (this.d_numberOfArmiesToPlace > l_country.getD_armies()) {
            this.setD_orderExecutionLog(this.currentOrder()
                    + " is not executed as armies given in advance order exceeds armies of source country : "
                    + this.d_sourceCountryName, "error");
            p_gameState.updateLog(orderExecutionLog(), "effect");
            return false;
        }
        if (this.d_numberOfArmiesToPlace == l_country.getD_armies()) {
            this.setD_orderExecutionLog(this.currentOrder() + " is not executed as source country : "
                            + this.d_sourceCountryName + " has " + l_country.getD_armies()
                            + " army units and all of those cannot be given advance order, at least one army unit has to retain the territory.",
                    "error");
            p_gameState.updateLog(orderExecutionLog(), "effect");
            return false;
        }
        if (!d_playerInitiator.negotiationValidation(this.d_targetCountryName)) {
            this.setD_orderExecutionLog(this.currentOrder() + " is not executed as " + d_playerInitiator.getPlayerName()
                    + " has negotiation pact with the target country's player!", "error");
            p_gameState.updateLog(orderExecutionLog(), "effect");
            return false;
        }
        return true;
    }

    /**
     * Generates the current order string.
     *
     * @return The current order string.
     */
    private String currentOrder() {
        return "Advance Order : " + "advance" + " " + this.d_sourceCountryName + " " + this.d_targetCountryName + " "
                + this.d_numberOfArmiesToPlace;
    }

    /**
     * Prints the details of the advance order.
     */
    @Override
    public void printOrder() {
        this.d_orderExecutionLog = "\n---------- Advance order issued by player " + this.d_playerInitiator.getPlayerName()
                + " ----------\n" + System.lineSeparator() + "Move " + this.d_numberOfArmiesToPlace + " armies from "
                + this.d_sourceCountryName + " to " + this.d_targetCountryName;
        System.out.println(System.lineSeparator() + this.d_orderExecutionLog);
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
     * Generates a list of random army units.
     *
     * @param p_size The size of the list.
     * @param p_role The role of the army units.
     * @return The list of random army units.
     */
    private List<Integer> generateRandomArmyUnits(int p_size, String p_role) {
        List<Integer> l_armyList = new ArrayList<>();
        Double l_probability = "attacker".equalsIgnoreCase(p_role) ? 0.6 : 0.7;
        for (int l_i = 0; l_i < p_size; l_i++) {
            int l_randomNumber = getRandomInteger(10, 1);
            Integer l_armyUnit = (int) Math.round(l_randomNumber * l_probability);
            l_armyList.add(l_armyUnit);
        }
        return l_armyList;
    }

    /**
     * Generates a random integer between the specified range.
     *
     * @param p_maximum The maximum value (exclusive).
     * @param p_minimum The minimum value (inclusive).
     * @return The random integer.
     */
    private static int getRandomInteger(int p_maximum, int p_minimum) {
        return ((int) (Math.random() * (p_maximum - p_minimum))) + p_minimum;
    }

    /**
     * Updates the continents of players involved in battle.
     *
     * @param p_playerOfSourceCountry The player of the source country.
     * @param p_playerOfTargetCountry The player of the target country.
     * @param p_gameState             The current game state.
     */
    private void updateContinents(Player p_playerOfSourceCountry, Player p_playerOfTargetCountry,
                                  GameState p_gameState) {
        System.out.println("Updating continents of players involved in battle...");
        List<Player> l_playesList = new ArrayList<>();
        p_playerOfSourceCountry.setD_continentsOwned(new ArrayList<>());
        p_playerOfTargetCountry.setD_continentsOwned(new ArrayList<>());
        l_playesList.add(p_playerOfSourceCountry);
        l_playesList.add(p_playerOfTargetCountry);

        PlayerService l_playerService = new PlayerService();
        l_playerService.performContinentAssignment(l_playesList, p_gameState.getD_map().getD_continents());
    }

    /**
     * Retrieves the name of the order.
     *
     * @return The name of the order.
     */
    @Override
    public String getOrderName() {
        return "advance";
    }

    /**
     *
     * Generates Advance Order result.
     *
     * @param p_gameState Current state of the game.
     * @param p_playerOfTargetCountry The player who currently owns the target country.
     * @param p_targetCountry The target country being attacked.
     * @param p_sourceCountry The country from which the attack is launched.
     */

    private void produceAdvanceResult(GameState p_gameState, Player p_playerOfTargetCountry, Country p_targetCountry,
                                      Country p_sourceCountry) {
        Integer l_armiesInAttack = (int) Math.round(d_numberOfArmiesToPlace * 0.6);
        Integer l_armiesToDefend = (int) Math.round(p_targetCountry.getD_armies() * 0.7);

        if(l_armiesInAttack > l_armiesToDefend) {
            Integer l_attackersArmiesLeft = l_armiesInAttack - l_armiesToDefend;
            p_targetCountry.setD_armies(l_attackersArmiesLeft);
            p_playerOfTargetCountry.getD_coutriesOwned().remove(p_targetCountry);
            d_playerInitiator.getD_coutriesOwned().add(p_targetCountry);
            this.d_playerInitiator.setD_oneCardPerTurn(true);
            this.setD_orderExecutionLog(
                    "Player : " + this.d_playerInitiator.getPlayerName() + " is assigned with Country : "
                            + p_targetCountry.getD_countryName() + " and armies : " + p_targetCountry.getD_armies(),
                    "default");
        }
        else if (l_armiesInAttack <= l_armiesToDefend) {
            Integer l_defendersArmiesLeft = l_armiesToDefend - l_armiesInAttack;
            p_targetCountry.setD_armies(l_defendersArmiesLeft);

            String l_country1 = "Country : " + p_targetCountry.getD_countryName() + " is left with "
                    + p_targetCountry.getD_armies() + " armies and is still owned by player : "
                    + p_playerOfTargetCountry.getPlayerName();
            String l_country2 = "Country : " + p_sourceCountry.getD_countryName() + " is left with "
                    + p_sourceCountry.getD_armies() + " armies and is still owned by player : "
                    + this.d_playerInitiator.getPlayerName();
            this.setD_orderExecutionLog(l_country1 + System.lineSeparator() + l_country2, "default");
        }
        p_gameState.updateLog(orderExecutionLog(), "effect");
        this.updateContinents(this.d_playerInitiator, p_playerOfTargetCountry, p_gameState);
    }
}
