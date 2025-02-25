package student;

import java.util.ArrayList;
import java.util.List;
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

  @Override
  public void clear() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'clear'");
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

  @Override
  public void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'addToList'");
  }

  @Override
  public void removeFromList(String str) throws IllegalArgumentException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'removeFromList'");
  }

}
