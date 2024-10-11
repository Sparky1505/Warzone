package Exceptions;

import java.io.Serializable;

/**
 * Handles exceptions related to invalid commands.
 */
public class InvalidCommand extends Exception implements Serializable {

    /**
     * Constructs a new InvalidCommand with the given error message.
     *
     * @param p_message The error message to be displayed when command is not valid.
     */
    public InvalidCommand(String p_message) {
        super(p_message);
    }
}