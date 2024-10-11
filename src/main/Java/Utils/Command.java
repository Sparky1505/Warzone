package Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.Serializable;

/**
 * The {@code Command} class represents a command parsed from user input.
 * It provides methods to process and extract information from the command string.
 */
public class Command implements Serializable{

    /** The original command string. */
    public String d_command;

    /**
     * Constructs a new Command object with the specified command string.
     *
     * @param p_command the command string to be processed
     */
    public Command(String p_command){
        this.d_command = p_command.trim().replaceAll(" +", " ");
    }

    /**
     * Gets the root command from the command string.
     *
     * @return the root command
     */
    public String getRootCommand(){
        return d_command.split(" ")[0];
    }

    /**
     * Extracts operations and their arguments from the command string.
     *
     * @return a list of maps containing operations and their arguments
     */
    public List<Map<String , String>> getOperationsAndArguments(){
        String l_rootCommand = getRootCommand();
        String l_operationsString =  d_command.replace(l_rootCommand, "").trim();

        if(null == l_operationsString || l_operationsString.isEmpty()) {
            return new ArrayList<Map<String , String>>();
        }
        boolean l_isFlagLessCommand = !l_operationsString.contains("-") && !l_operationsString.contains(" ");

        // handle commands to load files, ex: loadmap filename
        if(l_isFlagLessCommand){
            l_operationsString = "-filename "+l_operationsString;
        }

        List<Map<String , String>> l_operations_list  = new ArrayList<Map<String,String>>();
        String[] l_operations = l_operationsString.split("-");

        Arrays.stream(l_operations).forEach((operation) -> {
            if(operation.length() > 1) {
                l_operations_list.add(getOperationAndArgumentsMap(operation));
            }
        });

        return l_operations_list;
    }

    /**
     * Constructs a map containing an operation and its arguments.
     *
     * @param p_operation the operation string
     * @return a map containing the operation and its arguments
     */
    private Map<String, String> getOperationAndArgumentsMap(String p_operation){
        Map<String, String> l_operationMap = new HashMap<String, String>();

        String[] l_split_operation = p_operation.split(" ");
        String l_arguments = "";

        l_operationMap.put("operation", l_split_operation[0]);

        if(l_split_operation.length > 1){
            String[] l_arguments_values = Arrays.copyOfRange(l_split_operation, 1, l_split_operation.length);
            l_arguments = String.join(" ",l_arguments_values);
        }

        l_operationMap.put("arguments", l_arguments);

        return l_operationMap;
    }

    /**
     * Checks if the specified key is present in the input map and its corresponding value is not null or empty.
     *
     * @param p_key       The key to check for in the input map.
     * @param p_inputMap  The input map to check for the presence of the key.
     * @return            true if the key is present and its corresponding value is not null or empty, false otherwise.
     */
    public boolean checkRequiredKeysPresent(String p_key, Map<String, String> p_inputMap) {
        if(p_inputMap.containsKey(p_key) && null != p_inputMap.get(p_key)
                && !p_inputMap.get(p_key).isEmpty())
            return true;
        return false;
    }

    /**
     * Retrieves the value of the command attribute.
     *
     * @return The value of the command attribute.
     */
    public String getD_command() {
        return d_command;
    }

    }

