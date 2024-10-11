package Models;

import Exceptions.InvalidMap;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * Represents a country with its attributes and operations.
 */
public class  Country implements Serializable {

    /** The number of armies in the country. */
    Integer d_armies;

    /** The ID of the country. */
    Integer d_countryId;

    /** The ID of the continent to which the country belongs. */
    Integer d_continentId;

    /** The name of the country. */
    String d_countryName;

    /** The list of IDs of adjacent countries. */
    List<Integer> d_adjacentCountryIds = new ArrayList<Integer>();

    /**
     * Constructs a new country with the specified ID, name, and continent ID.
     * @param p_countryId The ID of the country.
     * @param p_countryName The name of the country.
     * @param p_continentId The ID of the continent to which the country belongs.
     */
    public Country(int p_countryId, String p_countryName, int p_continentId) {
        d_countryId = p_countryId;
        d_countryName = p_countryName;
        d_continentId = p_continentId;
        d_adjacentCountryIds = new ArrayList<>();
        d_armies = 0;
    }

    /**
     * Constructs a new country with the specified ID and continent ID.
     * @param p_countryId The ID of the country.
     * @param p_continentId The ID of the continent to which the country belongs.
     */
    public Country(int p_countryId, int p_continentId) {
        d_countryId = p_countryId;
        d_continentId = p_continentId;
    }

    /**
     * Constructs a new country with the specified name.
     * @param p_countryName The name of the country.
     */
    public Country(String p_countryName) {
        d_countryName = p_countryName;
    }

    /**
     * Retrieves the number of armies in the country.
     * @return The number of armies in the country.
     */
    public Integer getD_armies() {
        return d_armies;
    }

    /**
     * Sets the number of armies in the country.
     * @param p_armies The number of armies in the country.
     */
    public void setD_armies(Integer p_armies) {
        this.d_armies = p_armies;
    }

    /**
     * Retrieves the ID of the country.
     * @return The ID of the country.
     */
    public Integer getD_countryId() {
        return d_countryId;
    }

    /**
     * Sets the ID of the country.
     * @param p_countryId The ID of the country.
     */
    public void setD_countryId(Integer p_countryId) {
        this.d_countryId = p_countryId;
    }

    /**
     * Retrieves the ID of the continent to which the country belongs.
     * @return The ID of the continent to which the country belongs.
     */
    public Integer getD_continentId() {
        return d_continentId;
    }

    /**
     * Sets the ID of the continent to which the country belongs.
     * @param p_continentId The ID of the continent to which the country belongs.
     */
    public void setD_continentId(Integer p_continentId) {
        this.d_continentId = p_continentId;
    }

    /**
     * Retrieves the list of IDs of adjacent countries.
     * If the list is null, it initializes a new empty list.
     * @return The list of IDs of adjacent countries.
     */
    public List<Integer> getD_adjacentCountryIds() {
        if (d_adjacentCountryIds == null) {
            d_adjacentCountryIds = new ArrayList<Integer>();
        }
        return d_adjacentCountryIds;
    }

    /**
     * Sets the list of IDs of adjacent countries.
     * @param p_adjacentCountryIds The list of IDs of adjacent countries.
     */
    public void setD_adjacentCountryIds(List<Integer> p_adjacentCountryIds) {
        this.d_adjacentCountryIds = p_adjacentCountryIds;
    }

    /**
     * Retrieves the name of the country.
     * @return The name of the country.
     */
    public String getD_countryName() {
        return d_countryName;
    }

    /**
     * Sets the name of the country.
     * @param p_countryName The name of the country.
     */
    public void setD_countryName(String p_countryName) {
        this.d_countryName = p_countryName;
    }

    /**
     * Adds a neighboring country with the specified ID to the list of adjacent countries.
     * @param p_countryId The ID of the neighboring country to add.
     */
    public void addNeighbour(Integer p_countryId) {
        if (!d_adjacentCountryIds.contains(p_countryId)) {
            d_adjacentCountryIds.add(p_countryId);
        }
    }

    /**
     * Deletes the neighboring relationship with the country of the specified ID.
     * @param p_countryId The ID of the neighboring country to delete.
     */
    public void deleteNeighbour(Integer p_countryId) throws InvalidMap{
        if (d_adjacentCountryIds.contains(p_countryId)) {
            d_adjacentCountryIds.remove(d_adjacentCountryIds.indexOf(p_countryId));
        } else {
            throw new InvalidMap("No Such Neighbor Exists");
        }
    }
}
