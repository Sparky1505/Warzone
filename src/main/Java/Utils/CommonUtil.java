package Utils;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import Constants.ApplicationConstants;
import java.io.Serializable;


/**
 * Utility class providing common methods for various operations.
 */
public class CommonUtil implements Serializable{

    /**
     * Checks if a string is empty (null or contains only whitespace characters).
     *
     * @param p_str The string to check.
     * @return True if the string is empty, false otherwise.
     */
    public static boolean isEmpty(String p_str) {
        return (p_str == null || p_str.trim().isEmpty());
    }

    /**
     * Checks if a string is not empty (not null and contains at least one non-whitespace character).
     *
     * @param p_str The string to check.
     * @return True if the string is not empty, false otherwise.
     */
    public static boolean isNotEmpty(String p_str) {
        return !isEmpty(p_str);
    }

    /**
     * Checks if an object is null.
     *
     * @param p_object The object to check.
     * @return True if the object is null, false otherwise.
     */
    public static boolean isNull(Object p_object) {
        return (p_object == null);
    }

    /**
     * Checks if a collection is empty (null or contains no elements).
     *
     * @param p_collection The collection to check.
     * @return True if the collection is empty, false otherwise.
     */
    public static boolean isCollectionEmpty(Collection<?> p_collection) {
        return (p_collection == null || p_collection.isEmpty());
    }

    /**
     * Checks if a map is empty (null or contains no key-value mappings).
     *
     * @param p_map The map to check.
     * @return True if the map is empty, false otherwise.
     */
    public static boolean isMapEmpty(Map<?, ?> p_map) {
        return (p_map == null || p_map.isEmpty());
    }

    /**
     * Constructs the absolute file path given a file name.
     *
     * @param p_fileName The name of the file.
     * @return The absolute file path.
     */
    public static String getMapFilePath(String p_fileName) {
        String l_absolutePath = new File("").getAbsolutePath();
        return l_absolutePath + File.separator + ApplicationConstants.SRC_MAIN_RESOURCES + File.separator + p_fileName +ApplicationConstants.MAPFILEEXTENSION;
    }
}
