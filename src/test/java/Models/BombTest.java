package Models;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * This class is used to test functionality of Bomb class functions.
 */
public class BombTest {

    /**
     * Player 1 object.
     */
    Player d_player1;

    /**
     * Player 2 object.
     */
    Player d_player2;

    /**
     * Bomb Order1.
     */
    Bomb d_bombOrder1;

    /**
     * Bomb Order2.
     */
    Bomb d_bombOrder2;

    /**
     * Bomb Order3.
     */
    Bomb d_bombOrder3;

    /**
     * Bomb Order4.
     */
    Bomb d_bombOrder4;

    Order deployOrder;

    /**
     * name of the target country.
     */
    String d_targetCountry;

    /**
     * list of orders.
     */
    List<Order> d_order_list;

    /**
     * Game State object.
     */
    GameState d_gameState;
    /**
     * Initializes the testing environment before each test, setting up a game state with specific players,
     * countries, and orders to simulate various game scenarios. This method creates a test scenario where
     * two players, named Jay and Shahul, each own a set of countries. It also prepares a map populated with
     * a predefined list of countries, each assigned a certain number of armies, to facilitate testing of
     * game mechanics and order execution.
     *
     * The setup includes:
     * - Creating two players, Jay and Shahul, and assigning them ownership of "Finland" and "Norway".
     * - Initializing a map with five countries: "Finland", "Norway", "Japan", "India", and "Canada", with
     *   specific army allocations for "Japan", "India", and "Canada".
     * - Generating bomb orders for player Jay targeting "Japan", "Norway", "India", and "Canada", and assigning
     *   a subset of these orders to Shahul for execution.
     *
     * This setup aims to create a controlled environment for testing the effects of bomb orders on the map
     * and the interaction between players within the game state.
     */

    @Before
    public void setup() {
        d_gameState = new GameState();
        d_order_list = new ArrayList<Order>();

        d_player1 = new Player();
        d_player1.setPlayerName("Jay");
        d_player2 = new Player();
        d_player2.setPlayerName("Shahul");

        List<Country> l_countryList = new ArrayList<Country>();
        l_countryList.add(new Country("Finland"));
        l_countryList.add(new Country("Norway"));
        d_player1.setD_coutriesOwned(l_countryList);
        d_player2.setD_coutriesOwned(l_countryList);

        List<Country> l_mapCountries = new ArrayList<Country>();
        Country l_country1 = new Country(1, "Finland", 1);
        Country l_country2 = new Country(2, "Norway", 2);
        Country l_country3 = new Country(2, "Japan", 2);
        Country l_country4 = new Country(2, "India", 2);
        Country l_country5 = new Country(2, "Canada", 2);

        l_country3.setD_armies(4);
        l_country4.setD_armies(15);
        l_country5.setD_armies(1);

        l_mapCountries.add(l_country1);
        l_mapCountries.add(l_country2);
        l_mapCountries.add(l_country3);
        l_mapCountries.add(l_country4);
        l_mapCountries.add(l_country5);

        Map l_map = new Map();
        l_map.setD_countries(l_mapCountries);
        d_gameState.setD_map(l_map);
        d_bombOrder1 = new Bomb(d_player1, "Japan");
        d_bombOrder2 = new Bomb(d_player1, "Norway");
        d_bombOrder3 = new Bomb(d_player1, "India");
        d_bombOrder4 = new Bomb(d_player1, "Canada");

        d_order_list.add(d_bombOrder1);
        d_order_list.add(d_bombOrder2);

        d_player2.setD_ordersToExecute(d_order_list);

    }

    /**
     * Tests the execution of bomb orders within the game to ensure correct army reduction. This test method
     * verifies the bomb order's functionality by executing bomb orders on different countries and checking
     * if the number of armies is correctly halved, rounded down when necessary. It specifically tests the
     * following scenarios:
     *
     * 1. Bombing a country ("Japan") with an even number of armies reduces the army count by half.
     * 2. Bombing a country ("India") with an odd number of armies results in the army count being halved and
     *    rounded down.
     * 3. Bombing a country ("Canada") with only one army results in zero armies, effectively emptying the country
     *    of its defensive force.
     *
     * These tests validate the bomb card's expected impact on targeted countries' armies within the game's map,
     * ensuring the card behaves as intended across various army counts.
     */

    @Test
    public void testBombCardExecution() {
        // Test calculation of half armies.
        d_bombOrder1.execute(d_gameState);
        Country l_targetCountry = d_gameState.getD_map().getCountryByName("Japan");
        assertEquals("2", l_targetCountry.getD_armies().toString());

        // Test round down of armies calculation.
        d_bombOrder3.execute(d_gameState);
        Country l_targetCountry2 = d_gameState.getD_map().getCountryByName("India");
        assertEquals("7", l_targetCountry2.getD_armies().toString());

        // Testing:- targeting a territory with 1 army will leave 0.
        d_bombOrder4.execute(d_gameState);
        Country l_targetCountry3 = d_gameState.getD_map().getCountryByName("Canada");
        assertEquals("0", l_targetCountry3.getD_armies().toString());

    }
    /**
     * Tests the validation logic of bomb orders to determine if they are considered valid within the
     * current game state. This test checks two scenarios:
     *
     * 1. A bomb order (d_bombOrder1) targeting a country that meets all criteria for a valid bombing
     *    action. The test expects this order to be valid, asserting a true result.
     * 2. Another bomb order (d_bombOrder2) targeting a country that does not meet the necessary criteria
     *    for a valid bomb action. This might be due to factors such as targeting a country not owned by
     *    an opponent. The test expects this order to be invalid, asserting a false result.
     *
     * These tests ensure that the bomb order validation logic accurately identifies valid and invalid
     * bomb actions based on the game state and the specific conditions that define a valid bombing target.
     */

    @Test
    public void testValidBombOrder() {
        boolean l_actualBoolean = d_bombOrder1.valid(d_gameState);
        assertTrue(l_actualBoolean);
        boolean l_actualBoolean1 = d_bombOrder2.valid(d_gameState);
        assertFalse(l_actualBoolean1);
    }

}
