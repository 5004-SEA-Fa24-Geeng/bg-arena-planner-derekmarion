package student;

import java.util.stream.Stream;
import java.util.Map;
import java.util.Comparator;

public class GameSorter {

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

  public Stream<BoardGame> sort(Stream<BoardGame> games, GameData sortOn, boolean ascending) {
    Comparator<BoardGame> comparator = COMPARATORS.get(sortOn);
    if (!ascending) {
      comparator = comparator.reversed();
    }
    return games.sorted(comparator);
  }
}
