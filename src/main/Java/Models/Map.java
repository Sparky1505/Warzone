package Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import Exceptions.InvalidMap;
import Utils.CommonUtil;
import java.util.Collections;
import java.util.Map.Entry;
import java.io.Serializable;
/**
 * Represents a map consisting of continents and countries.
 */
public class Map implements Serializable{

    /**
     * File path of the map.
     */
    String d_mapFilePath;

    /**
     * List of continents in the map.
     */
    List<Continent> d_continents;

    /**
     * List of countries in the map.
     */
    List<Country> d_countries;

    /**
     * Mapping of country IDs to their reachability status.
     */
    HashMap<Integer, Boolean> d_countryReach = new HashMap<Integer, Boolean>();

    /**
     * Retrieves the file path of the map.
     * @return The file path of the map.
     */
    public String getD_mapFile() {
        return d_mapFilePath;
    }

    /**
     * Sets the file path of the map.
     * @param p_mapFile The file path of the map.
     */
    public void setD_mapFile(String p_mapFile) {
        this.d_mapFilePath = p_mapFile;
    }

    /**
     * Retrieves the list of continents.
     * @return The list of continents.
     */
    public List<Continent> getD_continents() {
        return d_continents;
    }

    /**
     * Sets the list of continents.
     * @param p_continents The list of continents.
     */
    public void setD_continents(List<Continent> p_continents) {
        this.d_continents = p_continents;
    }

    /**
     * Retrieves the list of countries.
     * @return The list of countries.
     */
    public List<Country> getD_countries() {
        return d_countries;
    }

    /**
     * Sets the list of countries.
     * @param p_countries The list of countries.
     */
    public void setD_countries(List<Country> p_countries) {
        this.d_countries = p_countries;
    }

    /**
     * Adds a continent to the map.
     * @param p_continent The continent to add.
     */
    public void createContinent(Continent p_continent){
        d_continents.add(p_continent);
    }

    /**
     * Adds a country to the map.
     * @param p_country The country to add.
     */
    public void createCountry(Country p_country){
        d_countries.add(p_country);
    }

    /**
     * Retrieves the IDs of all continents.
     * @return The list of continent IDs.
     */
    public List<Integer> getContinentIDs(){
        List<Integer> l_continentIDs = new ArrayList<Integer>();
        if (!d_continents.isEmpty()) {
            for(Continent c: d_continents){
                l_continentIDs.add(c.getD_continentID());
            }
        }
        return l_continentIDs;
    }

    /**
     * Retrieves the IDs of all countries.
     * @return The list of country IDs.
     */
    public List<Integer> getCountryIDs(){
        List<Integer> l_countryIDs = new ArrayList<Integer>();
        if(!d_countries.isEmpty()){
            for(Country c: d_countries){
                l_countryIDs.add(c.getD_countryId());
            }
        }
        return l_countryIDs;
    }

    /**
     * Validates the continents by printing their IDs.
     */
    public void validateContinents() {
        for(Continent c: d_continents) {
            System.out.println(c.getD_continentID());
        }
    }

    /**
     * Validates the countries by printing their IDs, continent IDs, and neighbors.
     */
    public void validateCountries() {
        for (Country c: d_countries) {
            System.out.println("Country Id "+ c.getD_countryId());
            System.out.println("Continent Id "+c.getD_continentId());
            System.out.println("Neighbours:");
            for (int i: c.getD_adjacentCountryIds()) {
                System.out.println(i);
            }
        }
    }

    /**
     * Validates the map by checking for null objects, continent connectivity, and country connectivity.
     * @return True if the map is valid, false otherwise.
     * @throws InvalidMap if the map is invalid.
     */
    public Boolean Validate() throws InvalidMap {
        return (!checkForNullObjects() && validateContinentConnectivity() && validateCountryConnectivity());
    }

    /**
     * Checks for null objects in the map.
     * @return True if there are no null objects, false otherwise.
     * @throws InvalidMap if there are null objects in the map.
     */
    public Boolean checkForNullObjects() throws InvalidMap{
        if(d_continents==null || d_continents.isEmpty()){
            throw new InvalidMap("Map must possess atleast one continent!");
        }
        if(d_countries==null || d_countries.isEmpty()){
            throw new InvalidMap("Map must possess atleast one country!");
        }
        for(Country c: d_countries){
            if(c.getD_adjacentCountryIds().size()<1){
                throw new InvalidMap(c.getD_countryName()+" does not possess any neighbour, hence isn't reachable!");
            }
        }
        return false;
    }

