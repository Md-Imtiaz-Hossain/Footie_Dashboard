package uk.ac.sheffield.com1003.assignment;

import uk.ac.sheffield.com1003.assignment.codeprovided.*;

import java.util.*;

/**
 * SKELETON IMPLEMENTATION
 */
public class PlayerCatalog extends AbstractPlayerCatalog {
    /**
     * Constructor
     */
    public PlayerCatalog(String eplFilename, String ligaFilename) {
        super(eplFilename, ligaFilename);
    }

    @Override
    public PlayerPropertyMap parsePlayerEntryLine(String line) throws IllegalArgumentException {
        List<String> playerData = Arrays.asList(line.split(","));
        PlayerPropertyMap playerPropertyMap = new PlayerPropertyMap();
        Iterator<String> playerDataIter = playerData.iterator();

        for (PlayerDetail detail : PlayerDetail.values()) {
            if (!playerDataIter.hasNext()) throw new IllegalArgumentException("Too few columns");
            playerPropertyMap.putDetail(detail, playerDataIter.next());
        }
        for (PlayerProperty property : PlayerProperty.values()) {
            if (!playerDataIter.hasNext()) throw new IllegalArgumentException("Too few columns");
            playerPropertyMap.put(property, Double.parseDouble(playerDataIter.next()));
        }
        return playerPropertyMap;
    }

    @Override
    public void updatePlayerCatalog() {
        List<PlayerEntry> epl = playerEntriesMap.get(League.EPL);
        List<PlayerEntry> liga = playerEntriesMap.get(League.LIGA);
        List<PlayerEntry> all = new ArrayList<>();
        all.addAll(epl);
        all.addAll(liga);
        playerEntriesMap.put(League.ALL, all);
    }

    @Override
    public double getMinimumValue(PlayerProperty playerProperty, List<PlayerEntry> playerEntryList)
            throws NoSuchElementException {
        if (playerEntryList.size() < 1) return 0;
        List<Double> propertyValues = new ArrayList<>();
        for (PlayerEntry player : playerEntryList) {
            propertyValues.add(player.getProperty(playerProperty));
        }
        return Collections.min(propertyValues);
    }

    @Override
    public double getMaximumValue(PlayerProperty playerProperty, List<PlayerEntry> playerEntryList) throws NoSuchElementException {
        if (playerEntryList.size() < 1) return 0;
        List<Double> propertyValues = new ArrayList<>();
        for (PlayerEntry player : playerEntryList) {
            propertyValues.add(player.getProperty(playerProperty));
        }
        return Collections.max(propertyValues);
    }

    @Override
    public double getMeanAverageValue(PlayerProperty playerProperty, List<PlayerEntry> playerEntryList) throws NoSuchElementException {
        if (playerEntryList.size() < 1) return 0;
        List<Double> propertyValues = new ArrayList<>();
        for (PlayerEntry player : playerEntryList) {
            propertyValues.add(player.getProperty(playerProperty));
        }
        OptionalDouble average = propertyValues.stream().mapToDouble(num -> num).average();
        if (average.isPresent()) return average.getAsDouble();
        else return 0;
    }

    @Override
    public List<PlayerEntry> getFirstFivePlayerEntries(League type) {
        List<PlayerEntry> playerEntriesList = getPlayerEntriesList(type);
        return playerEntriesList.subList(0, 5);
    }

}
