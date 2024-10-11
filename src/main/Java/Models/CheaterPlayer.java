package Models;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

import Services.PlayerService;

/**
 * CheaterPlayer class represents a player behavior strategy that cheats in the game.
 * This player will try to maximize its advantages by assigning more armies than allowed,
 * doubling armies on territories, and conquering neighboring enemies.
 */
public class CheaterPlayer extends PlayerBehaviorStrategy {

    /**
     * Creates an order for the Cheater player.
     *
     * @param p_player The player for which the order is to be created.
     * @param p_gameState The current game state.
     * @return Null as Cheater player does not implement deploy, advance, or card orders.
     * @throws IOException If there is an issue with updating logs.
     */
    @Override
    public String createOrder(Player p_player, GameState p_gameState) throws IOException {

        if(p_player.getD_noOfUnallocatedArmies() != 0) {
            while(p_player.getD_noOfUnallocatedArmies() > 0) {
                Random l_random = new Random();
                Country l_randomCountry = getRandomCountry(p_player.getD_coutriesOwned());
                int l_armiesToDeploy = l_random.nextInt(p_player.getD_noOfUnallocatedArmies()) + 1;

                l_randomCountry.setD_armies(l_armiesToDeploy);
                p_player.setD_noOfUnallocatedArmies(p_player.getD_noOfUnallocatedArmies() - l_armiesToDeploy);

                String l_logMessage = "Cheater Player: " + p_player.getPlayerName() +
                        " assigned " + l_armiesToDeploy +
                        " armies to  " + l_randomCountry.getD_countryName();

                p_gameState.updateLog(l_logMessage, "effect");
            }
        }

        try {
            conquerNeighboringEnemies(p_player, p_gameState);
        } catch (ConcurrentModificationException l_e){
        }

        doubleArmyOnEnemyNeighboredCounties(p_player, p_gameState);

        p_player.checkForMoreOrders(true);
        return null;
    }

    /**
     * Retrieves a random country from a list of countries owned by the player.
     *
     * @param p_listOfCountries The list of countries to choose from.
     * @return A randomly selected country.
     */
    private Country getRandomCountry(List<Country> p_listOfCountries){
        Random l_random = new Random();
        return p_listOfCountries.get(l_random.nextInt(p_listOfCountries.size()));
    }

    /**
     * Doubles the number of armies on enemy neighbored countries.
     *
     * @param p_player The player who owns the countries.
     * @param p_gameState The current game state.
     */
    private void doubleArmyOnEnemyNeighboredCounties(Player p_player, GameState p_gameState){
        List<Country> l_countriesOwned = p_player.getD_coutriesOwned();

        for(Country l_ownedCountry : l_countriesOwned) {
            ArrayList<Integer> l_countryEnemies = getEnemies(p_player, l_ownedCountry);

            if(l_countryEnemies.size() == 0) continue;

            Integer l_arimiesInTerritory = l_ownedCountry.getD_armies();

            if(l_arimiesInTerritory == 0) continue;

            l_ownedCountry.setD_armies(l_arimiesInTerritory*2);

            String l_logMessage = "Cheater Player: " + p_player.getPlayerName() +
                    " doubled the armies ( Now: " + l_arimiesInTerritory*2 +
                    ") in " + l_ownedCountry.getD_countryName();

            p_gameState.updateLog(l_logMessage, "effect");

        }
    }

    /**
     * Conquers neighboring enemies' countries.
     *
     * @param p_player The player who is conquering.
     * @param p_gameState The current game state.
     */
    private void conquerNeighboringEnemies(Player p_player, GameState p_gameState){
        List<Country> l_countriesOwned = p_player.getD_coutriesOwned();

        for(Country l_ownedCountry : l_countriesOwned) {
            ArrayList<Integer> l_countryEnemies = getEnemies(p_player, l_ownedCountry);

            for(Integer l_enemyId: l_countryEnemies) {
                Map l_loadedMap =  p_gameState.getD_map();
                Player l_enemyCountryOwner = this.getCountryOwner(p_gameState, l_enemyId);
                Country l_enemyCountry = l_loadedMap.getCountryByID(l_enemyId);
                this.conquerTargetCountry(p_gameState, l_enemyCountryOwner ,p_player, l_enemyCountry);

                String l_logMessage = "Cheater Player: " + p_player.getPlayerName() +
                        " Now owns " + l_enemyCountry.getD_countryName();

                p_gameState.updateLog(l_logMessage, "effect");
            }

        }
    }

