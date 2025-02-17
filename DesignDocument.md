# Board Game Arena Planner Design Document

This document is meant to provide a tool for you to demonstrate the design process. You need to work on this before you code, and after have a finished product. That way you can compare the changes, and changes in design are normal as you work through a project. It is contrary to popular belief, but we are not perfect our first attempt. We need to iterate on our designs to make them better. This document is a tool to help you do that.

## (INITIAL DESIGN): Class Diagram

Place your class diagrams below. Make sure you check the file in the browser on github.com to make sure it is rendering correctly. If it is not, you will need to fix it. As a reminder, here is a link to tools that can help you create a class diagram: [Class Resources: Class Design Tools](https://github.com/CS5004-khoury-lionelle/Resources?tab=readme-ov-file#uml-design-tools)

### Provided Code

Provide a class diagram for the provided code as you read through it. For the classes you are adding, you will create them as a separate diagram, so for now, you can just point towards the interfaces for the provided code diagram.

```mermaid
---
title Provided Code
---
classDiagram
  direction LR
  class BGArenaPlanner {
    -static final String DEFAULT_COLLECTION
    -BGArenaPlanner()
    +static void main(String[] args)
  }
  BGArenaPlanner ..> IPlanner : uses
  BGArenaPlanner ..> IGameList : uses
  BGArenaPlanner ..> ConsoleApp : uses

  class ConsoleApp {
    +ConsoleApp(IGameList gameList, IPlanner planner)
    +void start()
    -void randomNumber()
    -void processHelp()
    -void processFilter()
    -static void printFilterStream(Stream<BoardGame> games, GameData sortON)
    -void processListCommands()
    -void printCurrentList()
    -ConsoleText nextCommand()
    -String remainder()
    -static String getInput(String format, Object... args)
    -static void printOutput(String format, Object... output)
    -enum ConsoleText
    +String toString()
    +static ConsoleText fromString(String str)
  }
  ConsoleApp ..> IGameList : uses
  ConsoleApp ..> IPlanner : uses

  class IPlanner {
    <<interface>>
    +Stream~BoardGame~ filter(String filter)
    +Stream~BoardGame~ filter(String filter, GameData sortOn)
    +Stream~BoardGame~ filter(String filter, GameData sortOn, boolean ascending)
  }
  IPlanner ..> GamesLoader : uses

  class Planner {
      +Stream~BoardGame~ filter(String filter)
      +Stream~BoardGame~ filter(String filter, GameData sortOn)
      +Stream~BoardGame~ filter(String filter, GameData sortOn, boolean ascending)
  }
  IPlanner <|-- Planner

  class IGameList {
    <<interface>>
    +String ADD_ALL
    +List~String~ getGameNames()
    +void clear()
    +int count()
    +void saveGame(String filename)
    +void addToList(String str, Stream~BoardGame~ filtered) throws IllegalArgumentException
    +void removeFromList(String str) throws IllegalArgumentException
  }
  class GameList {
    +String ADD_ALL
    +List~String~ getGameNames()
    +void clear()
    +int count()
    +void saveGame(String filename)
    +void addToList(String str, Stream~BoardGame~ filtered) throws IllegalArgumentException
    +void removeFromList(String str) throws IllegalArgumentException
  }
  IGameList <|-- GameList

  class BoardGame {
    -final String name;
    -final int id;
    -final int minPlayers;
    -final int maxPlayers;
    -final int maxPlayTime;
    -final int minPlayTime;
    -final double difficulty; // avgweight
    -final int rank;
    -final double averageRating;
    -final int yearPublished;
    +BoardGame(String name, int id, int minPlayers, int maxPlayers, int minPlayTime, int maxPlayTime, double difficulty, int rank, double averageRating, int yearPublished)
    +String getName()
    +int getId()
    +int getMinPlayers()
    +int getMaxPlayers()
    +int getMaxPlayTime()
    +int getMinPlayTime()
    +double getDifficulty()
    +int getRank()
    +double getRating()
    +int getYearPublished()
    +String toStringWithInfo(GameData col)
    +String toString()
    +boolean equals(Object obj)
    +int hashCode()
    +static void main(String[] args)
  }

  class GameData {
  <<enumeration>>
  NAME("objectname"), ID("objectid"),
  RATING("average"), DIFFICULTY("avgweight"),
  RANK("rank"), MIN_PLAYERS("minplayers"), MAX_PLAYERS("maxplayers"),
  MIN_TIME("minplaytime"), MAX_TIME("maxplaytime"), YEAR("yearpublished");
  -final String columnName;
  +GameData(String columnName)
  +String getColumnName()
  +static GameData fromColumnName(String columnName)
  +static GameData fromString(String name)
  }

  class GamesLoader {
    -static final String DELIMITER
    -GamesLoader()
    +static Set<BoardGame> loadGamesFile(String filename)
    -static BoardGame toBoardGame(String line, Map<GameData, Integer> columnMap)
    -static Map<GameData, Integer> processHeader(String header)
  }
  GamesLoader ..> BoardGame : generates
  GamesLoader ..> GameData : uses

  class Operations {
    <<enumeration>>
    EQUALS("==")
    NOT_EQUALS("!=")
    GREATER_THAN(">")
    LESS_THAN("<")
    GREATER_THAN_EQUALS(">=")
    LESS_THAN_EQUALS("<=")
    CONTAINS("~=");
    -final String operator;
    +Operations(String operator)
    +String getOperator()
    +static Operations fromOperator(String operator)
    +static Operations getOperatorFromStr(String str)
  }
```

### Your Plans/Design

Create a class diagram for the classes you plan to create. This is your initial design, and it is okay if it changes. Your starting points are the interfaces.

```mermaid
---
title My plans/Designs
---
classDiagram
  class Planner {
      +Stream~BoardGame~ filter(String filter)
      +Stream~BoardGame~ filter(String filter, GameData sortOn)
      +Stream~BoardGame~ filter(String filter, GameData sortOn, boolean ascending)
  }
  IPlanner <|-- Planner

  class GameList {
    +String ADD_ALL
    +List~String~ getGameNames()
    +void clear()
    +int count()
    +void saveGame(String filename)
    +void addToList(String str, Stream~BoardGame~ filtered) throws IllegalArgumentException
    +void removeFromList(String str) throws IllegalArgumentException
  }
  IGameList <|-- GameList
```

## (INITIAL DESIGN): Tests to Write - Brainstorm

Write a test (in english) that you can picture for the class diagram you have created. This is the brainstorming stage in the TDD process.

> [!TIP]
> As a reminder, this is the TDD process we are following:
>
> 1. Figure out a number of tests by brainstorming (this step)
> 2. Write **one** test
> 3. Write **just enough** code to make that test pass
> 4. Refactor/update as you go along
> 5. Repeat steps 2-4 until you have all the tests passing/fully built program

You should feel free to number your brainstorm.

1. Test Methods for Planner class
2. Test Methods for GameList class

## (FINAL DESIGN): Class Diagram

Go through your completed code, and update your class diagram to reflect the final design. Make sure you check the file in the browser on github.com to make sure it is rendering correctly. It is normal that the two diagrams don't match! Rarely (though possible) is your initial design perfect.

For the final design, you just need to do a single diagram that includes both the original classes and the classes you added.

> [!WARNING]
> If you resubmit your assignment for manual grading, this is a section that often needs updating. You should double check with every resubmit to make sure it is up to date.

## (FINAL DESIGN): Reflection/Retrospective

> [!IMPORTANT]
> The value of reflective writing has been highly researched and documented within computer science, from learning to information to showing higher salaries in the workplace. For this next part, we encourage you to take time, and truly focus on your retrospective.

Take time to reflect on how your design has changed. Write in _prose_ (i.e. do not bullet point your answers - it matters in how our brain processes the information). Make sure to include what were some major changes, and why you made them. What did you learn from this process? What would you do differently next time? What was the most challenging part of this process? For most students, it will be a paragraph or two.

```

```
