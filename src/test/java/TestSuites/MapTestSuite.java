package TestSuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import Models.MapTest;
import Services.MapServiceTest;

/**
 * MapTestSuite designed for conducting diverse map-related evaluations, encompassing map loading,
 * editing of countries and continents, adjacency checks, map validation processes, and map saving functionality.
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ MapTest.class, MapServiceTest.class })
public class MapTestSuite {
}