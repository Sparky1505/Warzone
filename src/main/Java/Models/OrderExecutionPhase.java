package Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import Constants.ApplicationConstants;
import Controllers.GameEngine;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Services.GameService;
import Utils.Command;
import Utils.ExceptionLogHandler;
import Views.MapView;

/**
 * The {@code OrderExecutionPhase} class represents the phase in the game where orders are executed.
 * During this phase, players' orders are executed based on the game rules and conditions.
 * This phase typically follows the order submission phase in a game turn.
 *
 * <p>This class extends the {@code Phase} class and inherits its properties and behaviors.
 * It implements the specific functionality required for the order execution phase.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 *     OrderExecutionPhase executionPhase = new OrderExecutionPhase();
 *     executionPhase.executeOrders(gameState);
 * </pre>
 *
 * @see Phase
 * @see GameState
 */
public class OrderExecutionPhase extends Phase {

    /**
     * Constructs a new {@code OrderExecutionPhase} object with the specified game engine and game state.
     *
     * <p>This constructor initializes the order execution phase with the provided game engine and game state.
     * The game engine is responsible for managing the game flow and rules, while the game state holds the
     * current state of the game, including player information and the game board.</p>
     *
     * @param p_gameEngine The game engine responsible for managing the game flow and rules.
     * @param p_gameState  The game state holding the current state of the game, including player information
     *                     and the game board.
     */

    public OrderExecutionPhase(GameEngine p_gameEngine, GameState p_gameState) {
        super(p_gameEngine, p_gameState);
    }


    /**
     * This method is called to handle loading a game from a saved state. It prints an error message indicating
     * that loading a game is not supported in the current state.
     *
     * @param p_command The command object representing the load game command.
     * @param p_player  The player attempting to load the game.
     * @throws InvalidCommand If the command to load the game is invalid.
     * @throws InvalidMap     If the map data is invalid.
     * @throws IOException    If an I/O error occurs while handling the command.
     */
    @Override
    protected void performLoadGame(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        printInvalidCommandInState();
    }

