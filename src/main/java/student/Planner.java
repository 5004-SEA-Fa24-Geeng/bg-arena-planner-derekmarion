package student;

import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import student.Operations;
import student.GameData;

/**
 * Sets up filters for the board game data.
 * 
 * This the primary interface for the program. DO NOT MODIFY THIS FILE.
 * 
 * Students, you will need to implement the methods in this interface in a class
 * called
 * Planner.java. You can assume the constructor of Planner.java takes in a
 * Set<BoardGame> as a
 * parameter. This represents the total board game collection.
 * 
 * An important note, while most of methods return streams, each method builds
 * on each other / is
 * progressive. As such if filter by minPlayers, then filter by maxPlayers, the
 * maxPlayers filter
 * should be applied to the results of the minPlayers filter unless reset is
 * called between.
 * 
 */
public class Planner implements IPlanner {

  private Set<BoardGame> games;
  private Set<BoardGame> filteredGames;

  public Planner(Set<BoardGame> games) {
    this.games = games;
    this.filteredGames = new HashSet<>(games);
  }

  /**
   * 
   * Assumes the results are sorted in ascending order, and that the stream is
   * sorted by the name
   * of the board game (GameData.NAME).
   * 
   * @param filter The filter to apply to the board games.
   * @return A stream of board games that match the filter.
   * @see #filter(String, GameData, boolean
   */
  @Override
  public Stream<BoardGame> filter(String filter) {

    // Split filter string on commas to support multiple filter operations
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
        return filteredGames.stream();
      }

      String searchTerm = parsedFilter[1];

      // Apply filtering
      filteredGames = filteredGames.stream().filter(game -> matchesFilter(game, column, operator, searchTerm))
          .collect(Collectors.toSet());
    }
    return filteredGames.stream().sorted(Comparator.comparing(game -> game.getName().toLowerCase()));
  }

  /**
   * Getter for filteredGames
   * 
   * @return List of filtered games
   */
  public Set<BoardGame> getFilteredGames() {
    return filteredGames;
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
   * Filters the board games by the passed in text filter. Assumes the results are
   * sorted in
   * ascending order.
   * 
   * @param filter The filter to apply to the board games.
   * @param sortOn The column to sort the results on.
   * @return A stream of board games that match the filter.
   * @see #filter(String, GameData, boolean)
   */
  @Override
  public Stream<BoardGame> filter(String filter, GameData sortOn) {

    filteredGames = filter(filter).collect(Collectors.toSet());
    return filteredGames.stream().sorted(COMPARATORS.get(sortOn));
  }

  private static final Map<GameData, Comparator<BoardGame>> COMPARATORS = Map.of(
      GameData.NAME, Comparator.comparing(game -> game.getName().toLowerCase()),
      GameData.ID, Comparator.comparing(BoardGame::getId),
      GameData.RATING, Comparator.comparing(BoardGame::getRating),
      GameData.DIFFICULTY, Comparator.comparing(BoardGame::getDifficulty),
      GameData.RANK, Comparator.comparing(BoardGame::getRank),
      GameData.MIN_PLAYERS, Comparator.comparing(BoardGame::getMinPlayers),
      GameData.MAX_PLAYERS, Comparator.comparing(BoardGame::getMaxPlayers),
      GameData.MIN_TIME, Comparator.comparing(BoardGame::getMinPlayTime),
      GameData.MAX_TIME, Comparator.comparing(BoardGame::getMaxPlayTime),
      GameData.YEAR, Comparator.comparing(BoardGame::getYearPublished));

  @Override
  public Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending) {
    // Initialize the comparator in advance
    Comparator<BoardGame> comparator = COMPARATORS.get(sortOn);

    // Reverse comparator if ascending is false
    if (!ascending) {
      comparator = comparator.reversed();
    }

    filteredGames = filter(filter).collect(Collectors.toSet());
    return filteredGames.stream().sorted(comparator);
  }

  @Override
  public void reset() {
    filteredGames = Set.of();
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
