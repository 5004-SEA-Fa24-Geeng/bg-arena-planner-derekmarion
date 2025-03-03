import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileReader;
import java.io.BufferedReader;
import student.BoardGame;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.nio.file.Path;
import student.Planner;
import student.IPlanner;
import student.GameData;
import student.GameList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit test for the IGameList/GameList class.
 * <p>
 * Just a sample test to get you started, also using
 * setup to help out.
 */
public class TestGameList {

  @TempDir
  Path tempDir;

  private GameList games;

  BoardGame game1 = new BoardGame("17 days", 6, 1, 8, 70, 70, 9.0, 600, 9.0, 2005);
  BoardGame game2 = new BoardGame("20 days", 6, 1, 8, 70, 70, 9.0, 600, 9.0, 2005);

  @BeforeEach
  public void setup() {
    games = new GameList();
  }

  /**
   * Test that constructor initializes new empty list
   */
  @Test
  public void testConstructor() {
    assertNotNull(games);
    assertInstanceOf(GameList.class, games);
  }

  /**
   * Test getNames() method
   */
  @Test
  public void testGetNames() {
    assertEquals(List.of(), games.getGameNames());
  }

  /**
   * Test addToList() method
   */
  @Test
  public void testAddToListGameName() {
    Stream<BoardGame> gameStream = Stream.of(game1);
    games.addToList("17 days", gameStream);

    assertEquals(games.getGameNames(), List.of("17 days"));
  }

  @Test
  public void testAddToListSingleNumber() {
    Stream<BoardGame> gameStream = Stream.of(game1, game2);
    games.addToList("1", gameStream);

    assertEquals(games.getGameNames(), List.of("17 days"));
  }

  @Test
  public void testAddToListRange() {
    Stream<BoardGame> gameStream = Stream.of(game1, game2);
    games.addToList("1-2", gameStream);

    assertEquals(games.getGameNames(), List.of("17 days", "20 days"));
  }

  @Test
  public void testAddToListBadRange() {
    Stream<BoardGame> gameStream = Stream.of(game1, game2);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> games.addToList("1-3", gameStream));

    assertEquals("Invalid range: 1-3", exception.getMessage());
  }

  @Test
  public void testAddToListBadSelection() {
    Stream<BoardGame> gameStream = Stream.of(game1, game2);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> games.addToList("100", gameStream));

    assertEquals("Invalid selection: 100", exception.getMessage());
  }

  @Test
  public void testAddToListAll() {
    Stream<BoardGame> gameStream = Stream.of(game1, game2);
    games.addToList("all", gameStream);

    assertEquals(games.getGameNames(), List.of("17 days", "20 days"));
  }

  @Test
  public void testGameNotFound() {
    Stream<BoardGame> gameStream = Stream.of(game1, game2);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> games.addToList("random", gameStream));

    assertEquals("Game not found: random", exception.getMessage());
  }

  /**
   * Test the removeFromList method
   *
   */
  @Test
  public void testRemoveFromListGameName() {
    Stream<BoardGame> gameStream = Stream.of(game1, game2);
    games.addToList("all", gameStream);
    games.removeFromList("17 days");

    assertEquals(games.getGameNames(), List.of("20 days"));
  }

  @Test
  public void testRemoveFromListSingleNumber() {
    Stream<BoardGame> gameStream = Stream.of(game1, game2);
    games.addToList("all", gameStream);
    games.removeFromList("1");

    assertEquals(games.getGameNames(), List.of("20 days"));
  }

  @Test
  public void testRemoveFromListRange() {
    Stream<BoardGame> gameStream = Stream.of(game1, game2);
    games.addToList("all", gameStream);
    games.removeFromList("1-2");

    assertEquals(games.getGameNames(), List.of());
  }

  @Test
  public void testRemoveFromListAll() {
    Stream<BoardGame> gameStream = Stream.of(game1, game2);
    games.addToList("all", gameStream);
    games.removeFromList("all");

    assertEquals(games.getGameNames(), List.of());
  }

  @Test
  public void testGameNotFoundRemoval() {
    Stream<BoardGame> gameStream = Stream.of(game1, game2);
    games.addToList("all", gameStream);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> games.removeFromList("random"));

    assertEquals("Game not found: random", exception.getMessage());
  }

  @Test
  public void testRemoveFromListBadRange() {
    Stream<BoardGame> gameStream = Stream.of(game1, game2);
    games.addToList("all", gameStream);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> games.removeFromList("1-3"));

    assertEquals("Invalid range: 1-3", exception.getMessage());
  }

  @Test
  public void testRemoveFromListBadSelection() {
    Stream<BoardGame> gameStream = Stream.of(game1, game2);
    games.addToList("all", gameStream);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> games.removeFromList("100"));

    assertEquals("Invalid selection: 100", exception.getMessage());
  }

  /**
   * Test the count() method
   */
  @Test
  public void testCount() {
    Stream<BoardGame> gameStream = Stream.of(game1, game2);
    games.addToList("all", gameStream);

    assertEquals(games.count(), 2);
  }

  /**
   * Test the saveGame() method
   */
  @Test
  public void testSaveGame() throws IOException {
    Stream<BoardGame> gameStream = Stream.of(game1, game2);
    games.addToList("all", gameStream);

    String testFileName = tempDir.resolve("testFile.txt").toString();
    games.saveGame(testFileName);

    List<String> expectedLines = games.getGameNames();
    List<String> actualLines = readLinesFromFile(testFileName);

    assertEquals(expectedLines, actualLines);
  }

  // Helper method to read file contents
  private List<String> readLinesFromFile(String filename) throws IOException {
    List<String> lines = new java.util.ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      String line;
      while ((line = reader.readLine()) != null) {
        lines.add(line);
      }
    }
    return lines;
  }
}