    /**
     * Validates the connectivity of continents.
     * @return True if all continents are connected, false otherwise.
     * @throws InvalidMap if any continent is not connected.
     */
    public Boolean validateContinentConnectivity() throws InvalidMap {
        boolean l_flagConnectivity=true;
        for (Continent c:d_continents){
            if (null == c.getD_countries() || c.getD_countries().size()<1){
                throw new InvalidMap(c.getD_continentName() + " has no countries, it must possess atleast 1 country");
            }
            if(!subGraphConnectivity(c)){
                l_flagConnectivity=false;
            }
        }
        return l_flagConnectivity;
    }

    /**
     * Validates the connectivity of a subgraph within a continent.
     * @param p_continent The continent to validate.
     * @return True if the subgraph is connected, false otherwise.
     * @throws InvalidMap if any country within the continent is not reachable.
     */
    public boolean subGraphConnectivity(Continent p_continent) throws InvalidMap {
        HashMap<Integer, Boolean> l_continentCountry = new HashMap<Integer, Boolean>();

        for (Country c : p_continent.getD_countries()) {
            l_continentCountry.put(c.getD_countryId(), false);
        }
        dfsSubgraph(p_continent.getD_countries().get(0), l_continentCountry, p_continent);

        // Iterates Over Entries to locate unreachable countries in continent
        for (Entry<Integer, Boolean> entry : l_continentCountry.entrySet()) {
            if (!entry.getValue()) {
                Country l_country = getCountry(entry.getKey());
                String l_messageException = l_country.getD_countryName() + " in Continent " + p_continent.getD_continentName() + " is not reachable";
                throw new InvalidMap(l_messageException);
            }
        }
        return !l_continentCountry.containsValue(false);
    }

    /**
     * Depth-first search to check the connectivity of a subgraph.
     * @param p_c The starting country for the search.
     * @param p_continentCountry Mapping of country IDs to their reachability status.
     * @param p_continent The continent containing the subgraph.
     */
    public void dfsSubgraph(Country p_c, HashMap<Integer, Boolean> p_continentCountry, Continent p_continent) {
        p_continentCountry.put(p_c.getD_countryId(), true);
        for (Country c : p_continent.getD_countries()) {
            if (p_c.getD_adjacentCountryIds().contains(c.getD_countryId())) {
                if (!p_continentCountry.get(c.getD_countryId())) {
                    dfsSubgraph(c, p_continentCountry, p_continent);
                }
            }
        }
    }

    /**
     * Performs a depth-first search (DFS) to determine the connectivity of countries.
     * Marks the countries as reachable in the d_countryReach map.
     * @param p_c The country to start the DFS from.
     * @throws InvalidMap if a country is not reachable.
     */
    public void dfsCountry(Country p_c) throws InvalidMap {
        d_countryReach.put(p_c.getD_countryId(), true);
        for (Country l_nextCountry : getAdjacentCountry(p_c)) {
            if (!d_countryReach.get(l_nextCountry.getD_countryId())) {
                dfsCountry(l_nextCountry);
            }
        }
    }

    /**
     * Validates the connectivity of countries within the map.
     * @return True if all countries are reachable from each other, false otherwise.
     * @throws InvalidMap if any country is not reachable.
     */
    public boolean validateCountryConnectivity() throws InvalidMap {
        for (Country c : d_countries) {
            d_countryReach.put(c.getD_countryId(), false);
        }
        dfsCountry(d_countries.get(0));

        // Iterates over entries to locate the unreachable country
        for (Entry<Integer, Boolean> entry : d_countryReach.entrySet()) {
            if (!entry.getValue()) {
                String l_exceptionMessage = getCountry(entry.getKey()).getD_countryName() + " country is not reachable";
                throw new InvalidMap(l_exceptionMessage);
            }
        }
        return !d_countryReach.containsValue(false);
    }

