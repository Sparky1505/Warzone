package Views;

import Constants.ApplicationConstants;
import Models.GameState;
import Models.Tournament;
import org.davidmoten.text.utils.WordWrap;

import java.util.List;

/**
 *  The TournamentView class represents the view for the tournament objects.
 */
public class TournamentView {
    /**
     * Tournament object representing the current tournament.
     */
    Tournament d_tournament;

    /**
     * List of GameState objects representing the state of each game in the tournament.
     */
    List<GameState> d_gameStateObjects;

    /**
     * ANSI escape code for resetting color formatting to default.
     */
    public static final String ANSI_RESET = "\u001B[0m";

    /**
     * Constructs a new TournamentView with a specific Tournament object.
     *
     * @param p_tournament The Tournament object to be visualized.
     */
    public TournamentView(Tournament p_tournament){
        d_tournament = p_tournament;
        d_gameStateObjects = d_tournament.getD_gameStateList();
    }

    /**
     * Returns a colorized string if a color code is provided; otherwise, returns the original string.
     *
     * @param p_color The ANSI color code for the string.
     * @param p_s The string to be colorized.
     * @return A colorized string if a color is provided, else the original string.
     */
    private String getColorizedString(String p_color, String p_s) {
        if(p_color == null) return p_s;

        return p_color + p_s + ANSI_RESET;
    }

    /**
     * Renders a string centered within a specified width on the console.
     *
     * @param p_width The width within which the string should be centered.
     * @param p_s The string to be centered.
     */
    private void renderCenteredString (int p_width, String p_s) {
        String l_centeredString = String.format("%-" + p_width  + "s", String.format("%" + (p_s.length() + (p_width - p_s.length()) / 2) + "s", p_s));

        System.out.format(l_centeredString+"\n");
    }

    /**
     * Renders a separator line of a specific width on the console.
     */
    private void renderSeparator(){
        StringBuilder l_separator = new StringBuilder();

        for (int i = 0; i< ApplicationConstants.CONSOLE_WIDTH -2; i++){
            l_separator.append("-");
        }
        System.out.format("+%s+%n", l_separator.toString());
    }

    /**
     * Renders the map name and game number centered on the console.
     *
     * @param p_gameIndex The index of the current game in the tournament.
     * @param p_mapName The name of the map being played.
     */
    private void renderMapName(Integer p_gameIndex, String p_mapName){
        String l_formattedString = String.format("%s %s %d %s", p_mapName, " (Game Number: ",p_gameIndex, " )" );
        renderSeparator();
        renderCenteredString(ApplicationConstants.CONSOLE_WIDTH, l_formattedString);
        renderSeparator();
    }

    /**
     * Renders the game state information, including winner, losers, and game conclusion.
     *
     * @param p_gameState The GameState object containing game outcome details.
     */
    private void renderGames(GameState p_gameState){
        String l_winner;
        String l_conclusion;
        if(p_gameState.getD_winner()==null){
            l_winner = " ";
            l_conclusion = "Draw!";
        } else{
            System.out.println("Entered Here");
            l_winner = getColorizedString(p_gameState.getD_winner().getD_color(), p_gameState.getD_winner().getPlayerName());
            l_conclusion = "Winning Player Strategy: "+ p_gameState.getD_winner().getD_playerBehaviorStrategy().getPlayerBehavior();
        }
        String l_winnerString = String.format("%s %s", "Winner -> ", l_winner);
        StringBuilder l_commaSeparatedPlayers = new StringBuilder();

        for(int i=0; i<p_gameState.getD_playersFailed().size(); i++) {
            l_commaSeparatedPlayers.append(getColorizedString(p_gameState.getD_playersFailed().get(i).getD_color(), p_gameState.getD_playersFailed().get(i).getPlayerName()));
            if(i<p_gameState.getD_playersFailed().size()-1)
                l_commaSeparatedPlayers.append(", ");
        }
        String l_losingPlayers = "Losing Players -> "+ WordWrap.from(l_commaSeparatedPlayers.toString()).maxWidth(ApplicationConstants.CONSOLE_WIDTH).wrap();
        String l_conclusionString = String.format("%s %s", "Conclusion of Game -> ", l_conclusion);
        System.out.println(l_winnerString);
        System.out.println(l_losingPlayers);
        System.out.println(l_conclusionString);
    }

    /**
     * Visualizes tournament details by displaying each game's map name and outcomes, including winners and losers.
     */
    public void viewTournament(){
        int l_counter = 0;
        System.out.println();
        if(d_tournament!=null && d_gameStateObjects!=null){
            for(GameState l_gameState: d_tournament.getD_gameStateList()){
                l_counter++;
                renderMapName(l_counter, l_gameState.getD_map().getD_mapFile());
                renderGames(l_gameState);
            }
        }
    }

}
