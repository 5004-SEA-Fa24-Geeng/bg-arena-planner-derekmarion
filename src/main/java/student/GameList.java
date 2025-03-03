package student;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameList implements IGameList {

  private List<BoardGame> games;

  /**
   * Constructor for the GameList.
   */
  public GameList() {
    this.games = new ArrayList<>();
  }

  /**
   * Getter for game names
   *
   * @return game names
   */
  @Override
  public List<String> getGameNames() {
    List<String> gameNames = games.stream().map(BoardGame::getName).collect(Collectors.toList());
    return gameNames;
  }

  /**
   * Removes all games in the list (clears it out completely).
   */
  @Override
  public void clear() {
    games.clear();
  }

  @Override
  public int count() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'count'");
  }

  @Override
  public void saveGame(String filename) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'saveGame'");
  }

  /**
   * Adds a game or games to the list.
   * 
   * If a single name is specified, that takes priority. However, it could also
   * use a number such
   * as 1 which would indicate game 1 from the current filtered list should be
   * added to the list.
   * (1 being the first game in the list, normal counting).
   * 
   * A range can also be added, so if 1-5 was presented, it is assumed that games
   * 1 through 5
   * should be added to the list - or if the number is larger than the filtered
   * group 1-n (with n
   * being the last game in the filter). 1-1 type formatting
   * is allowed, and treated as just adding a single game.
   * 
   * If "all" is specified, then all games in the filtered collection should be
   * added to the list.
   * 
   * If any part of the string is not valid, an IllegalArgumentException should be
   * thrown. Such as
   * ranges being out of range.
   * 
   * @param str      the string to parse and add games to the list.
   * @param filtered the filtered list to use as a basis for adding.
   * @throws IllegalArgumentException if the string is not valid.
   */
  @Override
  public void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException {
    if (str == null || str.isEmpty()) {
      throw new IllegalArgumentException("Input cannot be null or empty.");
    }

    // Clean search term
    String trimmedStr = str.trim().toLowerCase();

    // Convert stream to list for manipulation
    List<BoardGame> filteredList = filtered.collect(Collectors.toList());

    // Case 1: Add all games to list
    if (trimmedStr.matches("all")) {
      games.addAll(filteredList);
      return;
    }

    // Case 2: Check for range e.g. 1-5
    if (trimmedStr.matches("\\d+-\\d+")) {
      String[] range = trimmedStr.split("-");
      int start = Integer.parseInt(range[0]);
      int end = Integer.parseInt(range[1]);

      // Validate range
      if (start < 1 || end < start || end > filteredList.size()) {
        throw new IllegalArgumentException("Invalid range: " + trimmedStr);
      }

      // Add games in provide range (accounting for 0 index)
      for (int i = start - 1; i < end; i++) {
        games.add(filteredList.get(i));
      }
      return;
    }

    // Case 3: Single number input
    if (trimmedStr.matches("\\d+")) {
      int index = Integer.parseInt(trimmedStr);

      // Validate index
      if (index < 1 || index > filteredList.size()) {
        throw new IllegalArgumentException("Invalid selection: " + trimmedStr);
      }

      // Add single game (adjusted for 0 index)
      games.add(filteredList.get(index - 1));
      return;
    }

    // Case 4: Game name
    Optional<BoardGame> game = filteredList.stream()
        .filter(g -> g.getName().toLowerCase().equals(trimmedStr))
        .findFirst();

    if (game.isPresent()) {
      games.add(game.get());
    } else {
      throw new IllegalArgumentException("Game not found: " + trimmedStr);
    }
  }

  /**
   * Removes a game or games from the list.
   * 
   * If a single name is specified, that takes priority. However, it could also
   * use a number such
   * as 1 which would indicate game 1 from the current games list should be
   * removed. A range can
   * also be specified to remove multiple games.
   * 
   * If all is provided, then clear should be called.
   * 
   * If any part of the string is not valid, an IllegalArgumentException should be
   * thrown. Such as
   * ranges being out of range, or none of the results doing anything.
   * 
   * @param str The string to parse and remove games from the list.
   * @throws IllegalArgumentException If the string is not valid.
   * 
   */
  @Override
  public void removeFromList(String str) throws IllegalArgumentException {

    if (str == null || str.isEmpty()) {
      throw new IllegalArgumentException("Input cannot be null or empty.");
    }

    // Clean search term
    String trimmedStr = str.trim().toLowerCase();

    // Case 1: Remove a all games from the list
    if (trimmedStr.matches("all")) {
      games.clear();
      return;
    }

    // Case 2: Check for range e.g. 1-5
    if (trimmedStr.matches("\\d+-\\d+")) {
      String[] range = trimmedStr.split("-");
      int start = Integer.parseInt(range[0]);
      int end = Integer.parseInt(range[1]);

      // Validate range
      if (start < 1 || end < start || end > games.size()) {
        throw new IllegalArgumentException("Invalid range: " + trimmedStr);
      }

      // Remove games in provided range (accounting for 0 index)
      games.subList(start - 1, end).clear();
      return;
    }

    // Case 3: Single number input
    if (trimmedStr.matches("\\d+")) {
      int index = Integer.parseInt(trimmedStr);

      // Validate index
      if (index < 1 || index > games.size()) {
        throw new IllegalArgumentException("Invalid selection: " + trimmedStr);
      }

      // Remove a single game (adjusted for 0 index)
      games.remove(games.get(index - 1));
      return;
    }

    // Case 4: Remove named game
    Optional<BoardGame> game = games.stream()
        .filter(g -> g.getName().toLowerCase().equals(trimmedStr))
        .findFirst();

    if (game.isPresent()) {
      games.remove(game.get());
    } else {
      throw new IllegalArgumentException("Game not found: " + trimmedStr);
    }
  }

}