    /**
     * Retrieves the list of countries adjacent to the given country.
     * @param p_country The country for which adjacent countries are retrieved.
     * @return List of adjacent countries.
     * @throws InvalidMap if the given country does not have any adjacent countries.
     */
    public List<Country> getAdjacentCountry(Country p_country) throws InvalidMap {
        List<Country> l_adjCountries = new ArrayList<Country>();

        if (p_country.getD_adjacentCountryIds().size() > 0) {
            for (int i : p_country.getD_adjacentCountryIds()) {
                l_adjCountries.add(getCountry(i));
            }
        } else {
            throw new InvalidMap(p_country.getD_countryName() + " doesn't have any adjacent countries");
        }
        return l_adjCountries;
    }

    /**
     * Retrieves the country object based on its unique ID.
     * @param p_countryId The ID of the country to retrieve.
     * @return The country object corresponding to the given ID, or null if not found.
     */
    public Country getCountry(Integer p_countryId) {
        return d_countries.stream().filter(l_country -> l_country.getD_countryId().equals(p_countryId)).findFirst().orElse(null);
    }

    /**
     * Retrieves the country object based on its name.
     * @param p_countryName The name of the country to retrieve.
     * @return The country object corresponding to the given name, or null if not found.
     */
    public Country getCountryByName(String p_countryName){
        return d_countries.stream().filter(l_country -> l_country.getD_countryName().equals(p_countryName)).findFirst().orElse(null);
    }

    /**
     * Retrieves the continent object based on its name.
     * @param p_continentName The name of the continent to retrieve.
     * @return The continent object corresponding to the given name, or null if not found.
     */
    public Continent getContinent(String p_continentName){
        return d_continents.stream().filter(l_continent -> l_continent.getD_continentName().equals(p_continentName)).findFirst().orElse(null);
    }

    /**
     * Retrieves the continent object based on its ID.
     * @param p_continentID The ID of the continent to retrieve.
     * @return The continent object corresponding to the given ID, or null if not found.
     */
    public Continent getContinentByID(Integer p_continentID){
        return d_continents.stream().filter(l_continent -> l_continent.getD_continentID().equals(p_continentID)).findFirst().orElse(null);
    }
    /**
     * Retrieves the Country Object based on its country ID.
     *
     * @param p_countryID The Id of the Country to be found
     * @return The country object corresponding to the given Id, or null if not found.
     */
    public Country getCountryByID(Integer p_countryID){
        return d_countries.stream().filter(l_country -> l_country.getD_countryId().equals(p_countryID)).findFirst().orElse(null);
    }

    /**
     * Creates a new continent with the specified name and control value.
     * @param p_continentName The name of the new continent.
     * @param p_controlValue The control value of the new continent.
     * @throws InvalidMap if the continent already exists.
     */
    public void createContinent(String p_continentName, Integer p_controlValue) throws InvalidMap{
        int l_continentId;

        if (d_continents!=null) {
            l_continentId=d_continents.size()>0?Collections.max(getContinentIDs())+1:1;
            if(CommonUtil.isNull(getContinent(p_continentName))){
                d_continents.add(new Continent(l_continentId, p_continentName, p_controlValue));
            }else{
                throw new InvalidMap("Continent cannot be added! It already exists!");
            }
        }else{
            d_continents= new ArrayList<Continent>();
            d_continents.add(new Continent(1, p_continentName, p_controlValue));
        }
    }

    /**
     * Deletes the continent with the specified name from the map.
     * @param p_continentName The name of the continent to delete.
     * @throws InvalidMap if the continent does not exist.
     */
    public void deleteContinent(String p_continentName) throws InvalidMap{
        if (d_continents!=null) {
            if(!CommonUtil.isNull(getContinent(p_continentName))){

                // Deletes the continent and updates neighbour as well as country objects
                if (getContinent(p_continentName).getD_countries()!=null) {
                    for(Country c: getContinent(p_continentName).getD_countries()){
                        deleteCountryNeighbours(c.getD_countryId());
                        updateNeighboursCont(c.getD_countryId());
                        d_countries.remove(c);
                    }
                }
                d_continents.remove(getContinent(p_continentName));
            }else{
                throw new InvalidMap("No such Continent exists!");
            }
        } else{
            throw new InvalidMap("No continents in the Map to remove!");
        }
    }