    /**
     * Retrieves the owner of a given country.
     *
     * @param p_gameState The current game state.
     * @param p_countryId The ID of the country.
     * @return The player who owns the country.
     */
    private Player getCountryOwner(GameState p_gameState, Integer p_countryId){
        List<Player> l_players = p_gameState.getD_players();
        Player l_owner = null;

        for(Player l_player: l_players){
            List<Integer> l_countriesOwned = l_player.getCountryIDs();
            if(l_countriesOwned.contains(p_countryId)){
                l_owner = l_player;
                break;
            }
        }

        return l_owner;
    }

    /**
     * Conquers a target country from an enemy player.
     *
     * @param p_gameState The current game state.
     * @param p_targetCPlayer The player who owns the target country.
     * @param p_cheaterPlayer The Cheater player who is conquering.
     * @param p_targetCountry The target country to conquer.
     */
    private void conquerTargetCountry(GameState p_gameState, Player p_targetCPlayer, Player p_cheaterPlayer, Country p_targetCountry) {
        p_targetCPlayer.getD_coutriesOwned().remove(p_targetCountry);
        p_cheaterPlayer.getD_coutriesOwned().add(p_targetCountry);
        this.updateContinents(p_cheaterPlayer, p_targetCPlayer, p_gameState);
    }

    /**
     * Updates continents ownership for the involved players.
     *
     * @param p_cheaterPlayer The Cheater player.
     * @param p_targetCPlayer The target country's owner.
     * @param p_gameState The current game state.
     */
    private void updateContinents(Player p_cheaterPlayer, Player p_targetCPlayer,
                                  GameState p_gameState) {
        List<Player> l_playesList = new ArrayList<>();
        p_cheaterPlayer.setD_continentsOwned(new ArrayList<>());
        p_targetCPlayer.setD_continentsOwned(new ArrayList<>());
        l_playesList.add(p_cheaterPlayer);
        l_playesList.add(p_targetCPlayer);

        PlayerService l_playerService = new PlayerService();
        l_playerService.performContinentAssignment(l_playesList, p_gameState.getD_map().getD_continents());
    }

    /**
     * Retrieves neighboring enemy countries of a given country.
     *
     * @param p_player The player who owns the country.
     * @param p_country The country to check for neighboring enemies.
     * @return List of neighboring enemy country IDs.
     */
    private ArrayList<Integer> getEnemies(Player p_player, Country p_country){
        ArrayList<Integer> l_enemyNeighbors = new ArrayList<Integer>();

        for(Integer l_countryID : p_country.getD_adjacentCountryIds()){
            if(!p_player.getCountryIDs().contains(l_countryID))
                l_enemyNeighbors.add(l_countryID);
        }
        return l_enemyNeighbors;
    }

    /**
     * Creates a deploy order for the Cheater player.
     *
     * @param p_player The player for which the order is to be created.
     * @param p_gameState The current game state.
     * @return Null as Cheater player does not deploy armies in a conventional manner.
     */
    @Override
    public String createDeployOrder(Player p_player, GameState p_gameState) {
        return null;
    }

    /**
     * Creates an advance order for the Cheater player.
     *
     * @param p_player The player for which the order is to be created.
     * @param p_gameState The current game state.
     * @return Null as Cheater player does not advance armies in a conventional manner.
     */
    @Override
    public String createAdvanceOrder(Player p_player, GameState p_gameState) {
        return null;
    }

    /**
     * Creates a card order for the Cheater player.
     *
     * @param p_player The player for which the order is to be created.
     * @param p_gameState The current game state.
     * @param p_cardName The name of the card to be used.
     * @return Null as Cheater player does not use cards in a conventional manner.
     */
    @Override
    public String createCardOrder(Player p_player, GameState p_gameState, String p_cardName) {
        return null;
    }

    /**
     * Retrieves the behavior of the player.
     *
     * @return The behavior of the player as a string ("Cheater").
     */
    @Override
    public String getPlayerBehavior() {
        return "Cheater";
    }
}
