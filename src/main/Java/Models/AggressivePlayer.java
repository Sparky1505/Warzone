package Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

/**
 * AggressivePlayer class represents a player behavior strategy that
 * focuses on aggressive gameplay.
 */
public class AggressivePlayer extends PlayerBehaviorStrategy {

    /** List of countries to deploy armies */
    ArrayList<Country> d_deployCountries = new ArrayList<Country>();

    /**
     * Creates an order for the player based on aggressive strategy.
     *
     * @param p_player The player for which the order is to be created.
     * @param p_gameState The current game state.
     * @return The created order as a string.
     */
    @Override
    public String createOrder(Player p_player, GameState p_gameState) {
        System.out.println("Creating order for : " + p_player.getPlayerName());
        String l_command;
        if (!checkIfArmiesDepoyed(p_player)) {
            if(p_player.getD_noOfUnallocatedArmies()>0) {
                l_command = createDeployOrder(p_player, p_gameState);
            }else{
                l_command = createAdvanceOrder(p_player, p_gameState);
            }
        } else {
            if(p_player.getD_cardsOwnedByPlayer().size()>0){
                System.out.println("Enters Card Logic");
                int l_index = (int) (Math.random() * 3) +1;
                switch (l_index) {
                    case 1:
                        System.out.println("Deploy!");
                        l_command = createDeployOrder(p_player, p_gameState);
                        break;
                    case 2:
                        System.out.println("Advance!");
                        l_command = createAdvanceOrder(p_player, p_gameState);
                        break;
                    case 3:
                        if (p_player.getD_cardsOwnedByPlayer().size() == 1) {
                            System.out.println("Cards!");
                            l_command = createCardOrder(p_player, p_gameState, p_player.getD_cardsOwnedByPlayer().get(0));
                            break;
                        } else {
                            Random l_random = new Random();
                            int l_randomIndex = l_random.nextInt(p_player.getD_cardsOwnedByPlayer().size());
                            l_command = createCardOrder(p_player, p_gameState, p_player.getD_cardsOwnedByPlayer().get(l_randomIndex));
                            break;
                        }
                    default:
                        l_command = createAdvanceOrder(p_player, p_gameState);
                        break;
                }
            } else{
                Random l_random = new Random();
                Boolean l_randomBoolean = l_random.nextBoolean();
                if(l_randomBoolean){
                    System.out.println("Without Card Deploy Logic");
                    l_command = createDeployOrder(p_player, p_gameState);
                }else{
                    System.out.println("Without Card Advance Logic");
                    l_command = createAdvanceOrder(p_player, p_gameState);
                }
            }
        }
        return l_command;
    }

    /**
     * Creates a deploy order for the player.
     *
     * @param p_player The player for which the order is to be created.
     * @param p_gameState The current game state.
     * @return The created deploy order as a string.
     */
    @Override
    public String createDeployOrder(Player p_player, GameState p_gameState) {
        Random l_random = new Random();
        Country l_strongestCountry = getStrongestCountry(p_player, p_gameState);
        d_deployCountries.add(l_strongestCountry);
        int l_armiesToDeploy = 1;
        if (p_player.getD_noOfUnallocatedArmies()>1) {
            l_armiesToDeploy = l_random.nextInt(p_player.getD_noOfUnallocatedArmies() - 1) + 1;
        }
        return String.format("deploy %s %d", l_strongestCountry.getD_countryName(), l_armiesToDeploy);
    }

    /**
     * Creates an advance order for the player.
     *
     * @param p_player The player for which the order is to be created.
     * @param p_gameState The current game state.
     * @return The created advance order as a string.
     */
    @Override
    public String createAdvanceOrder(Player p_player, GameState p_gameState) {
        Country l_randomSourceCountry = getRandomCountry(d_deployCountries);
        moveArmiesFromItsNeighbors(p_player, l_randomSourceCountry, p_gameState);

        Random l_random = new Random();
        Country l_randomTargetCountry = p_gameState.getD_map()
                .getCountry(l_randomSourceCountry.getD_adjacentCountryIds()
                        .get(l_random.nextInt(l_randomSourceCountry.getD_adjacentCountryIds().size())));

        int l_armiesToSend = l_randomSourceCountry.getD_armies() > 1 ? l_randomSourceCountry.getD_armies() : 1;

        return "advance " + l_randomSourceCountry.getD_countryName() + " " + l_randomTargetCountry.getD_countryName()
                + " " + l_armiesToSend;

    }