    /**
     * Creates a new country with the specified name and adds it to the specified continent.
     * @param p_countryName The name of the new country.
     * @param p_continentName The name of the continent to which the country belongs.
     * @throws InvalidMap if the country already exists or the continent does not exist.
     */
    public void createCountry(String p_countryName, String p_continentName) throws InvalidMap{
        int l_countryId;
        if(d_countries==null){
            d_countries= new ArrayList<Country>();
        }
        if(CommonUtil.isNull(getCountryByName(p_countryName))){
            l_countryId=d_countries.size()>0? Collections.max(getCountryIDs())+1:1;
            if(d_continents!=null && getContinentIDs().contains(getContinent(p_continentName).getD_continentID())){
                Country l_country= new Country(l_countryId, p_countryName, getContinent(p_continentName).getD_continentID());
                d_countries.add(l_country);
                for (Continent c: d_continents) {
                    if (c.getD_continentName().equals(p_continentName)) {
                        c.addCountry(l_country);
                    }
                }
            } else{
                throw new InvalidMap("Cannot add Country to a Continent that doesn't exist!");
            }
        }else{
            throw new InvalidMap("Country with name "+ p_countryName+" already Exists!");
        }
    }

    /**
     * Deletes the country with the specified name from the map.
     *
     * @param p_countryName The name of the country to delete.
     * @throws InvalidMap if the country does not exist.
     */
    public void deleteCountry(String p_countryName) throws InvalidMap{
        if(d_countries!=null && !CommonUtil.isNull(getCountryByName(p_countryName))) {
            for(Continent c: d_continents){
                if(c.getD_continentID().equals(getCountryByName(p_countryName).getD_continentId())){
                    c.deleteCountry(getCountryByName(p_countryName));
                }
                c.deleteCountryNeighbours(getCountryByName(p_countryName).getD_countryId());
            }
            deleteCountryNeighbours(getCountryByName(p_countryName).getD_countryId());
            d_countries.remove(getCountryByName(p_countryName));

        }else{
            throw new InvalidMap("Country:  "+ p_countryName+" does not exist!");
        }
    }

    /**
     * Adds a neighbouring country to the specified country.
     *
     * @param p_countryName     The name of the country to which the neighbour will be added.
     * @param p_neighbourName   The name of the neighbouring country to add.
     * @throws InvalidMap       if either of the countries does not exist.
     */
    public void addCountryNeighbour(String p_countryName, String p_neighbourName) throws InvalidMap{
        if(d_countries!=null){
            if(!CommonUtil.isNull(getCountryByName(p_countryName)) && !CommonUtil.isNull(getCountryByName(p_neighbourName))){
                d_countries.get(d_countries.indexOf(getCountryByName(p_countryName))).addNeighbour(getCountryByName(p_neighbourName).getD_countryId());
            } else{
                throw new InvalidMap("Invalid Neighbour Pair! Either of the Countries Doesn't exist!");
            }
        }
    }

    /**
     * Deletes all neighbours of the specified country.
     *
     * @param p_countryID       The ID of the country whose neighbours will be deleted.
     */
    public void deleteCountryNeighbours(Integer p_countryID) throws InvalidMap {
        for (Country c: d_countries) {
            if (!CommonUtil.isNull(c.getD_adjacentCountryIds())) {
                if (c.getD_adjacentCountryIds().contains(p_countryID)) {
                    c.deleteNeighbour(p_countryID);
                }
            }
        }
    }

    /**
     * Deletes the specified neighbour from the country.
     *
     * @param p_countryName     The name of the country from which the neighbour will be deleted.
     * @param p_neighbourName   The name of the neighbour country to delete.
     * @throws InvalidMap       if either of the countries does not exist.
     */
    public void deleteCountryNeighbour(String p_countryName, String p_neighbourName) throws InvalidMap{
        if(d_countries!=null){
            if(!CommonUtil.isNull(getCountryByName(p_countryName)) && !CommonUtil.isNull(getCountryByName(p_neighbourName))) {
                d_countries.get(d_countries.indexOf(getCountryByName(p_countryName))).deleteNeighbour(getCountryByName(p_neighbourName).getD_countryId());
            } else{
                throw new InvalidMap("Invalid Neighbour Pair! Either of the Countries Doesn't exist!");
            }
        }
    }

    /**
     * Updates the neighbours of all continents by removing the specified country from their neighbour lists.
     *
     * @param p_countryId   The ID of the country whose neighbours will be updated.
     */
    public void updateNeighboursCont(Integer p_countryId) throws InvalidMap {
        for(Continent c: d_continents){
            c.deleteCountryNeighbours(p_countryId);
        }
    }

}
