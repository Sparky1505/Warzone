package Models;

import java.io.IOException;
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
 * Represents the phase where players issue orders.
 */
public class IssueOrderPhase extends Phase {

    /**
     * Initializes a new instance of the IssueOrderPhase class.
     *
     * @param p_gameEngine The game engine associated with the phase.
     * @param p_gameState  The current game state.
     */
    public IssueOrderPhase(GameEngine p_gameEngine, GameState p_gameState) {
        super(p_gameEngine, p_gameState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performLoadGame(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        printInvalidCommandInState();
        p_player.askForOrder(this);
    }


    /**
     * {@inheritDoc}
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
     * This method is responsible for performing card handling for a player based on the entered command.
     * It checks if the player owns the card specified in the entered command, and if so, delegates the
     * handling of the command to the player's `handleCardCommands` method.
     *
     * @param p_enteredCommand The command entered by the player, typically specifying the card and its action.
     * @param p_player         The player whose card is being handled.
     * @throws IOException If an I/O error occurs while handling the card commands.
     */
    @Override
    protected void performCardHandle(String p_enteredCommand, Player p_player) throws IOException {
        if(p_player.getD_cardsOwnedByPlayer().contains(p_enteredCommand.split(" ")[0])) {
            p_player.handleCardCommands(p_enteredCommand, d_gameState);
        }

    }

    /**
     * Performs showing of the map.
     *
     * @param p_command The command to perform.
     * @param p_player  The player performing the command.
     * @throws InvalidCommand if the command is invalid.
     * @throws IOException    if an I/O error occurs.
     * @throws InvalidMap     if the map is invalid.
     */
    @Override
    protected void performShowMap(Command p_command, Player p_player) throws InvalidCommand, IOException, InvalidMap {
        MapView l_mapView = new MapView(d_gameState);
        l_mapView.showMap();
        p_player.askForOrder(this);
    }

    /**
     * Performs the advance order for a player.
     *
     * @param p_command The command entered by the player.
     * @param p_player  The player issuing the command.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    protected void performAdvance(String p_command, Player p_player) throws IOException {
        p_player.createAdvanceOrder(p_command, d_gameState);
        d_gameState.d_logEntryBuffer.updateLog(p_player.getD_playerLog(), "effect");

    }

    /**
     * Initializes the phase.
     */
    @Override
    public void initPhase(boolean p_isTournamentMode){
        while (d_gameEngine.getD_CurrentPhase() instanceof IssueOrderPhase) {
            issueOrders(p_isTournamentMode);
        }
    }

    /**
     * Performs the creation of a deploy order for a player.
     *
     * @param p_command The command entered by the player.
     * @param p_player  The player issuing the command.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    protected void performCreateDeploy(String p_command, Player p_player) throws IOException {
        p_player.createDeployOrder(p_command);
        d_gameState.d_logEntryBuffer.updateLog(p_player.getD_playerLog(), "effect");

    }

    /**
     * Performs the issuance of orders for players in the game.
     */
    protected void issueOrders(boolean p_isTournamentMode){
        // issue orders for each player
        do {
            for (Player l_player : d_gameState.getD_players()) {
                if(l_player.getD_coutriesOwned().size()==0){
                    l_player.setD_moreOrders(false);
                }
                if (l_player.getD_moreOrders() && !l_player.getPlayerName().equals("Neutral")) {
                    try {
                        l_player.issue_order(this);
                        l_player.checkForMoreOrders(p_isTournamentMode);
                    } catch (InvalidCommand | IOException | InvalidMap l_exception) {
                        d_gameEngine.setD_gameEngineLog(l_exception.getMessage(), "effect");
                    }
                }
            }
        } while (d_playerService.checkForMoreOrders(d_gameState.getD_players()));

        d_gameEngine.setOrderExecutionPhase();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performAssignCountries(Command p_command, Player p_player, boolean isTournamentMode, GameState p_gameState)
            throws InvalidCommand, IOException, InvalidMap {
        printInvalidCommandInState();
        p_player.askForOrder(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void createPlayers(Command p_command, Player p_player) throws InvalidCommand, IOException, InvalidMap {
        printInvalidCommandInState();
        p_player.askForOrder(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performEditNeighbour(Command p_command, Player p_player)
            throws InvalidCommand, InvalidMap, IOException {
        printInvalidCommandInState();
        p_player.askForOrder(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performEditCountry(Command p_command, Player p_player)
            throws InvalidCommand, InvalidMap, IOException {
        printInvalidCommandInState();
        p_player.askForOrder(this);
    }

    /**
     * Performs validation of the map.
     *
     * @param p_command The command entered by the player.
     * @param p_player  The player issuing the command.
     * @throws InvalidMap     if the map is invalid.
     * @throws InvalidCommand if the command is invalid.
     * @throws IOException    if an I/O error occurs.
     */
    @Override
    protected void performValidateMap(Command p_command, Player p_player) throws InvalidMap, InvalidCommand, IOException {
        printInvalidCommandInState();
        p_player.askForOrder(this);
    }

    /**
     * Loads the map.
     *
     * @param p_command The command entered by the player.
     * @param p_player  The player issuing the command.
     * @throws InvalidCommand if the command is invalid.
     * @throws InvalidMap     if the map is invalid.
     * @throws IOException    if an I/O error occurs.
     */
    @Override
    protected void performLoadMap(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        printInvalidCommandInState();
        p_player.askForOrder(this);
    }

    /**
     * Saves the map.
     *
     * @param p_command The command entered by the player.
     * @param p_player  The player issuing the command.
     * @throws InvalidCommand if the command is invalid.
     * @throws InvalidMap     if the map is invalid.
     * @throws IOException    if an I/O error occurs.
     */
    @Override
    protected void performSaveMap(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        printInvalidCommandInState();
        p_player.askForOrder(this);
    }

    /**
     * Edits a continent.
     *
     * @param p_command The command entered by the player.
     * @param p_player  The player issuing the command.
     * @throws IOException    if an I/O error occurs.
     * @throws InvalidCommand if the command is invalid.
     * @throws InvalidMap     if the map is invalid.
     */
    @Override
    protected void performEditContinent(Command p_command, Player p_player) throws IOException, InvalidCommand, InvalidMap {
        printInvalidCommandInState();
        p_player.askForOrder(this);
    }

    /**
     * Performs map editing.
     *
     * @param p_command The command entered by the player.
     * @param p_player  The player issuing the command.
     * @throws IOException    if an I/O error occurs.
     * @throws InvalidCommand if the command is invalid.
     * @throws InvalidMap     if the map is invalid.
     */
    @Override
    protected void performMapEdit(Command p_command, Player p_player) throws IOException, InvalidCommand, InvalidMap {
        printInvalidCommandInState();
        p_player.askForOrder(this);
    }

    /**
     * This method is intended to handle gameplay during a tournament. However, it is currently commented out
     * and does not perform any functionality. It may have been intended for future use or experimentation.
     *
     * @param p_enteredCommand The command entered by the player, which may be relevant for gameplay in a tournament.
     */
    @Override
    protected void tournamentGamePlay(Command p_enteredCommand) {
        //printInvalidCommandInState();
    }
}