    /**
     * This method is called to handle saving the current game state. It saves the game state to a file specified
     * in the command's arguments list.
     *
     * @param p_command The command object representing the save game command.
     * @param p_player  The player requesting to save the game.
     * @throws InvalidCommand If the command to save the game is invalid.
     * @throws InvalidMap     If the map data is invalid.
     * @throws IOException    If an I/O error occurs while handling the command.
     */
    @Override
    protected void performSaveGame(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        List<java.util.Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));

        if (l_operations_list == null || l_operations_list.isEmpty()) {
            throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_SAVEGAME);
        }

        for (Map<String, String> l_map : l_operations_list) {
            if (p_command.checkRequiredKeysPresent(ApplicationConstants.ARGUMENTS, l_map)) {
                String l_filename = l_map.get(ApplicationConstants.ARGUMENTS);
                GameService.saveGame(this, l_filename);
                d_gameEngine.setD_gameEngineLog("Game Saved Successfully to "+l_filename, "effect");
            } else {
                throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_SAVEGAME);
            }
        }
    }

    /**
     * This method is called to handle card-related commands during gameplay. It prints an error message indicating
     * that card handling is not supported in the current state.
     *
     * @param p_enteredCommand The command string representing the card-related command.
     * @param p_player         The player attempting to handle the card command.
     * @throws IOException If an I/O error occurs while handling the command.
     */
    @Override
    protected void performCardHandle(String p_enteredCommand, Player p_player) throws IOException {
        printInvalidCommandInState();
    }


    /**
     * This method is called to handle the advance command during gameplay. It prints an error message indicating
     * that the advance command is not supported in the current state.
     *
     * @param p_command The command object representing the advance command.
     * @param p_player  The player attempting to advance armies.
     */
    @Override
    protected void performAdvance(String p_command, Player p_player) {
        printInvalidCommandInState();
    }

    /**
     * Initializes the current game phase, executing orders and displaying the game map. It prompts the user to continue
     * for the next turn or exit the game based on whether it's in tournament mode or not.
     *
     * @param isTournamentMode Indicates whether the game is in tournament mode.
     */
    @Override
    public void initPhase(boolean isTournamentMode) {
        executeOrders();

        MapView l_map_view = new MapView(d_gameState);
        l_map_view.showMap();

        if (d_gameState.checkEndOftheGame(this))
            return;


        try {
            String l_continue = this.continueForNextTurn(isTournamentMode);
            if (l_continue.equalsIgnoreCase("N") && isTournamentMode) {
                d_gameEngine.setD_gameEngineLog("Start Up Phase", "phase");
                d_gameEngine.setD_CurrentPhase(new StartUpPhase(d_gameEngine, d_gameState));
            } else if (l_continue.equalsIgnoreCase("N") && !isTournamentMode) {
                d_gameEngine.setStartUpPhase();

            } else if (l_continue.equalsIgnoreCase("Y")) {
                System.out.println("\n" + d_gameState.getD_numberOfTurnsLeft() + " Turns are left for this game. Continuing for next Turn.\n");
                d_playerService.assignArmies(d_gameState);
                d_gameEngine.setIssueOrderPhase(isTournamentMode);
            } else {
                System.out.println("Invalid Input");
            }
        }  catch (IOException l_e) {
            System.out.println("Invalid Input");
        }

    }


    /**
     * Prompts the user to continue for the next turn or exit the game based on whether it's in tournament mode or not.
     *
     * @param isTournamentMode Indicates whether the game is in tournament mode.
     * @return The user's choice to continue for the next turn or exit the game.
     * @throws IOException If an I/O error occurs while reading the user's input.
     */
    private String continueForNextTurn(boolean isTournamentMode) throws IOException {
        String l_continue = new String();
        if (isTournamentMode) {
            d_gameState.setD_numberOfTurnsLeft(d_gameState.getD_numberOfTurnsLeft() - 1);
            l_continue = d_gameState.getD_numberOfTurnsLeft() == 0 ? "N" : "Y";
        } else {
            System.out.println("Press Y/y if you want to continue for next turn or else press N/n");
            BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));
            l_continue = l_reader.readLine();
        }
        return l_continue;
    }


    /**
     * Executes the orders submitted by the players in the game. It iterates through the list of players and executes
     * their orders one by one until all orders are executed.
     */
    protected void executeOrders() {
        d_gameState.addNeutralPlayer();
        // Executing orders
        d_gameEngine.setD_gameEngineLog("\nStarting Execution Of Orders.....", "start");
        while (d_playerService.unexecutedOrdersExists(d_gameState.getD_players())) {
            for (Player l_player : d_gameState.getD_players()) {
                Order l_order = l_player.next_order();
                if (l_order != null) {
                    l_order.printOrder();
                    d_gameState.d_logEntryBuffer.updateLog(l_order.orderExecutionLog(), "effect");
                    l_order.execute(d_gameState);
                }
            }
        }
        d_playerService.resetPlayersFlag(d_gameState.getD_players());
    }

    /**
     * This method is called to handle the deploy command during gameplay. It prints an error message indicating
     * that the deploy command is not supported in the current state.
     *
     * @param p_command The command object representing the deploy command.
     * @param p_player  The player attempting to deploy armies.
     */
    @Override
    protected void performShowMap(Command p_command, Player p_player) {
        MapView l_mapView = new MapView(d_gameState);
        l_mapView.showMap();
    }

    /**
     * This method is called to handle the deploy command during gameplay. It prints an error message indicating
     * that the deploy command is not supported in the current state.
     *
     * @param p_command The command object representing the deploy command.
     * @param p_player  The player attempting to deploy armies.
     */
    @Override
    protected void performCreateDeploy(String p_command, Player p_player) {
        printInvalidCommandInState();
    }


    /**
     * This method is called to handle assigning countries to players during the startup phase. It prints an error message
     * indicating that country assignment is not supported in the current state.
     *
     * @param p_command        The command object representing the assign countries command.
     * @param p_player         The player attempting to assign countries.
     * @param isTournamentMode Indicates whether the game is in tournament mode.
     * @param p_gameState      The current game state.
     * @throws InvalidCommand If the command to assign countries is invalid.
     * @throws IOException    If an I/O error occurs while handling the command.
     */
    @Override
    protected void performAssignCountries(Command p_command, Player p_player, boolean isTournamentMode,
                                          GameState p_gameState) throws InvalidCommand, IOException {
        printInvalidCommandInState();
    }
    /**
     * This method is called to handle creating players in the game. It prints an error message indicating that
     * player creation is not supported in the current state.
     *
     * @param p_command The command object representing the create players command.
     * @param p_player  The player attempting to create players.
     * @throws InvalidCommand If the command to create players is invalid.
     */
    @Override
    protected void createPlayers(Command p_command, Player p_player) throws InvalidCommand {
        printInvalidCommandInState();
    }

    /**
     * This method is called to handle editing the neighbors of a country on the game map. It prints an error message
     * indicating that editing neighbors is not supported in the current state.
     *
     * @param p_command The command object representing the edit neighbor command.
     * @param p_player  The player attempting to edit neighbors.
     * @throws InvalidCommand If the command to edit neighbors is invalid.
     * @throws InvalidMap     If the map data is invalid.
     * @throws IOException    If an I/O error occurs while handling the command.
     */
    @Override
    protected void performEditNeighbour(Command p_command, Player p_player)
            throws InvalidCommand, InvalidMap, IOException {
        printInvalidCommandInState();
    }


    /**
     * This method is called to handle editing properties of a country on the game map. It prints an error message
     * indicating that editing countries is not supported in the current state.
     *
     * @param p_command The command object representing the edit country command.
     * @param p_player  The player attempting to edit countries.
     * @throws InvalidCommand If the command to edit countries is invalid.
     * @throws InvalidMap     If the map data is invalid.
     * @throws IOException    If an I/O error occurs while handling the command.
     */
    @Override
    protected void performEditCountry(Command p_command, Player p_player)
            throws InvalidCommand, InvalidMap, IOException {
        printInvalidCommandInState();
    }


    /**
     * This method is called to handle validating the map of the game. It prints an error message indicating that
     * map validation is not supported in the current state.
     *
     * @param p_command The command object representing the validate map command.
     * @param p_player  The player attempting to validate the map.
     * @throws InvalidMap     If the map data is invalid.
     * @throws InvalidCommand If the command to validate the map is invalid.
     */
    @Override
    protected void performValidateMap(Command p_command, Player p_player) throws InvalidMap, InvalidCommand {
        printInvalidCommandInState();
    }


    /**
     * This method is called to handle loading a map for the game. It prints an error message indicating that
     * map loading is not supported in the current state.
     *
     * @param p_command The command object representing the load map command.
     * @param p_player  The player attempting to load the map.
     * @throws InvalidCommand If the command to load the map is invalid.
     * @throws InvalidMap     If the loaded map data is invalid.
     */
    @Override
    protected void performLoadMap(Command p_command, Player p_player) throws InvalidCommand, InvalidMap {
        printInvalidCommandInState();
    }


    /**
     * This method is called to handle saving the map of the game. It prints an error message indicating that
     * map saving is not supported in the current state.
     *
     * @param p_command The command object representing the save map command.
     * @param p_player  The player attempting to save the map.
     * @throws InvalidCommand If the command to save the map is invalid.
     * @throws InvalidMap     If the map data is invalid.
     */
    @Override
    protected void performSaveMap(Command p_command, Player p_player) throws InvalidCommand, InvalidMap {
        printInvalidCommandInState();
    }


    /**
     * This method is called to handle editing continents on the game map. It prints an error message indicating that
     * continent editing is not supported in the current state.
     *
     * @param p_command The command object representing the edit continent command.
     * @param p_player  The player attempting to edit continents.
     * @throws IOException    If an I/O error occurs while handling the command.
     * @throws InvalidCommand If the command to edit continents is invalid.
     * @throws InvalidMap     If the map data is invalid.
     */
    @Override
    protected void performEditContinent(Command p_command, Player p_player)
            throws IOException, InvalidCommand, InvalidMap {
        printInvalidCommandInState();
    }


    /**
     * This method is called to handle editing the map layout. It prints an error message indicating that
     * map editing is not supported in the current state.
     *
     * @param p_command The command object representing the map edit command.
     * @param p_player  The player attempting to edit the map.
     * @throws IOException    If an I/O error occurs while handling the command.
     * @throws InvalidCommand If the command to edit the map is invalid.
     * @throws InvalidMap     If the map data is invalid.
     */
    @Override
    protected void performMapEdit(Command p_command, Player p_player) throws IOException, InvalidCommand, InvalidMap {
        printInvalidCommandInState();
    }


    /**
     * This method is called to handle gameplay during a tournament mode. It sets a log message indicating the start
     * of tournament mode execution.
     *
     * @param p_enteredCommand The command entered for tournament gameplay.
     */
    @Override
    protected void tournamentGamePlay(Command p_enteredCommand) {
        d_gameEngine.setD_gameEngineLog("\nStarting Execution Of Tournament Mode.....", "start");

    }
}