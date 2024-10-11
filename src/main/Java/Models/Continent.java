package Models;

import Exceptions.InvalidMap;
import java.util.ArrayList;
import java.util.List;
import Utils.CommonUtil;
import java.io.Serializable;

/**
 * Represents a continent with its attributes and operations.
 */
public class Continent implements Serializable {

    /** The ID of the continent. */
    Integer d_continentID;

    /** The name of the continent. */
    String d_continentName;

    /** The value of the continent. */
    Integer d_continentValue;

    /** The list of countries within the continent. */
    List<Country> d_countries;

    /**
     * Constructs a new continent with the specified ID, name, and value.
     * @param p_continentID The ID of the continent.
     * @param p_continentName The name of the continent.
     * @param p_continentValue The value of the continent.
     */
    public Continent(Integer p_continentID, String p_continentName, int p_continentValue) {
        this.d_continentID = p_continentID;
        this.d_continentName = p_continentName;
        this.d_continentValue = p_continentValue;
    }

    /**
     * Constructs a new continent with default values.
     */
    public Continent() {

    }

    /**
     * Constructs a new continent with the specified name.
     * @param p_continentName The name of the continent.
     */
    public Continent(String p_continentName) {
        this.d_continentName = p_continentName;
    }

    /**
     * Retrieves the ID of the continent.
     * @return The ID of the continent.
     */
    public Integer getD_continentID() {
        return d_continentID;
    }

    /**
     * Sets the ID of the continent.
     * @param p_continentID The ID of the continent.
     */
    public void setD_continentID(Integer p_continentID) {
        this.d_continentID = p_continentID;
    }

    /**
     * Retrieves the name of the continent.
     * @return The name of the continent.
     */
    public String getD_continentName() {
        return d_continentName;
    }

    /**
     * Sets the name of the continent.
     * @param p_continentName The name of the continent.
     */
    public void setD_continentName(String p_continentName) {
        this.d_continentName = p_continentName;
    }

    /**
     * Retrieves the value of the continent.
     * @return The value of the continent.
     */
    public Integer getD_continentValue() {
        return d_continentValue;
    }

    /**
     * Sets the value of the continent.
     * @param p_continentValue The value of the continent.
     */
    public void setD_continentValue(Integer p_continentValue) {
        this.d_continentValue = p_continentValue;
    }

    /**
     * Retrieves the list of countries within the continent.
     * @return The list of countries within the continent.
     */
    public List<Country> getD_countries() {
        return d_countries;
    }

    /**
     * Sets the list of countries within the continent.
     * @param p_countries The list of countries within the continent.
     */
    public void setD_countries(List<Country> p_countries) {
        this.d_countries = p_countries;
    }

    /**
     * Adds a country to the continent.
     * @param p_country The country to add.
     */
    public void addCountry(Country p_country){
        if (d_countries!=null){
            d_countries.add(p_country);
        }
        else{
            d_countries=new ArrayList<>();
            d_countries.add(p_country);
        }
    }

    /**
     * Deletes a country from the continent.
     * @param p_country The country to delete.
     */
    public void deleteCountry(Country p_country) throws InvalidMap{
        if(d_countries==null){
            System.out.println("No such Country Exists");
        }else {
            d_countries.remove(p_country);
        }
    }

    /**
     * Deletes the neighboring relationship of a country with the specified ID within the continent.
     * @param p_countryId The ID of the country whose neighbors are to be deleted.
     */
    public void deleteCountryNeighbours(Integer p_countryId) throws InvalidMap{
        if (null!=d_countries && !d_countries.isEmpty()) {
            for (Country c: d_countries){
                if (!CommonUtil.isNull(c.d_adjacentCountryIds)) {
                    if (c.getD_adjacentCountryIds().contains(p_countryId)){
                        c.deleteNeighbour(p_countryId);
                    }
                }
            }
        }
    }
}
