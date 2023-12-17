package sktech.com.iimtiaz.ui.assignment.codeprovided.gui;

import sktech.com.iimtiaz.ui.assignment.codeprovided.AbstractPlayerCatalog;
import sktech.com.iimtiaz.ui.assignment.codeprovided.PlayerEntry;
import sktech.com.iimtiaz.ui.assignment.codeprovided.PlayerProperty;

import java.util.*;

/**
 * Abstract class providing some functionality to create a radar chart.
 * The radar chart will be drawn, see AbstractRadarChartPanel
 * <p>
 * Should be implemented uk.ac.sheffield.com1003.assignment.gui.RadarChart
 *
 * @author Maria-Cruz Villa-Uriol
 * <p>
 * Copyright (c) University of Sheffield 2023
 * @version 1.0 02/03/2023
 */
public abstract class AbstractRadarChart {
    protected Map<PlayerProperty, RadarAxisValues> radarAxesValues;
    protected final AbstractPlayerCatalog playerCatalog;
    protected List<PlayerEntry> filteredPlayerEntries;
    protected List<PlayerProperty> playerRadarChartProperties;

    /**
     * Constructor. Called by AbstractPlayerDashboardPanel
     *
     * @param playerCatalog              to allow for getting min / max / avg values
     * @param filteredPlayerEntriesList  a List of player entries to generate
     *                                   a radar chart for the selected categories, which have
     *                                   already been filtered by the GUI's queries.
     * @param radarChartPlayerProperties the PlayerProperty to generate a radar chart for.
     */
    public AbstractRadarChart(AbstractPlayerCatalog playerCatalog, List<PlayerEntry> filteredPlayerEntriesList,
                              List<PlayerProperty> radarChartPlayerProperties) {
        this.playerCatalog = playerCatalog;
        this.filteredPlayerEntries = filteredPlayerEntriesList;
        this.playerRadarChartProperties = radarChartPlayerProperties;
        this.radarAxesValues = new HashMap<>();
        updateRadarChartContents(radarChartPlayerProperties, filteredPlayerEntriesList);
    }

    /**
     * This method should completely update (i.e. reset) the radar chart,
     * based on a newly selected category and player entries.
     * Since these values may have changed completely, it is recommended that you generate an entirely new
     * set of RadarAxisValues with the appropriate values according to the properties in each category.
     * PlayerPropertyMap may give you some hints on how to use the Map interface.
     *
     * @param radarChartPlayerProperties the list of PlayerProperty that the radar chart should plot
     * @param filteredPlayerEntriesList  the PlayerEntry that have currently been filtered by the GUI
     */
    public abstract void updateRadarChartContents(List<PlayerProperty> radarChartPlayerProperties,
                                                  List<PlayerEntry> filteredPlayerEntriesList);

    /**
     * @return the list of player entry properties displayed by the radar chart
     */
    public abstract List<PlayerProperty> getPlayerRadarChartProperties() throws NoSuchElementException;

    /**
     * @return the map of player entry properties to RadarAxisValues displayed by this radar chart
     */
    public abstract Map<PlayerProperty, RadarAxisValues> getRadarPlotAxesValues() throws NoSuchElementException;

    /**
     * @return the player catalog displayed by this radar chart
     */
    public abstract AbstractPlayerCatalog getPlayerCatalog();

    /**
     * @return the filteres list of player entries
     */
    public abstract List<PlayerEntry> getFilteredPlayerEntries();

}
