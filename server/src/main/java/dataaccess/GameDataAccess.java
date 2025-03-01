package dataaccess;

import model.GameData;
import model.UserData;

import java.util.ArrayList;

public interface GameDataAccess {
    GameData getGame(int gameID) throws DataAccessException;

    void createGame(String gameName) throws DataAccessException;

    ArrayList<GameData> listGames() throws DataAccessException;

    void addPlayer(int gameID, String username, String playerColor);

    void clear() throws DataAccessException;

    //createGame: Create a new game.
    //getGame: Retrieve a specified game with the given game ID.
    //listGames: Retrieve all games.
    //updateGame: Updates a chess game. It should replace the chess game string
    //corresponding to a given gameID. This is used when players join a game or when a move is made.

}
