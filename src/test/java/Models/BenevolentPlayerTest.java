package Models;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
/**
 * This class tests the functionality of the BenevolentPlayer class.
 * It focuses on verifying that the benevolent player strategy correctly identifies
 * the weakest country owned by the player, as well as the weakest neighboring country,
 * to make decisions based on minimizing losses and protecting weaker territories.
 */
public class BenevolentPlayerTest {


    /**
     * Represents a player instance for testing, encapsulating the behavior and properties
     * necessary to evaluate the BenevolentPlayer strategy within a simulated game context.
     */
    Player d_player;
    /**
     * Interface for player behavior strategies, allowing for dynamic assignment of different
     * strategies to players. In this context, it's used to assign a benevolent strategy to the player.
     */
    PlayerBehaviorStrategy d_playerBehaviorStrategy;
    /**
     * An instance of BenevolentPlayer, representing the strategy under test.
     * This strategy focuses on reinforcing the weakest countries owned by the player,
     * aiming to ensure even distribution of power and defense capabilities across their territories.
     */
    BenevolentPlayer d_benevolentPlayer = new BenevolentPlayer();
    /**
     * Represents the game's current state, including all players, countries, and the map.
     * This is used to simulate a real game environment for the purpose of testing how
     * the BenevolentPlayer strategy interacts with various game scenarios.
     */
    GameState d_gameState = new GameState();
    /**
     * A specific country object used in the tests to either represent a country owned by the
     * player or to interact with the BenevolentPlayer strategy in predefined scenarios.
     * This country's attributes can be manipulated to simulate different game conditions.
     */
    Country d_country1;
    /**
     * Sets up the initial conditions before each test case.
     * This includes initializing countries with specific attributes and assigning
     * them to a player using a benevolent strategy. It also sets up the game state
     * with players, countries, and neighbors to simulate a game environment for testing.
     */
    @Before
    public void setup() {
        this.d_country1 = new Country(1, "India", 1);
        Country l_country2 = new Country(1, "China", 1);
        Country l_country3 = new Country(1, "Pakistan", 1);

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

        d_playerBehaviorStrategy = new BenevolentPlayer();
        d_player = new Player("Jay");
        d_player.setD_coutriesOwned(l_list);
        d_player.setStrategy(d_playerBehaviorStrategy);
        d_player.setD_noOfUnallocatedArmies(5);

        List<Player> l_listOfPlayer = new ArrayList<Player>();
        l_listOfPlayer.add(d_player);

        Map l_map = new Map();
        l_map.setD_countries(l_list);
        l_map.setD_countries(l_list);
        d_gameState.setD_map(l_map);
        d_gameState.setD_players(l_listOfPlayer);
    }
    /**
     * Tests if the BenevolentPlayer's method getWeakestCountry correctly identifies
     * the weakest country owned by the player. Based on the setup, "Pakistan"
     * is expected to be identified as the weakest due to having the least number of armies.
     */
    @Test
    public void testWeakestCountry() {
        assertEquals("Pakistan", d_player.getWeakestCountry(d_benevolentPlayer).getD_countryName());
    }
    /**
     * Tests if the BenevolentPlayer's method getWeakestNeighbor can correctly identify
     * the weakest neighboring country. Given the setup, "Pakistan" is expected to be
     * recognized as the weakest neighbor to "India" based on army strength.
     */
    @Test
    public void testWeakestNeighbor() {
        assertEquals("Pakistan", d_benevolentPlayer.getWeakestNeighbor(d_country1, d_gameState, d_player).getD_countryName());
    }

}