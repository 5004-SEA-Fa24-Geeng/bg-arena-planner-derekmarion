import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import student.BoardGame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import student.Planner;
import student.IPlanner;
import student.GameData;
import student.Operations;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * JUnit test for the Planner class.
 * 
 * Just a sample test to get you started, also using
 * setup to help out.
 */
public class TestPlanner {
  static Set<BoardGame> games;

  @BeforeAll
  public static void setup() {
    games = new HashSet<>();
    games.add(new BoardGame("17 days", 6, 1, 8, 70, 70, 9.0, 600, 9.0, 2005));
    games.add(new BoardGame("Chess", 7, 2, 2, 10, 20, 10.0, 700, 10.0, 2006));
    games.add(new BoardGame("Go", 1, 2, 5, 30, 30, 8.0, 100, 7.5, 2000));
    games.add(new BoardGame("Go Fish", 2, 2, 10, 20, 120, 3.0, 200, 6.5, 2001));
    games.add(new BoardGame("golang", 4, 2, 7, 50, 55, 7.0, 400, 9.5, 2003));
    games.add(new BoardGame("GoRami", 3, 6, 6, 40, 42, 5.0, 300, 8.5, 2002));
    games.add(new BoardGame("Monopoly", 8, 6, 10, 20, 1000, 1.0, 800, 5.0, 2007));
    games.add(new BoardGame("Tucano", 5, 10, 20, 60, 90, 6.0, 500, 8.0, 2004));
  }

  /**
   * Test the filter method with only filter arg
   */
  @Test
  public void testFilterName() {
    IPlanner planner = new Planner(games);
    List<BoardGame> filtered = planner.filter("name == Go").toList();
    assertEquals(1, filtered.size());
    assertEquals("Go", filtered.get(0).getName());
  }

  @Test
  public void testFilterNameContains() {
    IPlanner planner = new Planner(games);
    List<BoardGame> filtered = planner.filter("name ~=Go Fish").toList();
    assertEquals(1, filtered.size());
    assertEquals("Go Fish", filtered.get(0).getName());
  }

  @Test
  public void testFilterInvalidOperator() {
    Planner planner = new Planner(games);
    List<BoardGame> filtered = planner.filter("name ## Go").toList();
    assertEquals(planner.getFilteredGames(), filtered.stream().collect(Collectors.toSet()),
        "Invalid operator should return most recent filter");
  }

  @Test
  public void testFilterInvalidColumn() {
    Planner planner = new Planner(games);
    List<BoardGame> filtered = planner.filter("invalidColumn == Go").toList();
    assertEquals(planner.getFilteredGames(), filtered.stream().collect(Collectors.toSet()),
        "Invalid column should return most recent filter");
  }

  @Test
  public void testFilterByMinimumPlayers() {
    IPlanner planner = new Planner(games);
    List<BoardGame> filtered = planner.filter("minPlayers == 2").toList();
    assertTrue(filtered.stream().allMatch(game -> game.getMinPlayers() == 2));
  }

  @Test
  public void testFilterByYearPublished() {
    IPlanner planner = new Planner(games);
    List<BoardGame> filtered = planner.filter("year == 2004").toList();
    assertEquals(1, filtered.size());
    assertEquals(2004, filtered.get(0).getYearPublished());
  }

  @Test
  public void testFilterByMaxPlayers() {
    IPlanner planner = new Planner(games);
    List<BoardGame> filtered = planner.filter("maxPlayers > 5").toList();
    assertTrue(filtered.stream().allMatch(game -> game.getMaxPlayers() > 5));
  }

  @Test
  public void testFilterByRating() {
    IPlanner planner = new Planner(games);
    List<BoardGame> filtered = planner.filter("rating >= 9.0").toList();
    assertTrue(filtered.stream().allMatch(game -> game.getRating() >= 9.0));
  }

  @Test
  public void testFilterNoMatches() {
    IPlanner planner = new Planner(games);
    List<BoardGame> filtered = planner.filter("name == NonExistentGame").toList();
    assertTrue(filtered.isEmpty(), "Non-matching filter should return an empty list");
  }

  @Test
  void testMultipleFiltersName() {
    IPlanner planner = new Planner(games);
    List<String> filtered = planner.filter("name~=go,name~=fish").map(BoardGame::getName).collect(Collectors.toList());

    assertEquals(1, filtered.size());
    assertTrue(filtered.contains("Go Fish"));
    assertFalse(filtered.contains("Go"));
  }

  @Test
  void testMultipleFiltersNumber() {
    IPlanner planner = new Planner(games);
    List<String> filtered = planner.filter("minPlayers>4,maxPlayers<10").map(BoardGame::getName)
        .collect(Collectors.toList());

    assertEquals(1, filtered.size());
    assertTrue(filtered.contains("GoRami"));
  }

  /**
   * Test the filter method with sortOn
   */
  @Test
  void testFilterByNamePartial() {
    // Test filtering games with "Go" in the name (this is a case-insensitive
    // operation)
    IPlanner planner = new Planner(games);
    List<String> filteredGames = planner.filter("name ~= Go").map(BoardGame::getName)
        .collect(Collectors.toList());
    assertEquals(filteredGames, List.of("Go", "Go Fish", "golang", "GoRami"));
    assertEquals(4, filteredGames.size(), "Should find 4 games with 'Go' in the name");
  }

