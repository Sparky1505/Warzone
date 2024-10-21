package Models;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
/**
 * Tests the functionality of the AggressivePlayer class, particularly focusing on
 * ensuring the strongest country selection logic is implemented correctly.
 * This class sets up a simulated game state with predefined countries and players,
 * then verifies the aggressive player strategy behaves as expected.
 */
public class AggressivePlayerTest {
    /**
     * Represents a player entity in the test, used to verify the functionality
     * of the AggressivePlayer strategy in various scenarios.
     */
    Player d_player;
    /**
     * Interface for player behavior strategies. In the context of this test,
     * it is specifically used to assign an aggressive strategy to the player.
     */
    PlayerBehaviorStrategy d_playerBehaviorStrategy;
    /**
     * An instance of the AggressivePlayer, which is under test.
     * This class is expected to embody the aggressive strategy logic
     * for selecting the strongest country to reinforce or attack from.
     */
    AggressivePlayer d_aggressivePlayer = new AggressivePlayer();
    /**
     * Represents the game state for the test, containing all necessary components
     * such as the map, players, and countries. It's used to simulate a real game environment
     * in which the AggressivePlayer's decisions are tested.
     */
    GameState d_gameState = new GameState();
    /**
     * A specific country instance used in the test to evaluate
     * the aggressive strategy's interaction with countries.
     * This country acts as the base for aggressive actions in the test scenarios.
     */
    Country d_country1;
    /**
     * Sets up the initial state before each test case. This includes initializing
     * countries with specific attributes, assigning countries to players, and
     * setting up the game state with players and a map.
     */
    @Before
    public void setup() {
        this.d_country1 = new Country(1, "Spain", 1);
        Country l_country2 = new Country(1, "France", 1);
        Country l_country3 = new Country(1, "Portugal", 1);

        l_country2.setD_countryId(3);
        d_country1.addNeighbour(3);

        l_country3.setD_countryId(2);
        d_country1.addNeighbour(2);

        this.d_country1.setD_armies(10);
        l_country2.setD_armies(3);
        l_country3.setD_armies(2);

        ArrayList<Country> l_list = new ArrayList<Country>();
        l_list.add(d_country1);
        l_list.add(l_country2);
        l_list.add(l_country3);

        d_playerBehaviorStrategy = new AggressivePlayer();
        d_player = new Player("Nidhi");
        d_player.setD_coutriesOwned(l_list);
        d_player.setStrategy(d_playerBehaviorStrategy);
        d_player.setD_noOfUnallocatedArmies(8);

        List<Player> l_listOfPlayer = new ArrayList<Player>();
        l_listOfPlayer.add(d_player);

        Map l_map = new Map();
        l_map.setD_countries(l_list);
        l_map.setD_countries(l_list);
        d_gameState.setD_map(l_map);
        d_gameState.setD_players(l_listOfPlayer);
    }
    /**
     * Tests if the getStrongestCountry method in the AggressivePlayer class
     * correctly identifies the strongest country based on the number of armies.
     * The strongest country expected from this test is "Spain" due to its setup
     * with the highest number of armies.
     */
    @Test
    public void testStrongestCountry() {
        assertEquals("Spain", d_player.getStrongestCountry(d_gameState, d_aggressivePlayer).getD_countryName());
    }

}