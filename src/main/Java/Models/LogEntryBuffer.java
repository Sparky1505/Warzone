package Models;

import Views.LogWriter;

import java.io.Serializable;
import java.util.Observable;

/**
 * The LogEntryBuffer class extends Observable and represents a buffer for log messages.
 * It notifies observers (such as LogWriter) when a new log message is added.
 */
public class LogEntryBuffer extends Observable implements Serializable {

    /** The log message stored in the buffer. */
    String d_logMessage;

    /**
     * Constructs a new LogEntryBuffer and adds a LogWriter observer to it.
     */
    public LogEntryBuffer() {
        LogWriter l_logWriter = new LogWriter();
        this.addObserver(l_logWriter);
    }

    /**
     * Gets the log message stored in the buffer.
     *
     * @return the log message
     */
    public String getD_logMessage() {
        return d_logMessage;
    }

    /**
     * Updates the log message in the buffer based on the provided message and log type,
     * then notifies observers of the change.
     *
     * @param p_messageToUpdate the message to update the log with
     * @param p_logType         the type of log message (e.g., command, order, phase, effect, start, end)
     */
    public void logEvent(String p_messageToUpdate, String p_logType) {
        String log = p_logType.toLowerCase();
        if(log == "command") {
            d_logMessage = "\nInput Command: " + p_messageToUpdate + "\n";
        } else if (log == "order") {
            d_logMessage = "\nIssued Order: " + p_messageToUpdate + "\n";
        } else if (log == "phase") {
            d_logMessage = "\n-------" + p_messageToUpdate + "-------\n\n";
        } else if (log == "effect") {
            d_logMessage = "Log: " + p_messageToUpdate + "\n";
        } else if (log == "start" || log == "end") {
            d_logMessage = p_messageToUpdate + System.lineSeparator();
        }

        setChanged();
        notifyObservers();
    }
}
