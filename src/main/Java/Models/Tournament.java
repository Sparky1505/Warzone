package Models;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Constants.ApplicationConstants;
import Controllers.GameEngine;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Services.MapService;

/**
 * The Tournament class represents a tournament in the game. It holds a list of game states for each game
 * in the tournament.
 */
public class Tournament implements Serializable {

    /**
     * The Tournament class represents a tournament in the game. It holds a list of game states for each game
     * in the tournament.
     */
    MapService d_mapService = new MapService();
    /**
     * The list of game states for each game in the tournament.
     */
    List<GameState> d_gameStateList = new ArrayList<GameState>();

    /**
     * Returns the list of game states for each game in the tournament.
     *
     * @return The list of game states.
     */
    public List<GameState> getD_gameStateList() {
        return d_gameStateList;
    }

    /**
     * Sets the list of game states for each game in the tournament.
     *
     * @param d_gameStateList The list of game states to set.
     */
    public void setD_gameStateList(List<GameState> d_gameStateList) {
        this.d_gameStateList = d_gameStateList;
    }


    /**
     * Parses the argument specifying the number of additional games for the tournament. It creates additional game states
     * based on the specified number of games and adds them to the tournament's game state list.
     *
     * @param p_argument    The argument specifying the number of additional games.
     * @param p_gameEngine  The game engine instance.
     * @return True if the argument is parsed successfully and additional game states are created, false otherwise.
     * @throws InvalidMap   If the map data is invalid while loading additional game states.
     */
    private boolean parseNoOfGameArgument(String p_argument, GameEngine p_gameEngine) throws InvalidMap {
        int l_noOfGames = Integer.parseInt(p_argument.split(" ")[0]);

        if (l_noOfGames >= 1 && l_noOfGames <= 5) {
            List<GameState> l_additionalGameStates = new ArrayList<>();

            for (int l_gameNumber = 0; l_gameNumber < l_noOfGames - 1; l_gameNumber++) {
                for (GameState l_gameState : d_gameStateList) {
                    GameState l_gameStateToAdd = new GameState();
                    Models.Map l_loadedMap = d_mapService.loadMap(l_gameStateToAdd,
                            l_gameState.getD_map().getD_mapFile());
                    l_loadedMap.setD_mapFile(l_gameState.getD_map().getD_mapFile());

                    List<Player> l_playersToCopy = d_mapService.getPlayersToAdd(l_gameState.getD_players());
                    l_gameStateToAdd.setD_players(l_playersToCopy);

                    l_gameStateToAdd.setD_loadCommand();
                    l_additionalGameStates.add(l_gameStateToAdd);
                }
            }
            d_gameStateList.addAll(l_additionalGameStates);
            return true;
        } else {
            p_gameEngine.setD_gameEngineLog(
                    "User entered invalid number of games in command, Range of games :- 1<=number of games<=5",
                    "effect");
            return false;
        }
    }

    /**
     * Parses the argument specifying the maximum number of turns for each game in the tournament. It sets the maximum
     * number of turns for each game state in the tournament.
     *
     * @param p_argument    The argument specifying the maximum number of turns.
     * @param p_gameEngine  The game engine instance.
     * @return True if the argument is parsed successfully and maximum turns are set for each game state, false otherwise.
     */
    private boolean pasrseNoOfTurnsArguments(String p_argument, GameEngine p_gameEngine) {
        int l_maxTurns = Integer.parseInt(p_argument.split(" ")[0]);
        if (l_maxTurns >= 10 && l_maxTurns <= 50) {
            for (GameState l_gameState : d_gameStateList) {
                l_gameState.setD_maxnumberofturns(l_maxTurns);
                l_gameState.setD_numberOfTurnsLeft(l_maxTurns);
            }
            return true;
        } else {
            p_gameEngine.setD_gameEngineLog(
                    "User entered invalid number of turns in command, Range of turns :- 10<=number of turns<=50",
                    "effect");
            return false;
        }
    }

    /**
     * Parses the argument specifying player strategies for the tournament. It validates the strategies and sets them
     * for the players participating in the tournament.
     *
     * @param p_gameState    The game state object representing the current state of the game.
     * @param p_argument     The argument specifying the player strategies for the tournament.
     * @param p_gameEngine   The game engine instance.
     * @return True if the argument is parsed successfully and strategies are set for the players, false otherwise.
     */
    private boolean parseStrategyArguments(GameState p_gameState, String p_argument, GameEngine p_gameEngine) {
        String[] l_listofplayerstrategies = p_argument.split(" ");
        int l_playerStrategiesSize = l_listofplayerstrategies.length;
        List<Player> l_playersInTheGame = new ArrayList<>();
        List<String> l_uniqueStrategies = new ArrayList<>();

        for (String l_strategy : l_listofplayerstrategies) {
            if(l_uniqueStrategies.contains(l_strategy)) {
                p_gameEngine.setD_gameEngineLog(
                        "Repetitive strategy : " + l_strategy + " given. Kindly provide set of unique strategies.",
                        "effect");
                return false;
            }
            l_uniqueStrategies.add(l_strategy);
            if (!ApplicationConstants.TOURNAMENT_PLAYER_BEHAVIORS.contains(l_strategy)) {
                p_gameEngine.setD_gameEngineLog(
                        "Invalid Strategy passed in command. Only Aggressive, Benevolent, Random, Cheater strategies are allowed.",
                        "effect");
                return false;
            }
        }
        if (l_playerStrategiesSize >= 2 && l_playerStrategiesSize <= 4) {
            setTournamentPlayers(p_gameEngine, l_listofplayerstrategies, p_gameState.getD_players(),
                    l_playersInTheGame);
        } else {
            p_gameEngine.setD_gameEngineLog(
                    "User entered invalid number of strategies in command, Range of strategies :- 2<=strategy<=4",
                    "effect");
            return false;
        }
        if (l_playersInTheGame.size() < 2) {
            p_gameEngine.setD_gameEngineLog(
                    "There has to be at least 2 or more non human players eligible to play the tournament.", "effect");
            return false;
        }
        for (GameState l_gameState : d_gameStateList) {
            l_gameState.setD_players(d_mapService.getPlayersToAdd(l_playersInTheGame));
        }
        return true;
    }

