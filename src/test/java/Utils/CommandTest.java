package Utils;

import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


/**
 * The {@code CommandTest} class contains test cases for the {@code Command} class.
 * It ensures that the command parsing and extraction methods work correctly.
 */
public class CommandTest implements Serializable {

    /**
     * Tests the getRootCommand method with a valid command.
     */
    @Test
    public void test_validCommand_getRootCommand(){
        Command l_command = new Command("editcontinent -add continentID continentvalue");
        String l_rootCommand = l_command.getRootCommand();

        assertEquals("editcontinent",l_rootCommand);
    }

    /**
     * Tests the getRootCommand method with an empty command.
     */
    @Test
    public void test_inValidCommand_getRootCommand(){
        Command l_command = new Command("");
        String l_rootCommand = l_command.getRootCommand();

        assertEquals("", l_rootCommand);
    }

    /**
     * Tests the getRootCommand method with a single word command.
     */
    @Test
    public void test_singleWord_getRootCommand(){
        Command l_command = new Command("validatemap");
        String l_rootCommand = l_command.getRootCommand();

        assertEquals("validatemap", l_rootCommand);
    }


    /**
     * Tests the getRootCommand method with a command without flags.
     */
    @Test
    public void test_noFlagCommand_getRootCommand(){
        Command l_command = new Command("loadmap abc.txt");
        String l_rootCommand = l_command.getRootCommand();

        assertEquals("loadmap", l_rootCommand);
    }

    /**
     * Tests the getOperationsAndArguments method with a single command.
     */
    @Test
    public void test_singleCommand_getOperationsAndArguments(){
        Command l_command = new Command("editcontinent -remove continentID");
        List<Map<String , String>> l_actualOperationsAndValues = l_command.getOperationsAndArguments();

        // Preparing Expected Value
        List<Map<String , String>> l_expectedOperationsAndValues = new ArrayList<Map<String, String>>();

        Map<String, String> l_expectedCommandTwo = new HashMap<String, String>() {{
            put("arguments", "continentID");
            put("operation", "remove");
        }};
        l_expectedOperationsAndValues.add(l_expectedCommandTwo);

        assertEquals(l_expectedOperationsAndValues, l_actualOperationsAndValues);
    }

    /**
     * Tests the getOperationsAndArguments method with a single command containing extra spaces.
     * It ensures that the command is parsed correctly and returns the expected operations and arguments.
     */
    @Test
    public void test_singleCommandWithExtraSpaces_getOperationsAndArguments(){
        Command l_command = new Command("editcontinent      -remove continentID");
        List<Map<String , String>> l_actualOperationsAndValues = l_command.getOperationsAndArguments();

        // Preparing Expected Value
        List<Map<String , String>> l_expectedOperationsAndValues = new ArrayList<Map<String, String>>();

        Map<String, String> l_expectedCommandTwo = new HashMap<String, String>() {{
            put("arguments", "continentID");
            put("operation", "remove");
        }};
        l_expectedOperationsAndValues.add(l_expectedCommandTwo);

        assertEquals(l_expectedOperationsAndValues, l_actualOperationsAndValues);
    }

    /**
     * Tests the getOperationsAndArguments method with multiple commands.
     * It ensures that each command is parsed correctly and returns the expected operations and arguments.
     */
    @Test
    public void test_multiCommand_getOperationsAndArguments(){
        Command l_command = new Command("editcontinent -add continentID continentValue  -remove continentID");
        List<Map<String , String>> l_actualOperationsAndValues = l_command.getOperationsAndArguments();

        // Preparing Expected Value
        List<Map<String , String>> l_expectedOperationsAndValues = new ArrayList<Map<String, String>>();

        Map<String, String> l_expectedCommandOne = new HashMap<String, String>() {{
            put("arguments", "continentID continentValue");
            put("operation", "add");
        }};
        Map<String, String> l_expectedCommandTwo = new HashMap<String, String>() {{
            put("arguments", "continentID");
            put("operation", "remove");
        }};
        l_expectedOperationsAndValues.add(l_expectedCommandOne);
        l_expectedOperationsAndValues.add(l_expectedCommandTwo);

        assertEquals(l_expectedOperationsAndValues, l_actualOperationsAndValues);
    }

    /**
     * Tests the getOperationsAndArguments method with a command without flags.
     * It ensures that the command is parsed correctly and returns the expected operation and filename argument.
     */
    @Test
    public void test_noFlagCommand_getOperationsAndArguments(){
        Command l_command = new Command("loadmap abc.txt");
        List<Map<String , String>> l_actualOperationsAndValues = l_command.getOperationsAndArguments();

        // Preparing Expected Value
        List<Map<String , String>> l_expectedOperationsAndValues = new ArrayList<Map<String, String>>();

        Map<String, String> l_expectedCommandOne = new HashMap<String, String>() {{
            put("arguments", "abc.txt");
            put("operation", "filename");
        }};
        l_expectedOperationsAndValues.add(l_expectedCommandOne);

        assertEquals(l_expectedOperationsAndValues, l_actualOperationsAndValues);
    }

    /**
     * Tests the getOperationsAndArguments method with a command without flags but containing extra spaces.
     * It ensures that the command is parsed correctly and returns the expected operation and filename argument.
     */
    @Test
    public void test_noFlagCommandWithExtraSpaces_getOperationsAndArguments(){
        Command l_command = new Command("loadmap         abc.txt");
        List<Map<String , String>> l_actualOperationsAndValues = l_command.getOperationsAndArguments();

        // Preparing Expected Value
        List<Map<String , String>> l_expectedOperationsAndValues = new ArrayList<Map<String, String>>();

        Map<String, String> l_expectedCommandOne = new HashMap<String, String>() {{
            put("arguments", "abc.txt");
            put("operation", "filename");
        }};
        l_expectedOperationsAndValues.add(l_expectedCommandOne);

        assertEquals(l_expectedOperationsAndValues, l_actualOperationsAndValues);
    }

}