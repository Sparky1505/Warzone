package Exceptions;

import java.io.Serializable;

/**
 * This class represents exceptions related to invalid maps.
 */
public class InvalidMap extends Exception implements Serializable {

    /**
     * Constructs a new InvalidMap with the given error message.
     *
     * @param p_message The error message to be displayed when map is not valid.
     */
    public InvalidMap(String p_message) {
        super(p_message);
    }
}