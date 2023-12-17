package sktech.com.iimtiaz.ui.assignment;

import org.junit.jupiter.api.Test;
import sktech.com.iimtiaz.ui.assignment.codeprovided.Query;
import sktech.com.iimtiaz.ui.assignment.codeprovided.League;
import sktech.com.iimtiaz.ui.assignment.common.TestCommon;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestQueryParser {

    @Test
    public void testReadSingleQuery() {
        QueryParser parser = new QueryParser();
        List<Query> queries = parser.readQueries(TestCommon.tokenizeString("select liga where MINUTES != 2 and GOALS != 4 and ASSISTS != 6"));
        assertEquals(1, queries.size());
        assertEquals(3, queries.get(0).getSubQueryList().size());
        assertEquals(League.LIGA, queries.get(0).getLeagueType());
    }

    @Test
    public void testExceptionOnBadQuery() {
        QueryParser parser = new QueryParser();
        assertThrows(IllegalArgumentException.class, () -> parser.readQueries(TestCommon.tokenizeString("select epl where MINUTES &= none")));
    }
}
