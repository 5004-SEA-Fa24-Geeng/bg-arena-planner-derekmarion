import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import student.BoardGame;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

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
public class TestIGameList {

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
}
