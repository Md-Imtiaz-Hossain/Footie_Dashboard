package uk.ac.sheffield.com1003.assignment;

import uk.ac.sheffield.com1003.assignment.codeprovided.*;

import java.util.*;

/**
 * SKELETON IMPLEMENTATION
 */
public class QueryParser extends AbstractQueryParser {

    // Default implementation to be provided
    @Override
    public List<Query> readQueries(List<String> queryTokens) throws IllegalArgumentException {
        List<List<String>> singleQueryList = listSplitter(queryTokens, "select");

        List<Query> queryList = new ArrayList<>();
        for (List<String> query : singleQueryList) {
            List<List<String>> subQueryStringList = new ArrayList<>();
            League league;
            if (query.get(1).equals("or")) {
                subQueryStringList = listSplitter(query.subList(4, query.size()), "and");
                league = League.ALL;
            } else {
                subQueryStringList = listSplitter(query.subList(2, query.size()), "and");
                league = fromLeagueName(query.get(0));
            }
            List<SubQuery> subQueryList = new ArrayList<>();
            for (List<String> subQueryString : subQueryStringList) {
                if (subQueryString.size() != 3) throw new IllegalArgumentException("Malformed query");
                double val = 0;
                try {
                    val = Double.parseDouble(subQueryString.get(2));
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("Malformed query");
                }
                if (!SubQuery.isValidOperator(subQueryString.get(1)))
                    throw new IllegalArgumentException("Malformed query");
                subQueryList.add(new SubQuery(PlayerProperty.fromName(subQueryString.get(0)),
                        subQueryString.get(1),
                        val));
            }
            queryList.add(new Query(subQueryList, league));
        }
        return queryList;
    }


    private List<List<String>> listSplitter(List<String> tokenList, String splitOrigin) {
        List<Integer> splitIndexes = new ArrayList<>();
        for (int i = 0; i < tokenList.size(); i++) {
            if (tokenList.get(i).equals(splitOrigin)) {
                splitIndexes.add(i);
            }
        }
        //For subqueries with no split on the first subquery
        if (splitIndexes.size() == 0) {
            splitIndexes.add(-1);
        } else if (splitIndexes.get(0) != 0) {
            splitIndexes.add(0, -1);
        }
        List<List<String>> splitList = new ArrayList<>();
        for (int i = 0; i < splitIndexes.size(); i++) {
            Integer startIndex = splitIndexes.get(i);
            Integer endIndex;
            if (i + 1 < splitIndexes.size()) endIndex = splitIndexes.get(i + 1);
            else endIndex = tokenList.size();
            splitList.add(tokenList.subList(startIndex + 1, endIndex));
        }
        return splitList;
    }

    //Implemented following the example on Player`Property
    public League fromLeagueName(String leagueName) throws IllegalArgumentException {
        leagueName = leagueName.toUpperCase();
        League league = null;
        try {
            league = League.valueOf(leagueName);
        } catch (IllegalArgumentException e) {
            throw new NoSuchElementException("No such league (" + leagueName + ")!");
        }
        return league;
    }
}

