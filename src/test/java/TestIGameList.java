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
    BoardGame game = new BoardGame("17 days", 6, 1, 8, 70, 70, 9.0, 600, 9.0, 2005);
    Stream<BoardGame> gameStream = Stream.of(game);
    games.addToList("17 days", gameStream);

    assertEquals(games.getGameNames(), List.of("17 days"));
  }
}