  @Test
  void testFilterByNameNoMatches() {
    // Test filtering with no matches
    IPlanner planner = new Planner(games);
    List<BoardGame> filteredGames = planner.filter("name ~= namenotfound")
        .collect(Collectors.toList());

    assertTrue(filteredGames.isEmpty(), "Should return empty list for no matches");
  }

  @Test
  void testFilterByNameSortedAlphabetically() {
    // Test filtering and sorting by name
    IPlanner planner = new Planner(games);
    List<String> filteredNames = planner.filter("name ~= Go", GameData.NAME)
        .map(BoardGame::getName)
        .collect(Collectors.toList());

    assertEquals(List.of("Go", "Go Fish", "golang", "GoRami"), filteredNames,
        "Should sort 'Go' games alphabetically");
  }

  @Test
  void testFilterByYearSorted() {
    // Test filtering and sorting by year
    IPlanner planner = new Planner(games);
    List<Integer> filteredYears = planner.filter("name ~= Go", GameData.YEAR)
        .map(BoardGame::getYearPublished)
        .collect(Collectors.toList());

    assertEquals(List.of(2000, 2001, 2002, 2003), filteredYears,
        "Should sort 'Go' games by year");
  }

  @Test
  void testFilterByRatingSorted() {
    // Test filtering and sorting by rating
    IPlanner planner = new Planner(games);
    List<Double> filteredRatings = planner.filter("name ~= Go", GameData.RATING)
        .map(BoardGame::getRating)
        .collect(Collectors.toList());

    assertEquals(List.of(6.5, 7.5, 8.5, 9.5), filteredRatings,
        "Should sort 'Go' games by rating");
  }

  @Test
  void testFilterByPlayerCount() {
    // Test filtering by player count
    IPlanner planner = new Planner(games);
    List<BoardGame> filteredGames = planner.filter("minplayers == 2")
        .collect(Collectors.toList());

    assertEquals(4, filteredGames.size(), "Should find games with 2 players");
    assertTrue(filteredGames.stream().allMatch(game -> game.getMinPlayers() <= 2 && game.getMaxPlayers() >= 2),
        "All filtered games should support 2 players");
  }

  @Test
  void testFilterByPlayTime() {
    // Test filtering by play time
    IPlanner planner = new Planner(games);
    List<BoardGame> filteredGames = planner.filter("minplaytime == 30")
        .collect(Collectors.toList());

    assertEquals(1, filteredGames.size(), "Should find games with 30-minute play time");
    assertTrue(filteredGames.stream().anyMatch(game -> game.getName().equals("Go")),
        "Should include 'Go' game");
  }

  /**
   * Test filter, sortOn, ascending method signature
   */
  @Test
  void testFilterByRatingSortedDescending() {
    // Test filtering and sorting by rating in descending order
    IPlanner planner = new Planner(games);
    List<Double> filteredRatings = planner.filter("name ~= Go", GameData.RATING, false)
        .map(BoardGame::getRating)
        .collect(Collectors.toList());

    assertEquals(List.of(9.5, 8.5, 7.5, 6.5), filteredRatings,
        "Should sort 'Go' games by rating");
  }

  /**
   * Test the reset() method
   */
  @Test
  void testReset() {
    Planner planner = new Planner(games);
    List<Double> filtered = planner.filter("name ~= Go", GameData.RATING, false)
        .map(BoardGame::getRating)
        .collect(Collectors.toList());

    assertEquals(List.of(9.5, 8.5, 7.5, 6.5), filtered,
        "Should sort 'Go' games by rating");
    planner.reset();
    assertEquals(Set.of(), planner.getFilteredGames());
  }

  /**
   * Test the testParseFilter method
   */
  @Test
  public void testParseFilter() {
    Planner planner = new Planner(games);

    // Assuming Operations is an enum with .toString() returning the operator as a
    // string
    Operations equalsOp = Operations.EQUALS; // Assuming this is "=="
    Operations notEqualsOp = Operations.NOT_EQUALS; // Assuming this is "!="

    // Test with whitespace around
    assertArrayEquals(new String[] { "name", "Go" }, planner.parseFilter("name == Go", equalsOp));
    assertArrayEquals(new String[] { "age", "25" }, planner.parseFilter(" age!= 25 ", notEqualsOp));

    // Test without whitespace
    assertArrayEquals(new String[] { "name", "Go" }, planner.parseFilter("name==Go", equalsOp));

    // Test with extra spaces
    assertArrayEquals(new String[] { "salary", "1000" }, planner.parseFilter("  salary  == 1000  ", equalsOp));

    // Test with a multi-character operator
    Operations greaterOrEqualOp = Operations.GREATER_THAN_EQUALS; // Assuming this is ">="
    assertArrayEquals(new String[] { "price", "100" }, planner.parseFilter("price >= 100", greaterOrEqualOp));

    // Test with malformed input
    assertThrows(IllegalArgumentException.class, () -> planner.parseFilter("name Go", Operations.EQUALS));
  }
}