    /**
     * Parses the command for the tournament mode and delegates the parsing to specific methods based on the operation
     * specified in the command. Supported operations include 'M' for map selection, 'P' for player strategy selection,
     * 'G' for the number of additional games, and 'D' for setting the maximum number of turns.
     *
     * @param p_gameState    The game state object representing the current state of the game.
     * @param p_operation    The operation specified in the tournament command.
     * @param p_argument     The argument associated with the operation.
     * @param p_gameEngine   The game engine instance.
     * @return True if the command is parsed successfully, false otherwise.
     * @throws InvalidMap    If the map data is invalid.
     * @throws InvalidCommand If the command for the tournament mode is invalid.
     */
    public boolean parseTournamentCommand(GameState p_gameState, String p_operation, String p_argument,
                                          GameEngine p_gameEngine) throws InvalidMap, InvalidCommand {

        if (p_operation.equalsIgnoreCase("M")) {
            return parseMapArguments(p_argument, p_gameEngine);
        }
        if (p_operation.equalsIgnoreCase("P")) {
            return parseStrategyArguments(p_gameState, p_argument, p_gameEngine);
        }
        if (p_operation.equalsIgnoreCase("G")) {
            return parseNoOfGameArgument(p_argument, p_gameEngine);
        }
        if (p_operation.equalsIgnoreCase("D")) {
            return pasrseNoOfTurnsArguments(p_argument, p_gameEngine);
        }
        throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_TOURNAMENT_MODE);
    }

    /**
     * Sets the players eligible to participate in the tournament based on the specified player strategies.
     *
     * @param p_gameEngine              The game engine instance.
     * @param p_listofplayerstrategies The array of player strategies specified for the tournament.
     * @param p_listOfPlayers          The list of all players available in the game.
     * @param p_playersInTheGame       The list of players eligible to participate in the tournament.
     */
    private void setTournamentPlayers(GameEngine p_gameEngine, String[] p_listofplayerstrategies,
                                      List<Player> p_listOfPlayers, List<Player> p_playersInTheGame) {
        for (String l_strategy : p_listofplayerstrategies) {
            for (Player l_pl : p_listOfPlayers) {
                if (l_pl.getD_playerBehaviorStrategy().getPlayerBehavior().equalsIgnoreCase(l_strategy)) {
                    p_playersInTheGame.add(l_pl);
                    p_gameEngine.setD_gameEngineLog("Player:  " + l_pl.getPlayerName() + " with strategy: " + l_strategy
                            + " has been added in tournament.", "effect");
                }
            }
        }
    }

    /**
     * Parses the argument specifying the map files for the tournament. It loads the map files and creates game states
     * for each map file. The game states are added to the tournament's game state list.
     *
     * @param p_argument    The argument specifying the map files for the tournament.
     * @param p_gameEngine  The game engine instance.
     * @return True if the argument is parsed successfully and game states are created, false otherwise.
     * @throws InvalidMap   If the map data is invalid while loading the game states.
     */
    private boolean parseMapArguments(String p_argument, GameEngine p_gameEngine) throws InvalidMap {
        String[] l_listOfMapFiles = p_argument.split(" ");
        int l_mapFilesSize = l_listOfMapFiles.length;

        if (l_mapFilesSize >= 1 & l_mapFilesSize <= 5) {
            for (String l_mapToLoad : l_listOfMapFiles) {
                GameState l_gameState = new GameState();
                Models.Map l_loadedMap = d_mapService.loadMap(l_gameState, l_mapToLoad);
                l_loadedMap.setD_mapFile(l_mapToLoad);
                if (l_loadedMap.Validate()) {
                    l_gameState.setD_loadCommand();
                    p_gameEngine.setD_gameEngineLog(l_mapToLoad + " has been loaded to start the game", "effect");
                    d_gameStateList.add(l_gameState);
                } else {
                    d_mapService.resetMap(l_gameState, l_mapToLoad);
                    return false;
                }
            }
        } else {
            p_gameEngine.setD_gameEngineLog("User entered invalid number of maps in command, Range of map :- 1<=map<=5",
                    "effect");
            return false;
        }
        return true;
    }

}