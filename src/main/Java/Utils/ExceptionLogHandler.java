package Utils;
import Models.GameState;
import java.io.Serializable;

/**
 * The ExceptionLogHandler class implements the Thread.UncaughtExceptionHandler interface.
 * It handles uncaught exceptions by updating the log in the game state with the exception message.
 */
public class ExceptionLogHandler implements Thread.UncaughtExceptionHandler, Serializable {

    /** The game state to update the log with exception messages. */
    GameState d_gameState;

    /**
     * Constructs an ExceptionLogHandler with the specified game state.
     *
     * @param p_gameState The game state to update the log with exception messages.
     */
    public ExceptionLogHandler(GameState p_gameState){
        d_gameState = p_gameState;
    }

    /**
     * Handles uncaught exceptions by updating the log in the game state with the exception message.
     *
     * @param p_t The thread where the uncaught exception occurred.
     * @param p_e The uncaught exception.
     */
    @Override
    public void uncaughtException(Thread p_t, Throwable p_e) {
        // Updates the log in the game state with the exception message
        d_gameState.updateLog(p_e.getMessage(), "effect");
    }
}
