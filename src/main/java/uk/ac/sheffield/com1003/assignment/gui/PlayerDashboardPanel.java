package uk.ac.sheffield.com1003.assignment.gui;

import uk.ac.sheffield.com1003.assignment.codeprovided.*;
import uk.ac.sheffield.com1003.assignment.codeprovided.gui.AbstractPlayerDashboardPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SKELETON IMPLEMENTATION
 */

public class PlayerDashboardPanel extends AbstractPlayerDashboardPanel {

    public PlayerDashboardPanel(AbstractPlayerCatalog playerCatalog) {
        super(playerCatalog);
    }

    @Override
    public void populatePlayerDetailsComboBoxes() {
        List<PlayerEntry> allPlayerNames = playerCatalog.getPlayerEntriesList(League.fromName(comboLeagueTypes.getSelectedItem().toString()));
        playerNamesList.clear();
        nationList.clear();
        positionList.clear();
        teamList.clear();
        Set<String> positionSet = new HashSet<>();
        Set<String> nationSet = new HashSet<>();
        Set<String> teamSet = new HashSet<>();
        for (PlayerEntry entry : allPlayerNames) {
            positionSet.add(entry.getPosition());
            nationSet.add(entry.getNation());
            teamSet.add(entry.getTeam());
            playerNamesList.add(entry.getPlayerName());
        }
        nationList = new ArrayList<>(nationSet);
        teamList = new ArrayList<>(teamSet);
        positionList = new ArrayList<>(positionSet);
        playerNamesList.add(0, "");
        nationList.add(0, "");
        positionList.add(0, "");
        teamList.add(0, "");
        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) comboPlayerNames.getModel();
        DefaultComboBoxModel<String> nationModel = (DefaultComboBoxModel<String>) comboNations.getModel();
        DefaultComboBoxModel<String> positionModel = (DefaultComboBoxModel<String>) comboPositions.getModel();
        DefaultComboBoxModel<String> teamModel = (DefaultComboBoxModel<String>) comboTeams.getModel();
        model.removeAllElements();
        model.addAll(playerNamesList);
        nationModel.removeAllElements();
        nationModel.addAll(nationList);
        positionModel.removeAllElements();
        positionModel.addAll(positionList);
        teamModel.removeAllElements();
        teamModel.addAll(teamList);
        executeQuery();
    }

    /**
     * addListeners method - adds relevant actionListeners to the GUI components
     */
    @SuppressWarnings("unused")
    @Override
    public void addListeners() {
        // TODO implement
        ActionListener updateDetailsCombo = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                populatePlayerDetailsComboBoxes();
            }
        };
        ActionListener recalculateList = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Modifiers");
                System.out.println(e.getActionCommand());
                executeQuery();
            }
        };
        ActionListener clearSubQueries = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFilters();
            }
        };
        ActionListener addSubQueries = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFilter();
            }
        };

        ActionListener radarCategoryChange = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRadarChart();
            }
        };
        comboLeagueTypes.addActionListener(updateDetailsCombo);
        comboPositions.addActionListener(recalculateList);
        comboNations.addActionListener(recalculateList);
        comboTeams.addActionListener(recalculateList);
        comboPlayerNames.addActionListener(recalculateList);
        buttonClearFilters.addActionListener(clearSubQueries);
        buttonAddFilter.addActionListener(addSubQueries);
        minCheckBox.addActionListener(radarCategoryChange);
        maxCheckBox.addActionListener(radarCategoryChange);
        averageCheckBox.addActionListener(radarCategoryChange);
        comboRadarChartCategories.addActionListener(radarCategoryChange);
    }

    /**
     * clearFilters method - clears all filters from the subQueryList ArrayList and updates
     * the relevant GUI components
     */
    @Override
    public void clearFilters() {
        subQueryList.clear();
        subQueriesTextArea.setText("");
        executeQuery();
    }

    @Override
    public void updateRadarChart() {
        radarChart.updateRadarChartContents(List.of(Category.getCategoryFromName(comboRadarChartCategories.getSelectedItem().toString()).getProperties()), filteredPlayerEntriesList);
        repaint();
    }

    /**
     * updateStats method - updates the table with statistics after any changes which may
     * affect the JTable which holds the statistics
     */
    @Override
    public void updateStatistics() {
        StringBuilder lines = new StringBuilder();
        List<String> formatList = new ArrayList<>(Arrays.asList("%-14s", "%-7s"));
        List<String> titles = new ArrayList<>(Arrays.asList("League Type", "ID"));
        for (PlayerDetail playerDetail : PlayerDetail.values()) {
            StringBuilder format = new StringBuilder();
            String detailName = playerDetail.getName();
            titles.add(detailName);
            if (playerDetail.equals(PlayerDetail.PLAYER)) formatList.add("%-34s");
            else formatList.add(format.append("%-").append(detailName.length() + 3).append("s").toString());
        }
        for (PlayerProperty playerProperty : PlayerProperty.values()) {
            StringBuilder format = new StringBuilder();
            String propertyName = playerProperty.getName();
            titles.add(propertyName);
            format.append("%-").append(propertyName.length() + 3).append("s");
            formatList.add(format.toString());
        }
        lines.append(valuesToLine(titles, formatList));
        for (PlayerEntry playerEntry : filteredPlayerEntriesList) {
            List<String> playerInfoList = new ArrayList<>();
            playerInfoList.add(playerEntry.getLeagueType().toString());
            playerInfoList.add(String.valueOf(playerEntry.getId()));
            for (PlayerDetail playerDetail : PlayerDetail.values()) {
                playerInfoList.add(playerEntry.getDetail(playerDetail));
            }
            for (PlayerProperty playerProperty : PlayerProperty.values()) {
                playerInfoList.add(String.valueOf(playerEntry.getProperty(playerProperty)));
            }
            lines.append(valuesToLine(playerInfoList, formatList));
        }
        filteredPlayerEntriesTextArea.setText(lines.toString());
        filteredPlayerEntriesTextArea.setCaretPosition(0);
//        //Used monospaced font to fix alignments on Area - 2
        filteredPlayerEntriesTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
//        filteredPlayerEntriesTextArea.setColumns(100);

    }

    /**
     * updatePlayerCatalogDetailsBox method - updates the list of players when changes are made
     */
    @Override
    public void updatePlayerCatalogDetailsBox() {
        StringBuilder lines = new StringBuilder();
        List<String> propertyNames = new ArrayList<>();
        List<String> maxValues = new ArrayList<>();
        List<String> minValues = new ArrayList<>();
        List<String> avgValues = new ArrayList<>();
        List<String> formatList = new ArrayList<>();
        for (PlayerProperty playerProperty : PlayerProperty.values()) {
            StringBuilder format = new StringBuilder();
            String propertyName = playerProperty.getName();
            propertyNames.add(propertyName);
            format.append("%-").append(propertyName.length() + 3).append("s");
            formatList.add(format.toString());
            maxValues.add(String.valueOf(playerCatalog.getMaximumValue(playerProperty, filteredPlayerEntriesList)));
            minValues.add(String.valueOf(playerCatalog.getMinimumValue(playerProperty, filteredPlayerEntriesList)));
            avgValues.add(String.valueOf(String.format("%.2f", playerCatalog.getMeanAverageValue(playerProperty, filteredPlayerEntriesList))));
        }
        lines.append(String.format("%-11s", ""));
        lines.append(valuesToLine(propertyNames, formatList));
        lines.append(String.format("%-11s", "Maximum:"));
        lines.append(valuesToLine(maxValues, formatList));
        lines.append(String.format("%-11s", "Minimum"));
        lines.append(valuesToLine(minValues, formatList));
        lines.append(String.format("%-11s", "Mean:"));
        lines.append(valuesToLine(avgValues, formatList));
        statisticsTextArea.setText(lines.toString());
        statisticsTextArea.setCaretPosition(0);
        //Used monospaced font to fix alignments on Area - 3
        statisticsTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
    }

    private String valuesToLine(List<String> stringList, List<String> format) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < stringList.size(); i++) {
            stringBuilder.append(String.format(format.get(i), stringList.get(i)));
        }
        stringBuilder.append('\n');
        return stringBuilder.toString();
    }

    /**
     * executeQuery method - applies chosen query to the relevant list
     */
    @Override
    public void executeQuery() {
        Query query = new Query(subQueryList, League.fromName(comboLeagueTypes.getSelectedItem().toString()));
        List<PlayerEntry> queryFiltered = query.executeQuery(playerCatalog);
        if (comboPlayerNames.getSelectedItem() != null && !comboPlayerNames.getSelectedItem().toString().equals(""))
            queryFiltered = queryFiltered.stream().filter(playerEntry -> playerEntry.getPlayerName().equals(comboPlayerNames.getSelectedItem().toString())).collect(Collectors.toList());
        if (comboNations.getSelectedItem() != null && !comboNations.getSelectedItem().toString().equals(""))
            queryFiltered = queryFiltered.stream().filter(playerEntry -> playerEntry.getNation().equals(comboNations.getSelectedItem().toString())).collect(Collectors.toList());
        if (comboTeams.getSelectedItem() != null && !comboTeams.getSelectedItem().toString().equals(""))
            queryFiltered = queryFiltered.stream().filter(playerEntry -> playerEntry.getTeam().equals(comboTeams.getSelectedItem().toString())).collect(Collectors.toList());
        if (comboPositions.getSelectedItem() != null && !comboPositions.getSelectedItem().toString().equals(""))
            queryFiltered = queryFiltered.stream().filter(playerEntry -> playerEntry.getPosition().equals(comboPositions.getSelectedItem().toString())).collect(Collectors.toList());
        filteredPlayerEntriesList = queryFiltered;
        updatePlayerCatalogDetailsBox();
        updateStatistics();
        updateRadarChart();
    }

    /**
     * addFilters method - adds filters input into GUI to subQueryList ArrayList
     */
    @Override
    public void addFilter() {
        // TODO implement
        if (value.getText().equals("")) return;
        boolean inListAlready = false;
        SubQuery subQuery = new SubQuery(PlayerProperty.fromPropertyName(comboQueryProperties.getSelectedItem().toString()), comboOperators.getSelectedItem().toString(), Double.parseDouble(value.getText()));
        for (SubQuery q : subQueryList) {
            if (q.equals(subQuery)) {
                inListAlready = true;
                break;
            }
        }
//      subQuery.getPlayerProperty(); // Can check for duplicate player properties in query
        if (!inListAlready) subQueryList.add(subQuery);
        String subQueryString = subQueryList.toString();
        subQueriesTextArea.setText(subQueryString.substring(1, subQueryString.length() - 1));
        executeQuery();
    }

    @Override
    public boolean isMinCheckBoxSelected() {
        return minCheckBox.isSelected();
    }

    @Override
    public boolean isMaxCheckBoxSelected() {
        return maxCheckBox.isSelected();
    }

    @Override
    public boolean isAverageCheckBoxSelected() {
        return averageCheckBox.isSelected();
    }

}
