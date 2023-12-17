package sktech.com.iimtiaz.ui.assignment.codeprovided;

/**
 * Provides a helper enum with constants representing the accepted leagues.
 */
public enum League {
    ALL("All leagues"),
    EPL("English Premier League"),
    LIGA("La Liga");

    private final String leagueTypeName;

    League(String lTName) {
        leagueTypeName = lTName;
    }

    public String getName() {
        return leagueTypeName;
    }

    public static League fromName(String leagueName) {
        for (League l : League.values()) {
            if (l.getName().equals(leagueName))
                return l;
        }
        return null;
    }
}