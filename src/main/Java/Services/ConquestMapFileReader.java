package Services;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import Constants.ApplicationConstants;
import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Map;


/**
 * A class responsible for reading Conquest map files and creating corresponding map objects.
 */
public class ConquestMapFileReader implements Serializable {

    /**
     * Retrieves metadata lines from the given list of file lines based on the specified switch parameter.
     *
     * @param p_fileLines      The list of lines read from the Conquest map file.
     * @param p_switchParameter The parameter specifying the type of metadata to retrieve ("continent" or "country").
     * @return A list containing the metadata lines based on the specified switch parameter.
     */
    public List<String> getMetaData(List<String> p_fileLines, String p_switchParameter) {
        switch (p_switchParameter) {
            case "continent":
                List<String> l_continentLines = p_fileLines.subList(
                        p_fileLines.indexOf(ApplicationConstants.CONQUEST_CONTINENTS) + 1,
                        p_fileLines.indexOf(ApplicationConstants.CONQUEST_TERRITORIES) - 1);
                return l_continentLines;
            case "country":
                List<String> l_countryLines = p_fileLines
                        .subList(p_fileLines.indexOf(ApplicationConstants.CONQUEST_TERRITORIES) + 1, p_fileLines.size());
                return l_countryLines;
            default:
                return null;
        }
    }

    /**
     * Parses the metadata lines representing continents and creates Continent objects accordingly.
     *
     * @param p_continentList The list of metadata lines representing continents.
     * @return A list containing Continent objects parsed from the metadata lines.
     */
    public List<Continent> parseContinentsMetaData(List<String> p_continentList) {
        int l_continentId = 1;
        List<Continent> l_continents = new ArrayList<Continent>();

        for (String cont : p_continentList) {
            String[] l_metaData = cont.split("=");
            l_continents.add(new Continent(l_continentId, l_metaData[0], Integer.parseInt(l_metaData[1])));
            l_continentId++;
        }
        return l_continents;
    }

    /**
     * Reads the Conquest map file and populates the game state and map objects accordingly.
     *
     * @param p_gameState      The game state object to be populated with map data.
     * @param p_map            The map object to be populated with continents and countries.
     * @param p_linesOfFile    The list of lines read from the Conquest map file.
     */
    public void readConquestFile(GameState p_gameState, Map p_map, List<String> p_linesOfFile) {
        List<String> l_continentData = getMetaData(p_linesOfFile, "continent");
        List<Continent> l_continentObjects = parseContinentsMetaData(l_continentData);
        List<String> l_countryData = getMetaData(p_linesOfFile, "country");
        List<Country> l_countryObjects = parseCountriesMetaData(l_countryData, l_continentObjects);
        List<Country> l_updatedCountries = parseBorderMetaData(l_countryObjects, l_countryData);

        l_continentObjects = linkCountryContinents(l_updatedCountries, l_continentObjects);
        p_map.setD_continents(l_continentObjects);
        p_map.setD_countries(l_countryObjects);
        p_gameState.setD_map(p_map);
    }

    /**
     * Parses the border metadata lines and updates the country objects with neighboring country information.
     *
     * @param p_countriesList  The list of country objects to be updated.
     * @param p_countryLines   The list of metadata lines representing countries and their borders.
     * @return A list containing updated country objects with neighboring country information.
     */
    public List<Country> parseBorderMetaData(List<Country> p_countriesList, List<String> p_countryLines) {
        List<Country> l_updatedCountryList = new ArrayList<>(p_countriesList);
        String l_matchedCountry = null;
        for (Country l_cont : l_updatedCountryList) {
            for (String l_contStr : p_countryLines) {
                if ((l_contStr.split(",")[0]).equalsIgnoreCase(l_cont.getD_countryName())) {
                    l_matchedCountry = l_contStr;
                    break;
                }
            }
            if (l_matchedCountry.split(",").length > 4) {
                for (int i = 4; i < l_matchedCountry.split(",").length; i++) {
                    Country l_country = this.getCountryByName(p_countriesList, l_matchedCountry.split(",")[i]);
                    l_cont.getD_adjacentCountryIds().add(l_country.getD_countryId());
                }
            }
        }
        return l_updatedCountryList;
    }

    /**
     * Links countries to their respective continents based on continent IDs.
     *
     * @param p_countries   The list of country objects to be linked to continents.
     * @param p_continents  The list of continent objects representing continents.
     * @return A list of continent objects with countries linked to them.
     */
    public List<Continent> linkCountryContinents(List<Country> p_countries, List<Continent> p_continents) {
        for (Country c : p_countries) {
            for (Continent cont : p_continents) {
                if (cont.getD_continentID().equals(c.getD_continentId())) {
                    cont.addCountry(c);
                }
            }
        }
        return p_continents;
    }

    /**
     * Retrieves the continent object based on the continent name.
     *
     * @param p_continentList   The list of continent objects to search from.
     * @param p_continentName   The name of the continent to search for.
     * @return The continent object with the specified name.
     */
    public Continent getContinentByName(List<Continent> p_continentList, String p_continentName) {
        Continent l_continent = p_continentList.stream().filter(l_cont -> l_cont.getD_continentName().equalsIgnoreCase(p_continentName))
                .findFirst().orElse(null);
        return l_continent;
    }

    /**
     * Parses the metadata lines representing countries and creates Country objects accordingly.
     *
     * @param p_countriesList   The list of metadata lines representing countries.
     * @param p_continentList   The list of continent objects representing continents.
     * @return A list containing Country objects parsed from the metadata lines.
     */
    public List<Country> parseCountriesMetaData(List<String> p_countriesList, List<Continent> p_continentList) {
        List<Country> l_countriesList = new ArrayList<Country>();
        int l_country_id = 1;
        for (String country : p_countriesList) {
            String[] l_metaDataCountries = country.split(",");
            Continent l_continent = this.getContinentByName(p_continentList, l_metaDataCountries[3]);
            Country l_countryObj = new Country(l_country_id, l_metaDataCountries[0],
                    l_continent.getD_continentID());
            l_countriesList.add(l_countryObj);
            l_country_id++;
        }
        return l_countriesList;
    }

    /**
     * Retrieves the country object based on the country name.
     *
     * @param p_countrytList   The list of country objects to search from.
     * @param p_countryName    The name of the country to search for.
     * @return The country object with the specified name.
     */
    public Country getCountryByName(List<Country> p_countrytList, String p_countryName) {
        Country l_country = p_countrytList.stream().filter(l_cont -> l_cont.getD_countryName().equalsIgnoreCase(p_countryName))
                .findFirst().orElse(null);
        return l_country;
    }
}