    /**
     * Moves armies from neighbors of a given country to the country.
     *
     * @param p_player The player owning the country.
     * @param p_randomSourceCountry The country from which to move armies.
     * @param p_gameState The current game state.
     */
    public void moveArmiesFromItsNeighbors(Player p_player, Country p_randomSourceCountry, GameState p_gameState) {
        List<Integer> l_adjacentCountryIds = p_randomSourceCountry.getD_adjacentCountryIds();
        List<Country> l_listOfNeighbors = new ArrayList<Country>();
        for (int l_index = 0; l_index < l_adjacentCountryIds.size(); l_index++) {
            Country l_country = p_gameState.getD_map()
                    .getCountry(p_randomSourceCountry.getD_adjacentCountryIds().get(l_index));
            if (p_player.getD_coutriesOwned().contains(l_country)) {
                l_listOfNeighbors.add(l_country);
            }
        }

        int l_ArmiesToMove = 0;
        for (Country l_con : l_listOfNeighbors) {
            l_ArmiesToMove += p_randomSourceCountry.getD_armies() > 0
                    ? p_randomSourceCountry.getD_armies() + (l_con.getD_armies())
                    : (l_con.getD_armies());

        }
        p_randomSourceCountry.setD_armies(l_ArmiesToMove);
    }

    /**
     * Retrieves a random country from a list of countries.
     *
     * @param p_listOfCountries The list of countries to choose from.
     * @return A randomly selected country.
     */
    private Country getRandomCountry(List<Country> p_listOfCountries) {
        Random l_random = new Random();
        return p_listOfCountries.get(l_random.nextInt(p_listOfCountries.size()));
    }

    /**
     * Creates a card order for the player.
     *
     * @param p_player The player for which the order is to be created.
     * @param p_gameState The current game state.
     * @param p_cardName The name of the card to be used.
     * @return The created card order as a string.
     */
    @Override
    public String createCardOrder(Player p_player, GameState p_gameState, String p_cardName) {
        Random l_random = new Random();
        Country l_StrongestSourceCountry = getStrongestCountry(p_player, d_gameState);

        Country l_randomTargetCountry = p_gameState.getD_map()
                .getCountry(l_StrongestSourceCountry.getD_adjacentCountryIds()
                        .get(l_random.nextInt(l_StrongestSourceCountry.getD_adjacentCountryIds().size())));

        int l_armiesToSend = l_StrongestSourceCountry.getD_armies() > 1 ? l_StrongestSourceCountry.getD_armies() : 1;

        switch (p_cardName) {
            case "bomb":
                return "bomb " + l_randomTargetCountry.getD_countryName();
            case "blockade":
                return "blockade " + l_StrongestSourceCountry.getD_countryName();
            case "airlift":
                return "airlift " + l_StrongestSourceCountry.getD_countryName() + " "
                        + getRandomCountry(p_player.getD_coutriesOwned()).getD_countryName() + " " + l_armiesToSend;
            case "negotiate":
                return "negotiate" + " " + getRandomEnemyPlayer(p_player, p_gameState).getPlayerName();
        }
        return null;
    }

    /**
     * Retrieves a random enemy player from the game state.
     *
     * @param p_player The player for which an enemy is to be selected.
     * @param p_gameState The current game state.
     * @return A randomly selected enemy player.
     */
    private Player getRandomEnemyPlayer(Player p_player, GameState p_gameState) {
        ArrayList<Player> l_playerList = new ArrayList<Player>();
        Random l_random = new Random();

        for (Player l_player : p_gameState.getD_players()) {
            if (!l_player.equals(p_player))
                l_playerList.add(p_player);
        }
        return l_playerList.get(l_random.nextInt(l_playerList.size()));
    }

    /**
     * Retrieves the behavior of the player.
     *
     * @return The behavior of the player as a string ("Aggressive").
     */
    @Override
    public String getPlayerBehavior() {
        return "Aggressive";
    }

    /**
     * Finds the strongest country owned by the player.
     *
     * @param p_player The player for which the strongest country is to be found.
     * @param p_gameState The current game state.
     * @return The strongest country owned by the player.
     */
    public Country getStrongestCountry(Player p_player, GameState p_gameState) {
        List<Country> l_countriesOwnedByPlayer = p_player.getD_coutriesOwned();
        Country l_Country = calculateStrongestCountry(l_countriesOwnedByPlayer);
        return l_Country;
    }

    /**
     * Calculates the strongest country from a list of countries.
     *
     * @param l_listOfCountries The list of countries to evaluate.
     * @return The strongest country from the list.
     */
    public Country calculateStrongestCountry(List<Country> l_listOfCountries) {
        LinkedHashMap<Country, Integer> l_CountryWithArmies = new LinkedHashMap<Country, Integer>();

        int l_largestNoOfArmies;
        Country l_Country = null;
        for (Country l_country : l_listOfCountries) {
            l_CountryWithArmies.put(l_country, l_country.getD_armies());
        }
        l_largestNoOfArmies = Collections.max(l_CountryWithArmies.values());
        for (Entry<Country, Integer> entry : l_CountryWithArmies.entrySet()) {
            if (entry.getValue().equals(l_largestNoOfArmies)) {
                return entry.getKey();
            }
        }
        return l_Country;

    }

    /**
     * Checks if the player has deployed any armies.
     *
     * @param p_player The player to check.
     * @return True if the player has deployed armies, otherwise false.
     */
    private Boolean checkIfArmiesDepoyed(Player p_player){
        if(p_player.getD_coutriesOwned().stream().anyMatch(l_country -> l_country.getD_armies()>0)){
            return true;
        }
        return false;
    }

}
