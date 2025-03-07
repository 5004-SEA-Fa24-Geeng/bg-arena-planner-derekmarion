package student;

import java.util.Set;
import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Stream;

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
  private final GameFilter gameFilter;
  private final GameSorter gameSorter;

  public Planner(Set<BoardGame> games) {
    this.games = games;
    this.filteredGames = new HashSet<>(games);
    this.gameFilter = new GameFilter();
    this.gameSorter = new GameSorter();
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

    filteredGames = gameFilter.applyFilter(filteredGames, filter);
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
    filteredGames = gameFilter.applyFilter(filteredGames, filter);
    return gameSorter.sort(filteredGames.stream(), sortOn, true);
  }

  @Override
  public Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending) {
    filteredGames = gameFilter.applyFilter(filteredGames, filter);
    return gameSorter.sort(filteredGames.stream(), sortOn, ascending);
  }

  /**
   * Resets the list of filtered games to the original set
   */
  @Override
  public void reset() {
    filteredGames = games;
  }
}
