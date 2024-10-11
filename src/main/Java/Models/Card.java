package Models;

/**
 * Represents a card in the game.
 */
public interface Card extends Order {

    /**
     * Checks if the card order is valid.
     *
     * @param p_gameState The current game state.
     * @return True if the card order is valid, false otherwise.
     */
    public Boolean checkValidOrder(GameState p_gameState);

}
