package Models;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
/**
 * Tests the functionality of the CheaterPlayer class, focusing on verifying
 * the correctness of the cheater player's behavior in a game simulation.
 * This includes testing order creation, deployment of unallocated armies,
 * and the ability of a cheater player to claim ownership of enemy territories.
 */
public class CheaterPlayerTest {
    /**
     * The main player under test, configured to use the CheaterPlayer strategy.
     * This player's behavior is central to the test cases, verifying the strategy's impact
     * on game actions and outcomes.
     */
    Player d_player;
    /**
     * A secondary player, used to simulate interactions with another player in the game.
     * This player may be assigned a different strategy, providing a dynamic testing environment
     * to assess how the CheaterPlayer strategy affects or interacts with opponents.
     */
    Player d_randomPlayer;

    /**
     * Interface for player behavior strategies. While primarily used here to reference
     * the CheaterPlayer strategy, it allows for flexibility in assigning and testing
     * different strategies to the player objects within the test cases.
     */
    PlayerBehaviorStrategy d_playerBehaviorStrategy;
    /**
     * An instance of the CheaterPlayer, representing the strategy under test.
     * The CheaterPlayer strategy embodies game actions that bend or break the rules,
     * and this object's behavior is a key focus of the testing process.
     */
    CheaterPlayer d_cheaterPlayer = new CheaterPlayer();
    /**
     * Represents the current state of the game, including all players, countries, and the map.
     * It's a crucial part of the test setup, creating a context in which the CheaterPlayer's
     * decisions and their effects can be simulated and analyzed.
     */
    GameState d_gameState = new GameState();
    /**
     * A country object used within the test cases, often to simulate specific scenarios
     * where the CheaterPlayer's strategy will be applied. Manipulating this country's attributes
     * allows for targeted testing of the strategy's impact on game dynamics.
     */
    Country d_country1;

    /**
     * Sets up the test environment before each test method is executed.
     * This setup includes initializing countries, continents, and players,
     * as well as configuring the game state to reflect a scenario where the
     * cheater player strategy can be tested.
     */
    @Before
    public void setup() {

        this.d_country1 = new Country(1, "Spain", 1);
        Country l_country2 = new Country(2, "France", 1);
        Country l_country3 = new Country(3, "Portugal", 1);

        d_country1.addNeighbour(3);
        d_country1.addNeighbour(2);
        l_country2.addNeighbour(3);

        l_country2.setD_armies(3);
        l_country3.setD_armies(2);

        ArrayList<Country> l_allCountries = new ArrayList<Country>();
        l_allCountries.add(d_country1);
        l_allCountries.add(l_country2);
        l_allCountries.add(l_country3);

        Continent l_continent = new Continent("Europe");
        l_continent.setD_continentID(1);
        l_continent.setD_countries(l_allCountries);

        ArrayList<Continent> l_continents = new ArrayList<Continent>();
        l_continents.add(l_continent);

        ArrayList<Country> l_ownedCountriesPlayerOne = new ArrayList<Country>();
        l_ownedCountriesPlayerOne.add(d_country1);

        ArrayList<Country> l_ownedCountriesPlayerTwo = new ArrayList<Country>();
        l_ownedCountriesPlayerTwo.add(l_country2);
        l_ownedCountriesPlayerTwo.add(l_country3);

        d_playerBehaviorStrategy = new CheaterPlayer();
        d_player = new Player("Trent");
        d_player.setD_noOfUnallocatedArmies(10);
        d_player.setD_coutriesOwned(l_ownedCountriesPlayerOne);
        d_player.setStrategy(d_playerBehaviorStrategy);

        d_randomPlayer = new Player("Boult");
        RandomPlayer l_randomPlayerBehaviorStrategy = new RandomPlayer();
        d_randomPlayer.setStrategy(l_randomPlayerBehaviorStrategy);
        d_randomPlayer.setD_coutriesOwned(l_ownedCountriesPlayerTwo);
        d_randomPlayer.setD_noOfUnallocatedArmies(0);

        List<Player> l_listOfPlayer = new ArrayList<Player>();
        l_listOfPlayer.add(d_player);
        l_listOfPlayer.add(d_randomPlayer);

        Map l_map = new Map();
        l_map.setD_countries(l_allCountries);
        l_map.setD_continents(l_continents);
        d_gameState.setD_map(l_map);
        d_gameState.setD_players(l_listOfPlayer);
    }
    /**
     * Tests that the cheater player's order creation results in null,
     * indicating that the cheater does not create orders in the conventional manner.
     * @throws IOException if there's an I/O error during the test execution.
     */
    @Test
    public void testOrderCreationToBeNull() throws IOException {
        String l_receivedOrder = d_player.getPlayerOrder(d_gameState);
        assertNull(l_receivedOrder);
    }
    /**
     * Verifies that after attempting to deploy unallocated armies, the cheater player
     * has no unallocated armies left, implying all armies have been deployed.
     * @throws IOException if there's an I/O error during the test execution.
     */
    @Test
    public void testUnallocatedArmiesDeployment() throws IOException {
        String l_receivedOrder = d_player.getPlayerOrder(d_gameState);
        assertNull(l_receivedOrder);

        int l_unallocatedArmies = d_player.getD_noOfUnallocatedArmies();
        assertEquals(0, l_unallocatedArmies);
    }
    /**
     * Tests that the cheater player successfully claims ownership of all enemy territories,
     * demonstrating the unique capability of the cheater strategy to automatically win territories.
     * @throws IOException if there's an I/O error during the test execution.
     */
    @Test
    public void testCheaterOwnsAllEnemies() throws IOException {
        String l_receivedOrder = d_player.getPlayerOrder(d_gameState);
        assertNull(l_receivedOrder);

        int l_ownedCountriesCount = d_player.getD_coutriesOwned().size();
        assertEquals(3, l_ownedCountriesCount);

        int l_opponentOwnedCountriesCount = d_randomPlayer.getD_coutriesOwned().size();
        assertEquals(0, l_opponentOwnedCountriesCount);
    }
}
