package student;

import java.util.Set;
import java.util.stream.Collectors;

public class GameFilter {
  public Set<BoardGame> applyFilter(Set<BoardGame> games, String filter) {
    String[] filterParts = filter.split(",");

    for (String filterPart : filterParts) {
      // Get operator
      Operations operator = Operations.getOperatorFromStr(filterPart);
      if (operator == null) {
        System.out.println("No operator detected");
        continue;
      }

      // Parse search term and column name
      String[] parsedFilter = parseFilter(filterPart, operator);
      GameData column;
      try {
        column = GameData.fromString(parsedFilter[0]);
      } catch (IllegalArgumentException e) {
        System.out.println(e);
        return games;
      }

      String searchTerm = parsedFilter[1];

      // Apply filtering successively
      games = games.stream().filter(game -> matchesFilter(game, column, operator, searchTerm))
          .collect(Collectors.toSet());
    }
    return games;
  }

  /**
   * Checks if a game matches filter
   *
   * @param game
   * @param column
   * @param operator
   * @param searchTerm
   * @return boolean of whether a game matches a given filter
   */
  private boolean matchesFilter(BoardGame game, GameData column, Operations operator, String searchTerm) {
    // We don't know the type of gameValue at compile time so we keep it generic
    Object gameValue;

    switch (column) {
      case NAME:
        gameValue = game.getName();
        break;
      case ID:
        gameValue = game.getId();
        break;
      case RATING:
        gameValue = game.getRating();
        break;
      case DIFFICULTY:
        gameValue = game.getDifficulty();
        break;
      case RANK:
        gameValue = game.getRank();
        break;
      case MIN_PLAYERS:
        gameValue = game.getMinPlayers();
        break;
      case MAX_PLAYERS:
        gameValue = game.getMaxPlayers();
        break;
      case MIN_TIME:
        gameValue = game.getMinPlayTime();
        break;
      case MAX_TIME:
        gameValue = game.getMaxPlayTime();
        break;
      case YEAR:
        gameValue = game.getYearPublished();
        break;
      default:
        return false; // Unknown column
    }

    return applyOperator(gameValue, operator, searchTerm);

  }

  /**
   * Applies the given operator to the value and searchTerm
   *
   * @param value
   * @param operator
   * @param searchTerm
   * @return call to applyStringOperator or applyNumericOperator, boolean
   */
  private boolean applyOperator(Object value, Operations operator, String searchTerm) {
    if (value instanceof String) {
      return applyStringOperator((String) value, operator, searchTerm);
    } else if (value instanceof Integer) {
      return applyNumericOperator((Integer) value, operator, parseInt(searchTerm));
    } else if (value instanceof Double) {
      return applyNumericOperator((Double) value, operator, parseDouble(searchTerm));
    }
    return false;
  }

  /**
   * Applies given operator to string value and searchTerm
   *
   * @param value
   * @param operator
   * @param searchTerm
   * @return
   */
  private boolean applyStringOperator(String value, Operations operator, String searchTerm) {
    switch (operator) {
      case EQUALS:
        return value.equalsIgnoreCase(searchTerm);
      case NOT_EQUALS:
        return !value.equalsIgnoreCase(searchTerm);
      case CONTAINS:
        return value.toLowerCase().contains(searchTerm.toLowerCase());
      case GREATER_THAN_EQUALS:
        return value.toLowerCase().compareTo(searchTerm.toLowerCase()) >= 0;
      case GREATER_THAN:
        return value.toLowerCase().compareTo(searchTerm.toLowerCase()) > 0;
      case LESS_THAN_EQUALS:
        return value.toLowerCase().compareTo(searchTerm.toLowerCase()) <= 0;
      case LESS_THAN:
        return value.toLowerCase().compareTo(searchTerm.toLowerCase()) < 0;
      default:
        return false; // Unsupported operator for Strings
    }
  }

  /**
   * Applies given operator to numeric value and searchTerm
   *
   * @param <T>
   * @param value
   * @param operator
   * @param searchTerm
   * @return boolean of evaluation
   */
  private <T extends Number> boolean applyNumericOperator(T value, Operations operator, T searchTerm) {
    if (searchTerm == null)
      return false;

    double gameNumber = value.doubleValue();
    double searchNumber = searchTerm.doubleValue();

    switch (operator) {
      case EQUALS:
        return gameNumber == searchNumber;
      case NOT_EQUALS:
        return gameNumber != searchNumber;
      case GREATER_THAN_EQUALS:
        return gameNumber >= searchNumber;
      case LESS_THAN_EQUALS:
        return gameNumber <= searchNumber;
      case GREATER_THAN:
        return gameNumber > searchNumber;
      case LESS_THAN:
        return gameNumber < searchNumber;
      default:
        return false;
    }
  }

  /**
   * Extracts integer from String safely
   *
   * @param str
   * @return Integer from String
   */
  private Integer parseInt(String str) {
    try {
      return Integer.parseInt(str);
    } catch (NumberFormatException e) {
      return null; // Invalid number
    }
  }

  /**
   * Extracts double from String safely
   * 
   * @param str
   * @return Double from String
   */
  private Double parseDouble(String str) {
    try {
      return Double.parseDouble(str);
    } catch (NumberFormatException e) {
      return null; // Invalid number
    }
  }

  /**
   * Utility method to parse a filter
   * 
   * @param filter
   * @param operator
   * @return An array of strings containing column and search term
   */
  public String[] parseFilter(String filter, Operations operator) {
    // Split on operator while preserving spaces in search terms
    String[] parsedFilter = filter.split(operator.getOperator(), 2); // Split into max 2 parts

    if (parsedFilter.length < 2) {
      throw new IllegalArgumentException("Input does not contain a valid operation");
    }

    // Trim spaces around the column name and search term
    return new String[] { parsedFilter[0].trim(), parsedFilter[1].trim() };
  }

}
